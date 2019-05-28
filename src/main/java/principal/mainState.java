package principal;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class mainState extends BasicGameState {

	public static final int ID = 2;
	public HashMap<Integer, ArrayList<dinosaure>> dinos = new HashMap<Integer, ArrayList<dinosaure>>();
	public chrono time;
	public joueur perso;
	public HashMap<Integer, ArrayList<Laser>> lasers = new HashMap<Integer, ArrayList<Laser>>();
	private Image fond;
	private Sound sound;
	private Music background;
	private SpriteSheet sprites;
	private Animation fumee = new Animation();
	private Graphics g;
	private boolean clic = false;
	private explosion boom;
	private SpriteSheet death;
	private Sound chute;
	private Sound finChute;
	public int k;
	private int frameCount = 1000;
	private float deathTime;
	public SpriteSheet citron;
	private boolean first = true;
	private SpriteSheet GameOver;
	private Animation fin = new Animation();
	private boolean soundStop = false;
	private boolean soundStop2 = false;
	

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		time = new chrono();
		fond = new Image("Images/fond.png");
		background = new Music("musique/dk.ogg");
		sound = new Sound("musique/kaboom.wav");
		chute = new Sound("musique/chute.wav");
		finChute = new Sound("musique/finChute.wav");
		perso = new joueur();
		dinos.put(0, new ArrayList<dinosaure>());
		dinos.put(1, new ArrayList<dinosaure>());
		dinos.put(2, new ArrayList<dinosaure>());
		lasers.put(0, new ArrayList<Laser>());
		lasers.put(1, new ArrayList<Laser>());
		lasers.put(2, new ArrayList<Laser>());
		addDino();

		try {
			death = new SpriteSheet("sprites/citronMort.png", 150, 150);
			sprites = new SpriteSheet("sprites/fumee.png", 70, 140);
			citron = new SpriteSheet("sprites/citron.png", 150, 150);
			fumee.addFrame(new Image("sprites/vide.png"), 1);
			GameOver = new SpriteSheet("sprites/gameOver.png", 1000, 180);
			fin.addFrame(GameOver.getSprite(0, 6), 1000);
			for (int x = 0; x < 7; x++) {
				fin.addFrame(GameOver.getSprite(0, x), 100);
			}
			fin.setLooping(false);
			for (int x = 0; x < 4; x++) {
				fumee.addFrame(sprites.getSprite(x, 0), 50);
			}
			fumee.addFrame(new Image("sprites/vide.png"), 1);
			fumee.setLooping(false);
			fumee.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics graph){
		this.g = graph;
		g.drawImage(fond, 0, 0);
		if (!dinos.isEmpty()) {
			for (int i : dinos.keySet()) {
				for (int k = 0; k < dinos.get(i).size(); k++) {
					if (!dinos.get(i).isEmpty()) {
						dinos.get(i).get(k).draw(g);
						if (dinos.get(i).get(k).x <= 100 && perso.vie > 0) {
							dinos.get(i).remove(k);
							perso.vie -= 1;
						}
					}
				}
			}
		}
		if (!lasers.isEmpty()) {
			for (int i = 0; i < 3; i++) {
				if (!lasers.get(i).isEmpty()) {
					for (int k = 0; k < lasers.get(i).size(); k++) {
						lasers.get(i).get(k).draw(g);
						lasers.get(i).get(k).avance();
						for (int l = 0; l < dinos.get(i).size(); l++) {
							if (!lasers.get(i).isEmpty() && !dinos.get(i).isEmpty()) {
								if (dinos.get(i).get(l).x <= lasers.get(i).get(k).posX
										+ (lasers.get(i).get(k).img.getTextureWidth())
										&& dinos.get(i).get(l).ligne == lasers.get(i).get(k).posY
										&& lasers.get(i).get(k).posX < 1000
										&& lasers.get(i).get(k).posX <= dinos.get(i).get(l).x
												+ dinos.get(i).get(l).sprites.getTextureWidth()) {
									sound.stop();
									sound.play(1.0f, 0.4f);
									dinos.get(i).get(l).stop = true;
									boom = new explosion(dinos.get(i).get(l).x, dinos.get(i).get(l).y);
									boom.draw(graph);
									lasers.get(i).remove(k);
									dinos.get(i).get(l).mort();
								}
							}
						}
					}
				}
			}
		}
		if (boom != null) {
			boom.draw(graph);
		}
		if (clic && fumee.isStopped()) {
			clic = false;
			perso.changeLigne(k);
			addLaser(k);
		}
		int ligneCitr;
		int colCitr;
		if (perso.vie > 0) {
			ligneCitr = (10 - perso.vie) / 4;
			colCitr = (10 - perso.vie) % 4;
		} else {
			ligneCitr = 2;
			colCitr = 2;
		}

		if (perso.vie <= 0) {
				if (first) {
					first = false;
					deathTime = time.getDureeMs();				
				}
				if (0.5f * (time.getDureeMs() - deathTime) + perso.ninja.getHeight() < 230 + 200 * perso.ligne) {
					background.stop();
					perso.draw(g);
					if (!chute.playing()) {
						chute.play();
					}
					g.drawImage(death.getSprite(0, 0), 0, (0.5f * (time.getDureeMs() - deathTime)));
				} else {
					chute.stop();
					g.drawImage(death.getSprite(1, 0), 0, 230 + 200 * perso.ligne);
					if (!soundStop) {
						soundStop = true;
						finChute.play();
					}
					g.drawAnimation(fin, 0, 350);
					if (fin.getFrame() == 1 && !soundStop2) {
						soundStop2 = true;
						sound.play(1.0f, 0.6f);
					}
				}
		} else {
			perso.draw(g);
			g.drawAnimation(fumee, 42, 192 + 200 * perso.ligne);
			g.drawImage(citron.getSprite(colCitr, ligneCitr), 0, 0);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int arg) {
		if (perso.vie > 0) {
			if (frameCount >= 1000) {
				addDino();
				frameCount = 0;
			}
			else{
				frameCount++;
			}

		}
	}

	@Override
	public void keyPressed(int key, char c) {
		k = perso.ligne;
		switch (key) {
		case Input.KEY_UP:
			if (k - 1 >= 0) {
				k -= 1;
			} else {
				k = 0;
			}
			clic = true;
			break;
		case Input.KEY_DOWN:
			if (k + 1 < 3) {
				k += 1;
			} else {
				k = 3;
			}
			clic = true;
			break;
		case Input.KEY_1:
			k = 0;
			clic = true;
			break;
		case Input.KEY_2:
			k = 1;
			clic = true;
			break;

		case Input.KEY_3:
			k = 2;
			clic = true;
			break;
		}
		if (clic) {
			fumee.restart();
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		background.loop(1.0f, 0.4f);
		time.start();
	}

	@Override
	public int getID() {
		return ID;
	}

	private void addDino() {
		dinosaure d = new dinosaure(this);
		ArrayList<dinosaure> ar = dinos.get(d.ligne);
		ar.add(d);
		dinos.put(d.ligne, ar);
	}

	private void addLaser(int k) {
		Laser d = new Laser(k, 100, this);
		ArrayList<Laser> ar = lasers.get(k);
		ar.add(d);
		lasers.put(k, ar);
	}
}
