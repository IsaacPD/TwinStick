package Player;

import Effects.Laser;
import Effects.Projectile;
import Obstacles.Enemy;
import Run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

//TODO adjust player and projectile speed to something better
public class Player {
	public double health = 100;
	private Timer invinsible = new Timer(1000, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			invinsible.stop();
		}
	});
	private double friction = .80, maxSpeed = 15, speed = 3;
	private Point2D.Double velocity = new Point2D.Double(0, 0);
	private ArrayList<Projectile> proj = new ArrayList<>();

	private int playerSize = 90, frame = 0;
	private Rectangle2D.Double body;
	private ArrayList<BufferedImage> looks = new ArrayList<>();
	private Timer idle = new Timer(500, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame = (frame + 1) % looks.size();
		}
	});

	public Player() {
		body = new Rectangle2D.Double(100, 100, playerSize, playerSize);
		try {
			//look.add(read(new URL("https://upload.wikimedia.org/wikipedia/en/9/99/MarioSMBW.png"));
			looks.add(read(new File("originalcharacter.png")));
			looks.add(read(new File("girl.png")));
			looks.add(read(new File("ghost1.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		idle.start();
	}

	public void resetPos() {
		body.x = 100;
		body.y = 100;
	}

	public void moveX(int dir) {
		if (Math.abs(velocity.getX()) < maxSpeed)
			velocity.x += dir * speed;
		else if (velocity.getX() > maxSpeed)
			velocity.x = maxSpeed;
		else velocity.x = -maxSpeed;
	}

	public void moveY(int dir) {
		if (Math.abs(velocity.getY()) < maxSpeed)
			velocity.y += dir * speed;
		else if (velocity.getY() > maxSpeed)
			velocity.y = maxSpeed;
		else velocity.y = -maxSpeed;
	}

	public void update() {
		body.x += velocity.getX();
		body.y += velocity.getY();

		for (Projectile p : proj)
			p.project();
		removeProj();
	}

	public void slow() {
		velocity.setLocation(velocity.getX() * friction, velocity.getY() * friction);
		if (Math.abs(velocity.x) < .1)
			velocity.x = 0;
		if (Math.abs(velocity.y) < .1)
			velocity.y = 0;
	}

	public void draw(Graphics2D g) {
		float[] transform = {playerSize / 30, 0, 0, playerSize / 30, 0, 0};

		g.drawImage(looks.get(frame), new AffineTransformOp(new AffineTransform(transform),
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR), (int) body.x, (int) body.y);
		g.draw(body);
		drawHealth(g);
		for (Projectile p : proj) {
			p.draw(g);
		}
	}

	public void fire(int x, int y) {
		proj.add(new Laser(x, y, body.getCenterX(), body.getCenterY()));
	}

	public void fire(Point2D.Double p) {
		proj.add(new Laser(p, body.getCenterX(), body.getCenterY()));
	}

	//TODO push player outside of border entirely rather than a distance
	public void bounce(Line2D.Double l) {
		System.out.println(velocity.x + ", " + velocity.y);
		double angle = Math.atan((l.getY2() - l.getY1()) / (l.getX2() - l.getX1()));

		velocity.y *= -Math.sin(angle);
		velocity.x *= -Math.cos(angle);
		System.out.println(velocity.x + ", " + velocity.y);
	}

	public boolean gotHit(Enemy e) {
		if (e.hitPlayer() && !invinsible.isRunning()) {
			health -= 10;
			System.out.println("Player: " + health);
			invinsible.start();
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
		Rectangle2D.Double bar = new Rectangle2D.Double(20, -10, health, 5);
		g.fill(bar);
		g.translate(-body.x, -body.y);
	}

	public void resize() {
		playerSize = (int) (Game.height / 7.11111111111111);

		body = new Rectangle2D.Double(body.x, body.y, playerSize, playerSize);
	}
}
