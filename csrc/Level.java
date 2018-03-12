import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.ceil;

public class Level {
	private String mapName;
	private Point spawnPoint, size, tileSize;
	private BufferedImage backgroundTexture;

	private ArrayList<Tile> tileList;
	private ArrayList<Tileset> tilesets;
	private ArrayList<Rectangle2D.Double> collisionsRects;

	public Level(String mapName, Point spawnPoint) {
		this.mapName = mapName;
		this.spawnPoint = spawnPoint;
		this.size = new Point(0, 0);

		this.tileList = new ArrayList<>();
		this.tilesets = new ArrayList<>();
		this.collisionsRects = new ArrayList<>();

		loadMap(mapName);
	}

	public void update(float elapsedTime) {
	}

	public void draw(Graphics2D g) {
		for (Tile t : tileList)
			t.draw(g);
	}

	public Point getPlayerSpawnPoint() {
		return spawnPoint;
	}

	public String toString() {
		String result = "";
		for (Tile t : tileList)
			result += t.toString();
		return result;
	}

	ArrayList<Rectangle2D> checkTileCollisions(Rectangle2D other) {
		ArrayList<Rectangle2D> others = new ArrayList<>();
		for (Rectangle2D r : collisionsRects)
			if (other.intersects(r))
				others.add(r);
		return others;
	}

	private void loadMap(String mapName) {
		try {
			//Loading XML document
			File file = new File("maps/" + mapName + ".tmx");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			doc.getDocumentElement().normalize();

			//Parsing Map Element for widths and heights
			int width, height, tilewidth, tileheight;

			NodeList nodeList = doc.getElementsByTagName("map");
			Element mapElement = (Element) nodeList.item(0);

			width = Integer.parseInt(mapElement.getAttribute("width"));
			height = Integer.parseInt(mapElement.getAttribute("height"));
			tilewidth = Integer.parseInt(mapElement.getAttribute("tilewidth"));
			tileheight = Integer.parseInt(mapElement.getAttribute("tileheight"));

			//Initializing size and tileSize
			size = new Point(width, height);
			tileSize = new Point(tilewidth, tileheight);

			//Parsing tileset elements
			nodeList = mapElement.getElementsByTagName("tileset");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element tilesetElement = (Element) nodeList.item(i);
				int firstgid, tilesetColumns;

				firstgid = Integer.parseInt(tilesetElement.getAttribute("firstgid"));
				tilesetColumns = Integer.parseInt(tilesetElement.getAttribute("columns"));

				String path = "tilesets/" + tilesetElement.getAttribute("name") + ".bmp";
				BufferedImage tex = ImageIO.read(new File(path));

				tilesets.add(new Tileset(tex, firstgid, tilesetColumns));
			}

			//Parsing layer elements
			nodeList = mapElement.getElementsByTagName("layer");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element layerElement = (Element) nodeList.item(i);

				//Parsing data element
				Element dataElement = (Element) layerElement.getElementsByTagName("data").item(0);
				String data = dataElement.getTextContent();

				//Parsing data
				int row = -1, column = 0, COLUMNS = tilesets.get(0).columns;
				Scanner scan = new Scanner(data);
				scan.useDelimiter(",");
				while (scan.hasNext()) {
					String token = scan.next();
					if (token.equals("0") || token.equals("\n0")) {
						if (token.contains("\n")) {
							column = 1;
							row++;
						} else column++;
						continue;
					}
					if (token.contains("\n")) {
						if (token.length() > 2)
							token = token.substring(token.indexOf("\n") + 1);
						else continue;
						row++;
						column = 0;
						int num = Integer.parseInt(token);
						int tsRow, tsCol;
						tsRow = ((num % COLUMNS) != 0) ? num / COLUMNS : num / COLUMNS - 1;
						tsCol = ((num % COLUMNS) != 0) ? num % COLUMNS - 1 : COLUMNS - 1;

						tileList.add(new Tile(tilesets.get(0).texture, tileSize,
								new Point(tsCol * 32, tsRow * 32), new Point(column * 32, row * 32)));
						column++;
					} else {
						int num = Integer.parseInt(token);
						int tsRow, tsCol;
						tsRow = ((num % COLUMNS) != 0) ? num / COLUMNS : num / COLUMNS - 1;
						tsCol = ((num % COLUMNS) != 0) ? num % COLUMNS - 1 : COLUMNS - 1;

						tileList.add(new Tile(tilesets.get(0).texture, tileSize,
								new Point(tsCol * 32, tsRow * 32), new Point(column * 32, row * 32)));
						column++;
					}
				}
			}

			//Parsing objectGroup elements
			nodeList = mapElement.getElementsByTagName("objectgroup");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element objectGroupElement = (Element) nodeList.item(i);
				String name = objectGroupElement.getAttribute("name");

				//Parsing Object
				NodeList objects = objectGroupElement.getElementsByTagName("object");
				if (name.equalsIgnoreCase("collisions")) {
					for (int j = 0; j < objects.getLength(); j++) {
						Element object = (Element) objects.item(j);
						float x, y, recwidth, recheight;

						x = Float.parseFloat(object.getAttribute("x"));
						y = Float.parseFloat(object.getAttribute("y"));
						recwidth = Float.parseFloat(object.getAttribute("width"));
						recheight = Float.parseFloat(object.getAttribute("height"));

						collisionsRects.add(new Rectangle2D.Double(ceil(x), ceil(y), ceil(recwidth), ceil(recheight)));
					}
				} else if (name.equalsIgnoreCase("spawn points")) {
					for (int j = 0; j < objects.getLength(); j++) {
						Element object = (Element) objects.item(j);
						int x = Integer.parseInt(object.getAttribute("x"));
						int y = Integer.parseInt(object.getAttribute("y"));

						if (object.getAttribute("name").equalsIgnoreCase("player"))
							spawnPoint = new Point(x, y);
						else {
							//TODO add enemy spawns
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Tileset {
		BufferedImage texture;
		int FirstGid, columns;

		Tileset() {
			FirstGid = -1;
		}

		Tileset(BufferedImage texture, int firstGid, int columns) {
			this.texture = texture;
			this.FirstGid = firstGid;
			this.columns = columns;
		}
	}
}
