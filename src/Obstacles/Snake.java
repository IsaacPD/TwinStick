package Obstacles;

import Environment.Level;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Snake extends Enemy {

	public Snake() {
		super("snake");

		health = 20;
		damage = 5;

		hitBox = new Border(body);

		idle.setDelay(250);

		idle.start();
		script = new Timer(20, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int pX = Level.getP().getX();
				int pY = Level.getP().getY();

				body.x += (pX > body.x) ? 1 : -1;
				body.y += (pY > body.y) ? 1 : -1;

				hitBox = new Border(body);
			}
		});
	}

	@Override
	public int hitPlayer() {
		return (crosses() != null) ? 10 : -1;
	}

}
