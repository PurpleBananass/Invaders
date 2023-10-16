package engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

public class SoundManager {
    public static HashMap<String, Clip> clips = new HashMap<>();

    public static void playSound(String soundFilePath, String clipName, boolean isLoop) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    File soundFile = new File(soundFilePath);
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    if (isLoop) {
                        clip.loop(-1);
                    } else {
                        clip.start();
                    }
                    clips.put(clipName, clip); // Clip 객체를 배열에 저장
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void playSound(String soundFilePath, String clipName, boolean isLoop, float fadeInSpeed) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    File soundFile = new File(soundFilePath);
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                    floatControl.setValue(-40);
                    if (isLoop) {
                        clip.loop(-1);
                    } else {
                        clip.start();
                    }
                    clips.put(clipName, clip);
                    fadeIn(clipName, fadeInSpeed);// Clip 객체를 배열에 저장
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void stopSound(String clipName) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            clip.stop();
        }
    }

    public static void stopSound(String clipName, float fadeoutSpeed) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            new Thread(new Runnable() {
                public void run() {
                    float volume = ((FloatControl) clip.getControl(Type.MASTER_GAIN)).getValue();
                    FloatControl floatControl = (FloatControl) clips.get(clipName).getControl(Type.MASTER_GAIN);
                    while (volume > -40) {
                        floatControl.setValue(volume);
                        volume -= (0.4 * fadeoutSpeed);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    clip.stop();
                }
            }).start();
        }
    }

    private static void fadeIn(String clipName, float fadeInSpeed) {
        new Thread(new Runnable() {
            Clip clip = clips.get(clipName);

            public void run() {
                float volume = ((FloatControl) clip.getControl(Type.MASTER_GAIN)).getValue();
                FloatControl floatControl = (FloatControl) clips.get(clipName).getControl(Type.MASTER_GAIN);
                floatControl.setValue(-80);
                while (volume < 0) {
                    floatControl.setValue(volume);
                    volume += (0.4 * fadeInSpeed);
                    if(volume>0) volume = 0;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}