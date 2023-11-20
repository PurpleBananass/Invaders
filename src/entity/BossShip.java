package entity;

import engine.DrawManager;
import engine.GameState;

import java.util.List;

public class BossShip extends EnemyShip {
    /** Width of current screen. */
    private static final int WIDTH = 448;
    /** Height of current screen. */
    private static final int HEIGHT = 520;

    private int splitLevel; //silmeBoss
    public BossShip (final int positionX, final int positionY,
                     final DrawManager.SpriteType spriteType, final GameState gameState, final int splitLevel){
        super(positionX, positionY, spriteType, gameState);
        super.HP = 0;//따로 수정;
        super.pointValue = 0; //따로수정
        this.splitLevel = splitLevel;
    }

    /**
     * silmeBoss
     * @param Enemyship list of enemy ships
     */
    public void split(List<EnemyShip> Enemyship) {
        int currentX = this.positionX, currentY = this.positionY;
        if (this.splitLevel <= 0) return;
        currentX += 10;
        if (currentY < 20) currentY += 50;
        if (WIDTH - 20 < currentY) currentY -= 50;
        int firstY = currentY - 20, secondY = currentY + 20;
        BossShip first = new BossShip(currentX, firstY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        BossShip second = new BossShip(currentX, secondY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        Enemyship.add(first);
        Enemyship.add(second);
    }
    public void summon(List<EnemyShip> Enemyship){

    }
}
