package Environment;

import Run.Asset;
import Run.Game;

public abstract class Items extends Asset {

	Items() {
		super("item");
	}

	Items(String name, int size) {
		super(name, size);
	}

	Items(String s) {
		super(s);
	}

	public abstract void get();

	public static class Heart extends Items {

		public Heart() {
			super("heart");

			imageHeight = looks.get(0).getHeight();
			imageWidth = looks.get(0).getWidth();

			ratioWidth = Game.basisWidth / (imageWidth * 3);
			ratioHeight = Game.basisHeight / (imageHeight * 3);

			width = size * (int) (Game.width / ratioWidth);
			height = size * (int) (Game.height / ratioHeight);

			idle.setDelay(125);
			idle.start();
		}

		public void get() {
			Level.getP().health += 10;
		}
	}
}
