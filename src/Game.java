import Effects.Projectile;
import Environment.Level;
import Player.Direction;
import Player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JFrame implements ActionListener {
	ArrayList<Level> levels = new ArrayList<>();
	Level current;
	Timer gtime = new Timer(10, this);
	Timer npc = new Timer(30, this);

	public Game() {
		gtime.setActionCommand("GAME");
		npc.setActionCommand("NPC");

		levels.add(new Level());
		current = levels.get(0);

		setTitle("Work in progress");

		add(current);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addKeyListener(new KAdapter());
		pack();

		setResizable(false);
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
		current.getP().update();
		repaint();
	}

	public static void main(String... args) {
		Game g = new Game();
	}

	public class KAdapter implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent k) {
			int key = k.getKeyCode();
			Player p = current.getP();

			if (key == KeyEvent.VK_UP)
				p.fire(0, -1);

			else if (key == KeyEvent.VK_LEFT)
				p.fire(-1, 0);

			else if (key == KeyEvent.VK_RIGHT)
				p.fire(1, 0);

			else if (key == KeyEvent.VK_DOWN)
				p.fire(1, 0);

			else if (key == KeyEvent.VK_SPACE)
				if (gtime.isRunning()) gtime.stop();
				else gtime.start();

			else if (key == KeyEvent.VK_W)
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
			repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}

	public static class MouseAdapter implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			float x = e.getX(), y = e.getY();
			float constant = Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 1/2);
				
			Point2D.Float unitpoint = new Point(1/constant * x, 1/constant * y)
			p.fire(unitpoint);
		}

		@Override
		public void mousePressed(MouseEvent e) {

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
}
