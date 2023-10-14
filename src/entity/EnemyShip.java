package entity;

import java.awt.Color;
import java.util.Random;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import engine.GameState;

/**
 * Implements a enemy ship, to be destroyed by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class EnemyShip extends Entity {

	/** Point value of a bonus enemy. */
	private static final int BONUS_TYPE_POINTS = 100;

	private static final double ITEM_PROPORTIOIN = 0.8;

	public static final int RANDOM_BOUND = 10000;

	/** Cooldown between sprite changes. */
	private Cooldown animationCooldown;
	/** Checks if the ship has been hit by a bullet. */
	private boolean isDestroyed;
	/** 난이도 조절에 사용할 현재 스테이트 */
	private GameState gameState;
	/** Values of the ship, in points, when destroyed. */
	protected int pointValue;

	private boolean hasItem;

	private int itemRange;

	/** 적의 체력 */
	protected int HP;

	/** 총알 속도 */
	private static final int BULLET_SPEED = 4;

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 * @param spriteType
	 *            Sprite type, image corresponding to the ship.
	 */
	public EnemyShip(final int positionX, final int positionY,
					 final SpriteType spriteType, final GameState gameState) {
		super(positionX, positionY, 12 * 2, 8 * 2, Color.WHITE);
		this.gameState = gameState;
		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.isDestroyed = false;
		this.itemRange =  new Random().nextInt(RANDOM_BOUND);
		this.hasItem = itemGenerator(itemRange);
		this.HP = this.gameState.getLevel();
	}

	/**
	 * Constructor, establishes the ship's properties for a special ship, with
	 * known starting properties.
	 */
	public EnemyShip() {
		super(-32, 60, 16 * 2, 7 * 2, Color.RED);

		this.spriteType = SpriteType.EnemyShipSpecial;
		this.isDestroyed = false;
		this.pointValue = BONUS_TYPE_POINTS;
	}

	/**
	 * Getter for the score bonus if this ship is destroyed.
	 * 
	 * @return Value of the ship.
	 */
	public final int getPointValue() {
		return this.pointValue;
	}

	/**
	 * Moves the ship the specified distance.
	 * 
	 * @param distanceX
	 *            Distance to move in the X axis.
	 * @param distanceY
	 *            Distance to move in the Y axis.
	 */
	public final void move(final int distanceX, final int distanceY) {
		this.positionX += distanceX;
		this.positionY += distanceY;
	}

	/**
	 * Updates attributes, mainly used for animation purposes.
	 */
	public final void update() {
		if (this.animationCooldown.checkFinished()) {
			this.animationCooldown.reset();

			switch (this.spriteType) {
			case EnemyShipA1:
				this.spriteType = SpriteType.EnemyShipA2;
				break;
			case EnemyShipA2:
				this.spriteType = SpriteType.EnemyShipA1;
				break;
			case EnemyShipB1:
				this.spriteType = SpriteType.EnemyShipB2;
				break;
			case EnemyShipB2:
				this.spriteType = SpriteType.EnemyShipB1;
				break;
			case EnemyShipC1:
				this.spriteType = SpriteType.EnemyShipC2;
				break;
			case EnemyShipC2:
				this.spriteType = SpriteType.EnemyShipC1;
				break;
			default:
				break;
			}
		}
	}

	public void shoot(final Set<Bullet> bullets) {
		bullets.add(BulletPool.getBullet(positionX
				+ width / 2, positionY, BULLET_SPEED));
	}

	/**
	 * Destroys the ship, causing an explosion.
	 */
	public final void destroy() {
		this.HP--;
		if (this.HP <= 0) {
			this.isDestroyed = true;
			this.spriteType = SpriteType.Explosion;
		}
	}

	/**
	 * Checks if the ship has been destroyed.
	 * 
	 * @return True if the ship has been destroyed.
	 */
	public final boolean isDestroyed() {
		return this.isDestroyed;
	}

	/**
	 * 랜덤으로 Item을 가진 EnemyShip 생성*/
	private boolean itemGenerator(int rand_int){
		if(rand_int < (int)(RANDOM_BOUND * ITEM_PROPORTIOIN))
			return true;
		else
			return false;
	}

	/** EnemyShip이 아이템을 지닌 객체인지 확인 */
	public final boolean hasItem(){
		return this.hasItem;
	}

	public int getItemRange(){return this.itemRange;}
}


