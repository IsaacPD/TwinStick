package Effects;

import Player.Direction;

public class Laser extends Projectile {
	final int WIDTH = 10, HEIGHT = 30;

	public Laser(Direction d, int x, int y) {
		super(d);
		this.x = x;
		this.y = y;
		if (dir.equals(Direction.RIGHT) || dir.equals(Direction.LEFT)) {
			this.width = HEIGHT;
			this.height = WIDTH;
		} else {
			this.width = WIDTH;
			this.height = HEIGHT;
		}
		speed = 3;
	}
}
