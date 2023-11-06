package entity;

import java.awt.Color;
import java.util.Set;
import java.util.*;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.DrawManager.SpriteType;

/**
 * Implements a ship, to be controlled by the player.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 */
public class Ship extends Entity {

    /**
     * Time between shots.
     */
    private int shootingInterval = 750;

    /**
     * Original speed of the bullets shot by the ship.
     */
    private static final int ORIGINAL_BULLET_SPEED = -6;
    /**
     * Original movement of the ship for each unit of time.
     */
    private int originalSpeed = 2;

    /**
     * Life item purchase status
     **/
    private boolean hasLifeIncreaseItem = false;

    private static final int ITEM_USE_INTERVAL = 750;
    /**
     * Speed of the bullets shot by the ship.
     * private static final int BULLET_SPEED = -6;
     * /** Movement of the ship for each unit of time.
     * private static int SPEED = 2;
     * /** Movement of the ship when ship get speed item for each unit of time.
     **/
    private final int ITEM_SPEED_UP_VALUE = 4;

    /**
     * Minimum time between shots.
     */
    private Cooldown shootingCooldown;

    private Cooldown itemCooldown;
    /**
     * Time spent inactive between hits.
     */
    private Cooldown destructionCooldown;
    /**
     * Speed of the bullet.
     */
    private int BULLET_SPEED;
    /**
     * Speed of the ship.
     */
    private int speed;

    public boolean Invincible;

    private ItemQueue itemQueue;

    private Cooldown skillCooldown;
    private List<Ship> auxiliaryShips = new ArrayList<>();
    private boolean existAuxiliaryShips = false;
    private int FASTER_SHOOTING_INTERVAL = 300;

    private int impactInterval = 10000;

    private Cooldown speedupCooldown;

    private Cooldown invincibleCooldown;

    private Cooldown auxiliaryCooldown;
    private boolean hasPurchasedSpeedItem = false;

    /**
     * Constructor, establishes the ship's properties.
     *
     * @param positionX  Initial position of the ship in the X axis.
     * @param positionY  Initial position of the ship in the Y axis.
     * @param color      Initial color of the ship.
     * @param spriteType Initial spriteType of the ship.
     */
    public Ship(final int positionX, final int positionY, final Color color, SpriteType spriteType, boolean isAuxiliaryShip) {
        super(positionX, positionY, 13 * 2, 8 * 2, color);

        this.spriteType = spriteType;
        this.shootingCooldown = Core.getCooldown(shootingInterval);
        this.itemCooldown = Core.getCooldown(ITEM_USE_INTERVAL);
        this.destructionCooldown = Core.getCooldown(1000);
        this.skillCooldown = Core.getCooldown(1000);

        this.speedupCooldown = Core.getCooldown(impactInterval);
        this.invincibleCooldown = Core.getCooldown(impactInterval);
        this.auxiliaryCooldown = Core.getCooldown(impactInterval);

        this.speed = originalSpeed;
        this.BULLET_SPEED = ORIGINAL_BULLET_SPEED;
        this.itemQueue = new ItemQueue();
        this.Invincible = false;

        if (!isAuxiliaryShip) {
            this.auxiliaryShips.add(new Ship(positionX - 25, positionY, this.getColor(), SpriteType.AuxiliaryShips, true));
            this.auxiliaryShips.add(new Ship(positionX + 25, positionY, this.getColor(), DrawManager.SpriteType.AuxiliaryShips, true));
        }
    }

    /**
     * Moves the ship speed uni ts right, or until the right screen border is
     * reached.
     */
    public final void moveRight() {
        this.positionX += speed;
    }

    /**
     * Moves the ship speed units left, or until the left screen border is
     * reached.
     */
    public final void moveLeft() {
        this.positionX -= speed;
    }

    /**
     * Shoots a bullet upwards.
     *
     * @param bullets List of bullets on screen, to add the new bullet.
     * @return Checks if the bullet was shot correctly.
     */
    public final boolean shoot(final Set<Bullet> bullets, final int shooter) {
        if (this.shootingCooldown.checkFinished()) {
            this.shootingCooldown.reset();
            bullets.add(BulletPool.getBullet(positionX + this.width / 2,
                    positionY, BULLET_SPEED, shooter));
            return true;
        }
        return false;
    }

    public final boolean itemCoolTime() {
        if (this.itemCooldown.checkFinished()) {
            this.itemCooldown.reset();
            return true;
        }
        return false;
    }

    /**
     * Updates status of the ship.
     */
    public final void update() {
        this.skillCooldown.checkFinished();
        if (!this.destructionCooldown.checkFinished())
            this.spriteType = SpriteType.ShipDestroyed;
        else
            this.spriteType = SpriteType.Ship;
    }

