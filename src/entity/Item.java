package entity;

import java.awt.Color;

import engine.DrawManager.SpriteType;

/**
 * Implements an item that moves vertically only down.
 */
public class Item extends Entity {

    /**
     * Speed of the item, only positive number
     * positive is down.
     */
    private int speed;

    private boolean isGet;

    /**
     * Constructor, establishes the item's properties.
     *
     * @param positionX
     *            Initial position of the item in the X axis.
     * @param positionY
     *            Initial position of the item in the Y axis.
     */
    public Item(final int positionX, final int positionY) {
        super(positionX, positionY, 30, 30, Color.YELLOW);
        this.speed = 50;
        setSprite();
    }

    /**
     * Sets correct sprite for the item, based on speed.
     */
    public final void setSprite() {
        if (this.isGet)
            this.spriteType = SpriteType.DroppedItem;
        else
            this.spriteType = SpriteType.AcquiredItem;
    }

    /**
     * Updates the item's position.
     */
    public final void update() {
        this.positionY += this.speed;
    }


    /**
     * Getter for the speed of the item.
     *
     * @return Speed of the item.
     */
    public final int getSpeed() {
        return this.speed;
    }
}
