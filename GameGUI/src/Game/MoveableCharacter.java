package Game;

import java.awt.Graphics;

import javax.swing.JComponent;

import Game.DirectionsTuple.DirectionX;
import Game.DirectionsTuple.DirectionY;

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

	public MoveableCharacter(int x, int y, int width, int height, int speedX, int speedY) {
		this.x = x;
		this.y = y;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.speedX = speedX;
		this.speedY = speedY;
	}

	public void setDirectionX(DirectionX directionX) {
		this.currentDirections.directionX = directionX;
	}

	public DirectionX getDirectionX() {
		return currentDirections.directionX;
	}

	public void setDirectionY(DirectionY directionY) {
		this.currentDirections.directionY = directionY;
	}

	public DirectionY getDirectionY() {
		return currentDirections.directionY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public void setX(int x) {
		this.x = x;
	}

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

	public void moveOneStep() {

		System.out.println(currentDirections.directionY);

		switch (currentDirections.directionX) {
		case MOVE_RIGHT:
			x = x + speedX;
			break;
		case MOVE_LEFT:
			x = x - speedX;
			break;
		default:
			break;
		}

		switch (currentDirections.directionY) {
		case DOWN:
			y = y + WorldConstants.PHYSICS.FALLING_SPEED;
			break;
		case UP:
			y = y - speedY;
			break;
		default:
			break;
		}

		repaint();
	}

	public void moveToLocation(int newX, int newY) {
		// handle x directions
		if (newX > x)
			currentDirections.directionX = DirectionX.LOOK_RIGHT;
		else if (newX < x)
			currentDirections.directionX = DirectionX.LOOK_LEFT;
		// handle y directions
		
		this.x = newX;
		this.y = newY;
		repaint();
	}

	public void TryToJump() {
		if (isJumping) {
			System.out.println("can't jump. the character is already in the middle of a jump");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				isJumping = true;
				currentDirections.directionY = DirectionY.UP;
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

	public void fall(Surface[] surfaces) {
		// the character is in a middle of a jump.
		if (isJumping)
			return;
		for (Surface surface : surfaces) {

			// the character and the surface are on the same y axis scale.
			if (this.y + this.HEIGHT >= surface.getY()) {
				// the character is above the surface on the x axis.
				if (this.x + this.WIDTH >= surface.getX() && this.x <= surface.getX() + surface.getWidth()) {
					// move the character towards the surface
					currentDirections.directionY = DirectionY.STILL;
					return;
				}
			}
		}
		System.out.println("falling");
		// not jumping or standing on a surface
		currentDirections.directionY = DirectionY.DOWN;
	}

	public abstract void draw(Graphics g);

}
