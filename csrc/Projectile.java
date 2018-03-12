import java.awt.*;
import java.awt.geom.Point2D;

public class Projectile extends AnimatedSprite {
	public int distance = Game.WIDTH / 4;
	int speed;
	private Point2D.Double dir, start;

	public Projectile(String path, Point2D.Double p, double x, double y) {
		super("sprites/" + path + ".png", 0, 0, 13, 13, 0, 0, 150);
		speed = 1;
		start = new Point2D.Double(x, y);
		dir = p;
		setupAnimations();
		playAnimation("move");
	}

	@Override
	protected void setupAnimations() {
		addAnimation(4, 0, 0, "move", 13, 13, new Point(0, 0));
	}

	@Override
	public void update(int elapsedTime) {
		start.x += dir.x * speed;
		start.y += dir.y * speed;
		distance -= speed;
		super.update(elapsedTime);
	}

	public void draw(Graphics2D g) {
		double angle = Math.PI / 2 - Math.atan(dir.y / dir.x);
		g.translate(start.x, start.y);

		if (dir.x != 0 && dir.y != 0)
			g.rotate(-angle);
		else g.rotate(angle);

		super.draw(g, 0, 0);

		if (dir.x != 0 && dir.y != 0)
			g.rotate(angle);
		else g.rotate(-angle);

		g.translate(-start.x, -start.y);
	}
}
