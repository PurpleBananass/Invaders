package entity;

import engine.Cooldown;
import engine.DrawManager;
import engine.GameState;

import java.util.Set;

public class EnemyShipC extends EnemyShip {
    /** HP의 배율 */
    private final double HPPOWER = .1;
    /** 총알의 속도 배율 */
    private final double BULLETSPEEDPOWER = 2;
    /** 슈팅 쿨다운 배율 */
    private final double BULLETCOOLDOWN = 0.5;
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
    public final void shoot(final Set<Bullet> bullets, Cooldown shootingCooldown) {
        bullets.add(BulletPool.getBullet(positionX
                + width / 2, positionY, (int)(super.BULLET_SPEED * BULLETSPEEDPOWER),0));
        shootingCooldown.timedown(BULLETCOOLDOWN);
    }
}
