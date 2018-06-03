package Game;

public class FireAttack extends Sprite implements Flying {

	public static final String URL = "img/fireAttack.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 6;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final int SPEED_X = 35;
	public static final int SPEED_Y = 0;
	private Player shootingPlayer;
	private boolean isLookingRight;

	public FireAttack(Player player) {
		super(player.getX(), player.getY(), URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
		this.shootingPlayer = player;
		isLookingRight = shootingPlayer.isLookingRight();

	}

	public void move() {
		if (this.x < WorldConstants.Frame.WIDTH && this.x + WIDTH > WorldConstants.Frame.X) {
			// move
			if (isLookingRight)
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

	@Override
	public void removeSprite() {
		super.removeSprite();
		shootingPlayer.getAttacks().remove(this);
	}

}
