package entity;
import engine.DrawManager;

import java.util.Set;

public class EnemyTypeC extends EnemyShip {

    private int bullet_speed;
    public EnemyTypeC(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final int level, final int bullet_speed) {
        super(positionX, positionY, spriteType, level,bullet_speed);
        this.bullet_speed = bullet_speed;
    }

    public final void shoot(Set<Bullet> bullets) {
        bullets.add(BulletPool.getBullet(positionX
                + width / 2, positionY, bullet_speed));
    }
}