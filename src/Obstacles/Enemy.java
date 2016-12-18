package Obstacles;

import Environment.Level;
import Run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

//TODO border on enemy may be redundant, test and find out
public abstract class Enemy extends Rectangle2D.Double {
	final int size = 1;
	int sizeX;
	int sizeY;
	int frame;
	int imageHeight;
	int imageWidth;
	double ratioWidth, ratioHeight;
	Rectangle body;
	Border hitBox;
	BufferedImage[] looks;
	final Timer idle = new Timer(100, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame = (frame + 1) % looks.length;
		}
	});
	double health;

	public abstract boolean hitPlayer();

	public abstract boolean gotHit();

	public Line2D.Double crosses() {
		return hitBox.crossesBorder(Level.getP().getBody());
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public abstract void draw(Graphics2D g2);

	void drawHealth(Graphics2D g) {
		g.setColor(Color.red);
		g.translate(body.x, body.y);
		Rectangle2D.Double bar = new Rectangle2D.Double(3, -10, health, 5);
		g.fill(bar);
		g.translate(-body.x, -body.y);
	}

	public void resize() {
		sizeX = size * (int) (Game.width / ratioWidth);
		sizeY = size * (int) (Game.height / ratioHeight);

		body.width = imageWidth * (sizeX / imageWidth);
		body.height = imageHeight * (sizeY / imageHeight);
	}

}