    /**
     * Switches the ship to its destroyed state.
     */
    public final void destroy() {
        this.destructionCooldown.reset();
    }

    /**
     * Checks if the ship is destroyed.
     *
     * @return True if the ship is currently destroyed.
     */
    public final boolean isDestroyed() {
        return !this.destructionCooldown.checkFinished();
    }

    public int getBULLET_SPEED() {
        return BULLET_SPEED;
    }

    /**
     * Getter for the ship's speed.
     *
     * @return Speed of the ship.
     */
    public final int getSpeed() {
        return speed;
    }

    /**
     * Getter for the ship's speed.
     *
     * @return Speed of the ship.
     */
    public final void setSpeed(int sp) {
        this.speed = sp;
    }

    /**
     * Re-Setter for the ship's speed.
     */
    public final void resetSpeed() {
        this.speed = originalSpeed;
    }

    /**
     * Getter for the ship's shooting frequency speed.
     *
     * @return Ship's shooting frequency speed.
     */
    public final Cooldown getShootingInterval() {
        return this.shootingCooldown;
    }

    /**
     * Setter for the ship's shooting frequency speed.
     *
     * @return Speed of the ship's shooting frequency.
     */
    public final void setShootingInterval(int cldwn) {
        this.shootingCooldown = Core.getCooldown(cldwn);
    }

    /**
     * Re-Setter for the ship's shooting frequency speed.
     */
    public final void resetShootingInterval() {
        this.shootingCooldown = Core.getCooldown(shootingInterval);
    }

    public final boolean getItemImpact() {
        return (this.speed == this.originalSpeed + this.ITEM_SPEED_UP_VALUE || this.Invincible || this.existAuxiliaryShips);
    }

    public final void itemImpactUpdate() {
        if (this.speed == this.originalSpeed + this.ITEM_SPEED_UP_VALUE) {
            if (this.speedupCooldown.checkFinished()) resetSpeed();
        } else if (this.Invincible) {
            if (this.invincibleCooldown.checkFinished()) {
                Color c = this.getColor();
                if (c == Color.BLUE) {
                    Invincible = false;
                    changeColor(Color.GREEN);
                } else if (c == Color.magenta) {
                    Invincible = false;
                    changeColor(Color.RED);
                }
            }
        } else if (this.existAuxiliaryShips) {
            if (this.auxiliaryCooldown.checkFinished()) setExistAuxiliaryShips(false);
        }
    }

    /**
     * Set item_speed for 10sec when ship get speed item
     **/
    public void setItemSpeed() {
        this.speedupCooldown.reset();
        this.speed = this.originalSpeed + this.ITEM_SPEED_UP_VALUE;
    }

    /**
     * Set item_speed when ship buy speed item in store
     **/
    public void buyItemSpeed() {
        this.hasPurchasedSpeedItem = true;
    }


    public final boolean isInvincible() {
        return this.Invincible;
    }

    public final void runInvincible() {
        Color c = this.getColor();

        if (c == Color.GREEN || c == Color.BLUE) {
            this.invincibleCooldown.reset();
            this.Invincible = true;
            this.changeColor(Color.BLUE);
        } else if (c == Color.RED || c == Color.magenta) {
            this.invincibleCooldown.reset();
            this.Invincible = true;
            this.changeColor(Color.magenta);
        }
    }

    public final ItemQueue getItemQueue() {
        return this.itemQueue;
    }

    public List<Ship> getAuxiliaryShips() {
        return auxiliaryShips;
    }

    public boolean isExistAuxiliaryShips() {
        return existAuxiliaryShips;
    }

    public void setAuxiliaryShipsMode() {
        this.auxiliaryCooldown.reset();
        setExistAuxiliaryShips(true);
    }

    public void setExistAuxiliaryShips(boolean existAuxiliaryShips) {
        this.existAuxiliaryShips = existAuxiliaryShips;
    }

    public void applyFasterShootingItem() {
        this.shootingInterval = this.FASTER_SHOOTING_INTERVAL;
        this.shootingCooldown = Core.getCooldown(this.shootingInterval);

        for (Ship auxiliaryShip : auxiliaryShips) {
            auxiliaryShip.applyFasterShootingItem();
        }

    }

    public void applyLifeIncreaseItem(boolean isUsed) {
        this.hasLifeIncreaseItem = isUsed;
    }

    public boolean getHasLifeIncreaseItem() {
        return this.hasLifeIncreaseItem;
    }

    public void setOriginalSpeed(int originalSpeed) {
        if(hasPurchasedSpeedItem){
            this.originalSpeed = originalSpeed;
            this.speed = this.originalSpeed + 2;
        }else {
            this.originalSpeed = originalSpeed;
            this.speed = this.originalSpeed;
        }
    }

    public int getOriginalSpeed() {
        return originalSpeed;
    }
}
