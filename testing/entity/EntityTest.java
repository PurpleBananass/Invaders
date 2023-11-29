package entity;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {
    Entity entity = new Entity(50,50,30,40, Color.white);
    @Test
    void getColor() {
        assertEquals(entity.getColor(), Color.white);
    }

    @Test
    void setColor() {
        assertEquals(entity.getColor(), Color.white);
        entity.setColor(Color.red);
        assertEquals(entity.getColor(), Color.red);
    }

    @Test
    void resetColor() {
        assertEquals(entity.getColor(), Color.white);
        entity.setColor(Color.red);
        assertEquals(entity.getColor(), Color.red);
        entity.resetColor();
        assertEquals(entity.getColor(), Color.white);

    }

    @Test
    void getPositionX() {
        assertEquals(entity.getPositionX(), 50);
    }

    @Test
    void getPositionY() {
        assertEquals(entity.getPositionY(), 50);
    }

    @Test
    void setPositionX() {
        assertEquals(entity.getPositionX(), 50);
        entity.setPositionX(30);
        assertEquals(entity.getPositionX(), 30);
    }

    @Test
    void setPositionY() {
        assertEquals(entity.getPositionY(), 50);
        entity.setPositionY(20);
        assertEquals(entity.getPositionY(), 20);
    }

    @Test
    void getWidth() {
        assertEquals(entity.getWidth(), 30);
    }

    @Test
    void getHeight() {
        assertEquals(entity.getHeight(), 40);
    }

    @Test
    void changeColor() {
        assertEquals(entity.getColor(), Color.white);
        entity.changeColor(Color.red);
        assertEquals(entity.getColor(), Color.red);
    }
}