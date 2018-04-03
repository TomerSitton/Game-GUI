package Game;

import java.awt.Graphics;

import javax.swing.JComponent;

public class Surface extends JComponent {
	private int x;
	private int y;
	private int width;
	private int height;

	public Surface(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(x, y, width, height);
	}
}
