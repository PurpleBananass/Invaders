package entity;

import engine.DrawManager;
import engine.GameState;

public class EnemyShipA extends EnemyShip {
    /** HP의 배율 */
    private final double HPPOWER = 1;
    /** 제거시 올라가는 점수 */
    private final int POINT = 30;
    public EnemyShipA(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final GameState gameState) {
        super(positionX, positionY, spriteType, gameState);
        super.HP = (int)(super.HP * HPPOWER);
        super.pointValue = POINT;
    }

    public final void update() {
        if (this.animationCooldown.checkFinished()) {
            this.animationCooldown.reset();
            if (spriteType == DrawManager.SpriteType.EnemyShipA1)
                spriteType = DrawManager.SpriteType.EnemyShipA2;
            else
                spriteType = DrawManager.SpriteType.EnemyShipA1;
        }
    }

}
