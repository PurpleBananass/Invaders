package screen;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

import engine.*;
import entity.*;

/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen {

	/** Milliseconds until the screen accepts user input. */
	private static final int INPUT_DELAY = 6000;
	/** Bonus score for each life remaining at the end of the level. */
	private static final int LIFE_SCORE = 100;
	/** Minimum time between bonus ship's appearances. */
	private static final int BONUS_SHIP_INTERVAL = 20000;
	/** Maximum variance in the time between bonus ship's appearances. */
	private static final int BONUS_SHIP_VARIANCE = 10000;
	/** Time until bonus ship explosion disappears. */
	private static final int BONUS_SHIP_EXPLOSION = 500;
	/** Time from finishing the level to screen change. */
	private static final int SCREEN_CHANGE_INTERVAL = 1500;
	/** Height of the interface separation line. */
	private static final int SEPARATION_LINE_HEIGHT = 40;

	/** Current game state. */
	private GameState gameState;
	/** Current game difficulty settings. */
	private GameSettings gameSettings;
	/** Player's freedom. */
	private Replayability replayability = new Replayability(1);
	/** Current difficulty level number. */
	private int level;
	/** Formation of enemy ships. */
	private EnemyShipFormation enemyShipFormation;

	/** First Player's ship. */
	private Ship ship;
	/** Second Player's ship. **/
	private Ship ship2;

	private int shipWidth = 13*2;

	/** Bonus enemy ship that appears sometimes. */
	private EnemyShip enemyShipSpecial;
	/** Minimum time between bonus ship appearances. */
	private Cooldown enemyShipSpecialCooldown;
	/** Time until bonus ship explosion disappears. */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/** Time from finishing the level to screen change. */
	private Cooldown screenFinishedCooldown;

	/** Set of all bullets fired by on screen ships. */
	private Set<Bullet> bullets;

	private Set<Item> items;
	/** Current score. */
	private int score;
	/** First Player's lives left. */
	private int lives;
	/** Second Player's lives left. */
	private int lives2;
	/** Player 1's remaining magazines */
	private int magazine;
	/** Player 2's remaining magazines */
	private int magazine2;
	/** Number of bullets used in player 1's active magazine */
	private int bullet_count;
	/** Number of bullets used in player 2's active magazine */
	private int bullet_count2;
	/** Total bullets shot by the player1. */
	private int bulletsShot1;
	/** Total bullets shot by the player2. */
	private int bulletsShot2;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed;

	/** Moment the game starts. */
	private long gameStartTime;
	/** Checks if the level is finished. */
	private boolean levelFinished;
	/** Checks if a bonus life is received. */
	private boolean bonusLife;
	private int escapeCnt = 0;
	private int per=0;
	private int originalSpeed;
	private boolean speedBoosted;

	/** list of past high scores */
	private int highScore;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param gameState
	 *            Current game state.
	 * @param gameSettings
	 *            Current game settings.
	 * @param bonusLife
	 *            Checks if a bonus life is awarded this level.
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public GameScreen(final GameState gameState,
					  final GameSettings gameSettings, final boolean bonusLife,
					  final int width, final int height, final int fps) {
		super(width, height, fps);

		this.gameState = gameState;
		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife;
		this.level = gameState.getLevel();
		this.score = gameState.getScore();
		this.lives = gameState.getLivesRemaining1p();
		this.bulletsShot1 = gameState.getBulletsShot1();
		this.shipsDestroyed = gameState.getShipsDestroyed();

		if (gameState.getMode() == 2) {
			this.lives2 = gameState.getLivesRemaining2p();
			this.bulletsShot2 = gameState.getBulletsShot2();
		}

		if (this.bonusLife) {
			if (gameState.getMode() == 1) {
				this.lives++;
			} else {
				if (this.lives == Core.getMaxLives()) {
					this.lives2++;
				} else if (this.lives2 == Core.getMaxLives()) {
					this.lives++;
				} else {
					this.lives++;
					this.lives2++;
				}
			}
		}

		try {
			if (this.gameState.getMode() == 1) {
				List<Score> highScores = Core.getFileManager().loadHighScores(1);
				this.highScore = highScores.stream().mapToInt(Score::getScore).max().orElseThrow(NoSuchElementException::new);
			} else {
				List<Score> highScores = Core.getFileManager().loadHighScores(2);
				this.highScore = highScores.stream().mapToInt(Score::getScore).max().orElseThrow(NoSuchElementException::new);
			}
		} catch (NumberFormatException | NoSuchElementException | IOException e) {
			logger.warning("Couldn't load high score!");
		}
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);

		// 게임 모드 별 함선 생성 제어
		if (gameState.getMode() == 1){
			this.ship = new Ship(this.width / 2, this.height - 30, Color.GREEN, DrawManager.SpriteType.Ship, false);
		}
		if (gameState.getMode() == 2) {
			this.ship = new Ship(this.width / 2 - 85, this.height - 30, Color.GREEN, DrawManager.SpriteType.Ship, false);
			this.ship2 = new Ship(this.width / 2 + 60, this.height - 30, Color.RED, DrawManager.SpriteType.Ship, false);
		}

		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(BONUS_SHIP_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();
		this.items = new HashSet<Item>();

		// Special input delay / countdown.
		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();

		this.magazine=5;
		this.magazine2=5;
		this.bullet_count=0;
		this.bullet_count2=0;
	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score += LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		if (this.inputDelay.checkFinished() && !this.levelFinished) {

			if (gameState.getMode() == 1 && !this.ship.isDestroyed()) {
				boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
				boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);

				boolean isRightBorder = this.ship.getPositionX()
						+ this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship.getPositionX()
						- this.ship.getSpeed() < 1;

				if (moveRight && !isRightBorder) {
					this.ship.moveRight();
				}
				if (moveLeft && !isLeftBorder) {
					this.ship.moveLeft();
				}
				if ( replayability.getReplay()==0 && inputManager.isKeyDown(KeyEvent.VK_SPACE)){
					if (this.ship.shoot(this.bullets))
						this.bulletsShot1++;
					if(this.ship.isExistAuxiliaryShips()){
						for (Ship auxiliaryShip : this.ship.getAuxiliaryShips()) {
							if(auxiliaryShip.shoot(this.bullets))
								this.bulletsShot1++;
						}
					}
				}
				if (replayability.getReplay()==1) {
					if (this.bullet_count<=9 && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
						if(this.ship.shoot(this.bullets)){
							this.bulletsShot1++;
							this.bullet_count++;
						}
						if(this.ship.isExistAuxiliaryShips()){
							for (Ship auxiliaryShip : this.ship.getAuxiliaryShips()) {
								if(auxiliaryShip.shoot(this.bullets))
									this.bulletsShot1++;
							}
						}
					}
					if (inputManager.speed == 3) {
						per = 1;
					} else if (inputManager.countH_u >= 7 && inputManager.countH_d >= 7 && bullet_count <=7) {
						per = 2;
					}
					if (inputManager.magazine){
						inputManager.magazine = false;
						inputManager.countH_d = 0;
						inputManager.countH_u = 0;
						inputManager.speed = 0;
						this.magazine--;
						this.bullet_count=0;
					}
				}

				if(!this.ship.isDestroyed()){
					List<Ship> auxiliaryShips = this.ship.getAuxiliaryShips();
					if (this.ship.isExistAuxiliaryShips()) {
						auxiliaryShips.get(0).setPositionX(ship.getPositionX() - 30);
						auxiliaryShips.get(0).setPositionY(ship.getPositionY());
						auxiliaryShips.get(1).setPositionX(ship.getPositionX() + 30);
						auxiliaryShips.get(1).setPositionY(ship.getPositionY());
					} else {
						auxiliaryShips.get(0).destroy();
						auxiliaryShips.get(1).destroy();
					}
					if (inputManager.isKeyDown(KeyEvent.VK_G))
						if(this.ship.itemCoolTime())
							useItem(this.ship.getItemQueue().deque(), this.ship);
				}



			} else if (gameState.getMode() == 2 && !this.ship2.isDestroyed()) {
				boolean moveRight1p = inputManager.isKeyDown(KeyEvent.VK_D);
				boolean moveLeft1p = inputManager.isKeyDown(KeyEvent.VK_A);

				boolean moveRight2p = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
				boolean moveLeft2p = inputManager.isKeyDown(KeyEvent.VK_LEFT);

				boolean isRightBorder1p = this.ship.getPositionX()
						+ this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
				boolean isLeftBorder1p = this.ship.getPositionX()
						- this.ship.getSpeed() < 1;

				boolean isRightBorder2p = this.ship2.getPositionX()
						+ this.ship2.getWidth() + this.ship2.getSpeed() > this.width - 1;
				boolean isLeftBorder2p = this.ship2.getPositionX()
						- this.ship2.getSpeed() < 1;

				if (moveRight1p && !isRightBorder1p) {
					this.ship.moveRight();
				}
				if (moveRight2p && !isRightBorder2p) {
					this.ship2.moveRight();
				}
				if (moveLeft1p && !isLeftBorder1p) {
					this.ship.moveLeft();
				}
				if (moveLeft2p && !isLeftBorder2p) {
					this.ship2.moveLeft();
				}

				if (replayability.getReplay()==0){
					if (inputManager.isKeyDown(KeyEvent.VK_W)) {
						if (this.ship.shoot(this.bullets)) {
							this.bulletsShot1++;
							this.bullet_count++;
						}
						if(this.ship.isExistAuxiliaryShips()){
							for (Ship auxiliaryShip : this.ship.getAuxiliaryShips())
								if(auxiliaryShip.shoot(this.bullets)){
									this.bulletsShot1++;
								}
						}
					}
					if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
						if (this.ship2.shoot(this.bullets)) {
							this.bulletsShot2++;
							this.bullet_count2++;
						}
						if(this.ship2.isExistAuxiliaryShips()){
							for (Ship auxiliaryShip : this.ship2.getAuxiliaryShips())
								if(auxiliaryShip.shoot(this.bullets)){
									this.bulletsShot2++;
								}
						}
					}
				}else if (replayability.getReplay()==1){
					//player1
					if (this.bullet_count<=9 && inputManager.isKeyDown(KeyEvent.VK_W)) {
						if(this.ship.shoot(this.bullets)){
							this.bulletsShot1++;
							this.bullet_count++;
						}
						if(this.ship.isExistAuxiliaryShips()){
							for (Ship auxiliaryShip : this.ship.getAuxiliaryShips())
								if(auxiliaryShip.shoot(this.bullets)){
									this.bulletsShot1++;
								}
						}

					}
					if (inputManager.speed1 == 3)
						per = 1;
					if (inputManager.one >= 7 && inputManager.two>= 7 && bullet_count <=7)
						per = 2;
					if (inputManager.magazine){
						inputManager.magazine = false;
						inputManager.one = 0;
						inputManager.two = 0;
						inputManager.speed1 = 0;
						this.magazine--;
						this.bullet_count=0;
					}

					if(!this.ship.isDestroyed()){
						List<Ship> auxiliaryShips = this.ship.getAuxiliaryShips();
						if (this.ship.isExistAuxiliaryShips()) {
							auxiliaryShips.get(0).setPositionX(ship.getPositionX() - 30);
							auxiliaryShips.get(0).setPositionY(ship.getPositionY());
							auxiliaryShips.get(1).setPositionX(ship.getPositionX() + 30);
							auxiliaryShips.get(1).setPositionY(ship.getPositionY());
						} else {
							auxiliaryShips.get(0).destroy();
							auxiliaryShips.get(1).destroy();
						}
						if (inputManager.isKeyDown(KeyEvent.VK_TAB))
							if(this.ship.itemCoolTime())
								useItem(this.ship.getItemQueue().deque(), this.ship);
					}

					//player2
					if (this.bullet_count2<=9 && inputManager.isKeyDown(KeyEvent.VK_UP)) {
						if(this.ship2.shoot(this.bullets)){
							this.bulletsShot2++;
							this.bullet_count2++;
							if(this.ship2.isExistAuxiliaryShips()){
								for (Ship auxiliaryShip : this.ship2.getAuxiliaryShips())
									if(auxiliaryShip.shoot(this.bullets)){
										this.bulletsShot2++;
									}
							}
						}

					}
					if (inputManager.speed2 == 3)
						per = 3;
					if (inputManager.seven >= 7 && inputManager.eight >= 7 && bullet_count2 <=7)
						per = 4;
					if (inputManager.magazine2){
						inputManager.magazine2 = false;
						inputManager.seven = 0;
						inputManager.eight = 0;
						inputManager.speed2 = 0;
						this.magazine2--;
						this.bullet_count2=0;
					}

					// item
					if(!this.ship2.isDestroyed()){
						List<Ship> auxiliaryShips = this.ship2.getAuxiliaryShips();
						if (this.ship2.isExistAuxiliaryShips()) {
							auxiliaryShips.get(0).setPositionX(ship2.getPositionX() - 30);
							auxiliaryShips.get(0).setPositionY(ship2.getPositionY());
							auxiliaryShips.get(1).setPositionX(ship2.getPositionX() + 30);
							auxiliaryShips.get(1).setPositionY(ship2.getPositionY());
						} else {
							auxiliaryShips.get(0).destroy();
							auxiliaryShips.get(1).destroy();
						}
						if (inputManager.isKeyDown(KeyEvent.VK_ENTER))
							if(this.ship2.itemCoolTime())
								useItem(this.ship2.getItemQueue().deque(), this.ship2);
					}
				}
			}


			if (this.enemyShipSpecial != null) {
				if (!this.enemyShipSpecial.isDestroyed())
					this.enemyShipSpecial.move(2, 0);
				else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
					this.enemyShipSpecial = null;
			}

			if (this.enemyShipSpecial == null
					&& this.enemyShipSpecialCooldown.checkFinished()) {
				this.enemyShipSpecial = new EnemyShip();
				this.enemyShipSpecialCooldown.reset();
				this.logger.info("A special ship appears");
			}

			if (this.enemyShipSpecial != null
					&& this.enemyShipSpecial.getPositionX() > this.width) {
				this.escapeCnt++;
				if(this.level==7){
					this.lives--;
					this.logger.info("This level is 7 and escaped ship is 1, so you lost on life.");
				}
				else if(this.level==6 && this.escapeCnt==2){
					this.lives--;
					this.logger.info("Escaped 2.");
					this.escapeCnt = 0;
					this.logger.info("This level is 6 and escaped ship is 2, so you lost on life.");
				}
				else if(this.level==5 && this.escapeCnt==3){
					this.lives--;
					this.logger.info("Escaped 3.");
					this.escapeCnt = 0;
					this.logger.info("This level is 5 and escaped ship is 3, so you lost on life.");
				}
				else{
					this.logger.info("The special ship has escaped");
				}
				this.enemyShipSpecial = null;
			}

			if(this.magazine==0)
				this.lives =0;

			if(this.magazine2==0)
				this.lives2 =0;


			this.ship.update();
			if (this.gameState.getMode() == 2) {
				this.ship2.update();
			}

			this.enemyShipFormation.update();
			this.enemyShipFormation.shoot(this.bullets);
		}

		useSkill();
		manageCollisions();
		cleanBullets();
		updateItems();
		draw();

		if ((this.enemyShipFormation.isEmpty() || this.lives == 0 || (this.gameState.getMode() == 2 && this.lives2 == 0))
				&& !this.levelFinished) {
			this.levelFinished = true;
			this.screenFinishedCooldown.reset();
		}

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
			this.isRunning = false;
		AchievementManager.getInstance().checkLuckySeven(this.score);
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		if (this.gameState.getMode() == 1) {
			drawManager.drawEntity(this.ship, this.ship.getPositionX(),
					this.ship.getPositionY());
		} else {
			drawManager.drawEntity(this.ship, this.ship.getPositionX(),
					this.ship.getPositionY());
			drawManager.drawEntity(this.ship2, this.ship2.getPositionX(),
					this.ship2.getPositionY());
		}

		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

		enemyShipFormation.draw();

		for (Bullet bullet : this.bullets)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());


		for (Item item : this.items)
			drawManager.drawEntity(item, item.getPositionX(),
					item.getPositionY());
		if (this.ship.isExistAuxiliaryShips()) {
			for (Ship auxiliaryShip : this.ship.getAuxiliaryShips()) {
				drawManager.drawEntity(auxiliaryShip, auxiliaryShip.getPositionX(), auxiliaryShip.getPositionY());
			}
		}
		if (this.ship2.isExistAuxiliaryShips()) {
			for (Ship auxiliaryShip : this.ship2.getAuxiliaryShips()) {
				drawManager.drawEntity(auxiliaryShip, auxiliaryShip.getPositionX(), auxiliaryShip.getPositionY());
			}
		}

		drawManager.drawScore(this, this.score);
		drawManager.drawLives(this, this.lives);
		if (this.gameState.getMode() == 2) drawManager.drawLives2(this, this.lives2);
		drawManager.drawHighScore(this, this.highScore);
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);

		// Countdown to game start.
		if (!this.inputDelay.checkFinished()) {

			int countdown = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
					- this.gameStartTime)) / 1000);
			drawManager.drawCountDown(this, this.level, countdown,
					this.bonusLife);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ 12);
		}

		drawManager.completeDrawing(this);
	}

	/**
	 * Cleans bullets that go off screen.
	 */
	private void cleanBullets() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets) {
			bullet.update();
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	/**
	 * 아이템이 화면 아래나 Ship 닿을 시 아이템 청소
	 * */
	private void updateItems() {
		Set<Item> recyclableItem = new HashSet<Item>();
		for (Item item : this.items) {
			item.update();
			if (item.getPositionY() < SEPARATION_LINE_HEIGHT
					|| item.getPositionY() > this.height)
				recyclableItem.add(item);
		}
		this.items.removeAll(recyclableItem);
		ItemPool.recycle(recyclableItem);
	}

	/**
	 * Manages collisions between bullets and ships.
	 */
	private void manageCollisions() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets)
			if (bullet.getSpeed() > 0) {
				if (checkCollision(bullet, this.ship) && !this.levelFinished && !this.ship.isInvincible()) {
					recyclable.add(bullet);
					if (!this.ship.isDestroyed()) {
						this.ship.destroy();
						this.lives--;
						this.logger.info("Hit on player1 ship, " + this.lives
								+ " lives remaining.");
					}
				}

				if (this.gameState.getMode() == 2 && checkCollision(bullet, this.ship2) && !this.levelFinished) {
					recyclable.add(bullet);
					if (!this.ship2.isDestroyed()) {
						this.ship2.destroy();
						this.lives2--;
						this.logger.info("Hit on player2 ship, " + this.lives2
								+ " lives remaining.");
					}
				}

			} else {
				for (EnemyShip enemyShip : this.enemyShipFormation)
					if (!enemyShip.isDestroyed()
							&& checkCollision(bullet, enemyShip)) {
						this.score += enemyShip.getPointValue();
						this.shipsDestroyed++;
						if(enemyShip.hasItem()){
							items.add(new Item(enemyShip.getPositionX(), enemyShip.getPositionY(), enemyShip.getItemRange()));
						}
						this.enemyShipFormation.destroy(enemyShip);

						recyclable.add(bullet);
					}

				if (this.enemyShipSpecial != null
						&& !this.enemyShipSpecial.isDestroyed()
						&& checkCollision(bullet, this.enemyShipSpecial)) {
					this.score += this.enemyShipSpecial.getPointValue();
					this.shipsDestroyed++;
					this.enemyShipSpecial.destroy();
					this.enemyShipSpecialExplosionCooldown.reset();
					recyclable.add(bullet);
				}
			}

		Set<Item> recycableItem = new HashSet<Item>();
		for (Item item : this.items){
			if (checkCollision(item, this.ship) && !this.levelFinished){
				recycableItem.add(item);
				this.ship.getItemQueue().enque(item);
			}
		}


		this.bullets.removeAll(recyclable);
		this.items.removeAll(recycableItem);
		BulletPool.recycle(recyclable);
		ItemPool.recycle(recycableItem);
	}

	/** Use skill*/
	private void useSkill(){
		if (per>0 && !this.levelFinished) {
			if (per == 1 && !speedBoosted) { // s 연타 -> 1초간 속도 빨라지기
				originalSpeed = (int) ship.getSpeed();
				ship.setSpeed(originalSpeed + 2);
				this.logger.info("SpeedUp");

				ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
				executor.schedule(() -> {
					ship.setSpeed(originalSpeed);
					speedBoosted = false;
					executor.shutdown();
				}, 1, TimeUnit.SECONDS);

				speedBoosted = true;
			}else if (per == 2) { //위아래화살표 연타 -> 총 세발
				bullets.add(BulletPool.getBullet(ship.getPositionX(),
						ship.getPositionY(), ship.getBULLET_SPEED()));
				bullets.add(BulletPool.getBullet(ship.getPositionX() + shipWidth/2,
						ship.getPositionY(), ship.getBULLET_SPEED()));
				bullets.add(BulletPool.getBullet(ship.getPositionX() + shipWidth,
						ship.getPositionY(), ship.getBULLET_SPEED()));
				this.logger.info("Three bullets");
				this.bulletsShot1+=3;
				this.bullet_count+=3;
			}else if (per == 3 && !speedBoosted) { // s 연타 -> 1초간 속도 빨라지기
				originalSpeed = (int) ship2.getSpeed();
				ship2.setSpeed(originalSpeed + 2);
				this.logger.info("SpeedUp");

				ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
				executor.schedule(() -> {
					ship2.setSpeed(originalSpeed);
					speedBoosted = false;
					executor.shutdown();
				}, 1, TimeUnit.SECONDS);

				speedBoosted = true;
			}else if (per == 4) { //위아래화살표 연타 -> 총 세발
				bullets.add(BulletPool.getBullet(ship2.getPositionX(),
						ship2.getPositionY(), ship2.getBULLET_SPEED()));
				bullets.add(BulletPool.getBullet(ship2.getPositionX() + shipWidth/2,
						ship2.getPositionY(), ship2.getBULLET_SPEED()));
				bullets.add(BulletPool.getBullet(ship2.getPositionX() + shipWidth,
						ship2.getPositionY(), ship2.getBULLET_SPEED()));
				this.logger.info("Three bullets");
				this.bulletsShot2+=3;
				this.bullet_count2+=3;
			}
			per = 0;
			inputManager.countH_d = 0;
			inputManager.countH_u = 0;
			inputManager.one = 0;
			inputManager.speed = 0;
			inputManager.two = 0;
			inputManager.seven = 0;
			inputManager.eight = 0;
			inputManager.speed1 = 0;
			inputManager.speed2 = 0;
		}
	}

	/**
	 * Checks if two entities are colliding.
	 *
	 * @param a
	 *            First entity, the bullet.
	 * @param b
	 *            Second entity, the ship.
	 * @return Result of the collision test.
	 */
	private boolean checkCollision(final Entity a, final Entity b) {
		// Calculate center point of the entities in both axis.
		int centerAX = a.getPositionX() + a.getWidth() / 2;
		int centerAY = a.getPositionY() + a.getHeight() / 2;
		int centerBX = b.getPositionX() + b.getWidth() / 2;
		int centerBY = b.getPositionY() + b.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
		int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

	/**
	 * Returns a GameState object representing the status of the game for 1p mode.
	 *
	 * @return Current game state.
	 */
	public final GameState getGameState1p() {
		return new GameState(this.level, this.score, this.lives,
				this.bulletsShot1, this.shipsDestroyed);
	}
	/**
	 * Returns a GameState object representing the status of the game for 2p mode.
	 *
	 * @return Current game state.
	 */
	public final GameState getGameState2p() {
		return new GameState(this.level, this.score, this.lives, this.lives2,
				this.bulletsShot1, this.bulletsShot2,  this.shipsDestroyed);
	}

	/** 아이템 종류에 맞는 기능 실행 */
	private void useItem(Item item, Ship ship) {
		if(item == null) {
			this.logger.info("보유한 아이템이 없습니다");
		}
		else{
			if (!item.getIsGet() &&
					item.getItemType() == Item.ItemType.SubPlaneItem) {
				ship.setAuxiliaryShipsMode();
				this.logger.info("SubPlane Item 사용");
			}
			else if (!item.getIsGet() &&
					item.getItemType() == Item.ItemType.SpeedUpItem) {
				// 여기에 스피드업 아이템 코드 작성
				this.logger.info("SpeedUp Item 사용");
			}
			else if (!item.getIsGet() &&
					item.getItemType() == Item.ItemType.InvincibleItem) {
				this.ship.runInvincible();
				this.logger.info("Invincible Item 사용");
			}
			else if (!item.getIsGet() &&
					item.getItemType() == Item.ItemType.BombItem) {
				// 여기에 폭탄 아이템 코드 작성
				this.logger.info("Bomb Item 사용");
			}
			item.setIsGet();
		}
	}
}