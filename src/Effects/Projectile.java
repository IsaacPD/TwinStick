package Effects;

import Player.Direction;

import java.awt.*;

public abstract class Projectile extends Rectangle {
	int speed;
	public Color color = Color.orange;
	Direction dir;

	public Projectile(Direction d) {
		dir = d;
	}

	public void project() {
		if (dir.equals(Direction.RIGHT) || dir.equals(Direction.LEFT))
			this.x += (dir.equals(Direction.RIGHT)) ? speed : -speed;
		else this.y += (dir.equals(Direction.DOWN)) ? speed : -speed;
	}
}
