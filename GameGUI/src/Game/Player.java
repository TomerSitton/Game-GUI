package Game;

import java.awt.Graphics;
import java.util.ArrayList;

public class Player extends Sprite2 {
	public static final String URL = "img/sprite-boy-running.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 8;
	public static final int WIDTH = 110;
	public static final int HEIGHT = 150;
	public static final int SPEED_X = 15;
	public static final int SPEED_Y = 10;
//	private ArrayList<FireAttack> attacks = new ArrayList<>();

	public Player(int x, int y) {
		super(x, y, URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
	}

//	public void attack() {
//		// if (!attacks.isEmpty() && (System.currentTimeMillis()
//		// - attacks.get(attacks.size() - 1).getCreateTime() <
//		// FireAttack.WAIT_TIME))
//		// return;
//		FireAttack attack;
//		attack = new FireAttack(getX() + getWidth() / 2, getY() + getHeight() / 2, this);
//		if (getDirectionX() > 0)
//			attack.setDirectionX(MOVE_TO_THE_RIGHT);
//		else
//			attack.setDirectionX(MOVE_TO_THE_LEFT);
//		attacks.add(attack);
//	}

	@Override
	public void oneCycle(Surface [] surfaces) {
		super.oneCycle(surfaces);
//		attacks.forEach((at) -> at.move(at.getDirectionX()));
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
//		if (!attacks.isEmpty()) {
//			attacks.forEach((atk) -> atk.draw(g));
//		}
	}
//
//	public ArrayList<FireAttack> getAttacks() {
//		return attacks;
//	}


//	public class FireAttack extends Sprite2 implements Runnable {
//		public static final String URL = "img/fireAttack.png";
//		public static final int ROWS = 2;
//		public static final int COLUMNS = 6;
//		public static final int WIDTH = 50;
//		public static final int HEIGHT = 50;
//		public static final int SPEED_X = 15;
//		public static final int SPEED_Y = 0;
//		private Player player;
//		public static final int WAIT_TIME = 500;
//		private long createTime = 0;
//
//		public FireAttack(int x, int y, Player player) {
//			super(x, y, URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
//			createTime = System.currentTimeMillis();
//			this.player = player;
//			this.player.getParent().add(this);
//			Thread t = new Thread(this);
//			t.start();
//		}
//
//		public boolean hit(Sprite2... sprite) {
//			for (int i = 0; i < sprite.length; i++) {
//				if (this.getX() + this.getWidth() >= sprite[i].getX()
//						&& this.getX() <= sprite[i].getX() + sprite[i].getWidth())
//					if (this.getY() + this.getHeight() >= sprite[i].getY()
//							&& this.getY() <= sprite[i].getY() + sprite[i].getHeight())
//						return true;
//			}
//			return false;
//		}
//
//		@Override
//		public void run() {
//			while (getParent() != null) {
//				// checks if the shot left the frame
//				if (getX() > getParent().getWidth() || getX() < 0) {
//					this.removeSprite();
//				}
//				// checks if hit an enemy
//				for (Sprite2 sprite : Sprite2.getExistingSprites()) {
//					if (sprite instanceof BadAutoOperatedSprite) {
//						if (hit(sprite)) {
//							sprite.removeSprite();
//							this.removeSprite();
//							break;
//						}
//					}
//				}
//			}
//		}
//
//		@Override
//		public void removeSprite() {
//			super.removeSprite();
//			attacks.remove(this);
//			player.getParent().remove(this);
//		}
//
//		public long getCreateTime() {
//			return createTime;
//		}
//	}
}
