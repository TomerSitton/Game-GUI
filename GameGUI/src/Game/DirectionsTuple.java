package Game;

public class DirectionsTuple {
	enum DirectionX {
		LOOK_RIGHT, LOOK_LEFT;
	}

	DirectionX directionX = DirectionX.LOOK_RIGHT;

	public DirectionX getDirectionX() {
		return directionX;
	}
}