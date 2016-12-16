package Player;

import Effects.Laser;
import Effects.Projectile;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

//TODO add collision to enemies or vice versa (add collision to player from enemies)
public class Player {
	double health = 100;
	private double friction = .90, maxSpeed = 10;
	private Point2D.Double velocity = new Point2D.Double(0, 0);
	private ArrayList<Projectile> proj = new ArrayList<>();

	private Rectangle2D.Double body;

	public Player() {
		body = new Rectangle2D.Double(100, 100, 50, 50);
	}

	public void moveX(int dir) {
		if (velocity.getX() < maxSpeed)
			velocity.x += dir;
		else
			velocity.x = maxSpeed;
	}

	public void moveY(int dir) {
		if (velocity.getY() < maxSpeed)
			velocity.y += dir;
		else
			velocity.y = maxSpeed;
	}

	public void update() {
		body.x += velocity.getX();
		body.y += velocity.getY();
		velocity.setLocation(velocity.getX() * friction, velocity.getY() * .95);
		for (Projectile p : proj)
			p.project();
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.green);
		g.fill(body);
		for (Projectile p : proj) {
			p.draw(g);
		}
	}

	public void fire(int x, int y) {
		proj.add(new Laser(x, y, body.x, body.y));
	}

	public void fire(Point2D.Double p) {
		proj.add(new Laser(p, body.x, body.y));
	}

	public boolean intersects(Rectangle2D r) {
		return body.intersects(r);
	}

	public double getHealth() {
		return health;
	}

	public ArrayList<Projectile> getProj() {
		return proj;
	}

	public Rectangle2D.Double getBody() {
		return body;
	}
}
