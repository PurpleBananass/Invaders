package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {
    Bullet bullet = new Bullet(50,50,10,5);

    @Test
    void setSpeed() {
        assertEquals(bullet.getSpeed(), 10);
        bullet.setSpeed(30);
        assertEquals(bullet.getSpeed(), 30);
    }

    @Test
    void getSpeed() {
        assertEquals(bullet.getSpeed(), 10);
    }

    @Test
    void getShooter() {
        assertEquals(bullet.getShooter(), 5);
    }
}