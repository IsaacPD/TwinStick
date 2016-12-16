package Environment;

import Obstacles.Enemy;
import Obstacles.Snake;
import Player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Level extends JPanel {
	private static final Player p = new Player();
	private ArrayList<Enemy> enemies = new ArrayList<>();

	public Level() {
		this(Color.white);
		enemies.add(new Snake());
	}

	public Level(Color c) {
		setBackground(c);
		setPreferredSize(new Dimension(640, 480));
	}

	public static Player getP() {
		return p;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Color player = Color.green;

		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			boolean gone = false;

			if (p.gotHit(e))
				player = Color.red;
			if (e.gotHit())
				if (e.getHealth() <= 0)
					gone = enemies.remove(e);
			if (!gone)
				e.draw(g2);
		}

		g2.setColor(player);
		p.draw(g2);
	}
}
