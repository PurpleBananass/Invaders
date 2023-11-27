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
    private double speed;


    public static enum ItemType {

        SubPlaneItem,

        SpeedUpItem,

        InvincibleItem,

        BombItem

    };

    private ItemType itemType;

    private boolean isGet;

    /**
     * Constructor, establishes the item's properties.
     *
     * @param positionX
     *            Initial position of the item in the X axis.
     * @param positionY
     *            Initial position of the item in the Y axis.
     */
    public Item(final int positionX, final int positionY, int itemRange, double itemSpeed) {
        super(positionX, positionY, 30, 30, Color.YELLOW);
        this.speed = itemSpeed;
        this.spriteType = SpriteType.Item;
        setItemType(itemRange);
    }

    /**
     * Sets correct sprite for the item, based on speed.
     */
    public final void setItemType(int itemRange) {
        if(itemRange > EnemyShip.RANDOM_BOUND * EnemyShip.ITEM_PROPORTION * 0.75)
            this.itemType = ItemType.SubPlaneItem;
        else if(itemRange > EnemyShip.RANDOM_BOUND * EnemyShip.ITEM_PROPORTION * 0.5)
            this.itemType = ItemType.SpeedUpItem;
        else if(itemRange > EnemyShip.RANDOM_BOUND * EnemyShip.ITEM_PROPORTION * 0.25)
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
    public final double getSpeed() {
        return this.speed;
    }

    public final void setIsGet(){this.isGet = true;}

    public final boolean getIsGet(){return this.isGet;}

    public final ItemType getItemType(){return this.itemType;}


}
