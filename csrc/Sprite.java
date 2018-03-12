import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

public abstract class Sprite {
	protected BufferedImage spriteSheet;
	protected BufferedImage sprite;
	protected Rectangle2D.Double hitBox;
	float x, y;

	Sprite(String path, int sourceX, int sourceY, int width, int height, float posX, float posY) {
		try {
			spriteSheet = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		x = posX;
		y = posY;

		sprite = spriteSheet.getSubimage(sourceX, sourceY, width, height);
		hitBox = new Rectangle2D.Double(x, y, width, height);
	}

	void draw(Graphics2D g, int x, int y) {
		float transform[] = {Game.SCALE, 0, 0, Game.SCALE};
		AffineTransform at = new AffineTransform(transform);
		g.drawImage(sprite, new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR), x, y);
	}

	void update() {
		hitBox.x = x;
		hitBox.y = y;
	}

	public Side getCollisionSide(Rectangle2D other) {
		int amtRight, amtLeft, amtTop, amtBottom;
		amtRight = (int) (hitBox.getMaxX() - other.getX());
		amtLeft = (int) (other.getMaxX() - hitBox.getX());
		amtTop = (int) (other.getMaxY() - hitBox.getY());
		amtBottom = (int) (hitBox.getMaxY() - other.getY());

		int[] vals = {abs(amtRight), abs(amtLeft), abs(amtBottom), abs(amtTop)};
		int lowest = vals[0];
		for (int i : vals)
			if (i <= lowest)
				lowest = i;

		return
				lowest == abs(amtRight) ? Side.RIGHT :
						lowest == abs(amtBottom) ? Side.BOTTOM :
								lowest == abs(amtLeft) ? Side.LEFT :
										lowest == abs(amtTop) ? Side.TOP :
												null;
	}

	Rectangle2D.Double getHitBox() {
		return hitBox;
	}

	public enum Side {
		TOP, BOTTOM, LEFT, RIGHT
	}

}
