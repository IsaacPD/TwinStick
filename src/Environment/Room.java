package Environment;

import Obstacles.Border;
import Obstacles.Enemy;
import Player.Player;
import Run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import static Environment.Level.randomColor;
import static Environment.Level.rooms;

//TODO make enemies completely gone, as in they do not interact with the player when player is not in room
//TODO extend JLabel for use of background images
public class Room extends JPanel {
	private Room north, south, east, west;
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private Border border;
	private Timer cool = new Timer(30, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			update();
		}
	});

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
		rooms--;
		initialize();
		createBorder();
		cool.start();
	}

	private void createBorder() {
		int offSet = 15;
		int door = 100;

		ArrayList<Line2D.Double> lines = new ArrayList<>();
		if (west != null) {
			lines.add(new Line2D.Double(offSet, 0, offSet, Game.height / 2 - door));
			lines.add(new Line2D.Double(offSet, Game.height / 2 + door, offSet, Game.height));
		} else lines.add(new Line2D.Double(offSet, 0, offSet, Game.height));

		if (east != null) {
			lines.add(new Line2D.Double(Game.width - offSet, 0, Game.width - offSet, Game.height / 2 - door));
			lines.add(new Line2D.Double(Game.width - offSet, Game.height / 2 + door, Game.width - offSet, Game.height));
		} else lines.add(new Line2D.Double(Game.width - offSet, 0, Game.width - offSet, Game.height));

		if (north != null) {
			lines.add(new Line2D.Double(0, offSet, Game.width / 2 - door, offSet));
			lines.add(new Line2D.Double(Game.width / 2 + door, offSet, Game.width, offSet));
		} else lines.add(new Line2D.Double(0, offSet, Game.width, offSet));

		if (south != null) {
			lines.add(new Line2D.Double(0, Game.height - offSet, Game.width / 2 - door, Game.height - offSet));
			lines.add(new Line2D.Double(Game.width / 2 + door, Game.height - offSet, Game.width, Game.height - offSet));
		} else lines.add(new Line2D.Double(0, Game.height - offSet, Game.width, Game.height - offSet));

		border = new Border(lines);
	}

	private void orient(Room parent, int pos) {
		if (parent == null)
			return;
		if (pos == 2)
			south = parent;
		else if (pos == 8)
			north = parent;
		else if (pos == 4)
			west = parent;
		else if (pos == 6)
			east = parent;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		border.draw(g2);

		for (Enemy e : enemies)
			e.draw(g2);

		Level.getP().draw(g2);

	}

	public void initialize() {
		if (north == null)
			north = ((int) (Math.random() * 2) == 1 && rooms > 0) ? new Room(this, randomColor(), 2) : null;
		if (east == null)
			east = ((int) (Math.random() * 2) == 1 && rooms > 0) ? new Room(this, randomColor(), 4) : null;
		if (south == null)
			south = ((int) (Math.random() * 2) == 1 && rooms > 0) ? new Room(this, randomColor(), 8) : null;
		if (west == null)
			west = ((int) (Math.random() * 2) == 1 && rooms > 0) ? new Room(this, randomColor(), 6) : null;
	}

	private void update() {
		Player player = Level.getP();

		player.update();

		for (int i = enemies.size() - 1; i >= 0; i--) {
			Enemy e = enemies.get(i);

			Line2D.Double border = e.crosses();

			if (border != null) {
				player.gotHit(e);
				player.bounce(border);
			}
			if (e.gotHit())
				if (e.getHealth() <= 0)
					enemies.remove(e);
		}

		Line2D.Double roomB = border.crossesBorder();

		if (roomB != null)
			player.bounce(roomB);
		player.slow();

		repaint();
	}

	public Room getNorth() {
		return north;
	}

	void setNorth(Room north) {
		this.north = north;
	}

	public Room getSouth() {
		return south;
	}

	void setSouth(Room south) {
		this.south = south;
	}

	public Room getEast() {
		return east;
	}

	void setEast(Room east) {
		this.east = east;
	}

	public Room getWest() {
		return west;
	}

	void setWest(Room west) {
		this.west = west;
	}

	ArrayList<Enemy> getEnemies() {
		return enemies;
	}
}
