package Effects;


import java.awt.geom.Point2D;

public class Laser extends Projectile {
	final int WIDTH = 10, HEIGHT = 30;

	public Laser(double vx, double vy, double x, double y) {
		super(vx, vy, x, y);
	
		this.width = WIDTH;
		this.height = HEIGHT;
		
		speed = 3;
	}

	public Laser(Point2D.Double p, double x, double y) {
		super(p, x, y);
		
		this.width = WIDTH;
		this.height = HEIGHT;
		
		speed = 3;
	}
}
