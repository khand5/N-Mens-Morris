package sixmensmorris;
/**
 * This class contains the main method, and all variables universal to the game.
 * @author Kelvin Lin
 *
 */
public class Game {

	/**
	 * The main method. This runs the program.
	 * @param args	The command line arguments
	 */
	public static void main(String[] args){
		MenuController menuController = new MenuController();
		menuController.setVisible(true);
	}
	
}
