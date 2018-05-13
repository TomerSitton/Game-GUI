package Game;

import java.awt.Graphics;

import javax.swing.JComponent;

public abstract class MoveableCharacter extends JComponent {
	private int x, y;
	public final int WIDTH, HEIGHT;
	private boolean isJumping, onTheGround;
	private int speedX, speedY;

	/**
	 * constructs a new {@link MoveableCharacter} with the given arguments
	 * 
	 * @param x
	 *            - the starting location of the {@link MoveableCharacter} on
	 *            the x axis
	 * @param y
	 *            - the starting location of the {@link MoveableCharacter} on
	 *            the y axis
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
	 * sets the absolute speed of the Sprite on the X axis
	 * 
	 * @param speedX
	 *            The speed on the X Axis
	 */
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	/**
	 * sets the absolute speed of the Sprite on the Y axis
	 * 
	 * @param speedY
	 *            The speed on the Y Axis
	 */
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	/////////////////// getters /////////////////

	/**
	 * @return the location of the {@link MoveableCharacter} on the x axis
	 */
	public int getX() {
		return x;
	}

	/**
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
	 * @return the width of the {@link MoveableCharacter}
	 */
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * @return the height of the {@link MoveableCharacter}
	 */
	public int getHeight() {
		return HEIGHT;
	}

	/////////////////// other methods /////////////////

	/**
	 * this method moves the {@link MoveableCharacter} to the target x,y
	 * position and updates its {@link DirectionEnum} accordingly
	 * 
	 * @param newX
	 *            - the desired x location to the {@link MoveableCharacter} to
	 *            be at
	 * @param newY
	 *            - the desired y location to the {@link MoveableCharacter} to
	 *            be at
	 * @see DirectionEnum
	 */
	public void moveToLocation(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}

	/**
	 * this method changes the Y direction of the {@link MoveableCharacter} to
	 * be UP (thus making it move up) for a short time, and then stops
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
				System.out.println("end run");
			}
		}).start();

		System.out.println("end try jump");
	}

	/**
	 * this method makes the character move downwards if it is not standing on a
	 * {@link Surface} or if it is not in the middle of a jump
	 * 
	 * @param surfaces
	 *            - the surfaces in the JFrame.
	 * 
	 * @see Surface
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
		return WorldConstants.PHYSICS.FALLING_SPEED;
	}

	public boolean isTouching(MoveableCharacter otherCharacter) {
		if (x + WIDTH >= otherCharacter.x && x <= otherCharacter.x + otherCharacter.WIDTH)
			if (y + HEIGHT >= otherCharacter.y && y <= otherCharacter.getY() + otherCharacter.HEIGHT)
				return true;
		return false;
	}

	public abstract void draw(Graphics g);

}
