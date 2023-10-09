package entity;

import java.awt.Color;
import java.util.Set;
import java.util.*;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

/**
 * Implements a ship, to be controlled by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Ship extends Entity {

	/** Time between shots. */
	private static final int SHOOTING_INTERVAL = 750;

	/** Original speed of the bullets shot by the ship. */
	private static final int ORIGINAL_BULLET_SPEED = -6;
	/** Original movement of the ship for each unit of time. */
	private static final int ORIGINAL_SPEED = 2;

	private static final int ITEM_USE_INTERVAL = 50;
	/** Speed of the bullets shot by the ship.
	private static final int BULLET_SPEED = -6;
	/** Movement of the ship for each unit of time.
	private static int SPEED = 2;
	/** Movement of the ship when ship get speed item for each unit of time. **/
	private static final int item_SPEED = 6;

	/** Minimum time between shots. */
	private Cooldown shootingCooldown;

	private Cooldown itemCooldown;
	/** Time spent inactive between hits. */
	private Cooldown destructionCooldown;
	/** Speed of the bullet. */
	private int BULLET_SPEED;
	/** Speed of the ship. */
	private int SPEED;

	public boolean Invincible;

	private ItemQueue itemQueue;

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	public Ship(final int positionX, final int positionY, SpriteType spriteType) {
		super(positionX, positionY, 13 * 2, 8 * 2, Color.GREEN);
		this.spriteType = spriteType;
		this.shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
		this.itemCooldown = Core.getCooldown(ITEM_USE_INTERVAL);
		this.destructionCooldown = Core.getCooldown(1000);
		this.SPEED = ORIGINAL_SPEED;
		this.BULLET_SPEED = ORIGINAL_BULLET_SPEED;
		this.itemQueue = new ItemQueue();
		this.Invincible = false;
	}

	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 */
	public final void moveRight() {
		this.positionX += SPEED;
	}

	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public final void moveLeft() {
		this.positionX -= SPEED;
	}

	/**
	 * Shoots a bullet upwards.
	 * 
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 */
	public final boolean shoot(final Set<Bullet> bullets) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(positionX + this.width / 2,
					positionY, BULLET_SPEED));
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
	public final boolean isDestroyed() {return !this.destructionCooldown.checkFinished();}

	/**
	 * Getter for the ship's speed.
	 * 
	 * @return Speed of the ship.
	 */
	public final int getSpeed() {
		return SPEED;
	}

	/**
	 * Getter for the ship's speed.
	 *
	 * @return Speed of the ship.
	 */
	public final void setSpeed(int sp) {this.SPEED = sp;}

	/**
	 * Re-Setter for the ship's speed.
	 */
	public final void resetSpeed() {this.SPEED = ORIGINAL_SPEED;}

	/**
	 * Getter for the ship's shooting frequency speed.
	 *
	 * @return Ship's shooting frequency speed.
	 */
	public final Cooldown getShootingInterval() {return this.shootingCooldown;}

	/**
	 * Setter for the ship's shooting frequency speed.
	 *
	 * @return Speed of the ship's shooting frequency.
	 */
	public final void setShootingInterval(int cldwn) {this.shootingCooldown = Core.getCooldown(cldwn);}

	/**
	 * Re-Setter for the ship's shooting frequency speed.
	 */
	public final void resetShootingInterval() {this.shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);}
	
	/** Set item_speed when ship get speed item **/
	public void set_item_Speed() {this.SPEED = item_SPEED;}


	public final boolean isInvincible() {
		return this.Invincible;
	}

	public final void runInvincible() {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				Invincible = false;
				changeColor(Color.GREEN);
				timer.cancel();
			}
		};

		if (!this.isInvincible()) {
			this.Invincible = true;
			this.changeColor(Color.BLUE);
			timer.schedule(task, 10000);
		}

	}

	public final ItemQueue getItemQueue(){return this.itemQueue;}

}
