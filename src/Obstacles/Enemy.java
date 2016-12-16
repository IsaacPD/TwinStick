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
}
