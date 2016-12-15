package Effects;

import Player.Direction;

public class Laser extends Projectile {
	final int WIDTH = 10, HEIGHT = 30;

	public Laser(int vx, int vy, int x, int y) {
		super(vx, vy);
		this.x = x;
		this.y = y;
	
		this.width = WIDTH;
		this.height = HEIGHT;
		
		speed = 3;
	}
	
	public Laser(Point2D.Float p, int x, int y){
		super(p);
		this.x = x;
		this.y = y;
		
		this.width = WIDTH;
		this.height = HEIGHT;
		
		speed = 3;
	}
}
