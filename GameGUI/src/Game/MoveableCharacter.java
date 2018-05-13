package Game;

import java.awt.Graphics;

import javax.swing.JComponent;

import Game.DirectionsTuple.DirectionX;

public abstract class MoveableCharacter extends JComponent {
	private int x, y;
	public final int WIDTH, HEIGHT;
	private boolean isJumping;
	/*
	 * the current speeds of the character. this is not final because maybe the
	 * speeds of the characters will change during the game.
	 */
	private int speedX, speedY;

	// the directionTuple (x,y) of the instance
	protected DirectionsTuple currentDirections = new DirectionsTuple();

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
	 * set the direction of the {@link MoveableCharacter} on the x axis
	 * 
	 * @param directionX
	 *            - the new direction of the {@link MoveableCharacter}.
	 * @see DirectionsTuple.DirectionX
	 */
	public void setDirectionX(DirectionX directionX) {
		this.currentDirections.directionX = directionX;
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
	 * @return the direction of the {@link MoveableCharacter} on the x axis
	 * @see DirectionsTuple.DirectionX
	 */
	public DirectionX getDirectionX() {
		return currentDirections.directionX;
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
	 * position and updates its {@link DirectionsTuple} accordingly
	 * 
	 * @param newX
	 *            - the desired x location to the {@link MoveableCharacter} to
	 *            be at
	 * @param newY
	 *            - the desired y location to the {@link MoveableCharacter} to
	 *            be at
	 * @see DirectionsTuple
	 */
	public void moveToLocation(int newX, int newY) {
		// handle x directions
		if (newX > x)
			currentDirections.directionX = DirectionX.LOOK_RIGHT;
		else if (newX < x)
			currentDirections.directionX = DirectionX.LOOK_LEFT;

		this.x = newX;
		this.y = newY;
		repaint();
	}

	/**
	 * this method changes the Y direction of the {@link MoveableCharacter} to
	 * be UP (thus making it move up) for a short time, and then stops
	 */
	public void TryToJump() {
		if (isJumping) {
			System.out.println("can't jump. the character is already in the middle of a jump");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				isJumping = true;
				// currentDirections.directionY = DirectionY.UP;
				speedY = WorldConstants.PHYSICS.JUMPING_SPEED;
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
	public void fall(Surface[] surfaces) {
		// the character is in a middle of a jump.
		if (isJumping)
			return;
		for (Surface surface : surfaces) {

			// the character and the surface are on the same y axis scale.
			if (this.y + this.HEIGHT >= surface.getY()) {
				// the character is above the surface on the x axis.
				if (this.x + this.WIDTH >= surface.getX() && this.x <= surface.getX() + surface.getWidth()) {
					// the character is standing
					// currentDirections.directionY = DirectionY.STILL;
					speedY = 0;
					return;
				}
			}
		}
		// not jumping and not standing on a surface
		// currentDirections.directionY = DirectionY.DOWN;
		speedY = WorldConstants.PHYSICS.FALLING_SPEED;
	}

	public abstract void draw(Graphics g);

}
