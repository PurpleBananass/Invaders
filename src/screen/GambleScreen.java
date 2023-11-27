package screen;

import engine.*;
import entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GambleScreen extends Screen {
    private static final int SELECTION_TIME = 200;
    private static final int CHANGE_DELAY = 500;
    private Cooldown selectionCooldown;
    private Ship ship;
    /** Height of the interface separation line. */
    private static final int SEPARATION_LINE_HEIGHT = 40;
    /** Set of all bullets fired by on screen ships. */
    private Set<Bullet> bullets;
    /** Player's freedom. */
    private Replayability replayability = new Replayability(0);
    private int gambleMode = 0;
    private boolean isRightBorder;
    private boolean isLeftBorder;
    public static int bettingCurrency = 0;
    private int playerCurrency;
    private int mode = 1;
    private boolean selected = false;
    private Entity[] gambleEntity;
    private DrawManager.SpriteType[] sprites;
    //잭팟인지
    private boolean isJackpot = false;
    //보상을 받아야 하는지
    private boolean isGet = false;
    //베팅금을 돌려 받아야 하는지
    private boolean isGetBack = false;
    //게임이 끝났는지
    private boolean isGameEnd = false;
    //보상을 지급 받았는지
    private boolean isPrice = false;

    /**
     * Implements the high scores screen, it shows player records.
     *
     * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
     */
    public GambleScreen(final int width, final int height, final int fps) {
        super(width, height, fps);
        this.returnCode = 7;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
        try{
            playerCurrency = Core.getFileManager().getCurrentPlayer().getCurrency();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public final void initialize() {
        super.initialize();
        this.ship = new Ship(this.width / 2, this.height - 30, Color.GREEN, DrawManager.SpriteType.Ship, false);
        this.bullets = new HashSet<Bullet>();
        this.inputDelay = Core.getCooldown(CHANGE_DELAY);

        isJackpot = false;
        isGet = false;
        isGetBack = false;
        isGameEnd = false;
        isPrice = false;
        if(bettingCurrency > playerCurrency) bettingCurrency = playerCurrency;
        this.gambleEntity = new Entity[]{new Entity(this.width / 4 -12, this.height / 3, 12 * 2, 8 * 2, Color.WHITE, false, 0),
                new Entity(this.width / 2 -12, this.height / 3, 12 * 2, 8 * 2, Color.WHITE, false, 0),
                new Entity(this.width / 4 * 3 -12, this.height / 3, 12 * 2, 8 * 2, Color.WHITE, false, 0)};
        this.sprites = new DrawManager.SpriteType[]{DrawManager.SpriteType.EnemyShipA1, DrawManager.SpriteType.EnemyShipB1, DrawManager.SpriteType.EnemyShipC1, DrawManager.SpriteType.EnemyShipSpecial};
    }

        /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();
        isRightBorder = this.ship.getPositionX()
                + this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
        isLeftBorder = this.ship.getPositionX()
                - this.ship.getSpeed() < 1;
        manageCollisions();
        updateEntitySprite();
        cleanBullets();
        isGameEnd = gambleEntity[0].isDecideSprite() && gambleEntity[1].isDecideSprite() && gambleEntity[2].isDecideSprite();
        if(isGameEnd) checkResult();

        draw();
        switch(gambleMode) {
            case 0:
                if (this.selectionCooldown.checkFinished()
                        && this.inputDelay.checkFinished()) {
                    if (!selected) {
                        if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                            selected = true;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                            this.returnCode = 1;
                            this.isRunning = false;
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                            if (bettingCurrency - 10 < 0) bettingCurrency = 0;
                            else bettingCurrency -= 10;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                            if (bettingCurrency + 10 > playerCurrency) bettingCurrency = playerCurrency;
                            else bettingCurrency += 10;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
                            if (bettingCurrency + 100 > playerCurrency) bettingCurrency = playerCurrency;
                            else bettingCurrency += 100;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
                            if (bettingCurrency - 100 < 0) bettingCurrency = 0;
                            else bettingCurrency -= 100;
                            this.selectionCooldown.reset();
                        }

                    } else {
                        //겜블 종목 선택
                        if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                            if (mode == 1) mode = 3;
                            else mode--;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                            if (mode == 3) mode = 1;
                            else mode++;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && bettingCurrency > 0) {
                            gambleMode = mode;
                            playerCurrency -= bettingCurrency;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                            selected = false;
                            this.selectionCooldown.reset();
                        }
                    }
                }
                break;
            case 1:
                if (this.selectionCooldown.checkFinished()
                        && this.inputDelay.checkFinished()) {
                    if (!isLeftBorder && inputManager.isKeyDown(Core.getKeySettingCode(0))) {
                        this.ship.moveLeft();
                    }
                    if (!isRightBorder && inputManager.isKeyDown(Core.getKeySettingCode(1))) {
                        this.ship.moveRight();
                    }
                    if (replayability.getReplay() == 0 && inputManager.isKeyDown(Core.getKeySettingCode(2))) {
                        this.ship.shoot(this.bullets, 1);
                    }
                    if (isGameEnd && inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                        this.isRunning = false;
                    }
                }
                break;
            default:
                break;
        }
    }
    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawGambleTitle(this, playerCurrency);
        if(gambleMode == 0){
            drawManager.drawGambleMenu(this, mode, selected);
        }
        else if (gambleMode == 1) {
            drawManager.drawEntity(this.ship, this.ship.getPositionX(),
                    this.ship.getPositionY());
            for (Bullet bullet : this.bullets)
                drawManager.drawEntity(bullet, bullet.getPositionX(),
                        bullet.getPositionY());
            for(Entity entity : this.gambleEntity)
                drawManager.drawEntity(entity, entity.getPositionX(),
                        entity.getPositionY());
            if(isGameEnd) drawManager.drawGambleResult(this, isJackpot, isGet, isGetBack, bettingCurrency);
        }
        drawManager.completeDrawing(this);
    }

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

    private void manageCollisions() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets) {
            for (Entity entities : this.gambleEntity) {
                //총알이 적에게 닿으면 멈추기 구현해야함
                if (!entities.isDecideSprite() && checkCollision(bullet, entities)) {
                    entities.setDecideSprite(true);
                }
                recyclable.add(bullet);
            }
        }
    }
    private void updateEntitySprite(){
        for (Entity entity : this.gambleEntity){
            if(!entity.isDecideSprite()){
                if(entity.getSpriteNumber() <3) entity.setSpriteNumber(entity.getSpriteNumber() +1);
                else entity.setSpriteNumber(0);
                entity.changeEntitySprite(sprites[entity.getSpriteNumber()]);
            }
        }
    }
    private void checkResult(){
        int[] checkSprite = {0,0,0,0};
        for (Entity entity : this.gambleEntity){
            checkSprite[entity.getSpriteNumber()] += 1;
        }
        for(int i=0; i<4; i++){
            switch (checkSprite[i]){
                case 2:
                    this.isGetBack = true;
                    break;
                case 3:
                    this.isGet = true;
                    break;
                default:
                    break;
            }
        }
        if(checkSprite[3] > 2){
            this.isGet = false;
            this.isJackpot = true;
        }
        if(!isPrice) {
            try{
                if(this.isGet) {
                    playerCurrency += bettingCurrency*3;
                    Core.getFileManager().updateCurrencyOfCurrentPlayer(bettingCurrency*2);
                }
                else if(this.isGetBack) {
                    playerCurrency += (int)(bettingCurrency*(1.2));
                    Core.getFileManager().updateCurrencyOfCurrentPlayer((int)(bettingCurrency*(0.2)));
                }
                else if(this.isJackpot){
                    playerCurrency += bettingCurrency*7;
                    Core.getFileManager().updateCurrencyOfCurrentPlayer(bettingCurrency * 6);
                }
                else Core.getFileManager().updateCurrencyOfCurrentPlayer(-bettingCurrency);
                this.isPrice = true;
            } catch (IOException e){
                throw new RuntimeException("Currency Of Player is not updated.");
            }
        }
    }
}

