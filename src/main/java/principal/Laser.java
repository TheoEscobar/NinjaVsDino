package principal;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Laser {

	public int posY;
	public int posX;
	public SpriteSheet img;
	public Animation anim;
	private mainState main;
	
	public Laser(int posY,int posX,mainState main) {
		try {
			this.main=main;
			this.img = new SpriteSheet("sprites/feu.png",60,50);
			this.anim=new Animation();
			this.anim.addFrame(img.getSprite(0, 0), 10);
			this.anim.addFrame(img.getSprite(1, 0), 10);
			this.posX=posX;
			this.posY=posY;
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		g.drawAnimation(anim, posX, 260+200*posY);
	}
	
	public void avance() {
		posX+=1;
		if(posX>=1000) {
			mort();
		}
	}
	
	public void mort() {
		if(main.lasers.get(posY).contains(this)) {
			main.lasers.get(posY).remove(this);
		}
	}
}
