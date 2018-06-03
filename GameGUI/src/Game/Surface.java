package Game;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * This class represents a surface which {@link MoveableCharacter} can stand on
 * without falling
 * 
 * @author Sitton
 *
 */
public class Surface extends JComponent {

	/////////////////// fields /////////////////

	// serial version
	private static final long serialVersionUID = 1L;

	/**
	 * the position of the {@link Surface} on the x axis
	 */
	private int x;

	/**
	 * the position of the {@link Surface} on the y axis
	 */
	private int y;

	/**
	 * the width of the {@link Surface}
	 */
	private int width;

	/**
	 * the height of the {@link Surface}
	 */
	private int height;

	/////////////////// constructors /////////////////

	/**
	 * This constructs a new {@link Surface} with the given arguments
	 * 
	 * @param x
	 *            - the position of the {@link Surface} on the x axis
	 * @param y
	 *            - the position of the {@link Surface} on the y axis
	 * @param width
	 *            - the width of the {@link Surface}
	 * @param height
	 *            - the height of the {@link Surface}
	 */
	public Surface(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/////////////////// getters /////////////////
	/**
	 * returns the location of the {@link Surface} on the x axis
	 * 
	 * @return the location of the {@link Surface} on the x axis
	 */
	public int getX() {
		return x;
	}

	/**
	 * returns the location of the {@link Surface} on the y axis
	 * 
	 * @return the location of the {@link Surface} on the y axis
	 */
	public int getY() {
		return y;
	}

	/**
	 * returns the width of the {@link Surface}
	 * 
	 * @return the width of the {@link Surface}
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * returns the height of the {@link Surface}
	 * 
	 * @return the height of the {@link Surface}
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * draw a rectangle with the same values as the {@link Surface}
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(x, y, width, height);
	}
}
