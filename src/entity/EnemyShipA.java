package entity;

import engine.Cooldown;
import engine.DrawManager;
import engine.GameState;
import java.util.Set;

public class EnemyShipA extends EnemyShip {
  /** HP�� ���� */
  private final double HPPOWER = .8;
  /** �Ѿ��� �ӵ� ���� */
  private final double BULLETSPEEDPOWER = 5;
  /** ���� ��ٿ� ���� */
  private final double BULLETCOOLDOWN = 0;
  /** ���Ž� �ö󰡴� ���� */
  private final int POINT = 30;

  public EnemyShipA(
    final int positionX,
    final int positionY,
    final DrawManager.SpriteType spriteType,
    final GameState gameState
  ) {
    super(positionX, positionY, spriteType, gameState);
    super.HP = (int) (super.HP * HPPOWER);
    super.pointValue = POINT;
  }

  public final void update() {
    if (this.animationCooldown.checkFinished()) {
      this.animationCooldown.reset();
      if (spriteType == DrawManager.SpriteType.EnemyShipA1) spriteType =
        DrawManager.SpriteType.EnemyShipA2; else spriteType =
        DrawManager.SpriteType.EnemyShipA1;
    }
  }

  public final void shoot(
    final Set<Bullet> bullets,
    Cooldown shootingCooldown
  ) {
    bullets.add(
      BulletPool.getBullet(
        positionX + width / 2,
        positionY,
        (int) (super.BULLET_SPEED * BULLETSPEEDPOWER),
        0
      )
    );
    shootingCooldown.timedown(BULLETCOOLDOWN);
  }
}
