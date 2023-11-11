package EnginePrime;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

import java.util.UUID;

public class SoundManager  implements GManager{
    public void PreRender(){};
    
    public void LateRender(){};
    private static boolean IsMute = false;

    public class PlayProp {
        /* 
            단순히 재생만 하고자 할 경우 ClipName은 NULL로 지정.
        */ 
        public String FilePath = null;
        public String Clipname = null;
        public long MicroSecPos = 0;
        public int count = 0;
        public float fadeIn = 0;

        public PlayProp(String file, String name){
            FilePath = file;
            Clipname = name;
            Clip clip = clips.get(Clipname);
            if(clip !=null){
                MicroSecPos = clip.getMicrosecondPosition();
            }
        };
    }

    private static float master = 0;
    public static final float minimum = -80;
    public static final float maximum = 6;

    private static SoundManager instance;

    private SoundManager() {};


    public void Initialize(){
        //메모리 누수 방지
        GameManager gm = GameManager.getInstance();
        gm.ExitCode.add(new Runnable() {
            @Override
            public void run() {
                for (Clip clip : clips.values()) {
                    clip.close();
                }
            }
        });
    };

    public void PreUpdate(){};

    public void LateUpdate(){};

    public void Exit(){};

    public static boolean IsMute(){
        return IsMute;
    }

    public static float GetVolumePercent(){

        return (master - minimum) /(maximum -minimum) * 100;

    }



    private static void SetVolume(Clip clip , float value){
        FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
        if(IsMute){
            floatControl.setValue(minimum);
        }
        else{
            floatControl.setValue(value);
        }
    }

    public static void SetMute(boolean b){
        IsMute = b;
        for (Clip clip : clips.values()) {
            SetVolume(clip , 0);
        }
    }
    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();
        return instance;
    }

    private static HashMap<String, Clip> clips = new HashMap<>();

    public static void playSound(PlayProp prop) {
        try {
            Clip clip = clips.get(prop.Clipname);
            if (clip == null) {
                clip = NewClip(prop);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(prop.FilePath));
                clip.open(audioIn);
            }
            SetVolume(clip,master);
            clip.setMicrosecondPosition(prop.MicroSecPos);
            clip.loop(prop.count);
            fadeIn(prop.Clipname, prop.fadeIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Clip NewClip(PlayProp prop) {
        Clip clip;
        try {
            clip = AudioSystem.getClip();
            String clipname = prop.Clipname == null ? UUID.randomUUID().toString() : prop.Clipname;
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (clip.getMicrosecondPosition() == clip.getMicrosecondLength()) {
                        clips.remove(clipname);
                        CloseClip(clip);
                    }
                }
            });
            clips.put(clipname, clip);
            return clip;
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void PauseClip(String clipName) {
        Clip clip = clips.get(clipName);
        if(clip== null){
            return;
        }
        clip.stop();
    }

    public static void StopAll(){
        for (String key : clips.keySet()) {
            Clip clip = clips.get(key);
            CloseClip(clip);
        }
        clips.clear();
    }

    static void CloseClip(Clip clip){
        new Thread(new Runnable(){
            public void run() {
                clip.close();
            }
        }).start();
    }


    public static void StopClip(String clipName) {
        Clip clip = clips.get(clipName);
        if(clip== null){
            return;
        }
        clips.remove(clipName);
        CloseClip(clip);
    }
    public static void stopClip(String clipName, float fout) {
        fadeOut(clipName, fout);
    }


    private static void fadeIn(String name, float t) {
        if (t > 0) {
            Clip clip = clips.get(name);
            new Thread(new Runnable() {
                public void run() {
                    SetVolume(clip,minimum);
                    double elapsed = 0;
                    while (elapsed < t) {
                        elapsed += GameManager.getInstance().Et.GetElapsedSeconds();
                        elapsed = elapsed > t ? t : elapsed;
                        SetVolume(clip,(float) (minimum + master * elapsed / t));
                    }
                }
            }).start();
        }
    }

    private static void fadeOut(String name, float t) {
        Clip clip = clips.get(name);
        if(clip==null){
            return;
        }
        clips.remove(name);
        new Thread(new Runnable(){
            public void run() {
                double elapsed = 0;
                while (elapsed < t) {
                    elapsed += GameManager.getInstance().Et.GetElapsedSeconds();
                    elapsed = elapsed > t ? t : elapsed;
                    SetVolume(clip,(float)(master +(minimum - master )*elapsed/t));
                }
                CloseClip(clip);
            }
        }).start();
    }

    public static float GetMasterVolume() {
        return master;
    }

    public static void setMasterVolume(float volume) {
        master = volume;
        if (master > maximum)
            master = maximum;
        else if (master < minimum)
            master = minimum;

        for (Clip clip : clips.values()) {
            SetVolume(clip, master);
        }
    }
}