package Game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public abstract class Sprite2 extends MoveableCharacter implements Cyclic {
	private Image[][] COSTUMES;
	public BufferedImage bufferedImage;
	protected final int ROWS, COLUMNS;
	private int SUB_IMG_WIDTH, SUB_IMG_HEIGHT;

	protected int currentRow = 0, currentColumn = 0;

	private static ArrayList<Sprite2> existingSprites = new ArrayList<>();

	/////////////////// setters /////////////////

	public Sprite2(int x, int y, String url, int rows, int columns, int wantedWidth, int wantedHeight, int speedX,
			int speedY) {
		super(x, y, wantedWidth, wantedHeight, speedX, speedY);
		existingSprites.add(this);
		ROWS = rows;
		COLUMNS = columns;

		handlePictures(new ImageIcon(url));
	}

	private void handlePictures(ImageIcon imageIcon) {
		int currentX = 0, currentY = 0;

		this.SUB_IMG_WIDTH = imageIcon.getIconWidth() / COLUMNS;
		this.SUB_IMG_HEIGHT = imageIcon.getIconHeight() / ROWS;
		this.COSTUMES = new Image[ROWS][COLUMNS];

		// initialize the buffered image to fit the imageIcon
		bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		bufferedImage.getGraphics().drawImage(imageIcon.getImage(), 0, 0, null);

		// initialize the array of sub-images
		for (int i = 0; i < ROWS; i++) {
			currentX = 0;
			for (int j = 0; j < COLUMNS; j++) {
				COSTUMES[i][j] = bufferedImage.getSubimage(currentX, currentY, SUB_IMG_WIDTH, SUB_IMG_HEIGHT)
						.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
				currentX += SUB_IMG_WIDTH;
			}
			currentY += SUB_IMG_HEIGHT;
		}
	}

	@Override
	public void oneCycle(Surface[] surfaces) {
		moveByDirection();
		fall(surfaces);
	}

	@Override
	public void moveByDirection() {
		super.moveByDirection();
		switch (currentDirections.directionX) {
		case MOVE_RIGHT:
			currentRow = 0;
			currentColumn = getNextColumn();
			break;
		case MOVE_LEFT:
			currentRow = 1;
			currentColumn = getNextColumn();
			break;
		default:
			break;
		}
	}

	public int getNextColumn() {
		if (currentColumn == COLUMNS - 1)
			currentColumn = 0;
		else
			currentColumn++;
		return currentColumn;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(COSTUMES[currentRow][currentColumn], getX(), getY(), null);
	}

	public static ArrayList<Sprite2> getExistingSprites() {
		return existingSprites;
	}

	public void removeSprite() {
		existingSprites.remove(this);
	}

}
