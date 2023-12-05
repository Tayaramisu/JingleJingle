package com.tayaramisu;

import com.google.inject.Provides;

import java.util.HashMap;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;

import net.runelite.api.annotations.Varbit;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.runelite.api.Varbits.*;

@Slf4j
@PluginDescriptor(
        name = "Jingle Jingle",
        description = "Adds customizable jingles for achievement diary tasks, mahogany homes, and more!"
)
public class JingleJinglePlugin extends Plugin {
    @Inject
    private Client client;

    @Getter(AccessLevel.PACKAGE)
    @Inject
    private ClientThread clientThread;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private JingleJingleConfig config;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private OkHttpClient okHttpClient;

    private final int[] varbitsAchievementDiaries = {
            Varbits.DIARY_ARDOUGNE_EASY, Varbits.DIARY_ARDOUGNE_MEDIUM, Varbits.DIARY_ARDOUGNE_HARD, Varbits.DIARY_ARDOUGNE_ELITE,
            Varbits.DIARY_DESERT_EASY, Varbits.DIARY_DESERT_MEDIUM, Varbits.DIARY_DESERT_HARD, Varbits.DIARY_DESERT_ELITE,
            Varbits.DIARY_FALADOR_EASY, Varbits.DIARY_FALADOR_MEDIUM, Varbits.DIARY_FALADOR_HARD, Varbits.DIARY_FALADOR_ELITE,
            Varbits.DIARY_KANDARIN_EASY, Varbits.DIARY_KANDARIN_MEDIUM, Varbits.DIARY_KANDARIN_HARD, Varbits.DIARY_KANDARIN_ELITE,
            DIARY_KARAMJA_EASY, DIARY_KARAMJA_MEDIUM, DIARY_KARAMJA_HARD, Varbits.DIARY_KARAMJA_ELITE,
            Varbits.DIARY_KOUREND_EASY, Varbits.DIARY_KOUREND_MEDIUM, Varbits.DIARY_KOUREND_HARD, Varbits.DIARY_KOUREND_ELITE,
            Varbits.DIARY_LUMBRIDGE_EASY, Varbits.DIARY_LUMBRIDGE_MEDIUM, Varbits.DIARY_LUMBRIDGE_HARD, Varbits.DIARY_LUMBRIDGE_ELITE,
            Varbits.DIARY_MORYTANIA_EASY, Varbits.DIARY_MORYTANIA_MEDIUM, Varbits.DIARY_MORYTANIA_HARD, Varbits.DIARY_MORYTANIA_ELITE,
            Varbits.DIARY_VARROCK_EASY, Varbits.DIARY_VARROCK_MEDIUM, Varbits.DIARY_VARROCK_HARD, Varbits.DIARY_VARROCK_ELITE,
            Varbits.DIARY_WESTERN_EASY, Varbits.DIARY_WESTERN_MEDIUM, Varbits.DIARY_WESTERN_HARD, Varbits.DIARY_WESTERN_ELITE,
            Varbits.DIARY_WILDERNESS_EASY, Varbits.DIARY_WILDERNESS_MEDIUM, Varbits.DIARY_WILDERNESS_HARD, Varbits.DIARY_WILDERNESS_ELITE
    };


    private static final Pattern COLLECTION_LOG_ITEM_REGEX = Pattern.compile("New item added to your collection log:.*");
    private static final Pattern COMBAT_TASK_REGEX = Pattern.compile("Congratulations, you've completed an? .* combat task:.*");
    private static final Pattern MAHOMES_PATTERN = Pattern.compile("You have completed .* contracts with a total of .* points\\.");
    private static final Pattern FARMING_CONTRACT_PATTERN = Pattern.compile("You've completed a Farming Guild Contract. You should return to Guildmaster Jane\\.");
    private static final Pattern ACHIEVEMENT_DIARY_TASK_PATTERN = Pattern.compile("<col=dc143c>Well done! You have completed an? (\\w+) task in the (\\w+) area\\. Your Achievement Diary has been updated\\.</col>");

    private static final Set<Integer> badCollectionLogNotificationSettingValues = new HashSet<Integer>() {{
        add(0);
        add(2);
    }};

    private final Map<Integer, Integer> oldAchievementDiaries = new HashMap<>();

    private int lastLoginTick = -1;
    private int lastColLogSettingWarning = -1;

    private boolean gameStateLoggedIn = false;

