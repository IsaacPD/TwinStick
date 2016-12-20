package Obstacles;

import Environment.Level;
import Run.Asset;
import Run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

//TODO border on enemy may be redundant, test and find out
public abstract class Enemy extends Asset {
	public Timer script;
	Border hitBox;
	double health, damage;

	public Enemy() {
		super("enemy");
	}

	public Enemy(String name, int size) {
		super(name, size);
	}

	public Enemy(String name) {
		super(name);
	}

	public abstract int hitPlayer();

	public boolean gotHit() {
		if (Level.getP().hit(this)) {
			health -= damage;
			System.out.println(getClass() + ": " + health);
			return true;
		}
		return false;
	}

	public boolean intersects(double x, double y, double w, double h) {
		return hitBox.crossesBorder(new Rectangle2D.Double(x, y, w, h)) != null;
	}

	public double getHealth() {
		return health;
	}

	public Line2D.Double crosses() {
		return hitBox.crossesBorder(Level.getP().getBody());
	}

	void drawHealth(Graphics2D g) {
		g.setColor(Color.red);
		g.translate(body.x, body.y);
		Rectangle2D.Double bar = new Rectangle2D.Double(3, -10, health, 5);
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
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		drawHealth(g);
	}
}
