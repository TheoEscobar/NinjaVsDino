package principal;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class dinosaure {
	public SpriteSheet sprites;
	public Animation animation = new Animation();
	private float creationTime;
	private mainState main;
	public float x;
	public float y;
	public int ligne;
	public boolean stop = false;

	public dinosaure(mainState main) {
		this.main = main;
		int rand = (int) (Math.random() * (3));
		try {
			this.x = 930;
			this.y = 240 + 200 * rand;
			ligne = (int) ((y - 240) / 200);
			sprites = new SpriteSheet("sprites/Trex.png", 200, 120);
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 2; x++) {
					animation.addFrame(sprites.getSprite(x, y), 100);
				}
			}
			creationTime = main.time.getDureeMs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void mort() {
		if (main.dinos.get(ligne).contains(this)) {
			main.dinos.get(ligne).remove(this);
		}
	}

	private void move() {
		if (main.perso.vie>0) {
		this.x = 930 - (0.2f * (main.time.getDureeMs() - creationTime));
		}
	}

	public void draw(Graphics g) {
		if (!stop) {
			move();
		}
		g.drawAnimation(animation, x, y);
	}
}
