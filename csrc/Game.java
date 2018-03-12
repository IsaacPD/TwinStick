import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Game extends JPanel implements ActionListener {
	final static int WIDTH = 1024, HEIGHT = 576;
	final static float SCALE = 1.0f;
	private static Set<Integer> keys = new HashSet<>();
	final int FPS = 60, SPF = (int) (1 / (FPS / 1000.0));
	final int MAX_FRAME_TIME = 6000 / FPS;
	final Timer gLoop = new Timer(SPF, this);
	Player player;
	Level level;

	public Game() {
		level = new Level("map 1", new Point(100, 100));
		player = new Player(level.getPlayerSpawnPoint());
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addKeyListener(new PlayerAdapter());
		gLoop.start();
		System.out.println(level.toString());

		JFrame frame = new JFrame("Work in Progress");
		frame.add(this);
		frame.addKeyListener(new PlayerAdapter());
		frame.addMouseListener(new MAdapter());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String... args) {
		Game game = new Game();
	}

	//Draw
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		level.draw(g2);
		player.draw(g2);
		update();
	}

	//Game Loop
	@Override
	public void actionPerformed(ActionEvent e) {
		inputHandle();
		repaint();
	}

	private void update() {
		player.update(SPF);
		level.update(SPF);

		ArrayList<Rectangle2D> others;
		others = level.checkTileCollisions(player.getHitBox());
		if (others.size() > 0)
			player.handleTileCollision(others);
	}

	private void inputHandle() {

		if (keys.contains(KeyEvent.VK_W) || keys.contains(KeyEvent.VK_Z))
			player.moveY(-1);

		if (keys.contains(KeyEvent.VK_A) || keys.contains(KeyEvent.VK_LEFT))
			player.moveX(-1);

		if (keys.contains(KeyEvent.VK_D) || keys.contains(KeyEvent.VK_RIGHT))
			player.moveX(1);

		if (keys.contains(KeyEvent.VK_S) || keys.contains(KeyEvent.VK_DOWN))
			player.moveY(1);

		/*
		if (keys.contains(KeyEvent.VK_UP) && airTime == 0) {
			player.fire(0, -1);
			airTime++;
		}
		if (keys.contains(KeyEvent.VK_LEFT) && airTime == 0) {
			player.fire(-1, 0);
			airTime++;
		}
		if (keys.contains(KeyEvent.VK_RIGHT) && airTime == 0) {
			player.fire(1, 0);
			airTime++;
		}
		if (keys.contains(KeyEvent.VK_DOWN) && airTime == 0) {
			player.fire(0, 1);
			airTime++;
		}*/
	}

	public enum Direction {
		LEFT, RIGHT, UP, DOWN
	}

	public class PlayerAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			keys.add(e.getKeyCode());
			e.consume();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			keys.remove(e.getKeyCode());
			e.consume();

		}
	}

	public class MAdapter extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			double x = e.getX() - player.getHitBox().getCenterX(), y = e.getY() - player.getHitBox().getCenterY();
			double constant = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
			x = 1 / constant * x;
			y = 1 / constant * y;

			Point2D.Double unitPoint = new Point2D.Double(x, y);
			player.fire(unitPoint);
		}
	}
}
