package entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implements a pool of recyclable bullets.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class BulletPool {

	/** Set of already created bullets. */
	private static Map<Integer, Set<Bullet>> pool = new HashMap<Integer, Set<Bullet>>();

	/**
	 * Constructor, not called.
	 */
	private BulletPool() {

	}

	/**
	 * Returns a bullet from the pool if one is available, a new one if there
	 * isn't.
	 * 
	 * @param positionX
	 *            Requested position of the bullet in the X axis.
	 * @param positionY
	 *            Requested position of the bullet in the Y axis.
	 * @param speed
	 *            Requested speed of the bullet, positive or negative depending
	 *            on direction - positive is down.
	 * @param shooter
	 *            Player who shot the bullet.
	 *            0 for enemy, 1 for first player, 2 for second player.
	 * @return Requested bullet.
	 */
	public static Bullet getBullet(final int positionX,
			final int positionY, final int speed, final int shooter) {
		Bullet bullet;
		if (pool.containsKey(shooter) && !pool.get(shooter).isEmpty()) {
			bullet = pool.get(shooter).iterator().next();
			pool.get(shooter).remove(bullet);
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
			bullet.setPositionY(positionY);
			bullet.setSpeed(speed);
			bullet.setSprite();
			// Will add a function to select the Bigger version and the base version later.
//			bullet.setBiggerSprite();
		} else {
			bullet = new Bullet(positionX, positionY, speed, shooter);
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
		}
		return bullet;
	}

	/**
	 * Adds one or more bullets to the list of available ones.
	 * 
	 * @param bullets
	 *            Bullets to recycle.
	 */
	public static void recycle(final Set<Bullet> bullets) {
		for (Bullet bullet : bullets) {
			int shooter = bullet.getShooter();
			if (!pool.containsKey(shooter)) {
				pool.put(shooter, new HashSet<Bullet>());
			}
			pool.get(shooter).add(bullet);
		}
	}
}
