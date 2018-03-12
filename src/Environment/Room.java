package Environment;

import Audio.Effects;
import Obstacles.Border;
import Obstacles.Deb;
import Obstacles.Enemy;
import Obstacles.Snake;
import Player.Player;
import Run.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static Environment.Level.randomColor;
import static Environment.Level.rooms;

//TODO extend JLabel for use of background images
//TODO make rooms able to share children i.e. rooms can loop into a circle
//TODO make special rooms (i.e. rooms with locked doors) etc...
public class Room extends JPanel {
	private final ArrayList<Enemy> enemies = new ArrayList<>();
	private final ArrayList<Items> items = new ArrayList<>();
	private boolean isPaused = true;

	private Room north, south, east, west, parent, reservedE, reservedW, reservedN, reservedS;
	private Border border;
	public final Timer cool = new Timer(30, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isPaused) {
				update();
				if (Level.getP().health <= 0)
					cool.stop();
			} else repaint();
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
		setPreferredSize(new Dimension(Game.width, Game.height));
		rooms--;
		initialize();
		createBorder();

		int rand = Game.gen.nextInt(6);

		for (int i = 0; i < rand; i++) {
			enemies.add(new Snake());
		}

		rand = Game.gen.nextInt(3);

		for (int i = 0; i < rand; i++) {
			items.add(new Items.Heart());
		}

		for (int i = 0; i < rand; i++) {
			enemies.add(new Deb());
		}
	}

	public void createBorder() {
		int offSet = 15;
		int door = Level.getP().height;

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
		this.parent = parent;

		if (parent == null)
			return;

		if (pos == 2) {
			south = parent;
			if (parent.parent != null && (parent.east == parent.parent) && parent.parent.parent != null && (parent.parent.parent == parent.parent.north)) {
				east = (Game.gen.nextInt(2) == 1) ? parent.parent.parent : null;
				reservedE = (east == null) ? parent.parent.parent : null;
			}
		} else if (pos == 8) {
			north = parent;
			if (parent.parent != null && parent.west == parent.parent && parent.parent.parent != null && parent.parent.parent == parent.parent.south) {
				west = (Game.gen.nextInt(2) == 1) ? parent.parent.parent : null;
				reservedW = (west == null) ? parent.parent.parent : null;
			}
		} else if (pos == 4) {
			west = parent;
			if (parent.parent != null && parent.north == parent.parent && parent.parent.parent != null && parent.parent.parent == parent.parent.east) {
				north = (Game.gen.nextInt(2) == 1) ? parent.parent.parent : null;
				reservedN = (north == null) ? parent.parent.parent : null;
			}
		} else if (pos == 6) {
			east = parent;
			if (parent.parent != null && parent.north == parent.parent && parent.parent.parent != null && parent.parent.parent == parent.parent.west) {
				north = (Game.gen.nextInt(2) == 1) ? parent.parent.parent : null;
				reservedN = (north == null) ? parent.parent.parent : null;
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (Level.getP().health <= 0) {
			g2.drawString("GAME OVER", Game.width / 2 - "GAME OVER".length() / 2, Game.height / 2);
			return;
		}
		if (isPaused) {
			g2.drawString("PAUSED", Game.width / 2 - "PAUSED".length() / 2, Game.height / 2);
			return;
		}

		border.draw(g2);

		for (Enemy e : enemies)
			e.draw(g2);

		for (Items item : items)
			item.draw(g2);

		Level.getP().draw(g2);

	}

	private void initialize() {
		if (north == null && reservedN == null)
			north = (Game.gen.nextInt(2) == 1 && rooms > 0) ? new Room(this, randomColor(), 2) : null;
		if (east == null && reservedE == null)
			east = (Game.gen.nextInt(2) == 1 && rooms > 0) ? new Room(this, randomColor(), 4) : null;
		if (south == null && reservedS == null)
			south = (Game.gen.nextInt(2) == 1 && rooms > 0) ? new Room(this, randomColor(), 8) : null;
		if (west == null && reservedW == null)
			west = (Game.gen.nextInt(2) == 1 && rooms > 0) ? new Room(this, randomColor(), 6) : null;
	}

	void createRoom() {
		setPreferredSize(new Dimension(Game.width, Game.height));

		if (north != null && north != parent)
			north.createRoom();
		if (west != null && west != parent)
			west.createRoom();
		if (east != null && east != parent)
			east.createRoom();
		if (south != null && south != parent)
			south.createRoom();

		for (Enemy e : enemies)
			e.resize();

		for (Items item : items)
			item.resize();

		createBorder();
	}

	private void update() {
		Player player = Level.getP();

		Point2D.Double origin = player.update();
		Effects effects = new Effects();

		for (int i = enemies.size() - 1; i >= 0; i--) {
			Enemy e = enemies.get(i);

			if (e.hitPlayer() != -1) {
				player.gotHit(e);
				if (player.health <= 0)
					effects.playDeath();
			}
			if (e.gotHit())
				if (e.getHealth() <= 0) {
					enemies.remove(e);
					effects.playDeath();
				}
		}

		for (int i = items.size() - 1; i >= 0; i--) {
			Items item = items.get(i);

			if (item.body.intersects(player.getBody())) {
				item.get();
				items.remove(item);
			}
		}

		if (border.crossesBorder(player.getBody()) != null) {
			player.bounce(origin, border.crossesBorder(player.getBody()));
		}
		player.slow();

		repaint();
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

	public Room getParentRoom() {
		return parent;
	}

	ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void pause() {
		if (!cool.isRunning()) cool.start();

		isPaused = !isPaused;
		for (Enemy e : enemies) {
			if (e.script.isRunning()) e.script.stop();
			else e.script.start();
		}
		repaint();
	}

	public boolean isPaused() {
		return isPaused;
	}
}
