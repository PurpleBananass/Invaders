package entity;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import engine.GameState;
import engine.SoundManager;
import screen.GameScreen;

import java.awt.*;
import java.util.Random;
import java.util.Set;

/**
 * Implements a enemy ship, to be destroyed by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Boss extends Entity {

	/** Point value of a bonus enemy. */
	private static final int BONUS_TYPE_POINTS = 100;

	public static final double ITEM_PROPORTION = 1;
	public static final int RANDOM_BOUND = 10000;

	/** Cooldown between sprite changes. */
	protected Cooldown animationCooldown;
	/** Checks if the ship has been hit by a bullet. */
	private boolean isDestroyed;
	/** 난이도 조절에 사용할 현재 스테이트 */
	private GameState gameState;
	/** Values of the ship, in points, when destroyed. */
	protected int pointValue;

	/** 적의 체력 */
	protected int HP;

	protected int firstHP;

	/** 총알 속도 */
	protected static final int BULLET_SPEED = 12;
	/**패턴간의 간격 */
	private Cooldown patternCooldown;
	// 보스가 몇번째 패턴을 사용할지에 대한 랜덤 변수
	public static int patternNumber;

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
	public Boss(final int positionX, final int positionY,
                final SpriteType spriteType, final GameState gameState) {
		super(positionX, positionY, 448, 20, Color.yellow);
		this.patternCooldown = Core.getCooldown(5000);
		this.gameState = gameState;
		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.isDestroyed = false;
		this.HP = this.gameState.getLevel()*10;
		this.firstHP = this.HP;
	}


	public final void doPattern(final Set<Bullet> bullets){
		if(GameScreen.bossShootCheck && GameScreen.shootBetween.checkFinished()){
			switch (3){//patternNumber
				case 1:
					for (int i =0; i < 64; i++){
					bossShoot(bullets,i*6,80);}
					GameScreen.bossShootCheck = false;
					break;
				case 2:
					for (int i =0; i < 64; i++){
						bossShoot(bullets,448-i*6,80);}
					GameScreen.bossShootCheck = false;
					break;
				case 3:
					for (int i =0; i < 34; i++){
						bossShoot(bullets,i*6,80);
						bossShoot(bullets,448-i*6,80);
						}
					GameScreen.bossShootCheck = false;
					break;
			}
		}
		if(this.patternCooldown.checkFinished()&& !GameScreen.bossShootCheck){
			this.patternCooldown.reset();
			GameScreen.bossPatternDrawCheck = true;
			Random random = new Random();
			patternNumber = random.nextInt(2) + 1;
		}
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
	 * Updates attributes, mainly used for animation purposes.
	 */
	public void update() {

		return;
	}

	public void bossShoot(final Set<Bullet> bullets,int positionX, int positionY) {
		bullets.add(BulletPool.getBullet(positionX, positionY, BULLET_SPEED, 0));
	}

	/**
	 * Destroys the ship, causing an explosion.
	 */
	public final void bossAttacked() {
		this.HP--;
		if (this.HP <= 0) {
			SoundManager.playSound("SFX/S_Enemy_Destroy_a", "Enemy_destroyed", false, false);
			this.isDestroyed = true;
			this.spriteType = SpriteType.Explosion;
		}
	}

	public final void destroyByBomb(){
		this.HP = 0;
		this.isDestroyed = true;
		this.spriteType = SpriteType.Explosion;
	}
	/**
	 * Checks if the ship has been destroyed.
	 * 
	 * @return True if the ship has been destroyed.
	 */
	public final boolean isDestroyed() {return this.isDestroyed;}
	public final int getpositionY() { return this.positionY; }



	/**
	 * 랜덤으로 Item을 가진 EnemyShip 생성*/
	private boolean itemGenerator(int rand_int){
		if(rand_int < (int)(RANDOM_BOUND * ITEM_PROPORTION))
			return true;
		else
			return false;
	}


	public int getHP(){
		return this.HP;
	}

	public int getFirstHP(){
		return this.firstHP;
	}
}