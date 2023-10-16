//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package engine;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

public class SoundManager {
    private Clip clip;

    public SoundManager(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("sound file not found");
            }

            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            this.clip = AudioSystem.getClip();
            this.clip.open(sound);
        } catch (Exception var4) {
            System.out.println(var4);
        }

    }

    public void play() {
        this.clip.start();
    }

    public void loop() {
        this.clip.loop(-1);
    }

    public void stop() {
        this.clip.stop();
        this.clip.close();
    }

}
