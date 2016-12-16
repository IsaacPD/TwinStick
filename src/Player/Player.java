package Player;

import Effects.Laser;
import Effects.Projectile;
import Obstacles.Enemy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

//TODO add collision to enemies or vice versa (add collision to player from enemies)
public class Player {
	private Timer invinsible = new Timer(3000, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			invinsible.stop();
		}
	});
	private Timer idle = new Timer(500, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			BufferedImage temp = look[1];
			look[1] = look[0];
			look[0] = temp;
		}
	});

	double health = 100;
	private double friction = .75, maxSpeed = 15, speed = 3;
	private Point2D.Double velocity = new Point2D.Double(0, 0);
	private ArrayList<Projectile> proj = new ArrayList<>();

	private int playerSize = 150;
	private float[] transform = {playerSize / 30, 0, 0, playerSize / 30, 0, 0};
	private Rectangle2D.Double body;
	private BufferedImage[] look = new BufferedImage[10];

	public Player() {
		body = new Rectangle2D.Double(100, 100, playerSize, playerSize);
		try {
			//look = ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/en/9/99/MarioSMBW.png"));
			look[0] = read(new File("originalcharacter.png"));
			look[1] = read(new File("girl.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		idle.start();
	}

	public void moveX(int dir) {
		if (Math.abs(velocity.getX()) < maxSpeed)
			velocity.x += dir * speed;
		else if (velocity.getX() > 0)
			velocity.x = maxSpeed;
		else velocity.x = -maxSpeed;
	}

	public void moveY(int dir) {
		if (Math.abs(velocity.getY()) < maxSpeed)
			velocity.y += dir * speed;
		else if (velocity.getY() > 0)
			velocity.y = maxSpeed;
		else velocity.y = -maxSpeed;
	}

	public void update() {
		System.out.println(velocity.x + ", " + velocity.y);
		body.x += velocity.getX();
		body.y += velocity.getY();
		velocity.setLocation(velocity.getX() * friction, velocity.getY() * friction);
		if (Math.abs(velocity.x) < .1)
			velocity.x = 0;
		if (Math.abs(velocity.y) < .1)
			velocity.y = 0;
		for (Projectile p : proj)
			p.project();
		removeProj();
	}

	public void draw(Graphics2D g) {
		g.drawImage(look[0], new AffineTransformOp(new AffineTransform(transform),
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR), (int) body.x, (int) body.y);
		for (Projectile p : proj) {
			p.draw(g);
		}
	}

	public void fire(int x, int y) {
		proj.add(new Laser(x, y, body.x + playerSize / 2, body.y + playerSize / 2));
	}

	public void fire(Point2D.Double p) {
		proj.add(new Laser(p, body.x + playerSize / 2, body.y + playerSize / 2));
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
			if (e.intersects(p.getStart().x, p.getStart().y, p.width, p.height))
				return true;
		return false;
	}

	public double getHealth() {
		return health;
	}

	public Rectangle2D.Double getBody() {
		return body;
	}

	private void removeProj() {
		for (int i = proj.size() - 1; i >= 0; i--) {
			Projectile p = proj.get(i);
			if (p.distance <= 0)
				proj.remove(p);
		}
	}
}
