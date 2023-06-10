/**
 * Big text generator: https://fsymbols.com/generators/tarty/
 */

package com.tayaramisu;

import net.runelite.client.config.*;

@ConfigGroup(JingleJingleConfig.GROUP)
public interface JingleJingleConfig extends Config {
    String GROUP = "jingles";

    /**
     * ░██████╗░███████╗███╗░░██╗███████╗██████╗░░█████╗░██╗░░░░░
     * ██╔════╝░██╔════╝████╗░██║██╔════╝██╔══██╗██╔══██╗██║░░░░░
     * ██║░░██╗░█████╗░░██╔██╗██║█████╗░░██████╔╝███████║██║░░░░░
     * ██║░░╚██╗██╔══╝░░██║╚████║██╔══╝░░██╔══██╗██╔══██║██║░░░░░
     * ╚██████╔╝███████╗██║░╚███║███████╗██║░░██║██║░░██║███████╗
     * ░╚═════╝░╚══════╝╚═╝░░╚══╝╚══════╝╚═╝░░╚═╝╚═╝░░╚═╝╚══════╝
     */

    @Range(min = 0, max = 200)
    @ConfigItem(
            keyName = "jingleVolume",
            name = "Jingle volume",
            description = "How loud this plugin's jingles are played",
            position = 0
    )
    default int jingleVolume() {
        return 100;
    }

    @ConfigItem(
            keyName = "playCollectionLog",
            name = "Collection Log",
            description = "Should a jingle play when you fill a new collection log slot",
            position = 1
    )
    default boolean playCollectionLog() {
        return true;
    }

    @ConfigItem(
            keyName = "collectionLogJingle",
            name = "Collection Log Jingle",
            description = "The jingle to play when you fill a new collection log slot",
            position = 2
    )
    default Sound collectionLogJingle() {
        return Sound.LEAGUE_AREA_UNLOCK;
    }

    @ConfigItem(
            keyName = "playCombatTask",
            name = "Combat Task",
            description = "Should a jingle play when you complete a new combat task",
            position = 3
    )
    default boolean playCombatTask() {
        return true;
    }

    @ConfigItem(
            keyName = "combatTaskJingle",
            name = "Combat Task Jingle",
            description = "The jingle to play when you complete a new combat task",
            position = 4
    )
    default Sound combatTaskJingle() {
        return Sound.LEAGUE_TASK_COMPLETE;
    }

    @ConfigItem(
            keyName = "playMahomes",
            name = "Mahogany Homes",
            description = "Should a jingle play when you complete a Mahogany Homes contract",
            position = 5
    )
    default boolean playMahomes() {
        return true;
    }

    @ConfigItem(
            keyName = "mahomesJingle",
            name = "Mahogany Homes Jingle",
            description = "The jingle to play when you complete a Mahogany Homes contract",
            position = 6
    )
    default Sound mahomesJingle() {
        return Sound.SCHEMATICS_COMPLETED;
    }

    @ConfigItem(
            keyName = "playFarmingContract",
            name = "Farming Contract",
            description = "Should a jingle play when you complete a Farming Contract",
            position = 7
    )
    default boolean playFarmingContract() {
        return true;
    }

    @ConfigItem(
            keyName = "farmingContractJingle",
            name = "Farming Contract Jingle",
            description = "The jingle to play when you complete a Farming Contract",
            position = 8
    )
    default Sound farmingContractJingle() {
        return Sound.CACTI_CHECK;
    }

    /**
     * ░█████╗░░█████╗░██╗░░██╗██╗███████╗██╗░░░██╗███████╗███╗░░░███╗███████╗███╗░░██╗████████╗
     * ██╔══██╗██╔══██╗██║░░██║██║██╔════╝██║░░░██║██╔════╝████╗░████║██╔════╝████╗░██║╚══██╔══╝
     * ███████║██║░░╚═╝███████║██║█████╗░░╚██╗░██╔╝█████╗░░██╔████╔██║█████╗░░██╔██╗██║░░░██║░░░
     * ██╔══██║██║░░██╗██╔══██║██║██╔══╝░░░╚████╔╝░██╔══╝░░██║╚██╔╝██║██╔══╝░░██║╚████║░░░██║░░░
     * ██║░░██║╚█████╔╝██║░░██║██║███████╗░░╚██╔╝░░███████╗██║░╚═╝░██║███████╗██║░╚███║░░░██║░░░
     * ╚═╝░░╚═╝░╚════╝░╚═╝░░╚═╝╚═╝╚══════╝░░░╚═╝░░░╚══════╝╚═╝░░░░░╚═╝╚══════╝╚═╝░░╚══╝░░░╚═╝░░░
     *
     * ██████╗░██╗░█████╗░██████╗░██╗███████╗░██████╗
     * ██╔══██╗██║██╔══██╗██╔══██╗██║██╔════╝██╔════╝
     * ██║░░██║██║███████║██████╔╝██║█████╗░░╚█████╗░
     * ██║░░██║██║██╔══██║██╔══██╗██║██╔══╝░░░╚═══██╗
     * ██████╔╝██║██║░░██║██║░░██║██║███████╗██████╔╝
     * ╚═════╝░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝╚═╝╚══════╝╚═════╝░
     */
    @ConfigSection(
            name = "Achievement Diary Settings",
            description = "Jingle settings for achievement diary tasks",
            position = 9,
            closedByDefault = true
    )
    String achievementDiarySettings = "achievementDiarySettings";

