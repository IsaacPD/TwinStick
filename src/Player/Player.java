package Player;

import Effects.Projectile;
import Obstacles.Enemy;
import Run.Asset;
import Run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

//TODO adjust player speed to something better
public class Player extends Asset {
	private final Timer invincible = new Timer(1000, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			invincible.stop();
		}
	});
	private final Point2D.Double velocity = new Point2D.Double(0, 0);
	private final ArrayList<Projectile> proj = new ArrayList<>();
	public double health = 100;
	private double maxSpeed = 10, speed = 2;

	public Player() {
		super("originalcharacter", 2);

		speed = Game.width / 512;
		maxSpeed = Game.width / 102.4;

		try {
			//look.add(read(new URL("https://upload.wikimedia.org/wikipedia/en/9/99/MarioSMBW.png"));
			looks.add(read(new File("sprites\\girl.png")));
			looks.add(read(new File("sprites\\ghost1.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		body = new Rectangle2D.Double(100, 100, imageWidth * (width / imageWidth), imageHeight * (height / imageHeight));

		idle.setDelay(125);
		idle.start();
	}

	public void resetPos(int dir) {
		int offSet = (int) body.height / 2;
		if (dir == 2)
			body.y = Game.height - offSet;
		else if (dir == 8)
			body.y = offSet;
		else if (dir == 4)
			body.x = offSet;
		else if (dir == 6)
			body.x = Game.width - offSet;
	}

	public void moveX(int dir) {
		if (Math.abs(velocity.getX()) < maxSpeed)
			velocity.x += dir * speed;
		else if (velocity.getX() >= maxSpeed)
			velocity.x = maxSpeed;
		else if (velocity.getX() <= -maxSpeed)
			velocity.x = -maxSpeed;
	}

	public void moveY(int dir) {
		if (Math.abs(velocity.getY()) < maxSpeed)
			velocity.y += dir * speed;
		else if (velocity.getY() >= maxSpeed)
			velocity.y = maxSpeed;
		else if (velocity.getY() <= -maxSpeed)
			velocity.y = -maxSpeed;
	}

	public Point2D.Double update() {
		body.x += velocity.x;
		body.y += velocity.y;

		for (Projectile p : proj)
			p.project();
		removeProj();

		return new Point2D.Double(velocity.x, velocity.y);
	}

	public void slow() {
		double friction = .80;
		velocity.setLocation(velocity.getX() * friction, velocity.getY() * friction);
		if (Math.abs(velocity.x) < .1)
			velocity.x = 0;
		if (Math.abs(velocity.y) < .1)
			velocity.y = 0;
	}

	public void draw(Graphics2D g) {
		super.draw(g);

		drawHealth(g);
		for (Projectile p : proj) {
			p.draw(g);
		}
	}

	public void fire(int x, int y) {
		proj.add(new Projectile.Laser(x, y, body.getCenterX(), body.getCenterY()));
	}

	public void fire(Point2D.Double p) {
		proj.add(new Projectile.Laser(p, body.getCenterX(), body.getCenterY()));
	}

	public void bounce(Point2D.Double point, Line2D.Double l) {
		double angle = Math.PI / 2 - Math.atan((l.getY2() - l.getY1()) / (l.getX2() - l.getX1()));

		velocity.y *= -Math.sin(angle);
		velocity.x *= -Math.cos(angle);
		body.x -= point.x;
		body.y -= point.y;
	}

	public boolean gotHit(Enemy e) {
		if (!invincible.isRunning()) {
			health -= e.hitPlayer();
			System.out.println("Player: " + health);
			invincible.start();
			return true;
		}
		return false;
	}

	public boolean hit(Enemy e) {
		for (Projectile p : proj)
			if (e.intersects(p.getStart().x, p.getStart().y, p.width, p.height)) {
				proj.remove(p);
				return true;
			}
		return false;
	}

	public Rectangle2D.Double getBody() {
		return body;
	}

	public void clear() {
		proj.clear();
	}

	private void removeProj() {
		for (int i = proj.size() - 1; i >= 0; i--) {
			Projectile p = proj.get(i);
			if (p.distance <= 0)
				proj.remove(p);
		}
	}

	private void drawHealth(Graphics2D g) {
		g.setColor(Color.red);
		g.translate(body.x, body.y);
		Rectangle2D.Double bar = new Rectangle2D.Double(0, -10, health, 5);
		g.fill(bar);
		g.setColor(Color.black);
		g.draw(bar);
		g.translate(-body.x, -body.y);
	}

	public void resize() {
		width = size * (int) (Game.width / ratioWidth);
		height = size * (int) (Game.height / ratioHeight);

		body.width = imageWidth * (width / imageWidth);
		body.height = imageHeight * (height / imageHeight);

		for (Projectile p : proj)
			p.resize();

		speed = Game.width / 512;
		maxSpeed = Game.width / 102.4;
	}

	public int getX() {
		return (int) body.getCenterX();
	}

	public int getY() {
		return (int) body.getCenterY();
	}
}
