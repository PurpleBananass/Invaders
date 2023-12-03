package engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    GameState gameState = new GameState(4,0,1,2,5,10,10,20);
    @Test
    void getLevel() {
        assertEquals(gameState.getLevel(), 4);
    }

    @Test
    void getMode() {
        assertEquals(gameState.getMode() , 2);
    }

    @Test
    void getScore() {
        assertEquals(gameState.getScore(), 0);
    }

    @Test
    void getLivesRemaining1p() {
        assertEquals(gameState.getLivesRemaining1p(), 1);
    }

    @Test
    void getLivesRemaining2p() {
        assertEquals(gameState.getLivesRemaining2p(), 2);
    }

    @Test
    void getBulletsShot1() {
        assertEquals(gameState.getBulletsShot1(), 5);
    }

    @Test
    void getBulletsShot2() {
        assertEquals(gameState.getBulletsShot2(), 10);
    }

    @Test
    void getShipsDestroyed() {
        assertEquals(gameState.getShipsDestroyed(), 10);
    }

    @Test
    void getShipsDestroyed2() {
        assertEquals(gameState.getShipsDestroyed2(), 20);
    }
}