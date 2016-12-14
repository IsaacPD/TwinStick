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
		ArrayList<Projectile> proj = current.getP().getProj();
		for (Projectile p : proj) {
			p.project();
		}

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
				p.fire(Direction.UP);

			else if (key == KeyEvent.VK_LEFT)
				p.fire(Direction.LEFT);

			else if (key == KeyEvent.VK_RIGHT)
				p.fire(Direction.RIGHT);

			else if (key == KeyEvent.VK_DOWN)
				p.fire(Direction.DOWN);

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
