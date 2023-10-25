package engine;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
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
    private static final float one = ((Math.abs(minimum)+Math.abs(maximum))/100);
    private static float master = getValue(masterVolume);

    public static void playSound(String soundFilePathShort, String clipName, boolean isLoop, boolean isBgm) {
        String soundFilePath = "res/sound/"+soundFilePathShort+".wav";
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
                    FloatControl floatControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
                    floatControl.setValue(master);
                    clips.put(clipName, clip);
                    if(isBgm) {
                        bgms.add(clip);
                    } else {
                        clip.addLineListener(event -> {
                            if (event.getType() == LineEvent.Type.STOP) {
                                clip.close();
                            }
                        });
                    }
                    if (isLoop) {
                        clip.loop(-1);
                    } else {
                        clip.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void playSound(String soundFilePathShort, String clipName, boolean isLoop, boolean isBgm, float fadeInSpeed) {
        String soundFilePath = "res/sound/"+soundFilePathShort+".wav";
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
                    clips.put(clipName, clip);
                    if(isBgm) {
                        bgms.add(clip);
                    } else {
                        clip.addLineListener(event -> {
                            if (event.getType() == LineEvent.Type.STOP) {
                                clip.close();
                            }
                        });
                    }
                    if (isLoop) {
                        clip.loop(-1);
                    } else {
                        clip.start();
                    }
                    fadeIn(clip, fadeInSpeed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void stopSound(String clipName) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            bgms.remove(clip);
            clips.remove(clipName);
            clip.close();
        }
    }

    public static void stopSound(String clipName, float fadeoutSpeed) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            new Thread(new Runnable() {
                public void run() {
                    FloatControl floatControl = (FloatControl) clips.get(clipName).getControl(Type.MASTER_GAIN);
                    float volume = masterVolume;
                    float tmpOne = volume/100;
                    float minimumValue = getValue(0);
                    float value = getValue(volume);
                    while(floatControl.getValue()>minimumValue){
                        if(value<minimum){
                            floatControl.setValue(minimum);
                            break;
                        }
                        floatControl.setValue(value);
                        volume-=(tmpOne*fadeoutSpeed);
                        value = getValue(volume);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    bgms.remove(clip);
                    clips.remove(clipName);
                    clip.close();
                }
            }).start();
        }
    }

    private static void fadeIn(Clip clip, float fadeInSpeed) {
        new Thread(new Runnable() {

            public void run() {
                FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue(minimum);
                float volume = 0;
                float value = getValue(volume);
                float tmpOne = masterVolume/100;
                float maxValue = getValue(masterVolume);
                while(value<maxValue){
                    floatControl.setValue(value);
                    volume+=(tmpOne*fadeInSpeed);
                    value = getValue(volume);
                    floatControl.setValue(value);
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
        master = getValue(masterVolume);
        if(master > maximum) master = maximum;
        else if(master < minimum) master = minimum;
        for (Clip clip : clips.values()) {
            if (clip != null && clip.isActive()) {
                FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue(master);
            }
        }
    }

    public static void bgmSetting(boolean bgm){
        if(bgm){
            for(Clip clip : bgms){
                FloatControl floatControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue(getValue(masterVolume));
            }
        }
        else{
            for(Clip clip : bgms){
                FloatControl floatControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue(getValue(0));
            }
        }
    }

    private static float getVolume(float res) {
        double temp = (res - minimum) / one;
        return (float) Math.pow(10, temp / 50);
    }

    private static float getValue(float volume){
        float res = (float)(minimum + one*(50*Math.log10(volume)));
        if(res<minimum) return minimum;
        else if(res>maximum) return maximum;
        else return res;
    }

    public static boolean isPlaying(String clipName){
        Clip clip = clips.get(clipName);
        if(clip!=null && clip.isActive()) return true;
        else return false;
    }

    public static void setVolume(String clipName, float percent){
        Clip clip = clips.get(clipName);
        FloatControl floatcontrol = (FloatControl)clip.getControl(Type.MASTER_GAIN);
        float volume = getVolume(floatcontrol.getValue());
        floatcontrol.setValue(getValue((percent/100)*volume));
    }

    public static void playBGM(int levelNum) {
        String soundFilePathShort = "BGM/B_Level" + Integer.toString(levelNum);
        String clipName = "level" + Integer.toString(levelNum);
        playSound(soundFilePathShort, clipName, true, true);
    }

    public static void stopBGM(int levelNum, float fadeOutSpeed) {
        String clipName = "level" + Integer.toString(levelNum);
        stopSound(clipName, fadeOutSpeed);
    }

    public static void resetBGM(){
        for (int i = 0; i < 7; i++) {
            String clipName = "level" + Integer.toString(i);
            stopSound(clipName);
        }
    }
}