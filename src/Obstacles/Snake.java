package Obstacles;

import Environment.Level;

import java.awt.*;
import java.util.ArrayList;

public class Snake extends Enemy {

	private ArrayList<Rectangle> body = new ArrayList<>();

	public Snake() {
		body.add(new Rectangle(300, 300, 30, 10));
		health = 20;
	}

	public void script() {

	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.magenta);
		g2.fill(body.get(0));
	}

	@Override
	public boolean hitPlayer() {
		return body.get(0).intersects(Level.getP().getBody());
	}

	@Override
	public boolean gotHit() {
		if (Level.getP().hit(this) && !invinsible.isRunning()) {
			health -= 5;
			System.out.println("Snake: " + health);
			invinsible.start();
			return true;
		}
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return body.get(0).intersects(x, y, w, h);
	}
}
