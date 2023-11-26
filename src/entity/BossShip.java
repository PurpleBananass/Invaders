package entity;

import engine.DrawManager;
import engine.GameState;

import java.util.ArrayList;
import java.util.List;

public class BossShip extends EnemyShip {
    private List<BossShip> SplitList;
    private final int SpeedX = -2;
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
                     final DrawManager.SpriteType spriteType, final GameState gameState, final int splitLevel){
        super(positionX, positionY, spriteType, gameState);
        super.HP = 0;//따로 수정;
        super.pointValue = 0; //따로수정
        this.splitLevel = splitLevel;
        SplitList = new ArrayList<>();
        SplitList.add(this);
    }

    /**
     * when slime Boss dead this function
     */
    public void split() {
        int currentX = this.positionX, currentY = this.positionY;
        if (this.splitLevel <= 0) return;
        currentX += 10;
        if (currentY < 20) currentY += 50;
        if (WIDTH - 20 < currentY) currentY -= 50;
        int firstY = currentY - 20, secondY = currentY + 20;
        BossShip first = new BossShip(currentX, firstY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        BossShip second = new BossShip(currentX, secondY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        SplitList.add(first);
        SplitList.add(second);
    }
    /**
     * when Boss Die this function execute
     */
    public void Death(){

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
            for (int i = 1; i <= 360; i++) {
                positionX += r * Math.sin((double) (i / (double) 360));
                positionY += r * Math.cos((double) (i / (double) 360));
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
            int direction = 0;
            if(isRight()){direction = 1;}
            else {direction = -1;}
            int i;
            for (i = 1; i < r; i++){positionX += i;}
            for (i = 1; i < r; i++){positionX -= i;}
            for (i = 1; i < r; i++){positionY += direction * i;}
            for (i = 1; i < 2*r-1; i++){positionY -= direction * i;}
            for (i = 1; i < r; i++){positionY += direction * i;}
        }
    }

    /**
     * move along the diamond track
     */
    public void moveDiamond() {
        int r = moveTrackSize(positionX, positionY);
        if (r <= 0){moveTeleport();}
        else {}
    }

    /**
     * Teleport randomly
     */
    public void moveTeleport() {
    }
}
