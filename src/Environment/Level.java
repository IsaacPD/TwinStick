package Environment;

import Obstacles.Snake;
import Player.Player;

import javax.swing.*;
import java.awt.*;

public class Level extends JPanel {
	public static final Player p = new Player();
	public Room start;

	public Level() {
		initialize();
		setPreferredSize(new Dimension(640, 480));
	}

	public Level(Color c) {
		setBackground(c);
		setPreferredSize(new Dimension(640, 480));
		initialize();
	}

	public static Player getP() {
		return p;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Color player = Color.green;

		start.paintComponent(g);

		g2.setColor(player);
		p.draw(g2);
	}

	public void initialize() {
		start = new Room(Color.pink);
		start.setEast(new Room(start, Color.magenta, -1));
		start.setWest(new Room(start, Color.RED, 1));
		start.setNorth(new Room(start, new Color(255, 255, 0), 2));
		start.setSouth(new Room(start, Color.black, -2));
		start.getEnemies().add(new Snake());
	}
}
