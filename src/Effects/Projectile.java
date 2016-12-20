package Effects;

import Run.Game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Projectile extends Rectangle2D.Double {
	public int distance = (int) (Game.width / 3.41333333333333);
	double ratioWidth, ratioHeight;
	int speed;
	Color color = Color.orange;
	private Point2D.Double dir, start;

	Projectile(double x, double y, double startX, double startY) {
		dir = new Point2D.Double(x, y);
		start = new Point2D.Double(startX, startY);
	}

	Projectile(Point2D.Double p, double startX, double startY) {
		dir = p;
		start = new Point2D.Double(startX, startY);
	}

	public void project() {
		start.x += dir.getX() * speed;
		start.y += dir.getY() * speed;
		distance -= speed;
	}

	public void draw(Graphics2D g) {
		double angle = Math.PI / 2 - Math.atan(dir.y / dir.x);
		g.translate(start.x, start.y);

		if (dir.x != 0 && dir.y != 0)
			g.rotate(-angle);
		else g.rotate(angle);

		g.setColor(color);
		g.fill(this);
		g.setColor(Color.black);
		g.draw(this);

		if (dir.x != 0 && dir.y != 0)
			g.rotate(angle);
		else g.rotate(-angle);

		g.translate(-start.x, -start.y);
	}

	public void resize() {
		this.width = Game.width / ratioWidth;
		this.height = Game.height / ratioHeight;
	}

	public Point2D.Double getDir() {
		return dir;
	}

	public Point2D.Double getStart() {
		return start;
	}

	public static class Laser extends Projectile {
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

	public static class FireBall extends Projectile {
		private final int WIDTH = 15, HEIGHT = 15, SPEED = 120;

		public FireBall(double vx, double vy, double x, double y) {
			super(vx, vy, x, y);

			ratioWidth = 1024 / WIDTH;
			ratioHeight = 576 / HEIGHT;

			this.width = Game.width / ratioWidth;
			this.height = Game.height / ratioHeight;

			speed = distance / SPEED;
		}

		public FireBall(Point2D.Double p, double x, double y) {
			super(p, x, y);

			color = Color.red;

			ratioWidth = 1024 / WIDTH;
			ratioHeight = 576 / HEIGHT;

			this.width = Game.width / ratioWidth;
			this.height = Game.height / ratioHeight;

			speed = distance / SPEED;
		}
	}
}
