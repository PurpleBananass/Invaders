package entity;

public class EnemyTypeB extends EnemyShip {

    public EnemyTypeB(final int positionX, final int positionY,
                      final SpriteType spriteType) {
        super(positionX, positionY, spriteType);
    }

    public final void shoot(final Set<Bullet> bullets) {
        bullets.add(BulletPool.getBullet(positionX
                + width / 2, positionY, EnemyShipFormations.BULLET_SPEED));
    }
}