import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Tile {
	private BufferedImage tileSet;
	private Point size, tilesetPosition, position;

	public Tile(BufferedImage tileSet, Point size, Point tilesetPosition, Point position) {
		this.tileSet = tileSet;
		this.size = size;
		this.position = new Point((int) (position.x * Game.SCALE), (int) (position.y * Game.SCALE));
		this.tilesetPosition = tilesetPosition;
	}

	public void update(int elapsedTime) {

	}

	public void draw(Graphics2D g) {
		float transform[] = {Game.SCALE, 0, 0, Game.SCALE};
		BufferedImage tile = tileSet.getSubimage(tilesetPosition.x, tilesetPosition.y, size.x, size.y);
		g.drawImage(tile, new AffineTransformOp(new AffineTransform(transform),
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR), position.x, position.y);
	}

	public String toString() {
		String result = "";

		result += "Size: " + size.toString() + "\n";
		result += "Tileset Position: " + tilesetPosition.toString() + "\n";
		result += "Position: " + position.toString() + "\n";

		return result;
	}
}
