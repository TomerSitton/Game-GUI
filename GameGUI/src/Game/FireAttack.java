package Game;

/**
 * This class represents a fire attack which a {@link Player} can create.</br>
 * </br>
 * 
 * The {@link FireAttack} will move until it hits an opposing {@link Player} or
 * reach the end of the screen.
 * 
 * @author Sitton
 *
 * @see Player
 * @see WorldConstants.Frame
 */
public class FireAttack extends Sprite implements Flying {

	/////////////////// fields /////////////////

	// serial version
	private static final long serialVersionUID = 1L;

	/**
	 * the {@link FireAttack} constants
	 */
	public static final String URL = "img/fireAttack.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 6;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final int SPEED_X = 35;
	public static final int SPEED_Y = 0;

	/**
	 * The {@link Player} this {@link FireAttack} belongs to
	 */
	private Player shootingPlayer;

	/**
	 * a boolean determining the direction of the movement of the {@link FireAttack}
	 * (according to the {@link Player#isLookingRight()} method)
	 */
	private boolean isMovingRight;

	/////////////////// constructors /////////////////

	/**
	 * This constructs a new {@link FireAttack}
	 * 
	 * @param player
	 *            - The {@link Player} this {@link FireAttack} belongs to
	 */
	public FireAttack(Player player) {
		super(player.getX(), player.getY(), URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
		this.shootingPlayer = player;
		isMovingRight = shootingPlayer.isLookingRight();
	}

	/////////////////// other methods /////////////////

	/**
	 * This method handles the movement of the {@link FireAttack}.</br>
	 * </br>
	 * 
	 * It moves the {@link FireAttack} to the correct position according to its
	 * {@link MoveableCharacter#speedX speedX} and its {@link #isMovingRight}
	 * variables.</br>
	 * </br>
	 * 
	 * The method will remove the {@link FireAttack} if it hit an opposing player or
	 * if it reached the end of the screen.
	 * 
	 * @see #removeSprite()
	 */
	public void move() {
		if (this.x < WorldConstants.Frame.WIDTH && this.x + WIDTH > WorldConstants.Frame.X) {
			// move
			if (isMovingRight)
				x = x + speedX;
			else
				x = x - speedX;
			// check hit
			for (Sprite sprite : this.spritesTouching()) {
				if (sprite instanceof Player && sprite != this.shootingPlayer) {
					((Player) sprite).looseHealth();
					removeSprite();
				}
			}

		} else
			removeSprite();
	}

	/**
	 * {@inheritDoc}</br>
	 * </br>
	 * 
	 * Also, this method will remove the {@link FireAttack} from the
	 * {@link Player#attacks} field.
	 * 
	 */
	@Override
	public void removeSprite() {
		super.removeSprite();
		shootingPlayer.getAttacks().remove(this);
	}

}
