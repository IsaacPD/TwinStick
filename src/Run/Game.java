package Run;

import Environment.Level;
import Player.Player;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static Environment.Level.current;

//TODO create pause menu
//TODO make it possible to save game, add game over condition, add start screen
//TODO make more items and npcs
//TODO make a map
public class Game extends JFrame implements ActionListener {
	public static final int basisWidth = 1280, basisHeight = 720;
	public final static Random gen = new Random();
	public static int width = 1024, height = 576;
	private final Player p;
	private Set<Integer> keys = new HashSet<>();
	private final Timer npc = new Timer(30, this);
	private final Timer airTimer = new Timer(15, this);
	private Level levels = new Level();
	private int airTime = 0;

	public Game() {
		setSize(width, height);

		npc.setActionCommand("NPC");
		airTimer.setActionCommand("Air");

		current = levels.start;
		current.createBorder();
		p = Level.getP();

		setTitle("Work in progress");

		add(current);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addKeyListener(new PlayerAdapter());
		addMouseListener(new MAdapter());
		pack();

		setResizable(false);
		setVisible(true);

		npc.start();
		airTimer.start();
	}

	public static void main(String... args) {
		Game g = new Game();
	}

	public void loadLevel(int index) {
		remove(current);
		current.pause();
		current.cool.stop();

		current = (index == -1) ? current.getWest() :
				(index == 1) ? current.getEast() :
						(index == 2) ? current.getSouth()
								: current.getNorth();

		add(current);
		current.pause();
		p.clear();
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (airTime > 0 && e.getActionCommand().equals("Air"))
			airTime = (airTime + 1) % 18;

		if (p.getBody().getCenterX() < -15) {
			loadLevel(-1);
			p.resetPos(6);
		} else if (p.getBody().getCenterX() > Game.width) {
			loadLevel(1);
			p.resetPos(4);
		} else if (p.getBody().getCenterY() < -15) {
			loadLevel(-2);
			p.resetPos(2);
		} else if (p.getBody().getCenterY() > Game.height) {
			loadLevel(2);
			p.resetPos(8);
		}

		if (!current.isPaused() && current.cool.isRunning())
			inputHandle();
	}

	private void inputHandle() {

		if (keys.contains(KeyEvent.VK_W))
			p.moveY(-1);

		if (keys.contains(KeyEvent.VK_A))
			p.moveX(-1);

		if (keys.contains(KeyEvent.VK_D))
			p.moveX(1);

		if (keys.contains(KeyEvent.VK_S))
			p.moveY(1);

		if (keys.contains(KeyEvent.VK_UP) && airTime == 0) {
			p.fire(0, -1);
			airTime++;
		}
		if (keys.contains(KeyEvent.VK_LEFT) && airTime == 0) {
			p.fire(-1, 0);
			airTime++;
		}
		if (keys.contains(KeyEvent.VK_RIGHT) && airTime == 0) {
			p.fire(1, 0);
			airTime++;
		}
		if (keys.contains(KeyEvent.VK_DOWN) && airTime == 0) {
			p.fire(0, 1);
			airTime++;
		}
	}

	public class PlayerAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				current.pause();

			if (e.getKeyCode() == KeyEvent.VK_8 || e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
				Game.height *= 2;
				Game.width *= 2;
				setSize(width, height);
				p.resize();
				levels.createRoom();
				repaint();
			}

			if (e.getKeyCode() == KeyEvent.VK_2)
				loadLevel(2);

			if (e.getKeyCode() == KeyEvent.VK_6 || e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
				Game.height /= 2;
				Game.width /= 2;
				setSize(width, height);
				p.resize();
				levels.createRoom();
				repaint();
			}
			if (!current.isPaused() && current.cool.isRunning()) {
				keys.add(e.getKeyCode());
				e.consume();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (!current.isPaused() && current.cool.isRunning()) {
				keys.remove(e.getKeyCode());
				e.consume();
			}
		}
	}

	public class MAdapter extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (!current.isPaused() && current.cool.isRunning()) {
				double x = e.getX() - p.getBody().getCenterX(), y = e.getY() - p.getBody().getCenterY();
				double constant = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
				x = 1 / constant * x;
				y = 1 / constant * y;

				Point2D.Double unitPoint = new Point2D.Double(x, y);
				p.fire(unitPoint);
			}
		}

	}
}