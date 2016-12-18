package Obstacles;

import Environment.Level;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.List;

public class Border {
	private ArrayList<Line2D.Double> border = new ArrayList<>();

	public Border(RectangularShape r) {
		double x = r.getX(), y = r.getY(), xMax = r.getMaxX(), yMax = r.getMaxY();
		border.add(new Line2D.Double(x, y, xMax, y));
		border.add(new Line2D.Double(x, y, x, yMax));
		border.add(new Line2D.Double(xMax, y, xMax, yMax));
		border.add(new Line2D.Double(x, yMax, xMax, yMax));
	}

	public Border(List<Line2D.Double> b) {
		for (Line2D.Double aDouble : b) {
			border.add(aDouble);
		}
	}

	public Line2D.Double crossesBorder() {
		for (Line2D.Double l : border)
			if (Level.getP().getBody().intersectsLine(l))
				return l;
		return null;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		for (Line2D line : border)
			g.draw(line);
	}

	@Override
	public String toString() {
		String result = super.toString() + "\n";

		for (Line2D l : border)
			result += "[l(" + l.getP1().toString() + " ), " + "(" + l.getP2().toString() + ")]\n";
		return result;
	}
}
