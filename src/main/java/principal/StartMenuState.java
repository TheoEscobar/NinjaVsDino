package principal;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartMenuState extends BasicGameState {

	public static final int ID = 1;
	private Image background;
	private StateBasedGame game;
	private Music sound;
	public SpriteSheet sprites;
	public Animation animation = new Animation();
	private Sound ninNin;
	public chrono time;
	private float loopTime;
	private boolean go = false;

	

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
		this.time = new chrono();
		this.loopTime = 0;
		this.background = new Image("Images/fond.png");
		;
		this.sound = new Music("musique/na.ogg");
		this.ninNin = new Sound("musique/nin-nin.wav");
		sprites = new SpriteSheet("sprites/Trex.png", 200, 120);
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {
				animation.addFrame(sprites.getSprite(x, y), 100);
			}
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		background.draw(0, 0);
		g.setColor(new Color(154, 44, 12));
		g.drawString("Appuyez sur une touche", 420, 400);
		g.drawAnimation(animation, 930 - 0.2f * (time.getDureeMs() - loopTime), 680);
		if (930 - 0.2f * (time.getDureeMs() - loopTime) < -200) {
			loopTime = time.getDureeMs();
		}
		if (!ninNin.playing() && go) {
			game.enterState(mainState.ID);
		}
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int key, char c) {
		if (!go) {
			sound.stop();
			ninNin.play();
			go = true;
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game){
		sound.loop();
		time.start();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		time.stop();
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
