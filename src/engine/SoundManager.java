package engine;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

public class SoundManager {
    private static HashMap<String, Clip> clips = new HashMap<>();
    private static ArrayList<Clip> bgms = new ArrayList<>();
    private static float masterVolume;

    static {
        try {
            masterVolume = Core.getFileManager().loadSettings().get(0).getValue();
        } catch (IOException e) {
            Core.getLogger().warning("Couldn't load Settings!");
        }
    }

    private static final float minimum = -80;
    private static final float maximum = 6;
    private static final float one = Math.abs((minimum - maximum) / 100);
    private static float master = (float) (minimum + one * (50 * Math.log10(masterVolume)));

    private static String[] bgmList = {"res/sound/BGM/BGM_LevelDesign_GameScreen_Level1_SpaceInvadersOST_Venus.wav", "res/sound/BGM/BGM_LevelDesign_GameScreen_Level2_SpaceInvadersOST_Uranus.wav", "res/sound/BGM/BGM_LevelDesign_GameScreen_Level3_SpaceInvadersOST_Earth.wav", "res/sound/BGM/BGM_LevelDesign_GameScreen_Level4_SpaceInvadersOST_Jupiter.wav", "res/sound/BGM/BGM_LevelDesign_GameScreen_Level5_SpaceInvadersOST_Neptune.wav", "res/sound/BGM/BGM_LevelDesign_GameScreen_Level6_SpaceInvadersOST_Pluto.wav", "res/sound/BGM/BGM_LevelDesign_GameScreen_Level7_SpaceInvaders OST_Saturn.wav", "res/sound/BGM/BGM_MainMenu_TitleScreen_Main_AlienSpaceship.wav", "res/sound/BGM/BGM_MainMenu_TitleScreen_Main_AmbientSpace.wav", "res/sound/BGM/BGM_MainMenu_TitleScreen_Main_SpaceHanger.wav", "res/sound/BGM/BGM_MainMenu_TitleScreen_Main_Spacedrone.wav", "res/sound/BGM/BGM_Player&EnemyShipVariety_Ship_ShipMoving_ShipMoving.wav", "res/sound/BGM/BGM_Records&Achievement_AchievementScreen_AchievementScreen_MorseCode.wav", "res/sound/BGM/BGM_Records&Achievement_HighScoreScreen_HighScoreScreen_UfoSounds.wav", "res/sound/BGM/BGM_Records&Achievement_ScoreScreen_GameOver_UfoLanding.wav"};
    private static ArrayList<String> bgmArray = new ArrayList<>(Arrays.asList(bgmList));

    public static void playSound(String soundFilePath, String clipName, boolean isLoop, boolean isBgm) {
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
                    floatControl.setValue(master);
                    if (isLoop) {
                        clip.loop(-1);
                    } else {
                        clip.start();
                    }
                    clips.put(clipName, clip);
                    if (isBgm) bgms.add(clip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void playSound(String soundFilePath, String clipName, boolean isLoop, boolean isBgm, float fadeInSpeed) {
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
                    floatControl.setValue(minimum);
                    if (isLoop) {
                        clip.loop(-1);
                    } else {
                        clip.start();
                    }
                    clips.put(clipName, clip);
                    if (isBgm) bgms.add(clip);
                    fadeIn(clipName, fadeInSpeed);
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
            bgms.remove(clip);
        }
    }

    public static void stopSound(String clipName, float fadeoutSpeed) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            new Thread(new Runnable() {
                public void run() {
                    FloatControl floatControl = (FloatControl) clips.get(clipName).getControl(Type.MASTER_GAIN);
                    float volume = masterVolume;
                    while (volume > minimum) {
                        volume -= fadeoutSpeed;
                        if (volume < minimum) volume = minimum;
                        floatControl.setValue((float) (minimum + one * (50 * Math.log10(volume))));
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    clip.stop();
                    bgms.remove(clip);
                }
            }).start();
        }
    }

    private static void fadeIn(String clipName, float fadeInSpeed) {
        new Thread(new Runnable() {
            Clip clip = clips.get(clipName);

            public void run() {
                float volume = 0;
                FloatControl floatControl = (FloatControl) clips.get(clipName).getControl(Type.MASTER_GAIN);
                floatControl.setValue(minimum);
                while (volume < masterVolume) {
                    volume += fadeInSpeed;
                    if (volume > masterVolume) volume = masterVolume;
                    floatControl.setValue((float) (minimum + one * (50 * Math.log10(volume))));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public static void setMasterVolume(float volume) {
        masterVolume = volume;
        master = (float) (minimum + one * (50 * Math.log10(volume)));
        for (Clip clip : clips.values()) {
            if (clip != null && clip.isActive()) {
                FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue(master);
            }
        }
    }

    public static void bgmSetting(boolean bgm) {
        if (bgm) {
            for (Clip clip : bgms) {
                FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue(master);
            }
        } else {
            for (Clip clip : bgms) {
                FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue((float) (minimum + one * (50 * Math.log10(0))));
            }
        }
    }

    public static void setVolume(String clipName, double percent) {
        Clip clip = clips.get(clipName);
        FloatControl floatcontrol = (FloatControl) clip.getControl(Type.MASTER_GAIN);
        float volume = floatcontrol.getValue();
        floatcontrol.setValue((float) (volume * percent));
    }

    public static void playBGM(int levelNum) {
        String soundFilePath = bgmArray.get(levelNum);
        String clipName = "level" + Integer.toString(levelNum);
        playSound(soundFilePath, clipName, true, true);
    }

    public static void stopBGM(int levelNum, float fadeOutSpeed) {
        String clipName = "level" + Integer.toString(levelNum);
        stopSound(clipName, fadeOutSpeed);
    }
    
    public static void resetBGM(){
        for (int i = 0; i < bgmList.length; i++) {
            String clipName = "level" + Integer.toString(i);
            stopSound(clipName);
        }
    }
}