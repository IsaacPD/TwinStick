package Obstacles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;

//TODO add basic enemy outline as well as a test enemy
public abstract class Enemy extends Rectangle2D.Double {
	Timer invinsible = new Timer(1000, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			invinsible.stop();
		}
	});
	Rectangle body;

	double health;

	public abstract boolean hitPlayer();

	public abstract boolean gotHit();

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public abstract void draw(Graphics2D g2);

	public void drawHealth(Graphics2D g) {
		g.setColor(Color.red);
		g.translate(body.x, body.y);
		Rectangle2D.Double bar = new Rectangle2D.Double(3, -10, health, 5);
		g.fill(bar);
		g.translate(-body.x, -body.y);
	}
}
