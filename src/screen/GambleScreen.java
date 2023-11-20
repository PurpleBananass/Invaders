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
    private static final int SELECTION_TIME = 300;
    private Cooldown selectionCooldown;
    private Ship ship;
    /** Height of the interface separation line. */
    private static final int SEPARATION_LINE_HEIGHT = 40;
    /** Set of all bullets fired by on screen ships. */
    private Set<Bullet> bullets;
    /** Player's freedom. */
    private Replayability replayability = new Replayability(0);
    private int gambleMode = 0;
    private boolean isRightBorder = this.ship.getPositionX()
            + this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
    private boolean isLeftBorder = this.ship.getPositionX()
            - this.ship.getSpeed() < 1;
    public int bettingCurrency = 0;
    private int playerCurrency;
    private int mode = 1;
    private boolean gamblePlaying = true;
    private boolean selected = false;
    private EnemyShip[] gambleEntity = {new EnemyShip(0,0, DrawManager.SpriteType.EnemyShipA1),
            new EnemyShip(0,0, DrawManager.SpriteType.EnemyShipA1),
            new EnemyShip(0,0, DrawManager.SpriteType.EnemyShipA1)};

    /**
     * Implements the high scores screen, it shows player records.
     *
     * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
     */
    public GambleScreen(final int width, final int height, final int fps) {
        super(width, height, fps);
        this.returnCode = 10;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();

        this.ship = new Ship(this.width / 2, this.height - 30, Color.GREEN, DrawManager.SpriteType.Ship, false);
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
        manageCollisions();
        cleanBullets();
        try{
            playerCurrency = Core.getFileManager().getCurrentPlayer().getCurrency();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        draw();
        switch (gambleMode){
            //도박 들어 가기 전 베팅, 게임 선택 창
            case 0:
                if (this.selectionCooldown.checkFinished()
                        && this.inputDelay.checkFinished()) {
                    if (selected) {
                        if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                            selected = true;
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                            this.returnCode = 1;
                            this.isRunning = false;
                        }
                    } else {
                        //겜블 종목 선택
                        if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                            if (mode == 1) mode = 3;
                            else mode--;
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                            if (mode == 3) mode = 1;
                            else mode++;
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                            gambleMode = mode;
                        }
                        if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                            selected = false;
                        }
                    }
                }
            //빠칭코
            case 1:
                if (this.selectionCooldown.checkFinished()
                        && this.inputDelay.checkFinished()){
                    if (!isLeftBorder && inputManager.isKeyDown(Core.getKeySettingCode(0))) {
                        this.ship.moveLeft();
                    }
                    if (!isRightBorder && inputManager.isKeyDown(Core.getKeySettingCode(1))) {
                        this.ship.moveRight();
                    }
                    if (replayability.getReplay() == 0 && inputManager.isKeyDown(Core.getKeySettingCode(2))) {
                        this.ship.shoot(this.bullets, 1);
                    }

                    if(gamblePlaying && inputManager.isKeyDown(KeyEvent.VK_ESCAPE)){
                        this.isRunning = false;
                    }
                }
                break;
            default:
                break;
        }

    }
    /**
     * 해야할 것:
     * 빠칭코 엔티티 그려넣기
     * 엔티티와 총알의 거리를 재고 엔티티 멈추게 하기
     * drawManager에서 도박 메뉴창 그리기
     * Currency와 베팅 연결하기
     */


    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawGambleTitle(this, playerCurrency);
        switch (gambleMode){
            case 0:
                drawManager.drawGambleMenu(this);
                break;
            case 1:
                drawManager.drawEntity(this.ship, this.ship.getPositionX(),
                        this.ship.getPositionY());
                drawManager.drawGambleEntity(this);
                for (Bullet bullet : this.bullets)
                    drawManager.drawEntity(bullet, bullet.getPositionX(),
                            bullet.getPositionY());


                break;
        }

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
            for (EnemyShip enemyShip : this.gambleEntity) {
                //총알이 적에게 닿으면 멈추기 구현해야함
                if (!enemyShip.isDestroyed() && checkCollision(bullet, enemyShip)) {
                    
                }
                recyclable.add(bullet);
            }
        }

    }
}

