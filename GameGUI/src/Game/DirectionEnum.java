package Game;

public class DirectionEnum {
	enum DirectionX {
		LOOK_RIGHT, LOOK_LEFT;
	}

	DirectionX directionX = DirectionX.LOOK_RIGHT;

	public DirectionX getDirectionX() {
		return directionX;
	}
}