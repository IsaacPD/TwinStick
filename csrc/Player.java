import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

//TODO mimic movement in the src/Player class
public class Player extends AnimatedSprite {
	final float WALK_SPEED = 1.8f * Game.SCALE, MAX_SPEED = 3.0f * Game.SCALE,
			GRAVITY = 0.5f * Game.SCALE, GRAVITY_CAP = 2.0f;

	private final Point2D.Double velocity = new Point2D.Double(0, 0);
	double health = 100;
	boolean grounded = false;
	Game.Direction facing;
	private String[] projectiles = {"hearty"};
	private ArrayList<Projectile> fired = new ArrayList<>();

	public Player(Point spawn) {
		super("sprites/charSheet.png", 0, 0, 30, 30, spawn.x, spawn.y, 150);
		setupAnimations();
		playAnimation("Idle");
	}

	public void moveY(int dir) {
		if (Math.abs(velocity.getY()) < MAX_SPEED)
			velocity.y += dir * WALK_SPEED;
		else if (velocity.getY() >= MAX_SPEED)
			velocity.y = MAX_SPEED;
		else if (velocity.getY() <= -MAX_SPEED)
			velocity.y = -MAX_SPEED;

		if (dir > 0) {
			playAnimation("RunDown");
			facing = Game.Direction.RIGHT;
		} else {
			grounded = false;
			playAnimation("RunUp");
			facing = Game.Direction.LEFT;
		}
	}

	public void moveX(int dir) {
		if (Math.abs(velocity.getX()) < MAX_SPEED)
			velocity.x += dir * WALK_SPEED;
		else if (velocity.getX() >= MAX_SPEED)
			velocity.x = MAX_SPEED;
		else if (velocity.getX() <= -MAX_SPEED)
			velocity.x = -MAX_SPEED;

		if (dir > 0) {
			playAnimation("RunRight");
			facing = Game.Direction.RIGHT;
		} else {
			playAnimation("RunLeft");
			facing = Game.Direction.LEFT;
		}
	}

	public void slow() {
		double friction = .80;
		velocity.setLocation(velocity.getX() * friction, velocity.getY() * friction);
		if (Math.abs(velocity.x) < .0001)
			velocity.x = 0;
		if (Math.abs(velocity.y) < .0001)
			velocity.y = 0;
	}

	//TODO add gravity/physics
	@Override
	public void update(int elapsedTime) {
		if (velocity.y <= GRAVITY_CAP && !grounded)
			velocity.y += GRAVITY;

		x += velocity.x;
		y += velocity.y;
		slow();

		if (velocity.x == 0 && velocity.y == 0)
			playAnimation("Idle");

		for (Projectile p : fired) {
			p.update(elapsedTime);
		}

		for (int i = fired.size() - 1; i >= 0; i--)
			if (fired.get(i).distance <= 0)
				fired.remove(i);

		super.update(elapsedTime);
	}

	public void draw(Graphics2D g) {
		drawHealth(g);
		super.draw(g, (int) this.x, (int) this.y);
		for (Projectile p : fired) {
			p.draw(g);
		}
	}

	public void handleTileCollision(ArrayList<Rectangle2D> others) {
		for (Rectangle2D r : others) {
			Side collisionSide = getCollisionSide(r);
			switch (collisionSide) {
				case TOP:
					y = (float) (r.getMaxY() + 1);
					velocity.y = 0;
					break;
				case BOTTOM:
					y = (float) (r.getY() - hitBox.getHeight() - 1);
					velocity.y = 0;
					grounded = true;
					break;
				case LEFT:
					x = (float) (r.getMaxX() + 1);
					break;
				case RIGHT:
					x = (float) (r.getX() - hitBox.getWidth() - 1);
					break;
			}
		}
	}

	public void fire(Point2D.Double point) {
		Projectile proj = new Projectile(projectiles[0], point, hitBox.getCenterX(), hitBox.getCenterY());
		fired.add(proj);
	}

	@Override
	protected void animationDone(String currentAnimation) {
		super.animationDone(currentAnimation);
	}

	@Override
	protected void setupAnimations() {
		addAnimation(7, 60, 0, "Idle", 30, 30, new Point(0, 0));
		addAnimation(7, 60, 0, "RunRight", 30, 30, new Point(0, 0));
		addAnimation(7, 60, 0, "RunLeft", 30, 30, new Point(0, 0));
		addAnimation(7, 60, 0, "RunUp", 30, 30, new Point(0, 0));
		addAnimation(7, 60, 0, "RunDown", 30, 30, new Point(0, 0));
	}

	public float getPX() {
		return x;
	}

	public float getPY() {
		return y;
	}

	private void drawHealth(Graphics2D g) {
		Rectangle bar = new Rectangle(0, -10, (int) health / 10, 5);
		g.translate(hitBox.x, hitBox.y);
		g.draw(bar);
		g.setColor(Color.red);
		g.fill(bar);
		g.translate(-hitBox.x, -hitBox.y);
	}
}
