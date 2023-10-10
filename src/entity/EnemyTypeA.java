package entity;

import engine.DrawManager;
import java.awt.Color;
import java.util.Set;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

public class EnemyTypeA extends EnemyShip {
    public EnemyTypeA(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final int level) {
        super(positionX, positionY, spriteType, level);
    }

    public final void shoot(Set<Bullet> bullets) {
        bullets.add(BulletPool.getBullet(positionX
                + width / 2, positionY, EnemyShipFormation.bulletSpeed()));
    }
}