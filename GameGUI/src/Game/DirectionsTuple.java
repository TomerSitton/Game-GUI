package Game;

public class DirectionsTuple {
	enum DirectionX {
		MOVE_RIGHT, MOVE_LEFT, LOOK_RIGHT, LOOK_LEFT;
	}

	enum DirectionY {
		UP, DOWN, STILL;
	}

	DirectionX directionX = DirectionX.LOOK_RIGHT;
	DirectionY directionY = DirectionY.STILL;

	public DirectionX getDirectionX() {
		return directionX;
	}

	public DirectionY getDirectionY() {
		return directionY;
	}
}