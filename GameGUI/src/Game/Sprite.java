package Game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * This class represents a <a href=
 * "https://gamedevelopment.tutsplus.com/tutorials/an-introduction-to-spritesheet-animation--gamedev-13099"
 * >sprite animation</a> character.</br>
 * 
 * It handles the changing of the {@link Sprite}'s costumes.
 * 
 * @author Sitton
 */
public abstract class Sprite extends MoveableCharacter {

	/////////////////// fields /////////////////

	// serial number
	private static final long serialVersionUID = 1L;

	/**
	 * A double dimensional array representing the different states (images) of the
	 * sprite
	 */
	private Image[][] COSTUMES;

	/**
	 * The original image of the sprite
	 */
	private BufferedImage bufferedImage;

	/**
	 * The number of rows and columns in the original sprite image (i.e the number
	 * of rows and columns in the {@link #COSTUMES} field
	 */
	private final int ROWS, COLUMNS;

	/**
	 * The height and width of the costumes of the sprite
	 */
	private int SUB_IMG_WIDTH, SUB_IMG_HEIGHT;

	/**
	 * The current row/column, representing the current costume, in the
	 * {@link #COSTUMES} field
	 */
	protected int currentRow = 0, currentColumn = 0;

	/**
	 * A boolean determines the side the {@link Sprite} is looking at (in order to
	 * set the correct {@link #currentRow})
	 */
	private boolean isLookingRight = true;

	/**
	 * An {@link ArrayList} containing all the existing {@link Sprite} instances
	 */
	private final static ArrayList<Sprite> existingSprites = new ArrayList<>();

	// TODO - use enum instead...?
	/**
	 * The costume constant of the {@link Sprite}.</br>
	 * The images of the sprite are devided to rows and columns. Each two rows
	 * represents the same type of image, but looking to the other direction.
	 * Therefore, the costumeConst determines which type of the sprite images needs
	 * to be used: </br>
	 * 0 - first&second rows</br>
	 * 2 - third&fourth rows</br>
	 * 4 - fifth&sixth rows</br>
	 * etc...
	 */
	protected int costumeConst = 0;

	/////////////////// constructors /////////////////

	/**
	 * This constructs a new {@link Sprite} with the given parameters
	 * 
	 * @param x
	 *            - the starting location of the {@link Sprite} on the x axis
	 * @param y
	 *            - the starting location of the {@link Sprite} on the y axis
	 * @param url
	 *            - the url location of the image of the sprite
	 * @param rows
	 *            - the number of rows in the sprite image
	 * @param columns
	 *            - the number of rows in the sprite image
	 * @param wantedWidth
	 *            - the desired width of the {@link Sprite}
	 * @param wantedHeight
	 *            - the desired height of the {@link Sprite}
	 * @param speedX
	 *            - the speed of the {@link Sprite} on the x axis
	 * @param speedY
	 *            - the speed of the {@link Sprite} on the y axis
	 */
	public Sprite(int x, int y, String url, int rows, int columns, int wantedWidth, int wantedHeight, int speedX,
			int speedY) {
		super(x, y, wantedWidth, wantedHeight, speedX, speedY);
		existingSprites.add(this);
		ROWS = rows;
		COLUMNS = columns;

		handlePictures(new ImageIcon(url));
	}

	/**
	 * This constructs a new {@link Sprite} with the given parameters
	 * 
	 * @param location
	 *            - the starting location of the {@link Sprite}
	 * @param url
	 *            - the url location of the image of the sprite
	 * @param rows
	 *            - the number of rows in the sprite image
	 * @param columns
	 *            - the number of rows in the sprite image
	 * @param wantedWidth
	 *            - the desired width of the {@link Sprite}
	 * @param wantedHeight
	 *            - the desired height of the {@link Sprite}
	 * @param speedX
	 *            - the speed of the {@link Sprite} on the x axis
	 * @param speedY
	 *            - the speed of the {@link Sprite} on the y axis
	 */
	public Sprite(Point location, String url, int rows, int columns, int wantedWidth, int wantedHeight, int speedX,
			int speedY) {
		this((int) location.getX(), (int) location.getY(), url, rows, columns, wantedWidth, wantedHeight, speedX,
				speedY);
	}

	/////////////////// other methods /////////////////

	/**
	 * This method cuts the sprite image in the right places (by rows and columns)
	 * and saves it in the {@link #COSTUMES} field which represent the individual
	 * images of the character.
	 * 
	 * @param imageIcon
	 *            - the original sprite image
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
	 * This method calls the {@link MoveableCharacter#moveToLocation(newX, newY)}
	 * method, and also updates the {@link #currentColumn} and {@link #currentRow}
	 * fields of the {@link Sprite} in order to fit to the correct costume of the
	 * movement.
	 * 
	 */
	@Override
	public void moveToLocation(int newX, int newY) {
		if (newX > getX()) {
			currentRow = 0 + costumeConst;
			isLookingRight = true;
		} else if (newX < getX()) {
			currentRow = 1 + costumeConst;
			isLookingRight = false;
		} else if (isLookingRight)
			currentRow = 0 + costumeConst;
		else
			currentRow = 1 + costumeConst;

		currentColumn = getNextColumn();
		super.moveToLocation(newX, newY);
	}

	/**
	 * This method returns the next column of the sprite image (if the current
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

	/**
	 * 
	 * @return the {@link #isLookingRight} field's value - true if looking right and
	 *         false if not
	 */
	public boolean isLookingRight() {
		return isLookingRight;
	}

	/**
	 * This method draws the {@link Sprite} using its {@link #currentRow} and
	 * {@link #currentColumn} fields, and its current {@link MoveableCharacter#x x
	 * and y position} values
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(COSTUMES[currentRow][currentColumn], getX(), getY(), null);
	}

	/**
	 * @return The number of existing {@link Sprite} instances
	 */
	public static ArrayList<Sprite> getExistingSprites() {
		return existingSprites;
	}

	/**
	 * returns an {@link ArrayList} containing all the {@link Sprite} instances that
	 * are currently touching the {@link Sprite} this method ran on
	 * 
	 * @return - an {@link ArrayList} containing all the {@link Sprite}s touching my
	 *         sprite
	 */
	public ArrayList<Sprite> spritesTouching() {
		ArrayList<Sprite> touching = new ArrayList<>();
		for (Sprite sprite : existingSprites) {
			if (this.isTouching(sprite) && sprite != this) {
				touching.add(sprite);
			}
		}
		return touching;
	}

	/**
	 * Removes a sprite form the {@link #existingSprites} {@link ArrayList}
	 * 
	 * @see #getExistingSprites()
	 */
	public void removeSprite() {
		existingSprites.remove(this);
	}
}
