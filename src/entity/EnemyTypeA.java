package entity;
import engine.DrawManager;

<<<<<<< HEAD
import java.util.Set;
=======
import java.awt.Color;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

>>>>>>> 7d7c2d8de2fc6706505c1c544b5844476c924c61
public class EnemyTypeA extends EnemyShip {
    private int bullet_speed;
    public EnemyTypeA(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final int level, final int bullet_speed) {
        super(positionX, positionY, spriteType, level,bullet_speed);
        this.bullet_speed = bullet_speed;
    }

    public final void shoot(Set<Bullet> bullets) {
        bullets.add(BulletPool.getBullet(positionX
<<<<<<< HEAD
                + width / 2, positionY, bullet_speed));
=======
                + width / 2, positionY, EnemyShipFormation.bulletSpeed()));
>>>>>>> 7d7c2d8de2fc6706505c1c544b5844476c924c61
    }
}