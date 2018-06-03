package Game;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * This class represents a character in the game which has the ability to move.
 * 
 * @author Sitton
 */
public abstract class MoveableCharacter extends JComponent {

	/////////////////// fields /////////////////

	// serial version
	private static final long serialVersionUID = 1L;

	/**
	 * The positions of the {@link MoveableCharacter} on the X and Y axes
	 */
	protected int x, y;

	/**
	 * The width and the height of the {@link MoveableCharacter}
	 */
	public final int WIDTH, HEIGHT;

	/**
	 * The speeds of the {@link MoveableCharacter} on the X and Y axes
	 */
	protected int speedX, speedY;

	/**
	 * This is a boolean regarding the current state of the
	 * {@link MoveableCharacter}:</br>
	 * isJumping is true when the {@link MoveableCharacter} is in the middle of a
	 * jump</br>
	 */
	private boolean isJumping;
	/**
	 * This is a boolean regarding the current state of the
	 * {@link MoveableCharacter}:</br>
	 * onTheGround is true when the {@link MoveableCharacter} is standing on an
	 * instance of a {@link Surface}
	 */
	private boolean onTheGround;

	/////////////////// constructors /////////////////

	/**
	 * constructs a new {@link MoveableCharacter} with the given arguments
	 * 
	 * @param x
	 *            - the starting location of the {@link MoveableCharacter} on the x
	 *            axis
	 * @param y
	 *            - the starting location of the {@link MoveableCharacter} on the y
	 *            axis
	 * @param width
	 *            - the width of the {@link MoveableCharacter}
	 * @param height
	 *            - the height of the {@link MoveableCharacter}
	 * @param speedX
	 *            - the speed of the {@link MoveableCharacter} on the x axis
	 * @param speedY
	 *            - the speed of the {@link MoveableCharacter} on the y axis
	 */
	public MoveableCharacter(int x, int y, int width, int height, int speedX, int speedY) {
		this.x = x;
		this.y = y;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.speedX = speedX;
		this.speedY = speedY;
	}

	/////////////////// setters /////////////////

	/**
	 * set the location of the {@link MoveableCharacter} on the x axis
	 * 
	 * @param x
	 *            - the wanted location for the {@link MoveableCharacter} to be
	 *            positioned at
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * set the location of the {@link MoveableCharacter} on the y axis
	 * 
	 * @param y
	 *            - the wanted location for the {@link MoveableCharacter} to be
	 *            positioned at
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * sets the absolute speed of the {@link MoveableCharacter} on the X axis
	 * 
	 * @param speedX
	 *            The speed on the X Axis
	 */
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	/**
	 * sets the absolute speed of the {@link MoveableCharacter} on the Y axis
	 * 
	 * @param speedY
	 *            The speed on the Y Axis
	 */
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	/////////////////// getters /////////////////

	/**
	 * returns the location of the {@link MoveableCharacter} on the x axis
	 * 
	 * @return the location of the {@link MoveableCharacter} on the x axis
	 */
	public int getX() {
		return x;
	}

	/**
	 * returns the location of the {@link MoveableCharacter} on the y axis
	 * 
	 * @return the location of the {@link MoveableCharacter} on the y axis
	 */
	public int getY() {
		return y;
	}

	/**
	 * returns the absolute speed of the Sprite on the X axis
	 * 
	 * @return The absolute speed of the Sprite on the X axis
	 */
	public int getSpeedX() {
		return speedX;
	}

	/**
	 * returns the absolute speed of the Sprite on the Y axis
	 * 
	 * @return The absolute speed of the Sprite on the Y axis
	 */
	public int getSpeedY() {
		return speedY;
	}

	/**
	 * returns the width of the {@link MoveableCharacter}
	 * 
	 * @return the width of the {@link MoveableCharacter}
	 */
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * returns the height of the {@link MoveableCharacter}
	 * 
	 * @return the height of the {@link MoveableCharacter}
	 */
	public int getHeight() {
		return HEIGHT;
	}

	/////////////////// other methods /////////////////

	/**
	 * This method moves the {@link MoveableCharacter} to the target (x,y) position
	 * 
	 * @param newX
	 *            - the desired x location to the {@link MoveableCharacter} to be at
	 * @param newY
	 *            - the desired y location to the {@link MoveableCharacter} to be at
	 */
	public void moveToLocation(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}

	/**
	 * This method changes the {@link #isJumping} field to be true for a short time,
	 * thus making the {@link #getCurrentYSpeed(Surface[])} method return the
	 * jumping speed of the {@link MoveableCharacter}, which makes it move upwards
	 * for that short amount of time.</br>
	 * </br>
	 * 
	 * note - if the {@link MoveableCharacter} is already in the middle of a jump or
	 * is not standing on a {@link Surface}, this method will do nothing.
	 * 
	 * @see #isJumping
	 * @see #onTheGround
	 * @see #getCurrentYSpeed(Surface[])
	 */
	public void TryToJump() {
		if (isJumping || !onTheGround) {
			System.out.println("can't jump. the character is already in the middle of a jump");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				isJumping = true;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				isJumping = false;
			}
		}).start();

	}

	/**
	 * This method makes the character move downwards if it is not standing on a
	 * {@link Surface} or if it is not in the middle of a jump
	 * 
	 * 
	 * This method determines the current Y speed of the
	 * {@link MoveableCharacter}.</br>
	 * </br>
	 * 
	 * If it's trying to jump this method will return the {@link #speedY}
	 * field.</br>
	 * If it's standing on a surface this method will return 0.</br>
	 * If it's not jumping and not standing on the ground, this method will return
	 * {@link WorldConstants.PHYSICS#FALLING_SPEED the constant falling speed.}
	 * 
	 * @param surfaces
	 *            - the surfaces in the JFrame.
	 * 
	 * @see Surface
	 * @see WorldConstants.PHYSICS#FALLING_SPEED
	 */
	public int getCurrentYSpeed(Surface[] surfaces) {
		// the character is in a middle of a jump.
		if (isJumping)
			return -speedY;
		for (Surface surface : surfaces) {

			// the character and the surface are on the same y axis scale.
			if (this.y + this.HEIGHT >= surface.getY()) {
				// the character is above the surface on the x axis.
				if (this.x + this.WIDTH >= surface.getX() && this.x <= surface.getX() + surface.getWidth()) {
					// the character is standing
					onTheGround = true;
					return 0;
				}
			}
		}
		// not jumping and not standing on a surface
		onTheGround = false;
		if (this instanceof Flying)
			return 0;
		return WorldConstants.PHYSICS.FALLING_SPEED;
	}

	/**
	 * This method determines if the instance of the {@link MoveableCharacter} is
	 * touching the {@link MoveableCharacter} given as an argument
	 * 
	 * @param otherCharacter
	 *            - the {@link MoveableCharacter} to check if touching the instance
	 *            of the {@link MoveableCharacter}
	 * @return true if the two {@link MoveableCharacter}s are touching. false if
	 *         not.
	 */
	public boolean isTouching(MoveableCharacter otherCharacter) {
		if (x + WIDTH >= otherCharacter.x && x <= otherCharacter.x + otherCharacter.WIDTH)
			if (y + HEIGHT >= otherCharacter.y && y <= otherCharacter.getY() + otherCharacter.HEIGHT)
				return true;
		return false;
	}

	public abstract void draw(Graphics g);

}
