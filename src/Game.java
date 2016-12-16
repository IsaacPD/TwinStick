import Environment.Level;
import Player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Game extends JFrame implements ActionListener {
	private ArrayList<Level> levels = new ArrayList<>();
	private Level current;
	private Player p;
	private Timer gtime = new Timer(10, this);
	private Timer npc = new Timer(30, this);
	public static int width, height;

	public Game() {
		setSize(640, 480);

		gtime.setActionCommand("GAME");
		npc.setActionCommand("NPC");

		Dimension d = this.getSize();
		width = d.width;
		height = d.height;

		levels.add(new Level());
		current = levels.get(0);
		p = Level.getP();

		setTitle("Work in progress");

		add(current);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addKeyListener(new PlayerAdapter());
		addKeyListener(new ProjAdapter());
		addMouseListener(new MouseAdapter());
		pack();

		setResizable(true);
		setVisible(true);

		npc.start();
	}

	public void loadLevel(int index) {
		remove(current);
		current = levels.get(index);
		add(current);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("NPC"))
			p.update();
		repaint();

		Dimension d = this.getSize();
		width = d.width;
		height = d.height;
	}

	public static void main(String... args) {
		Game g = new Game();
	}

	public class PlayerAdapter implements KeyListener {

		private final Set<Integer> pressed = new HashSet<>();

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public synchronized void keyPressed(KeyEvent k) {
			pressed.add(k.getKeyCode());
			if (pressed.size() >= 1) {
				for (int key : pressed) {

					if (key == KeyEvent.VK_W)
						p.moveY(-1);

					else if (key == KeyEvent.VK_A)
						p.moveX(-1);

					else if (key == KeyEvent.VK_D)
						p.moveX(1);

					else if (key == KeyEvent.VK_S)
						p.moveY(1);
					else if (key == KeyEvent.VK_1)
						levels.add(new Level(Color.cyan));
					else if (key == KeyEvent.VK_2)
						loadLevel(levels.size() - 1);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			pressed.remove(e.getKeyCode());
		}
	}

	public class MouseAdapter implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			double x = e.getX() - p.getBody().x, y = e.getY() - p.getBody().y;
			double constant = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
			x = 1 / constant * x;
			y = 1 / constant * y;

			Point2D.Double unitPoint = new Point2D.Double(x, y);
			p.fire(unitPoint);
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}

	private class ProjAdapter implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent k) {
			int key = k.getKeyCode();
			if (key == KeyEvent.VK_UP)
				p.fire(0, -1);

			else if (key == KeyEvent.VK_LEFT)
				p.fire(-1, 0);

			else if (key == KeyEvent.VK_RIGHT)
				p.fire(1, 0);

			else if (key == KeyEvent.VK_DOWN)
				p.fire(0, 1);

			else if (key == KeyEvent.VK_SPACE)
				if (gtime.isRunning()) gtime.stop();
				else gtime.start();
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}
}
