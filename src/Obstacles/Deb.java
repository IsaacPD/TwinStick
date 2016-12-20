package Obstacles;

import Effects.Projectile;
import Environment.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Deb extends Enemy {
	private ArrayList<Projectile.FireBall> fire = new ArrayList<>();
	private int interval = 1;

	public Deb() {
		super("devil", 2);

		health = 50;
		damage = 5;

		script = new Timer(20, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (interval == 0) {
					double x = Level.getP().getX() - body.x, y = Level.getP().getY() - body.y;
					double constant = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
					x = 1 / constant * x;
					y = 1 / constant * y;

					Point2D.Double unitPoint = new Point2D.Double(x, y);
					fire.add(new Projectile.FireBall(unitPoint, body.x, body.y));
				}
				if (health <= 25) {
					body.x += (Level.getP().getX() > body.x) ? 1 : -1;
					body.y += (Level.getP().getY() > body.y) ? 1 : -1;
				}

				interval = (interval + 1) % 50;
			}
		});

		hitBox = new Border(body);

		idle.setDelay(250);

		idle.start();
	}

	@Override
	public int hitPlayer() {
		for (Projectile p : fire) {
			if (Level.getP().getBody().intersects(new Rectangle2D.Double(p.getStart().x, p.getStart().y, p.width, p.height)))
				return 20;
		}
		return -1;
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		for (Projectile p : fire) {
			p.project();
		}
		removeProj();

		for (Projectile p : fire)
			p.draw(g);

	}

	private void removeProj() {
		for (int i = fire.size() - 1; i >= 0; i--) {
			Projectile p = fire.get(i);
			if (p.distance <= 0)
				fire.remove(p);
		}
	}
}
