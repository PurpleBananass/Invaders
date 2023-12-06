package entity;

import entity.Bullet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    private Bullet bullet;

    @BeforeEach
    void setUp() {
        bullet = new Bullet(3, 80, 10, 0);
        bullet.setXspeed(3);
    }

    @Test
    void update() {
        bullet.update();
        assertEquals(6, bullet.getPositionX());
        assertEquals(90, bullet.getPositionY());
    }

    void setXspeed(){
        bullet.setXspeed(19);
        assertEquals(19,bullet.getX_speed());
    }

    void setYspeed(){
        bullet.setSpeed(23);
        assertEquals(23,bullet.getSpeed());
    }
}