    @ConfigItem(
            keyName = "playArdougneDiary",
            name = "Ardougne Diary",
            description = "Should a jingle play when you complete an Ardougne achievement diary task",
            position = 0,
            section = "achievementDiarySettings"
    )
    default boolean playArdougneDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "ardougneTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete an Ardougne achievement diary task",
            position = 1,
            section = "achievementDiarySettings"
    )
    default Sound ardougneTaskJingle() {
        return Sound.DICE_WIN;
    }
    @ConfigItem(
            keyName = "ardougneCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Ardougne achievement diary",
            position = 2,
            section = "achievementDiarySettings"
    )
    default Sound ardougneCompletionJingle() {
        return Sound.SHAIKAHAN_DEFEATED;
    }

    @ConfigItem(
            keyName = "playDesertDiary",
            name = "Desert Diary",
            description = "Should a jingle play when you complete a Desert achievement diary task",
            position = 3,
            section = "achievementDiarySettings"
    )
    default boolean playDesertDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "desertTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Desert achievement diary task",
            position = 4,
            section = "achievementDiarySettings"
    )
    default Sound desertTaskJingle() {
        return Sound.AGILITY_PYRAMID;
    }
    @ConfigItem(
            keyName = "desertCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Desert achievement diary",
            position = 5,
            section = "achievementDiarySettings"
    )
    default Sound desertCompletionJingle() {
        return Sound.TOA_PATH_COMPLETE;
    }

    @ConfigItem(
            keyName = "playFaladorDiary",
            name = "Falador Diary",
            description = "Should a jingle play when you complete a Falador achievement diary task",
            position = 6,
            section = "achievementDiarySettings"
    )
    default boolean playFaladorDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "faladorTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Falador achievement diary task",
            position = 7,
            section = "achievementDiarySettings"
    )
    default Sound faladorTaskJingle() {
        return Sound.RECRUIT_DRIVE_1;
    }
    @ConfigItem(
            keyName = "faladorCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Falador achievement diary",
            position = 8,
            section = "achievementDiarySettings"
    )
    default Sound faladorCompletionJingle() {
        return Sound.RECRUIT_DRIVE_2;
    }

    @ConfigItem(
            keyName = "playFremennikDiary",
            name = "Fremennik Diary",
            description = "Should a jingle play when you complete a Fremennik achievement diary task",
            position = 9,
            section = "achievementDiarySettings"
    )
    default boolean playFremennikDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "fremennikTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Fremennik achievement diary task",
            position = 10,
            section = "achievementDiarySettings"
    )
    default Sound fremennikTaskJingle() {
        return Sound.FREM_BERATING_KING;
    }
    @ConfigItem(
            keyName = "fremennikCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Fremennik achievement diary",
            position = 11,
            section = "achievementDiarySettings"
    )
    default Sound fremennikCompletionJingle() {
        return Sound.KELDAGRIM_TRADING;
    }

    @ConfigItem(
            keyName = "playKandarinDiary",
            name = "Kandarin Diary",
            description = "Should a jingle play when you complete a Kandarin achievement diary task",
            position = 12,
            section = "achievementDiarySettings"
    )
    default boolean playKandarinDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "kandarinTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Kandarin achievement diary task",
            position = 13,
            section = "achievementDiarySettings"
    )
    default Sound kandarinTaskJingle() {
        return Sound.BURTHORPE_GAMES;
    }
    @ConfigItem(
            keyName = "kandarinCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Kandarin achievement diary",
            position = 14,
            section = "achievementDiarySettings"
    )
    default Sound kandarinCompletionJingle() {
        return Sound.GNOME_SUCCESS;
    }

    @ConfigItem(
            keyName = "playKaramjaDiary",
            name = "Karamja Diary",
            description = "Should a jingle play when you complete a Karamja achievement diary task",
            position = 15,
            section = "achievementDiarySettings"
    )
    default boolean playKaramjaDiary() {
        return false; // Karamja already has jingles, so don't override them by default
    }
    @ConfigItem(
            keyName = "karamjaTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Karamja achievement diary task",
            position = 16,
            section = "achievementDiarySettings"
    )
    default Sound karamjaTaskJingle() {
        return Sound.GNOMEBALL_GOAL;
    }
    @ConfigItem(
            keyName = "karamjaCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Karamja achievement diary",
            position = 17,
            section = "achievementDiarySettings"
    )
    default Sound karamjaCompletionJingle() {
        return Sound.MM1_JUNGLE_DEMON;
    }

    @ConfigItem(
            keyName = "playKourendDiary",
            name = "Kourend Diary",
            description = "Should a jingle play when you complete a Kourend & Kebos achievement diary task",
            position = 18,
            section = "achievementDiarySettings"
    )
    default boolean playKourendDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "kourendTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Kourend & Kebos achievement diary task",
            position = 19,
            section = "achievementDiarySettings"
    )
    default Sound kourendTaskJingle() {
        return Sound.RFD_LUMBRIDGE_GUIDE;
    }
    @ConfigItem(
            keyName = "kourendCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Kourend & Kebos achievement diary",
            position = 20,
            section = "achievementDiarySettings"
    )
    default Sound kourendCompletionJingle() {
        return Sound.RATCATCHER_KING_DIES;
    }

    @ConfigItem(
            keyName = "playLumbridgeDiary",
            name = "Lumbridge Diary",
            description = "Should a jingle play when you complete a Lumbridge achievement diary task",
            position = 21,
            section = "achievementDiarySettings"
    )
    default boolean playLumbridgeDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "lumbridgeTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Lumbridge achievement diary task",
            position = 22,
            section = "achievementDiarySettings"
    )
    default Sound lumbridgeTaskJingle() {
        return Sound.EASTER_2005;
    }
    @ConfigItem(
            keyName = "lumbridgeCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Lumbridge achievement diary",
            position = 23,
            section = "achievementDiarySettings"
    )
    default Sound lumbridgeCompletionJingle() {
        return Sound.LEAGUE_TUT_COMPLETE;
    }

    @ConfigItem(
            keyName = "playMorytaniaDiary",
            name = "Morytania Diary",
            description = "Should a jingle play when you complete a Morytania achievement diary task",
            position = 24,
            section = "achievementDiarySettings"
    )
    default boolean playMorytaniaDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "morytaniaTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Morytania achievement diary task",
            position = 25,
            section = "achievementDiarySettings"
    )
    default Sound morytaniaTaskJingle() {
        return Sound.TEMPLE_TREK_SUCCESS;
    }
    @ConfigItem(
            keyName = "morytaniaCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Morytania achievement diary",
            position = 26,
            section = "achievementDiarySettings"
    )
    default Sound morytaniaCompletionJingle() {
        return Sound.TOB_WAVE_COMPLETE;
    }

    @ConfigItem(
            keyName = "playVarrockDiary",
            name = "Varrock Diary",
            description = "Should a jingle play when you complete a Varrock achievement diary task",
            position = 27,
            section = "achievementDiarySettings"
    )
    default boolean playVarrockDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "varrockTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Varrock achievement diary task",
            position = 28,
            section = "achievementDiarySettings"
    )
    default Sound varrockTaskJingle() {
        return Sound.FORGETTABLE_PUZZLE;
    }
    @ConfigItem(
            keyName = "varrockCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Varrock achievement diary",
            position = 29,
            section = "achievementDiarySettings"
    )
    default Sound varrockCompletionJingle() {
        return Sound.POSTIE_PETE_THEME;
    }

    @ConfigItem(
            keyName = "playWesternProvincesDiary",
            name = "Western Provinces Diary",
            description = "Should a jingle play when you complete a Western Provinces achievement diary task",
            position = 30,
            section = "achievementDiarySettings"
    )
    default boolean playWesternProvincesDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "westernProvincesTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Western Provinces achievement diary task",
            position = 31,
            section = "achievementDiarySettings"
    )
    default Sound westernProvincesTaskJingle() {
        return Sound.CASTLE_WARS_WIN;
    }
    @ConfigItem(
            keyName = "westernProvincesCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Western Provinces achievement diary",
            position = 32,
            section = "achievementDiarySettings"
    )
    default Sound westernProvincesCompletionJingle() {
        return Sound.SECURITY_BOX_HEALTH;
    }

    @ConfigItem(
            keyName = "playWildernessDiary",
            name = "Wilderness Diary",
            description = "Should a jingle play when you complete a Wilderness achievement diary task",
            position = 33,
            section = "achievementDiarySettings"
    )
    default boolean playWildernessDiary() {
        return true;
    }
    @ConfigItem(
            keyName = "wildernessTaskJingle",
            name = "Task Jingle",
            description = "The jingle to play when you complete a Wilderness achievement diary task",
            position = 34,
            section = "achievementDiarySettings"
    )
    default Sound wildernessTaskJingle() {
        return Sound.JORMUNGAND_DEFEATED;
    }
    @ConfigItem(
            keyName = "wildernessCompletionJingle",
            name = "Completion Jingle",
            description = "The jingle to play when you complete any tier of the Wilderness achievement diary",
            position = 35,
            section = "achievementDiarySettings"
    )
    default Sound wildernessCompletionJingle() {
        return Sound.FIGHT_PITS_CHAMP;
    }
}
