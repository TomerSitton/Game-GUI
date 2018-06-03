package Game;

/**
 * This interface contains a variety of constants used in the game
 * 
 * @author Sitton
 *
 */
public interface WorldConstants {

	/**
	 * This interface contains constants regarding the frame of the game
	 * 
	 * @author Sitton
	 *
	 */
	public static interface Frame {

		/**
		 * the x coordinate of the frame
		 */
		public static final int X = 10;

		/**
		 * the x coordinate of the frame
		 */
		public static final int Y = 10;

		/**
		 * the width of the frame
		 */
		public static final int WIDTH = 1300;

		/**
		 * the height of the frame
		 */
		public static final int HEIGHT = 700;
	}

	/**
	 * This interface contains constants regarding the ground
	 * 
	 * @author Sitton
	 *
	 */
	public static interface GROUND {

		/**
		 * the x coordinate of the ground
		 */
		public static final int X = 100;

		/**
		 * the y coordinate of the ground
		 */
		public static final int Y = 500;

		/**
		 * the width of the ground
		 */
		public static final int WIDTH = 1300;

		/**
		 * the height of the ground
		 */
		public static final int HEIGHT = 100;
	}

	/**
	 * This interface contains constants regarding the physics of the game
	 * 
	 * @author Sitton
	 *
	 */
	public static interface PHYSICS {
		/**
		 * the speed in which non-{@link Flying flying} characters fall when not
		 * standing on a {@link Surface}
		 */
		public static final int FALLING_SPEED = 10;
	}

	/**
	 * This interface contains constants regarding the hearts of the players
	 * 
	 * @author Sitton
	 *
	 */
	public static interface HEARTS {
		/**
		 * the distance between hearts of the same group
		 */
		public static final int DISTANCE_INDIVIDUALS = 50;
		/**
		 * the distance between groups of hears
		 */
		public static final int DISTANCE_GROUPS = 200;

		/**
		 * the indexes of the hearts
		 */
		public static final int LEFT_HEART_INDEX = 0;
		public static final int MIDDLE_HEART_INDEX = 1;
		public static final int RIGHT_HEART_INDEX = 2;

		/**
		 * the x coordinate of the first heart
		 */
		public static final int X = 10;
		/**
		 * the y coordinate of the first heart
		 */
		public static final int Y = 20;

	}

}
