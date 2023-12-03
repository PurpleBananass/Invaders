package entity;

import engine.DrawManager;
import engine.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyShipTest {
    GameState gameState = new GameState(4,0,1,2,5,10,10,20);
    EnemyShip enemyShip = new EnemyShip(50,50, DrawManager.SpriteType.EnemyShipA1, gameState);
    EnemyShip enemyShipSpecial = new EnemyShip();
    @Test
    void getPointValue() {
        assertEquals(enemyShipSpecial.getPointValue(), 100);
    }

    @Test
    void move(){
        assertEquals(enemyShip.getPositionY(),50);
        enemyShip.move(0,30);
        assertEquals(enemyShip.getPositionY(), 80);
    }

    @Test
    void destroyByBomb(){
        assertEquals(enemyShip.isDestroyed(), false);
        enemyShip.destroyByBomb();
        assertEquals(enemyShip.isDestroyed(),true);
    }
    @Test
    void isDestroyed() {
        assertEquals(enemyShip.isDestroyed(), false);
        enemyShip.destroyByBomb();
        assertEquals(enemyShip.isDestroyed(), true);
        assertEquals(enemyShipSpecial.isDestroyed(), false);
        enemyShipSpecial.destroyByBomb();
        assertEquals(enemyShipSpecial.isDestroyed(),true);
    }

    @Test
    void getpositionY() {
        assertEquals(enemyShip.getpositionY(), 50);
    }

}