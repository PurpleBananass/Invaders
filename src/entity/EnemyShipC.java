package entity;

import engine.DrawManager;
import engine.GameState;

public class EnemyShipC extends EnemyShip {
    /** HP의 배율 */
    private final double HPPOWER = 0.1;
    /** 제거시 올라가는 점수 */
    private final int POINT = 20;
    public EnemyShipC(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final GameState gameState) {
        super(positionX, positionY, spriteType, gameState);
        super.HP = (int)(super.HP * HPPOWER);
        super.pointValue = POINT;
    }

    public final void update() {
        if (this.animationCooldown.checkFinished()) {
            this.animationCooldown.reset();
            if (spriteType == DrawManager.SpriteType.EnemyShipC1)
                spriteType = DrawManager.SpriteType.EnemyShipC2;
            else
                spriteType = DrawManager.SpriteType.EnemyShipC1;
        }
    }
}
