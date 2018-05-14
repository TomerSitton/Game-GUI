package Game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public abstract class Sprite2 extends MoveableCharacter {
	/**
	 * a double dimensional array representing the different states of the sprite
	 */
	private Image[][] COSTUMES;
	/**
	 * the original image of the sprite
	 */
	public BufferedImage bufferedImage;
	/**
	 * the number of rows and columns in the original sprite image (i.e the number
	 * of rows and columns in the {@link Sprite2#COSTUMES} variable
	 */
	private final int ROWS, COLUMNS;
	/**
	 * the height and width of the costumes of the sprite
	 */
	private int SUB_IMG_WIDTH, SUB_IMG_HEIGHT;
	/**
	 * the current row/column, representing the current costume, in the
	 * {@link Sprite2#COSTUMES} variable
	 */
	private int currentRow = 0, currentColumn = 0;

	private boolean isLookingRight = true;
	/**
	 * and {@link ArrayList} representing the all the existing sprites
	 */
	private final static ArrayList<Sprite2> existingSprites = new ArrayList<>();

	/////////////////// setters /////////////////
	/**
	 * this constructs a new {@link Sprite2} with the given parameters
	 * 
	 * @param x
	 *            - the starting location of the {@link Sprite2} on the x axis
	 * @param y
	 *            - the starting location of the {@link Sprite2} on the y axis
	 * @param url
	 *            - the url location of the image of the sprite
	 * @param rows
	 *            - the number of rows in the sprite image
	 * @param columns
	 *            - the number of rows in the sprite image
	 * @param wantedWidth
	 *            - the desired width of the {@link Sprite2}
	 * @param wantedHeight
	 *            - the desired height of the {@link Sprite2}
	 * @param speedX
	 *            - the speed of the {@link Sprite2} on the x axis
	 * @param speedY
	 *            - the speed of the {@link Sprite2} on the y axis
	 */
	public Sprite2(int x, int y, String url, int rows, int columns, int wantedWidth, int wantedHeight, int speedX,
			int speedY) {
		super(x, y, wantedWidth, wantedHeight, speedX, speedY);
		existingSprites.add(this);
		ROWS = rows;
		COLUMNS = columns;

		handlePictures(new ImageIcon(url));
	}

	/**
	 * this method cuts the sprite image in the right places (by rows and columns)
	 * and saves it in a double dimensional array named COSTUMES which represent the
	 * individual images of the character.
	 * 
	 * @param imageIcon
	 */
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

	/**
	 * this method calls the {@link MoveableCharacter#moveToLocation(newX, newY)}
	 * method, and also and also changes the sprite's its column and row values in
	 * order to fit the correct image in the COSTUMES images array
	 */
	@Override
	public void moveToLocation(int newX, int newY) {
		if (newX > getX()) {
			currentRow = 0;
			isLookingRight = true;
		} else if (newX < getX()) {
			currentRow = 1;
			isLookingRight = false;
		}
		currentColumn = getNextColumn();
		super.moveToLocation(newX, newY);
	}

	/**
	 * this method returns the next column of the sprite image (if the current
	 * column is the last column in the image, than it will return the first column)
	 * 
	 * @return - the column after the current column in the sprite's image
	 */
	public int getNextColumn() {
		if (currentColumn == COLUMNS - 1)
			currentColumn = 0;
		else
			currentColumn++;
		return currentColumn;
	}

	public boolean isLookingRight() {
		return isLookingRight;
	}

	/**
	 * this method draws the {@link Sprite2} using its current row and column, and
	 * its current x and y values
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(COSTUMES[currentRow][currentColumn], getX(), getY(), null);
	}

	/**
	 * 
	 * @return the number of existing {@link Sprite2} instances
	 */
	public static ArrayList<Sprite2> getExistingSprites() {
		return existingSprites;
	}

	/**
	 * returns an {@link ArrayList} containing all the {@link Sprite2}s that are
	 * currently touching the sprite this method ran on
	 * 
	 * @return - an {@link ArrayList} containing all the {@link Sprite2}s touching
	 *         my sprite
	 */
	public ArrayList<Sprite2> spritesTouching() {
		ArrayList<Sprite2> touching = new ArrayList<>();
		for (Sprite2 sprite : existingSprites) {
			if (this.isTouching(sprite) && sprite != this) {
				touching.add(sprite);
			}
		}
		return touching;
	}

	/**
	 * removes a sprite form the existingSprites {@link ArrayList}
	 * 
	 * @see Sprite2#getExistingSprites()
	 */
	public void removeSprite() {
		existingSprites.remove(this);
	}
}
