package Game;

public class DirectionsTuple {
	enum DirectionX {
		MOVE_RIGHT, MOVE_LEFT, LOOK_RIGHT, LOOK_LEFT;
	}

	enum DirectionY {
		UP, DOWN, LOOK_UP, LOOK_DOWN;
	}

	DirectionX directionX = DirectionX.LOOK_RIGHT;
	DirectionY directionY = DirectionY.LOOK_UP;

	public DirectionX getDirectionX() {
		return directionX;
	}

	public DirectionY getDirectionY() {
		return directionY;
	}
}