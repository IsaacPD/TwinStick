package Run;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public abstract class Asset {
	private final String path = "C:\\Users\\isaac\\Desktop\\Dev Stuff\\TwinStick\\";
	public ArrayList<BufferedImage> looks = new ArrayList<>();
	public Timer idle = new Timer(500, new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame = (frame + 1) % looks.size();
		}
	});
	public int imageHeight, imageWidth, size = 1;
	public int width, height, frame;
	public double ratioWidth, ratioHeight;

	public Rectangle2D.Double body;

	public Asset(String name) {
		this(name, 1);
	}

	public Asset(String name, int size) {
		this.size = size;

		try {
			for (File f : new File(path).listFiles()) {
				if (f.getName().contains(name))
					looks.add(ImageIO.read(new File(f.getName())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		imageHeight = looks.get(0).getHeight();
		imageWidth = looks.get(0).getWidth();

		ratioWidth = Game.basisWidth / (imageWidth * 3);
		ratioHeight = Game.basisHeight / (imageHeight * 3);

		width = size * (int) (Game.width / ratioWidth);
		height = size * (int) (Game.height / ratioHeight);

		body = new Rectangle2D.Double(Game.gen.nextInt(8 * Game.width / 10) + Game.width / 10, Game.gen.nextInt(8 * Game.height / 10) + Game.height / 10,
				imageWidth * (width / imageWidth), imageHeight * (height / imageHeight));
	}

	public void draw(Graphics2D g) {
		float[] transform = {width / imageWidth, 0, 0, height / imageHeight};
		g.drawImage(looks.get(frame), new AffineTransformOp(new AffineTransform(transform),
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR), (int) body.x, (int) body.y);
	}
}
