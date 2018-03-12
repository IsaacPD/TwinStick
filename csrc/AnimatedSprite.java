import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimatedSprite extends Sprite {
	protected double timeToUpdate;
	protected boolean currentAnimationOnce;
	protected String currentAnimation;
	private Map<String, ArrayList<BufferedImage>> animations = new HashMap<>();
	private Map<String, Point> offsets = new HashMap<>();
	private int frame;
	private double timeElapsed;
	private boolean visible;

	AnimatedSprite() {
		super("", 0, 0, 0, 0, 0, 0);
	}

	AnimatedSprite(String path, int sourceX, int sourceY, int width, int height, float posX, float posY, float timeToUpdate) {
		super(path, sourceX, sourceY, width, height, posX, posY);
		frame = 0;
		this.timeToUpdate = timeToUpdate;
		visible = true;
		currentAnimationOnce = false;
		currentAnimation = "";
		timeElapsed = 0;
	}

	public void playAnimation(String animation, boolean once) {
		currentAnimationOnce = once;
		if (!currentAnimation.equals(animation)) {
			currentAnimation = animation;
			frame = 0;
		}
	}

	public void playAnimation(String animation) {
		playAnimation(animation, false);
	}

	public void draw(Graphics2D g, int x, int y) {
		float transform[] = {Game.SCALE, 0, 0, Game.SCALE};
		AffineTransform at = new AffineTransform(transform);
		if (visible) {
			int posX = x + offsets.get(currentAnimation).x;
			int posY = y + offsets.get(currentAnimation).y;
			BufferedImage spriteFrame = animations.get(currentAnimation).get(frame);
			g.drawImage(spriteFrame, new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR), posX, posY);
		}
	}

	public void update(int elapsedTime) {
		super.update();

		timeElapsed += elapsedTime;

		if (timeElapsed >= timeToUpdate) {
			timeElapsed -= timeToUpdate;
			if (frame < animations.get(currentAnimation).size() - 1)
				frame++;
			else {
				if (currentAnimationOnce)
					setVisible(false);
				frame = 0;
				animationDone(currentAnimation);
			}
		}
	}

	protected void addAnimation(int frames, int x, int y, String name, int width, int height, Point offset) {
		ArrayList<BufferedImage> sprites = new ArrayList<>();
		for (int i = 0; i < frames; i++) {
			sprites.add(spriteSheet.getSubimage(((i + x) * width) % spriteSheet.getWidth(), y, width, height));
		}
		animations.put(name, sprites);
		offsets.put(name, offset);
	}

	protected void resetAnimations() {
		animations.clear();
		offsets.clear();
	}

	protected void stopAnimation() {
		frame = 0;
		animationDone(currentAnimation);
	}

	protected void setVisible(boolean visible) {
		this.visible = visible;
	}

	protected void animationDone(String currentAnimation) {

	}

	protected void setupAnimations() {

	}
}
