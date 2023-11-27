package entity;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import engine.GameState;
import engine.SoundManager;
import screen.GameScreen;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Implements a enemy ship, to be destroyed by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Boss extends Entity {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
	protected static int BULLET_SPEED = 12;
	/**패턴간의 간격 */
	private Cooldown patternCooldown;
	// 보스가 몇번째 패턴을 사용할지에 대한 랜덤 변수
	public static int patternNumber;
	// 보스가 특정 패턴 사용시 안전한곳의 위치
	public static int[] safeArea = new int[3];

	private Cooldown patternCooldown_5;

	private Cooldown patternCooldown_6;

	private int positionOfPattern_4 = 0;

	private int positionOfPattern_6 = 0;

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
		this.patternCooldown_5 = Core.getCooldown(50);
		this.patternCooldown_6 = Core.getCooldown(2000);
		this.gameState = gameState;
		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.isDestroyed = false;
		this.HP = this.gameState.getLevel()*10;
		this.firstHP = this.HP;
	}

	/**
	 * Update the boss's pattern.
	 *
	 * @param bullets
	 *            Bullets set to add the bullet being shot.
	 */

	public final void doPattern(final Set<Bullet> bullets){
		if(GameScreen.bossShootCheck && GameScreen.shootBetween.checkFinished()){
			switch (patternNumber){//patternNumber
				case 1:
					for (int i =0; i < 64; i++){
					bossShoot(bullets,i*6,80,12);}
					GameScreen.bossShootCheck = false;
					break;
				case 2:
					for (int i =0; i < 64; i++){
						bossShoot(bullets,448-i*6,80,12);}
					GameScreen.bossShootCheck = false;
					break;
				case 3:
					for (int i =0; i < 34; i++){
						bossShoot(bullets,i*6,80,12);
						bossShoot(bullets,448-i*6,80,12);
						}
					GameScreen.bossShootCheck = false;
					break;
				case 4:
					for (int i =0; i < 43; i++){
						bossShoot(bullets,i*6,80,12);
						bossShootAfter(bullets,448-i*6,80,1000,12);
					}
					GameScreen.bossShootCheck = false;
					break;
				case 5:
					if(this.patternCooldown_5.checkFinished()){
						patternCooldown_5.reset();
						positionOfPattern_4++;
						if(positionOfPattern_4 < 51){
						bossShoot(bullets,positionOfPattern_4*6,80,4);
						bossShoot(bullets,positionOfPattern_4*6+150,80,4);}
						else{
							if(positionOfPattern_4>111){
								positionOfPattern_4 = 0;
								GameScreen.bossShootCheck = false;
								this.patternCooldown.reset();
								break;
							}
							bossShoot(bullets,448-(positionOfPattern_4-51)*6,80,4);
							bossShoot(bullets,448-150-(positionOfPattern_4-51)*6,80,4);
						}
					}

					break;
				case 6:
					if(patternCooldown_6.checkFinished()){
						patternCooldown_6.reset();
						switch (Boss.safeArea[positionOfPattern_6]) {
							case 1:
								for (int i =0; i < 50; i++){
									bossShoot(bullets,448/3+i*6,80,18);}
								break;
							case 2:
								for (int i =0; i < 30; i++){
									bossShoot(bullets,i*6,80,18);
									bossShoot(bullets,448/3*2+i*6,80,18);}
								break;
							case 3:
								for (int i =0; i < 50; i++){
									bossShoot(bullets,i*6,80,18);}
								break;
						}
						positionOfPattern_6++;
						if(positionOfPattern_6 >= 3){
							positionOfPattern_6 = 0;
							GameScreen.bossShootCheck = false;
							this.patternCooldown.reset();
						}
						break;
					}
				case 7:
					boosShootWithX(bullets,0,80,getXMovement(0,8),12);
					boosShootWithX(bullets,224,80,getXMovement(224,8),12);
					boosShootWithX(bullets,448,80,getXMovement(448,8),12);
					GameScreen.bossShootCheck = false;
					break;
			}
		}
		if(this.patternCooldown.checkFinished()&& !GameScreen.bossShootCheck){
			this.patternCooldown.reset();
			GameScreen.bossPatternDrawCheck = true;
			setPatternNumber();
			}
		}


	private void setPatternNumber(){
		Random random = new Random();
		patternNumber = random.nextInt(7) + 1;
		if(true){//patternNumber == 6
			for (int i=0; i<3; i++){
				int k = random.nextInt(3)+1;
				safeArea[i] = k;
			}
		}

	}

	private int getXMovement(int xspot,int yspeed){
		double movementX, movementY;
		movementY = (520-80)/yspeed;
		movementX = (GameScreen.x1-xspot)/movementY;
		return (int) movementX;
	}
	public static int getPatternNumber(){return patternNumber;}
	/**
	 * Getter for the score bonus if this ship is destroyed.
	 * 
	 * @return Value of the ship.
	 */
	public final int getPointValue() {
		return this.pointValue;
	}
	public void bossShoot(final Set<Bullet> bullets,int positionX, int positionY,int speed) {
		bullets.add(BulletPool.getBullet(positionX, positionY, speed, 0));
	}

	public void boosShootWithX(final Set<Bullet> bullets,int positionX, int positionY,int xspeed, int yspeed){
		Bullet bullet = BulletPool.getBullet(positionX, positionY, yspeed, 0);
		bullet.setXspeed(xspeed);
		bullets.add(bullet);
	}
	public void bossShootAfter(final Set<Bullet> bullets,int positionX, int positionY,int delay, int speed) {
		scheduler.schedule(() -> bossShoot(bullets, positionX, positionY,speed), delay, TimeUnit.MILLISECONDS);
	}

	private void bossShootCheckAfter(int time){
		scheduler.schedule(() -> this.patternCooldown.reset(), time, TimeUnit.SECONDS);
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