package Environment;

import Player.Player;
import Run.Game;

import javax.swing.*;
import java.awt.*;

//TODO set minimum number of rooms to be created
public class Level extends JPanel {
	private static final Player p = new Player();
	public static Room current;
	public static int rooms = 20;
	public Room start;

	public Level() {
		initialize();
		setPreferredSize(new Dimension(640, 480));
		start.pause();
	}

	public static Player getP() {
		return p;
	}

	public static Color randomColor() {
		return new Color(Game.gen.nextInt(255), Game.gen.nextInt(255), Game.gen.nextInt(255));
	}

	public void createRoom() {
		start.createRoom();
	}

	private void initialize() {
		start = new Room(Color.pink);
		/*start.setEast(new Room(start, Color.magenta, 4));
		start.setWest(new Room(start, Color.RED, 6));
		start.setNorth(new Room(start, 2));
		start.setSouth(new Room(start, Color.black, 8));*/
		current = start;
	}
}
