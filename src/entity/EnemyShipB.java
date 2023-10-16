package entity;

import engine.DrawManager;
import engine.GameState;

public class EnemyShipB extends EnemyShip {
    /** HP의 배율 */
    private final double HPPOWER = .4;
    /** 제거시 올라가는 점수 */
    private final int POINT = 10;
    public EnemyShipB(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final GameState gameState) {
        super(positionX, positionY, spriteType, gameState);
        super.HP = (int)(super.HP * HPPOWER);
        super.pointValue = POINT;
    }

    public final void update() {
        if (this.animationCooldown.checkFinished()) {
            this.animationCooldown.reset();
            if (spriteType == DrawManager.SpriteType.EnemyShipB1)
                spriteType = DrawManager.SpriteType.EnemyShipB2;
            else
                spriteType = DrawManager.SpriteType.EnemyShipB1;
        }
    }
}
