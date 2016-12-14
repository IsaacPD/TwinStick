package Player;

public enum Direction {
	UP(0), DOWN(2), LEFT(3), RIGHT(1);

	int dir;

	Direction(int i) {
		dir = i;
	}

	public boolean isOpposite(Direction d) {
		boolean result = false;

		switch (this) {
			case DOWN:
				result = d == Direction.UP;
				break;

			case LEFT:
				result = d == Direction.RIGHT;
				break;

			case RIGHT:
				result = d == Direction.LEFT;
				break;

			case UP:
				result = d == Direction.DOWN;
				break;
		}

		return result;
	}
}