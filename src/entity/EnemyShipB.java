package entity;

import engine.DrawManager;
import engine.GameState;

public class EnemyShipB extends EnemyShip {
    /** HP의 배율 */
    private final double HPPOWER = 0.5;
    /** 제거시 올라가는 점수 */
    private final int POINT = 10;
    public EnemyShipB(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final GameState gameState) {
        super(positionX, positionY, spriteType, gameState);
        super.HP = (int)(super.HP * HPPOWER);
        super.pointValue = POINT;
    }
}
