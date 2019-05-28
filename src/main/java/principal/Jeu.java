package principal;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;



public class Jeu extends StateBasedGame{
	
	public Jeu(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args){
		try {
			new AppGameContainer(new Jeu("Jeu"), 1000, 800, false).start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new StartMenuState());
		addState(new mainState());
	}
}
