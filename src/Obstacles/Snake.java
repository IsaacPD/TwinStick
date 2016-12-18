package Obstacles;

import Environment.Level;
import Run.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Snake extends Enemy {

	public Snake() {
		looks = new BufferedImage[2];
		health = 20;

		try {
			looks[0] = ImageIO.read(new File("snake17.png"));
			looks[1] = ImageIO.read(new File("snake117.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		imageHeight = looks[0].getHeight();
		imageWidth = looks[0].getWidth();

		ratioWidth = Game.basisWidth / (imageWidth * 3);
		ratioHeight = Game.basisHeight / (imageHeight * 3);

		sizeX = size * (int) (Game.width / ratioWidth);
		sizeY = size * (int) (Game.height / ratioHeight);

		body = new Rectangle(300, 300, imageWidth * (sizeX / imageWidth), imageHeight * (sizeY / imageHeight));
		hitBox = new Border(body);

		idle.start();
	}

	public void script() {

	}

	@Override
	public void draw(Graphics2D g) {
		float[] transform = {sizeX / imageWidth, 0, 0, sizeY / imageHeight};

		g.drawImage(looks[frame], new AffineTransformOp(new AffineTransform(transform),
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR), body.x, body.y);
		g.draw(body);
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
		return hitBox.crossesBorder(new Rectangle2D.Double(x, y, w, h)) != null;
	}
}
