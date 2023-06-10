/**
 * From C Engineer Completed: https://github.com/m0bilebtw/c-engineer-completed
 */

package com.tayaramisu;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

@Singleton
@Slf4j
public class SoundEngine {

    @Inject
    private JingleJingleConfig config;

    @Inject
    private Client client;

    private static final long CLIP_MTIME_UNLOADED = -2;

    private long lastClipMTime = CLIP_MTIME_UNLOADED;
    private Clip clip = null;

    private boolean loadClip(Sound sound) {
        try (InputStream stream = new BufferedInputStream(SoundFileManager.getSoundStream(sound))) {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream)) {
                clip.open(audioInputStream); // liable to error with pulseaudio, works on windows, one user informs me mac works
            }
            return true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.warn("Failed to load jingle " + sound, e);
        }
        return false;
    }

    public void playClip(Sound sound) {
        long currentMTime = System.currentTimeMillis();
        if (clip == null || currentMTime != lastClipMTime || !clip.isOpen()) {
            if (clip != null && clip.isOpen()) {
                clip.close();
            }

            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                lastClipMTime = CLIP_MTIME_UNLOADED;
                log.warn("Failed to get clip for jingle " + sound, e);
                return;
            }

            lastClipMTime = currentMTime;
            if (!loadClip(sound)) {
                return;
            }
        }

        int lastMusicVolume = client.getMusicVolume();
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float gain = 20f * (float) Math.log10(lastMusicVolume / 100f) - 0.07f;
        gain = Math.min(gain, volume.getMaximum());
        gain = Math.max(gain, volume.getMinimum());
        volume.setValue(gain);

        client.setMusicVolume(0);
        clip.addLineListener(e -> {
            if (e.getType() == LineEvent.Type.STOP) {
                client.setMusicVolume(lastMusicVolume);
            }
        });

        // 1 tick delay to allow time for in-game music to stop before starting the jingle
        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // From RuneLite base client Notifier class:
                        // Using loop instead of start + setFramePosition prevents the clip
                        // from not being played sometimes, presumably a race condition in the
                        // underlying line driver
                        clip.loop(0);
                        // Close the thread
                        t.cancel();
                    }
                },
                600
        );
    }

    public void close() {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }
    }
}
