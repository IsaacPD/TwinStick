package Effects;

import Player.Direction;

import java.awt.*;

public abstract class Projectile extends Rectangle {
	int speed;
	public Color color = Color.orange;
	private Point2D.Float dir;

	public Projectile(int x, int y) {
		dir = new Point2D.Float(x, y);
	}
	
	public Projectile(Point2D.Float p){
		dir = p;	
	}

	public void project() {
		this.x += dir.getX()*speed;
		this.y += dir.getY()*speed;
	}
	
	public Point2D.Float getDir(){
		return dir;
	}
}
