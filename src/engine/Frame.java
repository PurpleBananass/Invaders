package engine;

import java.awt.Insets;

import javax.swing.JFrame;

import screen.Screen;

/**
 * Implements a frame to show screens on.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
@SuppressWarnings("serial")
public class Frame extends JFrame {

	/** Frame width. */
	private int width;
	/** Frame height. */
	private int height;
	/** Screen currently shown. */
	private Screen currentScreen;
	/** bottom hud space not shown by getHeight to not break relative positioning **/
	private int bottomHudHeight;

	/**
	 * Initializes the new frame.
	 * 
	 * @param width
	 *            Frame width.
	 * @param height
	 *            Frame height.
	 */
	public Frame(final int width, final int height, final int bottomHudHeight) {
		setSize(width, height+bottomHudHeight);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);

		Insets insets = getInsets();
		this.width = width - insets.left - insets.right;
		this.height = height + bottomHudHeight - insets.top + insets.bottom;
		this.bottomHudHeight = bottomHudHeight;
		setTitle("Invaders");

		addKeyListener(Core.getInputManager());
	}

	/**
	 * Sets current screen.
	 * 
	 * @param screen
	 *            Screen to show.
	 * @return Return code of the finished screen.
	 */
	public final int setScreen(final Screen screen) {
		currentScreen = screen;
		currentScreen.initialize();
		return currentScreen.run();
	}

	/**
	 * Getter for frame width.
	 * 
	 * @return Frame width.
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Getter for frame height.
	 *
	 * @return Frame height.
	 */

	public final int getHeight() {
		return this.height - bottomHudHeight;
	}

	/**
	 * Getter for bottom hud height.
	 *
	 * @return bottom hud height.
	 */

	public final int getBottomHudHeight() {
		return bottomHudHeight;
	}
}
