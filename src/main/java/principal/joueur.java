package principal;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class joueur {
	public int vie;
	public int ligne;
	public Image ninja;
	
	public joueur() {
		try {
			ninja = new Image("sprites/ninja.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.vie=10;
		this.ligne=1;
	}
	
	public void changeLigne(int k) {
		if (k<3 && k>=0) {this.ligne =k;}
	}
	
	public void draw(Graphics g) {	
		g.drawImage(ninja, 50, 230+200*ligne);
	}
}
