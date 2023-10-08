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


    public static enum ItemType {

        SubPlaneItem,

        SpeedUpItem,

        InvincibleItem,

        BombItem

    };

    private ItemType itemType;

    private int ItemRange;

    private boolean isGet;

    /**
     * Constructor, establishes the item's properties.
     *
     * @param positionX
     *            Initial position of the item in the X axis.
     * @param positionY
     *            Initial position of the item in the Y axis.
     */
    public Item(final int positionX, final int positionY, int itemRange) {
        super(positionX, positionY, 30, 30, Color.YELLOW);
        this.speed = 5;
        this.spriteType = SpriteType.Item;
        setItemType(itemRange);
    }

    /**
     * Sets correct sprite for the item, based on speed.
     */
    public final void setItemType(int itemRange) {
        if(itemRange > EnemyShip.RANDOM_BOUND * 0.6)
            this.itemType = ItemType.SubPlaneItem;
        else if(itemRange > EnemyShip.RANDOM_BOUND * 0.4)
            this.itemType = ItemType.SpeedUpItem;
        else if(itemRange > EnemyShip.RANDOM_BOUND * 0.2)
            this.itemType = ItemType.InvincibleItem;
        else
            this.itemType = ItemType.BombItem;
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

    public final void setIsGet(){this.isGet = true;}

    public final boolean getIsGet(){return this.isGet;}

    public final ItemType getItemType(){return this.itemType;}


}
