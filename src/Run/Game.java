package Run;

import Environment.Level;
import Player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import static Environment.Level.current;

//TODO make game scale with screen size and adjust projectile speeds
public class Game extends JFrame implements ActionListener {
	public static int width = 640, height = 480;
	private final Player p;
	private final Timer npc = new Timer(30, this);
	private final Timer airTimer = new Timer(15, this);
	private Level levels = new Level();
	private int airTime = 0;

	public Game() {
		setSize(width, height);

		npc.setActionCommand("NPC");
		airTimer.setActionCommand("Air");

		current = levels.start;
		p = Level.getP();

		setTitle("Work in progress");

		add(current);
		addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				super.windowStateChanged(e);
				width = getWidth();
				height = getHeight();
				current.createBorder();
				p.resize();
				repaint();
			}
		});

		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addKeyListener(new PlayerAdapter());
		addMouseListener(new MouseAdapter());
		pack();

		setResizable(true);
		setVisible(true);

		npc.start();
		airTimer.start();
	}

	public static void main(String... args) {
		Game g = new Game();
	}

	public void loadLevel(int index) {
		remove(current);
		current.cool.stop();

		current = (index == -1) ? current.getWest() :
				(index == 1) ? current.getEast() :
						(index == 2) ? current.getSouth()
								: current.getNorth();

		add(current);
		current.cool.start();
		p.clear();
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (airTime > 0 && e.getActionCommand().equals("Air"))
			airTime = (airTime + 1) % 18;

		Dimension d = this.getSize();
		width = d.width;
		height = d.height;

		if (p.getBody().x < -15) {
			loadLevel(-1);
			p.resetPos();
		} else if (p.getBody().x > Game.width + 15) {
			loadLevel(1);
			p.resetPos();
		} else if (p.getBody().y < -15) {
			loadLevel(-2);
			p.resetPos();
		} else if (p.getBody().y > Game.height + 15) {
			loadLevel(2);
			p.resetPos();
		}

		repaint();
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

					else if (key == KeyEvent.VK_UP && airTime == 0) {
						p.fire(0, -1);
						airTime++;
					} else if (key == KeyEvent.VK_LEFT && airTime == 0) {
						p.fire(-1, 0);
						airTime++;
					} else if (key == KeyEvent.VK_RIGHT && airTime == 0) {
						p.fire(1, 0);
						airTime++;
					} else if (key == KeyEvent.VK_DOWN && airTime == 0) {
						p.fire(0, 1);
						airTime++;
					}

					/*else if (key == KeyEvent.VK_SPACE)
						if (gTime.isRunning()) gTime.stop();
						else gTime.start();*/

					else if (key == KeyEvent.VK_8)
						loadLevel(-2);

					else if (key == KeyEvent.VK_4)
						loadLevel(-1);

					else if (key == KeyEvent.VK_2)
						loadLevel(2);

					else if (key == KeyEvent.VK_6)
						loadLevel(1);
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
			double x = e.getX() - p.getBody().getCenterX(), y = e.getY() - p.getBody().getCenterY();
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
}
