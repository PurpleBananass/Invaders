package engine;

import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

public class SoundManager {
    public static HashMap<String, Clip> clips= new HashMap<>();

    public static void playSound(String soundFilePath, String clipName, boolean isLoop) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    File soundFile = new File(soundFilePath);
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    if(isLoop){clip.loop(-1);}
                    else{clip.start();}
                    clips.put(clipName, clip); // Clip 객체를 배열에 저장
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void stopSound(String clipName){
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            clip.stop();
        }
    }
}