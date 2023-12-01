package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    Item item = new Item(30, 30, 745, 10);
    @Test
    void setItemType() {
        assertEquals(item.getItemType(), Item.ItemType.SpeedUpItem);
        item.setItemType(800);
        assertEquals(item.getItemType(), Item.ItemType.SubPlaneItem);
        item.setItemType(300);
        assertEquals(item.getItemType(), Item.ItemType.InvincibleItem);
        item.setItemType(0);
        assertEquals(item.getItemType(), Item.ItemType.BombItem);
    }

    @Test
    void update() {
        assertEquals(item.positionY, 30);
        item.update();
        assertEquals(item.positionY, 40);
    }

    @Test
    void getSpeed() {
        assertEquals(item.getSpeed(), 10);
    }

    @Test
    void setIsGet() {
        assertFalse(item.getIsGet());
        item.setIsGet();
        assertTrue(item.getIsGet());
    }

    @Test
    void getIsGet() {
        assertFalse(item.getIsGet());
        item.setIsGet();
        assertTrue(item.getIsGet());
    }

    @Test
    void getItemType() {
        assertEquals(item.getItemType(), Item.ItemType.SpeedUpItem);
        item.setItemType(800);
        assertEquals(item.getItemType(), Item.ItemType.SubPlaneItem);
        item.setItemType(300);
        assertEquals(item.getItemType(), Item.ItemType.InvincibleItem);
        item.setItemType(0);
        assertEquals(item.getItemType(), Item.ItemType.BombItem);
    }
}