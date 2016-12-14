package Player;

import Effects.Laser;
import Effects.Projectile;

import java.awt.*;
import java.util.ArrayList;

public class Player {
	double health = 100;
	private double xspeed = 2, yspeed = 2;
	private ArrayList<Projectile> proj = new ArrayList<>();

	Rectangle body;

	public Player() {
		body = new Rectangle(100, 100, 50, 100);
	}

	public void moveX(int dir) {
		body.x += xspeed * dir;
	}

	public void moveY(int dir) {
		body.y += yspeed * dir;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.green);
		g.fill(body);
		for (Projectile p : proj) {
			g.setColor(p.color);
			g.fill(p);
		}
	}

	public void fire(Direction d) {
		proj.add(new Laser(d, (int) body.getX(), (int) body.getY()));
	}

	public void stop() {

	}

	public boolean intersects(Rectangle r) {
		return body.intersects(r);
	}

	public double getHealth() {
		return health;
	}

	public ArrayList<Projectile> getProj() {
		return proj;
	}

	public Rectangle getBody() {
		return body;
	}
}
