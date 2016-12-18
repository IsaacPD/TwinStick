package Effects;


import Run.Game;

import java.awt.geom.Point2D;

public class Laser extends Projectile {
	private final int WIDTH = 10, HEIGHT = 30, SPEED = 60;

	public Laser(double vx, double vy, double x, double y) {
		super(vx, vy, x, y);

		ratioWidth = 1024 / WIDTH;
		ratioHeight = 576 / HEIGHT;

		this.width = Game.width / ratioWidth;
		this.height = Game.height / ratioHeight;

		speed = distance / SPEED;
	}

	public Laser(Point2D.Double p, double x, double y) {
		super(p, x, y);

		ratioWidth = 1024 / WIDTH;
		ratioHeight = 576 / HEIGHT;

		this.width = Game.width / ratioWidth;
		this.height = Game.height / ratioHeight;

		speed = distance / SPEED;
	}
}
