package Environment;

import Obstacles.Enemy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Room extends JPanel {
	private Room north, south, east, west;
	private ArrayList<Enemy> enemies = new ArrayList<>();

	public Room(Room parent, int pos) {
		this(parent, Color.yellow, pos);
	}

	public Room(Color c) {
		this(null, c, 0);
	}

	public Room(Room parent, Color c, int pos) {
		setBackground(c);
		orient(parent, pos);
		setPreferredSize(new Dimension(640, 480));
	}

	private void orient(Room parent, int pos) {
		if (parent == null)
			return;
		if (pos == 2)
			south = parent;
		else if (pos == -2)
			north = parent;
		else if (pos == -1)
			west = parent;
		else if (pos == 1)
			east = parent;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			boolean gone = false;

			if (Level.getP().gotHit(e)) {
				Level.getP().bounce();
			}
			if (e.gotHit())
				if (e.getHealth() <= 0)
					gone = enemies.remove(e);
			if (!gone)
				e.draw(g2);
		}

		Level.getP().draw(g2);
	}

	public Room getNorth() {
		return north;
	}

	public Room getSouth() {
		return south;
	}

	public Room getEast() {
		return east;
	}

	public Room getWest() {
		return west;
	}

	public void setNorth(Room north) {
		this.north = north;
	}

	public void setSouth(Room south) {
		this.south = south;
	}

	public void setEast(Room east) {
		this.east = east;
	}

	public void setWest(Room west) {
		this.west = west;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
}
