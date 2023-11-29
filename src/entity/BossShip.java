package entity;

import engine.DrawManager;
import engine.GameState;

import java.util.List;


public class BossShip extends EnemyShip {
    private final int SpeedY = -2;
    /** Width of current screen. */
    private static final int WIDTH = 448;
    /** Height of current screen. */
    private static final int HEIGHT = 520;
    /** Width of boss ship. */
    private static final int BOSS_WIDTH = 50;
    /** Height of boss ship. */
    private static final int BOSS_HEIGHT = 30;
    private int splitLevel;

    public BossShip (final int positionX, final int positionY,
                     final DrawManager.SpriteType spriteType, final GameState gameState, int splitLevel){
        super(positionX, positionY, spriteType, gameState);
        super.HP = splitLevel;//따로 수정;
        super.pointValue = 100*splitLevel; //따로수정
        this.splitLevel = splitLevel;
        this.Move();
    }

    /**
     * when slime Boss dead this function
     */
    public void split(List<EnemyShip> enemyShipList) {
        int currentX = this.positionX, currentY = this.positionY;
        if (splitLevel <= 0) return;
        currentX += 10;
        if (currentX < 20) currentX += 50;
        if (WIDTH - 20 < currentX) currentX -= 50;
        int firstX = currentX - 20, secondX = currentX + 20;
        BossShip first = new BossShip(firstX, currentY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        BossShip second = new BossShip(secondX, currentY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        enemyShipList.add(first);
        enemyShipList.add(second);
    }
    /**
     * when Boss Die this function execute
     */
    public void Death(List<EnemyShip> enemyShipList){
        split(enemyShipList);
    }

    /**
     * when Boss attack this function execute
     */
    public void Attack() {

    }

    /**
     * when Boss attack this function execute
     */
    public void Move(){
        while (this.HP > 0) {
            int select = (int) (Math.random() * 7);
            switch (select) {
                case 0:
                case 1:
                    moveCircle();
                case 2:
                case 3:
                    moveCross();
                case 4:
                case 5:
                    moveDiamond();
                case 6:
                case 7:
                    moveTeleport();
            }
        }
    }

    /**
     * tell the direction of boss ship in perspective of gamer
     * @return true if turn right
     */
    public boolean isRight(){
        double dValue = Math.random();
        int lr = (int)(dValue * 2);
        if (lr != 0){return true;} // right
        else {return false;} // left
    }

    public int moveTrackSize(int nowShipX, int nowShipY){
        double dValue = Math.random();
        int minimX = Math.min((WIDTH - nowShipX - BOSS_WIDTH), nowShipX);
        return (int)(dValue * Math.min(minimX, (HEIGHT - nowShipY - BOSS_HEIGHT)));
    }

    /**
     * move along the circle track
     */
    public void moveCircle() {
        int r = moveTrackSize(positionX, positionY);
        if (r <= 0){moveTeleport();}
        else {
            if(isRight()){int x = 1, y = -1;}
            else {int x = -1, y = -1;}
            for (int i = 1; i <= 36; i++) {
                this.setPositionX(positionX += (int)(r * Math.sin((double) (i / (double) 36))));
                this.setPositionY(positionY += (int)(r * Math.sin((double) (i / (double) 36))));
                this.update();
                // 특정 조건에서 총도 쏘면 좋을 듯
            }
        }
    }

    /**
     * move along the cross track
     */
    public void moveCross() {
        int r = moveTrackSize(positionX, positionY);
        if (r <= 0){moveTeleport();}
        else {
            int forward;
            if(isRight()){forward = 1;}
            else {forward = -1;}
            int i;
            for (i = 1; i < r/10; i++){positionX += 10;this.update();}
            for (i = 1; i < r/10; i++){positionX -= 10;this.update();}
            for (i = 1; i < r/10; i++){positionY += forward*10;this.update();}
            for (i = 1; i < r/5 - 1; i++){positionY -= forward*10;this.update();}
            for (i = 1; i < r/10; i++){positionY += forward*10;this.update();}
        }
    }

    /**
     * move along the diamond track
     */
    public void moveDiamond() {
        int r = moveTrackSize(positionX, positionY);
        if (r <= 0){moveTeleport();}
        else {
            int i;
            for (i = 1; i < r/20; i++){positionX += 10;positionY += 10;this.update();}
            for (i = 1; i < r/20; i++){positionX -= 10;positionY += 10;this.update();}
            for (i = 1; i < r/20; i++){positionX -= 10;positionY -= 10;this.update();}
            for (i = 1; i < r/20; i++){positionX += 10;positionY -= 10;this.update();}
        }
    }

    /**
     * Teleport randomly
     */
    public void moveTeleport() {
        double randomX = Math.random();
        double randomY = Math.random();
        positionX = (int) (randomX * (WIDTH - BOSS_WIDTH));
        positionY = (int) (randomY * (HEIGHT - BOSS_HEIGHT));
        this.update();
    }
}
