package Environment;

import Player.Player;

import javax.swing.*;
import java.awt.*;

public class Level extends JPanel {
	private static final Player p = new Player();

	public Level() {
		this(Color.white);
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
		p.draw(g2);
	}
}