    @Override
    protected void startUp() throws Exception {
        clientThread.invoke(this::setupOldMaps);
        lastLoginTick = -1;
        executor.submit(() -> {
            SoundFileManager.ensureDownloadDirectoryExists();
            SoundFileManager.downloadAllMissingSounds(okHttpClient);
        });
    }

    @Override
    protected void shutDown() throws Exception {
        oldAchievementDiaries.clear();
        soundEngine.close();
    }

    private void setupOldMaps() {
        if (client.getGameState() != GameState.LOGGED_IN) {
            oldAchievementDiaries.clear();
        } else {
            for (@Varbit int diary : varbitsAchievementDiaries) {
                int value = client.getVarbitValue(diary);
                oldAchievementDiaries.put(diary, value);
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        gameStateLoggedIn = event.getGameState() == GameState.LOGGED_IN;
        switch (event.getGameState()) {
            case LOGIN_SCREEN:
            case HOPPING:
            case LOGGING_IN:
            case LOGIN_SCREEN_AUTHENTICATOR:
                oldAchievementDiaries.clear();
            case CONNECTION_LOST:
                // set to -1 here in-case of race condition with varbits changing before this handler is called
                // when game state becomes LOGGED_IN
                lastLoginTick = -1;
                lastColLogSettingWarning = client.getTickCount(); // avoid warning during DC
                break;
            case LOGGED_IN:
                lastLoginTick = client.getTickCount();
                break;
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM) {
            return;
        }

        if (COLLECTION_LOG_ITEM_REGEX.matcher(chatMessage.getMessage()).matches() && config.playCollectionLog()) {
            soundEngine.playClip(config.collectionLogJingle());
        } else if (COMBAT_TASK_REGEX.matcher(chatMessage.getMessage()).matches() && config.playCombatTask()) {
            soundEngine.playClip(config.combatTaskJingle());
        } else if (MAHOMES_PATTERN.matcher(chatMessage.getMessage()).matches() && config.playMahomes()) {
            soundEngine.playClip(config.mahomesJingle());
        } else if (FARMING_CONTRACT_PATTERN.matcher(chatMessage.getMessage()).matches() && config.playFarmingContract()) {
            soundEngine.playClip(config.farmingContractJingle());
        } else {
            Matcher m = ACHIEVEMENT_DIARY_TASK_PATTERN.matcher(chatMessage.getMessage());
            if(m.matches()) {
                if (m.group(2).equals("Ardougne") && config.playArdougneDiary()) {
                    soundEngine.playClip(config.ardougneTaskJingle());
                } else if (m.group(2).equals("Desert") && config.playDesertDiary()) {
                    soundEngine.playClip(config.desertTaskJingle());
                } else if (m.group(2).equals("Falador") && config.playFaladorDiary()) {
                    soundEngine.playClip(config.faladorTaskJingle());
                } else if (m.group(2).equals("Fremennik") && config.playFremennikDiary()) {
                    soundEngine.playClip(config.fremennikTaskJingle());
                } else if (m.group(2).equals("Kandarin") && config.playKandarinDiary()) {
                    soundEngine.playClip(config.kandarinTaskJingle());
                } else if (m.group(2).equals("Karamja") && config.playKaramjaDiary()) {
                    soundEngine.playClip(config.karamjaTaskJingle());
                } else if (m.group(2).equals("Kourend & Kebos") && config.playKourendDiary()) {
                    soundEngine.playClip(config.kourendTaskJingle());
                } else if (m.group(2).equals("Lumbridge & Draynor") && config.playLumbridgeDiary()) {
                    soundEngine.playClip(config.lumbridgeTaskJingle());
                } else if (m.group(2).equals("Morytania") && config.playMorytaniaDiary()) {
                    soundEngine.playClip(config.morytaniaTaskJingle());
                } else if (m.group(2).equals("Varrock") && config.playVarrockDiary()) {
                    soundEngine.playClip(config.varrockTaskJingle());
                } else if (m.group(2).equals("Western Provinces") && config.playWesternProvincesDiary()) {
                    soundEngine.playClip(config.westernProvincesTaskJingle());
                } else if (m.group(2).equals("Wilderness") && config.playWildernessDiary()) {
                    soundEngine.playClip(config.wildernessTaskJingle());
                }
            }
        }
    }

    private void checkAndWarnForCollectionLogNotificationSetting(int newVarbitValue) {
        if (!config.playCollectionLog())
            return;

        if (!gameStateLoggedIn)
            return;

        if (badCollectionLogNotificationSettingValues.contains(newVarbitValue)) {
            if (lastColLogSettingWarning == -1 || client.getTickCount() - lastColLogSettingWarning > 16) {
                lastColLogSettingWarning = client.getTickCount();
                sendHighlightedMessage("Please enable \"Collection log - New addition notification\" in your game settings for the Collection Log jingle to play! (The chat message one, pop-up doesn't matter)");
            }
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged varbitChanged) {
        if (varbitChanged.getVarbitId() == Varbits.COLLECTION_LOG_NOTIFICATION) {
            checkAndWarnForCollectionLogNotificationSetting(varbitChanged.getValue());
        }

        // As we can't listen to specific varbits, we get a tonne of events BEFORE the game has even set the player's
        // diary varbits correctly, meaning it assumes every diary is on 0, then suddenly every diary that has been
        // completed gets updated to the true value and tricks the plugin into thinking they only just finished it.
        // To avoid this behaviour, we make sure the current tick count is sufficiently high that we've already passed
        // the initial wave of varbit changes from logging in.
        if (lastLoginTick == -1 || client.getTickCount() - lastLoginTick < 8) {
            return; // Ignoring varbit change as only just logged in
        }

        // Apparently I can't check if it's a particular varbit using the names from Varbits enum, so this is the way
        for (@Varbit int diary : varbitsAchievementDiaries) {
            int newValue = client.getVarbitValue(diary);
            int previousValue = oldAchievementDiaries.getOrDefault(diary, -1);
            oldAchievementDiaries.put(diary, newValue);
            if (previousValue != -1 && previousValue != newValue && isAchievementDiaryCompleted(diary, newValue)) { // && config.announceAchievementDiary()
                // value was not unknown (we know the previous value), value has changed, and value indicates diary is completed now
                switch(diary) {
                    case DIARY_ARDOUGNE_EASY:
                    case DIARY_ARDOUGNE_MEDIUM:
                    case DIARY_ARDOUGNE_HARD:
                    case DIARY_ARDOUGNE_ELITE:
                        if (config.playArdougneDiary()) {
                            soundEngine.playClip(config.ardougneCompletionJingle());
                        }
                        break;
                    case DIARY_DESERT_EASY:
                    case DIARY_DESERT_MEDIUM:
                    case DIARY_DESERT_HARD:
                    case DIARY_DESERT_ELITE:
                        if (config.playDesertDiary()) {
                            soundEngine.playClip(config.desertCompletionJingle());
                        }
                        break;
                    case DIARY_FALADOR_EASY:
                    case DIARY_FALADOR_MEDIUM:
                    case DIARY_FALADOR_HARD:
                    case DIARY_FALADOR_ELITE:
                        if (config.playFaladorDiary()) {
                            soundEngine.playClip(config.faladorCompletionJingle());
                        }
                        break;
                    case DIARY_FREMENNIK_EASY:
                    case DIARY_FREMENNIK_MEDIUM:
                    case DIARY_FREMENNIK_HARD:
                    case DIARY_FREMENNIK_ELITE:
                        if (config.playFremennikDiary()) {
                            soundEngine.playClip(config.fremennikCompletionJingle());
                        }
                        break;
                    case DIARY_KANDARIN_EASY:
                    case DIARY_KANDARIN_MEDIUM:
                    case DIARY_KANDARIN_HARD:
                    case DIARY_KANDARIN_ELITE:
                        if (config.playKandarinDiary()) {
                            soundEngine.playClip(config.kandarinCompletionJingle());
                        }
                        break;
                    case DIARY_KARAMJA_EASY:
                    case DIARY_KARAMJA_MEDIUM:
                    case DIARY_KARAMJA_HARD:
                    case DIARY_KARAMJA_ELITE:
                        if (config.playKaramjaDiary()) {
                            soundEngine.playClip(config.karamjaCompletionJingle());
                        }
                        break;
                    case DIARY_KOUREND_EASY:
                    case DIARY_KOUREND_MEDIUM:
                    case DIARY_KOUREND_HARD:
                    case DIARY_KOUREND_ELITE:
                        if (config.playKourendDiary()) {
                            soundEngine.playClip(config.kourendCompletionJingle());
                        }
                        break;
                    case DIARY_LUMBRIDGE_EASY:
                    case DIARY_LUMBRIDGE_MEDIUM:
                    case DIARY_LUMBRIDGE_HARD:
                    case DIARY_LUMBRIDGE_ELITE:
                        if (config.playLumbridgeDiary()) {
                            soundEngine.playClip(config.lumbridgeCompletionJingle());
                        }
                        break;
                    case DIARY_MORYTANIA_EASY:
                    case DIARY_MORYTANIA_MEDIUM:
                    case DIARY_MORYTANIA_HARD:
                    case DIARY_MORYTANIA_ELITE:
                        if (config.playMorytaniaDiary()) {
                            soundEngine.playClip(config.morytaniaCompletionJingle());
                        }
                        break;
                    case DIARY_VARROCK_EASY:
                    case DIARY_VARROCK_MEDIUM:
                    case DIARY_VARROCK_HARD:
                    case DIARY_VARROCK_ELITE:
                        if (config.playVarrockDiary()) {
                            soundEngine.playClip(config.varrockCompletionJingle());
                        }
                        break;
                    case DIARY_WESTERN_EASY:
                    case DIARY_WESTERN_MEDIUM:
                    case DIARY_WESTERN_HARD:
                    case DIARY_WESTERN_ELITE:
                        if (config.playWesternProvincesDiary()) {
                            soundEngine.playClip(config.westernProvincesCompletionJingle());
                        }
                        break;
                    case DIARY_WILDERNESS_EASY:
                    case DIARY_WILDERNESS_MEDIUM:
                    case DIARY_WILDERNESS_HARD:
                    case DIARY_WILDERNESS_ELITE:
                        if (config.playWildernessDiary()) {
                            soundEngine.playClip(config.wildernessCompletionJingle());
                        }
                        break;
                }
            }
        }
    }

    private boolean isAchievementDiaryCompleted(int diary, int value) {
        switch (diary) {
            case DIARY_KARAMJA_EASY:
            case DIARY_KARAMJA_MEDIUM:
            case DIARY_KARAMJA_HARD:
                return value == 2; // jagex, why?
            default:
                return value == 1;
        }
    }

    @Provides
    JingleJingleConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(JingleJingleConfig.class);
    }

    private void sendHighlightedMessage(String message) {
        String highlightedMessage = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(message)
                .build();

        chatMessageManager.queue(QueuedMessage.builder()
                .type(ChatMessageType.CONSOLE)
                .runeLiteFormattedMessage(highlightedMessage)
                .build());
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (JingleJingleConfig.GROUP.equals(event.getGroup())) {
            /** Play the sound the user just selected */
            if ("collectionLogJingle".equals(event.getKey())) {
                soundEngine.playClip(config.collectionLogJingle());
            } else if ("combatTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.combatTaskJingle());
            } else if ("mahomesJingle".equals(event.getKey())) {
                soundEngine.playClip(config.mahomesJingle());
            } else if ("farmingContractJingle".equals(event.getKey())) {
                soundEngine.playClip(config.farmingContractJingle());
            } else if ("ardougneTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.ardougneTaskJingle());
            } else if ("ardougneCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.ardougneCompletionJingle());
            } else if ("desertTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.desertTaskJingle());
            } else if ("desertCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.desertCompletionJingle());
            } else if ("faladorTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.faladorTaskJingle());
            } else if ("faladorCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.faladorCompletionJingle());
            } else if ("fremennikTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.fremennikTaskJingle());
            } else if ("fremennikCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.fremennikCompletionJingle());
            } else if ("kandarinTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.kandarinTaskJingle());
            } else if ("kandarinCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.kandarinCompletionJingle());
            } else if ("karamjaTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.karamjaTaskJingle());
            } else if ("karamjaCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.karamjaCompletionJingle());
            } else if ("kourendTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.kourendTaskJingle());
            } else if ("kourendCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.kourendCompletionJingle());
            } else if ("lumbridgeTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.lumbridgeTaskJingle());
            } else if ("lumbridgeCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.lumbridgeCompletionJingle());
            } else if ("morytaniaTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.morytaniaTaskJingle());
            } else if ("morytaniaCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.morytaniaCompletionJingle());
            } else if ("varrockTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.varrockTaskJingle());
            } else if ("varrockCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.varrockCompletionJingle());
            } else if ("westernProvincesTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.westernProvincesTaskJingle());
            } else if ("westernProvincesCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.westernProvincesCompletionJingle());
            } else if ("wildernessTaskJingle".equals(event.getKey())) {
                soundEngine.playClip(config.wildernessTaskJingle());
            } else if ("wildernessCompletionJingle".equals(event.getKey())) {
                soundEngine.playClip(config.wildernessCompletionJingle());
            }
        }
    }
}
