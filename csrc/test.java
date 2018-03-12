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
import java.util.Scanner;

public class test {
	public static void main(String... args) {
		try {
			//Loading XML document
			File file = new File("maps/map 1.tmx");
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
			System.out.println(new Point(width, height));
			System.out.println(new Point(tilewidth, tileheight));

			//Parsing tileset elements
			nodeList = mapElement.getElementsByTagName("tileset");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element tilesetElement = (Element) nodeList.item(i);
				int firstgid, tilesetColumns;

				firstgid = Integer.parseInt(tilesetElement.getAttribute("firstgid"));
				tilesetColumns = Integer.parseInt(tilesetElement.getAttribute("columns"));

				String path = "tilesets/" + tilesetElement.getAttribute("name") + ".bmp";
				BufferedImage tex = ImageIO.read(new File(path));
				System.out.println(tex.toString());
			}

			//Parsing layer elements
			nodeList = mapElement.getElementsByTagName("layer");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element layerElement = (Element) nodeList.item(i);

				//Parsing data element
				Element dataElement = (Element) layerElement.getElementsByTagName("data").item(0);
				String data = dataElement.getTextContent();

				//Parsing data
				int row = -1, column = 0, COLUMNS = 16;
				Scanner scan = new Scanner(data);
				scan.useDelimiter(",");
				while (scan.hasNext()) {
					String token = scan.next();
					if (token.equals("0")) {
						column++;
						continue;
					}
					if (token.contains("\n")) {
						if (token.length() > 2)
							token = token.substring(2);
						else continue;
						row++;
						column = 0;
						int num = Integer.parseInt(token);
						int tsRow, tsCol;
						tsRow = ((num % COLUMNS) != 0) ? num / COLUMNS : num / COLUMNS - 1;
						tsCol = ((num % COLUMNS) != 0) ? num % COLUMNS - 1 : COLUMNS - 1;
						System.out.println(new Point(tsCol * 32, tsRow * 32));
						System.out.println(new Point(column, row));
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

						System.out.println(new Rectangle2D.Double(x, y, recwidth, recheight));
					}
				} else if (name.equalsIgnoreCase("spawn points")) {
					for (int j = 0; j < objects.getLength(); j++) {
						Element object = (Element) objects.item(j);
						float x = Integer.parseInt(object.getAttribute("x"));
						float y = Integer.parseInt(object.getAttribute("y"));

						if (object.getAttribute("name").equalsIgnoreCase("player"))
							System.out.println(new Point((int) x, (int) y));
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
}

