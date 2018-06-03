package Game;

/**
 * This class represents a heart. The number of hearts equals to the number of
 * health each player has left.
 * 
 * @author Sitton
 *
 */
public class Heart extends Sprite implements Runnable {

	/////////////////// fields /////////////////

	// serial version
	private static final long serialVersionUID = 1L;
	/**
	 * the heart's sprite constants
	 */
	public static final String URL = "img/heart2.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 1;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	/**
	 * the owner of the heart
	 */
	private final Player owner;
	/**
	 * the index of the heart
	 */
	private int index;

	/////////////////// constructors /////////////////

	/**
	 * This constructs a new {@link Heart} with the given arguments.
	 * 
	 * @param owner
	 *            - the owner of the heart
	 * @param index
	 *            - the index of the heart
	 */
	public Heart(Player owner, int index) {
		super(determineXPosition(owner, index), WorldConstants.HEARTS.Y, URL, ROWS, COLUMNS, WIDTH, HEIGHT, 0, 0);
		this.index = index;
		this.owner = owner;
		new Thread(this).start();
	}

	/**
	 * This method determines the x position of an heart according to its owner and
	 * its index
	 * 
	 * @param owner
	 *            - the owner of the heart
	 * @param index
	 *            -the index of the heart
	 * @return - the x position of an heart
	 */
	public static int determineXPosition(Player owner, int index) {
		return WorldConstants.HEARTS.X + (owner.getIndex() - 1) * WorldConstants.HEARTS.DISTANCE_GROUPS
				+ index * WorldConstants.HEARTS.DISTANCE_INDIVIDUALS;
	}

	/**
	 * This method runs as long as the owner is not dead.</br>
	 * It checks the owner's health and remove the correct heart if the player has
	 * lost health from the last check
	 */
	@Override
	public void run() {
		int prevHealth = owner.getHealth();
		while (owner.getHealth() >= 0) {
			int currentHealth = owner.getHealth();
			if (currentHealth < prevHealth) {
				if (index + 1 == prevHealth)
					removeSprite();
			}
			prevHealth = currentHealth;
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}</br>
	 * </br>
	 * In addition, this sets the {@link Sprite#costumeConst costume constants} of
	 * the heart to 1 in order to remove it from the screen
	 */
	@Override
	public void removeSprite() {
		super.removeSprite();
		this.costumeConst = 1;
	}

}
