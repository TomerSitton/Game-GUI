package Game;

import java.awt.Point;

public class Heart extends Sprite2 implements Runnable {

	public static final String URL = "img/heart2.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 1;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	private final Player owner;
	private int index;

	public Heart(Player owner, int index) {
		super(determinePosition(owner, index), URL, ROWS, COLUMNS, WIDTH, HEIGHT, 0, 0);
		this.index = index;
		this.owner = owner;
		System.out.println("heart index - " + index);
		new Thread(this).start();
	}

	public static Point determinePosition(Player owner, int index) {
		return new Point(WorldConstants.HEARTS.X + (owner.getIndex() - 1) * WorldConstants.HEARTS.DISTANCE_GROUPS
				+ index * WorldConstants.HEARTS.DISTANCE_INDIVIDUALS, WorldConstants.HEARTS.Y);
	}

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

	@Override
	public void removeSprite() {
		super.removeSprite();
		this.costumeConst = 1;
	}

}
