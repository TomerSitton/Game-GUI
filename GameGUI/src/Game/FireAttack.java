package Game;

public class FireAttack extends Sprite2 implements Flying {

	public static final String URL = "img/fireAttack.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 6;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final int SPEED_X = 35;
	public static final int SPEED_Y = 0;
	private Player myPlayer;
	private boolean isLookingRight;

	public FireAttack(Player player) {
		super(player.getX(), player.getY(), URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
		this.myPlayer = player;
		isLookingRight = myPlayer.isLookingRight();

	}

	public void move() {
		if (this.x < WorldConstants.Frame.WIDTH && this.x + WIDTH > WorldConstants.Frame.X) {
			// move
			if (isLookingRight)
				x = x + speedX;
			else
				x = x - speedX;
			// check hit
			for (Sprite2 sprite : this.spritesTouching()) {
				if (sprite instanceof Player && sprite != this.myPlayer) {
					((Player) sprite).looseHealth();
				}
			}

		} else
			removeSprite();
	}

}
