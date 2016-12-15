package Player;

import Effects.Laser;
import Effects.Projectile;

import java.awt.*;
import java.util.ArrayList;

public class Player {
	float health = 100;
	private Point2D.Float velocity = new Point2D.Float(0, 0);
	private ArrayList<Projectile> proj = new ArrayList<>();

	Rectangle body;

	public Player() {
		body = new Rectangle2D.Float(100, 100, 50, 50);
	}

	public void moveX(int dir) {
		if(velocity.getX() < 5)
			velocity.translate(xspeed * dir, 0);
	}

	public void moveY(int dir) {
		if(velocity.getY() < 5)
			velocity.translate(0, yspeed * dir);
	}
	
	public void update(){
		body.x += velocity.getX();
		body.y += velocity.getY();
		velocity.setLocation(velocity.getX()*.95, velocity.getY()*.95);
		for(Projectile p: proj)
			p.project();
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.green);
		g.fill(body);
		for (Projectile p : proj) {
			g.setColor(p.color);
			g.fill(p);
		}
	}

	public void fire(int x, int y) {
		proj.add(new Laser(x, y, (int) body.getX(), (int) body.getY()));
	}
	
	public void fire(Point2D.Float p){
		proj.add(new Laser(p, body.getX(), body,getY()));
	}

	public boolean intersects(Rectangle2D r) {
		return body.intersects(r);
	}

	public float getHealth() {
		return health;
	}

	public ArrayList<Projectile> getProj() {
		return proj;
	}

	public Rectangle2D.Float getBody() {
		return body;
	}
}
