package entity;

import engine.DrawManager;
import engine.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BossTest {
    private Boss boss;
    Set<Bullet> bullets;
    @BeforeEach
    void setUp() {
        boss = new Boss(0,0, DrawManager.SpriteType.Boss,new GameState(1,1,1,1,1));
    }

    @Test
    void doPattern() {
        boss.doPattern(bullets);
        for (Bullet bullet: bullets){
            assertEquals(bullet.getSpeed(),4);
        }
    }

    @Test
    void bossShoot() {
    }

    @Test
    void boosShootWithX() {
    }

    @Test
    void bossAttacked() {
    }
}