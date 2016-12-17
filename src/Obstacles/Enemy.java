package Obstacles;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public abstract class Enemy extends Rectangle2D.Double {
	public int size;
	public Rectangle body;
	Border hitBox;

	double health;

	public abstract boolean hitPlayer();

	public abstract boolean gotHit();

	public Line2D.Double crosses() {
		return hitBox.crossesBorder();
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
}
