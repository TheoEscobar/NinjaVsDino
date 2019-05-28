package principal;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class explosion {
	public Animation boom = new Animation();
	public SpriteSheet sheet;
	public float posX;
	public float posY;
	
	public explosion(float posX,float posY) {
		try {
			this.posX=posX;
			this.posY=posY;
			Image imgBoom = new Image("sprites/videBoom.png");
			sheet = new SpriteSheet("sprites/boom.png",341,180);
			for(int y=0;y<2;y++) {
				for(int x=0;x<3;x++) {
					boom.addFrame(sheet.getSprite(x, y), 100);
				}
			}
			boom.addFrame(imgBoom, 1);
			boom.setLooping(false);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void draw(Graphics g) {
		g.drawAnimation(boom, posX-141, posY-60);
	}
}
