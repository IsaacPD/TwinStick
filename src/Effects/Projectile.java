package Effects;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Projectile extends Rectangle2D.Double {
	public int distance = 300;
	int speed;
	private Color color = Color.orange;
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

		if (dir.x != 0 && dir.y != 0)
			g.rotate(angle);
		else g.rotate(-angle);

		g.translate(-start.x, -start.y);
	}

	public Point2D.Double getDir() {
		return dir;
	}

	public Point2D.Double getStart() {
		return start;
	}
}
