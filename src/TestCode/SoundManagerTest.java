package TestCode;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.File;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import EnginePrime.SoundManager;

public class SoundManagerTest {

	static SoundManager.PlayProp SoundProp;
	static SoundManager sm = SoundManager.getInstance();
	@BeforeAll
    static void setup() {
		sm.Initialize();
		SoundProp = sm.new PlayProp(
                "Sound" + File.separator + "BGM" + File.separator +"B_Level1.wav", "TEST");
    }

    @Test
    void PlayClip() {
        sm.playSound(SoundProp);
		assertNotNull(sm.GetClip("TEST"));
	}

    @Test
    void StopClip() {
		PlayClip();
        sm.StopClip("TEST");
		assertNull(sm.GetClip("TEST"));
	}
}
