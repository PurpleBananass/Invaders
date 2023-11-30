package screen;

import engine.*;
import entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.random;

public class GambleScreen extends Screen {
    private static final int SELECTION_TIME = 200;
    private static final int CHANGE_DELAY = 500;
    private Cooldown selectionCooldown;
    /** Rock Paper Scissors' inner delay*/
    private Cooldown rpsDelay;
    private Cooldown changeDelay;
    private Ship ship;
    /** Height of the interface separation line. */
    private static final int SEPARATION_LINE_HEIGHT = 40;
    /** Set of all bullets fired by on screen ships. */
    private Set<Bullet> bullets;
    /** Player's freedom. */
    private Replayability replayability = new Replayability(0);
    //겜블 모드 0 == 선택창, 1 == 파칭코, 2 == 가위바위보
    private int gambleMode = 0;
    private boolean isRightBorder;
    private boolean isLeftBorder;
    //플레이어의 베팅금
    public static int bettingCurrency = 0;
    //플레이어의 재화
    private int playerCurrency;
    //선택창에서의 겜블 모드 1 == 파칭코, 2 == 가위바위보
    private int mode = 1;
    //베팅금을 정했는지
    private boolean bettingSelected = false;
    //파칭코에 나오는 엔티티
    private Entity[] gambleEntity;
    //엔티티들의 스프라이트 0,1,2 == 일반, 3 == 잭팟
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
    //컴퓨터가 고르는 가위바위보 0 == 바위, 1 == 보, 2 == 가위
    private int computerSelect = 0;
    //플레이어가 고른 가위바위보
    private int playerSelect = 0;
    //플레이어가 가위바위보 선택했는지
    private boolean rpsSelected = false;
    //가위바위보 상금의 배율
    private final double[] rpsPriceRate = {1,2,3,5,1,1.5,1.5,2,1.2,7};
    //가위바위보 상금이 정해졌는지
    private double selectedPriceRate;
    private boolean checkLoop = false;

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
        this.rpsDelay = Core.getCooldown(1000);
        this.changeDelay = Core.getCooldown(30);
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
        switch (gambleMode){
            case 1:
                isRightBorder = this.ship.getPositionX()
                        + this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
                isLeftBorder = this.ship.getPositionX()
                        - this.ship.getSpeed() < 1;
                manageCollisions();
                updateEntitySprite();
                cleanBullets();
                isGameEnd = gambleEntity[0].isDecideSprite() && gambleEntity[1].isDecideSprite() && gambleEntity[2].isDecideSprite();
                if(isGameEnd) checkResult();
                break;
            case 2:
                updateComputerSelect();
                if(!isGameEnd && rpsSelected) checkRPSResult();
                if(isGameEnd){
                    if(!isPrice)randomRPSPrice();
                    checkResult();
                }
                break;
            default:
                break;
        }
        draw();
        switch(gambleMode) {
            case 0:
                if (this.selectionCooldown.checkFinished()
                        && this.inputDelay.checkFinished()) {
                    if (!bettingSelected) {
                        if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && bettingCurrency > 0) {
                            bettingSelected = true;
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
                            if (mode == 2) mode--;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                            if (mode == 1) mode++;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                            gambleMode = mode;
                            playerCurrency -= bettingCurrency;
                            this.selectionCooldown.reset();
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                            bettingSelected = false;
                            this.selectionCooldown.reset();
                        }
                    }
                }
                break;
            //파칭코
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
            //가위바위보
            case 2:
                if (this.selectionCooldown.checkFinished()
                        && this.inputDelay.checkFinished()) {
                    if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                        if (playerSelect >0) playerSelect--;
                        this.selectionCooldown.reset();
                    }
                    if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                        if (playerSelect<2) playerSelect++;
                        this.selectionCooldown.reset();
                    }
                    if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && this.rpsDelay.checkFinished()) {
                        rpsSelected = true;
                        this.selectionCooldown.reset();
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
        switch (gambleMode){
            // 메뉴창
            case 0:
                drawManager.drawGambleMenu(this, mode, bettingSelected);
                break;
            // 파칭코
            case 1:
                drawManager.drawPachinkoRate();
                drawManager.drawEntity(this.ship, this.ship.getPositionX(),
                        this.ship.getPositionY());
                for (Bullet bullet : this.bullets)
                    drawManager.drawEntity(bullet, bullet.getPositionX(),
                            bullet.getPositionY());
                for(Entity entity : this.gambleEntity)
                    drawManager.drawEntity(entity, entity.getPositionX(),
                            entity.getPositionY());
                if(isGameEnd) drawManager.drawGambleResult(this, isJackpot, isGet, isGetBack, bettingCurrency, 3);
                break;
            // 가위바위보
            case 2:
                drawManager.drawRockPaperScissors(this,computerSelect,playerSelect);
                if(isGameEnd){
                    rpsDelay.reset();
                    int i=0;
                    while(!rpsDelay.checkFinished() && !checkLoop){
                        if(isGet){
                            drawManager.drawRPSPriceRate(this,selectedPriceRate);
                            drawManager.completeDrawing(this);
                        }
                    }
                    checkLoop = true;
                    drawManager.drawGambleResult(this, isJackpot, isGet, isGetBack, bettingCurrency, selectedPriceRate);
                }
                if(!rpsDelay.checkFinished() && !isGameEnd){
                    drawManager.drawDrawString(this);
                    drawManager.completeDrawing(this);
                }
                break;
            default:
                break;
        }
        drawManager.completeDrawing(this);
    }
    /**
     * Delete bullets if they get out screen
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
     * Check collisions between gamble entities and bullets
     * If collisions occur, gamble entities are decided their sprite.
     */
    private void manageCollisions() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets) {
            for (Entity entities : this.gambleEntity) {
                if (!entities.isDecideSprite() && checkCollision(bullet, entities)) {
                    entities.setDecideSprite(true);
                }
                recyclable.add(bullet);
            }
        }
    }
    /**
     * Update gamble entities' sprites
     */
    private void updateEntitySprite(){
        if(this.changeDelay.checkFinished()) {
            for (Entity entity : this.gambleEntity) {
                if (!entity.isDecideSprite()) {
                    if (entity.getSpriteNumber() < 3) entity.setSpriteNumber(entity.getSpriteNumber() + 1);
                    else entity.setSpriteNumber(0);
                    entity.changeEntitySprite(sprites[entity.getSpriteNumber()]);
                }
            }
            this.changeDelay.reset();
        }
    }
    /**
     * Check gamble's result and Update players' currency
     */
    private void checkResult(){
        switch (gambleMode){
            //파칭코
            case 1:
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
                break;
            //가위바위보
            case 2:
                if(!isPrice) {
                    try{
                        if(this.isGet) {
                            playerCurrency += (int) (bettingCurrency* selectedPriceRate);
                            Core.getFileManager().updateCurrencyOfCurrentPlayer((int)(bettingCurrency*(selectedPriceRate -1)));
                        }
                        else Core.getFileManager().updateCurrencyOfCurrentPlayer(-bettingCurrency);
                        this.isPrice = true;
                    } catch (IOException e){
                        throw new RuntimeException("Currency Of Player is not updated.");
                    }
                }
                break;
            default:
                break;
        }
    }
    /**
     * Update Computer's selection in {Rock,Paper,Scissors}
     */
    private void updateComputerSelect(){
        if(!rpsSelected && this.rpsDelay.checkFinished() && this.changeDelay.checkFinished()) {
            computerSelect = (computerSelect + 1) % 3;
            this.changeDelay.reset();
        }
    }
    /**
     * Check Rock Paper Scissors' Result
     * If draw, give delay.
     */
    private void checkRPSResult(){
        if(computerSelect == playerSelect){
            rpsDelay.reset();
            rpsSelected = false;
        }
        else if(playerSelect == computerSelect+1 || playerSelect == computerSelect -2){
            isGameEnd = true;
            isGet = true;
        }
        else isGameEnd = true;
    }
    /**
     * Select Rock Paper Scissors' Price randomly
     */
    private void randomRPSPrice(){
        int i = (int) (random()*random()*random()*100 %10);
        selectedPriceRate = rpsPriceRate[i];
    }
}

