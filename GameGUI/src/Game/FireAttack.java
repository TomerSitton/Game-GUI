package Game;

public class FireAttack extends Sprite2 implements Runnable, Flying {

	public static final String URL = "img/fireAttack.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 6;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final int SPEED_X = 15;
	public static final int SPEED_Y = 0;
	private Player myPlayer;
	private boolean isLookingRight;

	public FireAttack(Player player) {
		super(player.getX(), player.getY(), URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
		this.myPlayer = player;
		isLookingRight = myPlayer.isLookingRight();
		new Thread(this).start();

	}

	@Override
	public void run() {
		while (this.x < WorldConstants.Frame.WIDTH && this.x + WIDTH > WorldConstants.Frame.X) {
			for (Sprite2 sprite : this.spritesTouching()) {
				if (sprite instanceof Player && sprite != this.myPlayer) {
					((Player) sprite).looseHealth();
					System.out.println(((Player) sprite).getHealth());
				}
			}
			if (isLookingRight)
				x = x + speedX;
			else
				x = x - speedX;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.removeSprite();
	}
}
