package entity;

import engine.DrawManager.SpriteType;

import java.awt.*;

/**
 * Implements items, which display on the game screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class ShowItem extends Entity {
	/**
	 * Constructor, establishes the item's properties.
	 *
	 * @param positionX
	 *            Initial position of the item in the X axis.
	 * @param positionY
	 *            Initial position of the item in the Y axis.
	 */
	public ShowItem(final int positionX, final int positionY) {
		super(positionX, positionY, 13 * 2, 8 * 2, Color.YELLOW);
		this.spriteType = SpriteType.Ship;
	}
}
