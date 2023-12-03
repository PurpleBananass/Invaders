package entity;

import engine.DrawManager;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    Ship ship = new Ship(50,50, Color.GREEN, DrawManager.SpriteType.Ship, false);
    @Test
    void moveRight() {
        assertEquals(ship.getPositionX(), 50);
        assertEquals(ship.positionX, 50);
        ship.moveRight();
        assertEquals(ship.positionX, 52);
        assertEquals(ship.positionX, 52);
    }

    @Test
    void moveLeft() {
        assertEquals(ship.getPositionX(), 50);
        assertEquals(ship.positionX, 50);
        ship.moveLeft();
        assertEquals(ship.positionX, 48);
        assertEquals(ship.positionX, 48);
    }


    @Test
    void itemCoolTime() {
        assertEquals(ship.itemCoolTime(), true);
    }

    @Test
    void update() {
        assertEquals(ship.spriteType, DrawManager.SpriteType.Ship);
        ship.destroy();
        ship.update();
        assertEquals(ship.spriteType, DrawManager.SpriteType.ShipDestroyed);
    }

    @Test
    void destroy() {
        assertEquals(ship.spriteType, DrawManager.SpriteType.Ship);
        ship.destroy();
        ship.update();
        assertEquals(ship.spriteType, DrawManager.SpriteType.ShipDestroyed);
    }

    @Test
    void isDestroyed() {
        assertEquals(ship.isDestroyed(), false);
        ship.destroy();
        assertEquals(ship.isDestroyed(), true);
    }

    @Test
    void getBULLET_SPEED() {
        assertEquals(ship.getBULLET_SPEED(), -6);
    }

    @Test
    void getSpeed() {
        assertEquals(ship.getSpeed(), 2);
    }

    @Test
    void setSpeed() {
        assertEquals(ship.getSpeed(), 2);
        ship.setSpeed(3);
        assertEquals(ship.getSpeed(), 3);
    }

    @Test
    void resetSpeed() {
        assertEquals(ship.getSpeed(), 2);
        ship.setSpeed(3);
        assertEquals(ship.getSpeed(), 3);
        ship.resetSpeed();
        assertEquals(ship.getSpeed(), 2);
    }

    @Test
    void getItemImpact() {
        assertEquals(ship.getItemImpact(), false);
        ship.setItemSpeed();
        assertEquals(ship.getItemImpact(), true);
    }

    @Test
    void setItemSpeed() {
        assertEquals(ship.getItemImpact(), false);
        ship.setItemSpeed();
        assertEquals(ship.getItemImpact(), true);
    }

    @Test
    void isInvincible() {
        assertEquals(ship.isInvincible(), false);
        ship.runInvincible();
        assertEquals(ship.isInvincible(), true);
    }

    @Test
    void runInvincible() {
        assertEquals(ship.isInvincible(), false);
        ship.runInvincible();
        assertEquals(ship.isInvincible(), true);
    }

    @Test
    void isExistAuxiliaryShips() {
        assertEquals(ship.isExistAuxiliaryShips(), false);
        ship.setExistAuxiliaryShips(true);
        assertEquals(ship.isExistAuxiliaryShips(), true);
        ship.setExistAuxiliaryShips(false);
        assertEquals(ship.isExistAuxiliaryShips(), false);
    }

    @Test
    void setAuxiliaryShipsMode() {
        assertEquals(ship.isExistAuxiliaryShips(), false);
        ship.setAuxiliaryShipsMode();
        assertEquals(ship.isExistAuxiliaryShips(), true);
    }

    @Test
    void setExistAuxiliaryShips() {
        assertEquals(ship.isExistAuxiliaryShips(), false);
        ship.setExistAuxiliaryShips(true);
        assertEquals(ship.isExistAuxiliaryShips(), true);
        ship.setExistAuxiliaryShips(false);
        assertEquals(ship.isExistAuxiliaryShips(), false);
    }

    @Test
    void applyLifeIncreaseItem() {
        assertEquals(ship.getHasLifeIncreaseItem(), false);
        ship.applyLifeIncreaseItem(true);
        assertEquals(ship.getHasLifeIncreaseItem(), true);
    }

    @Test
    void getHasLifeIncreaseItem() {
        assertEquals(ship.getHasLifeIncreaseItem(), false);
        ship.applyLifeIncreaseItem(true);
        assertEquals(ship.getHasLifeIncreaseItem(), true);
    }

    @Test
    void setOriginalSpeed() {
        assertEquals(ship.getOriginalSpeed(), 2);
        ship.setOriginalSpeed(3);
        assertEquals(ship.getOriginalSpeed(), 3);
    }

    @Test
    void getOriginalSpeed() {
        assertEquals(ship.getOriginalSpeed(), 2);
        ship.setOriginalSpeed(3);
        assertEquals(ship.getOriginalSpeed(), 3);
    }
}