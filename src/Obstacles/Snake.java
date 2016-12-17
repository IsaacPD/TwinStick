package Obstacles;

import Environment.Level;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Snake extends Enemy {

	BufferedImage look;

	public Snake() {
		size = 100;
		body = new Rectangle(300, 300, size, size);
		health = 20;
		try {
			look = ImageIO.read(new File("devil.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		hitBox = new Border(body);
	}

	public void script() {

	}

	@Override
	public void draw(Graphics2D g) {
		float[] transform = {size / 30, 0, 0, size / 30, 0, 0};

		g.drawImage(look, new AffineTransformOp(new AffineTransform(transform),
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR), body.x, body.y);
		g.fill(body);
		hitBox.draw(g);
		drawHealth(g);
	}

	@Override
	public boolean hitPlayer() {
		return crosses() != null;
	}

	@Override
	public boolean gotHit() {
		if (Level.getP().hit(this)) {
			health -= 5;
			System.out.println("Snake: " + health);
			return true;
		}
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return body.intersects(x, y, w, h);
	}
}
