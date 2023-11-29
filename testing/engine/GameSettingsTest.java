package engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSettingsTest {
    GameSettings gameSettings = new GameSettings(50, 50, 5,5, .5, true);
    @Test
    void getFormationWidth() {
        assertEquals(gameSettings.getFormationWidth(), 50);
    }

    @Test
    void getFormationHeight() {
        assertEquals(gameSettings.getFormationHeight(), 50);
    }

    @Test
    void getBaseSpeed() {
        assertEquals(gameSettings.getBaseSpeed(), 5);
    }

    @Test
    void getShootingFrecuency() {
        assertEquals(gameSettings.getShootingFrecuency(), 5);
    }

    @Test
    void getBossStage() {
        assertEquals(gameSettings.getBossStage(), true);
    }

    @Test
    void getItemSpeed(){
        assertEquals(gameSettings.getItemSpeed(), .5);
    }
}