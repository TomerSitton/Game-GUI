package Game;

public abstract class WorldConstants {

	public static interface Frame {
		public static final int GROUND_LEVEL = 1100;
		public static final int X = 10;
		public static final int Y = 10;
		public static final int WIDTH = 1300;
		public static final int HEIGHT = 700;
	}

	public static interface GROUND {
		public static final int X = 100;
		public static final int Y = 500;
		public static final int WIDTH = 1300;
		public static final int HEIGHT = 100;
	}

	public static interface Player1 {
		public static final int X = 500;
		public static final int Y = 50;
	}

	public static interface PHYSICS {
		public static final int FALLING_SPEED = 10;
	}

	public static interface HEARTS {
		public static final int DISTANCE_INDIVIDUALS = 50;
		public static final int DISTANCE_GROUPS = 200;

		public static final int X = 10;
		public static final int Y = 20;
	}

}
