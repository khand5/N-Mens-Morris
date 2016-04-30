package sixmensmorris;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
/**
 * Defines a controller to mediate the views and models used in the menu.
 * @author Kelvin Lin, Jeremy Klotz
 * @version 1
 */
public class MenuController extends JFrame{
	private JFrame jFrame;
	private MenuView view;

	/**
	 * Method to construct the output that the controller will handle everything inside of. 
	 */
	public MenuController(){
		
		//Instantiate views
		jFrame = new JFrame("Six Men's Morris"); // name of window
		jFrame.setSize(500, 500); // set size of window to be 500 by 500 units long
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE); // close window by pressing the X button top right corner
		view = new MenuView(2);
		jFrame.add(view); // add the view to the window based on the state 
		jFrame.setVisible(true); // make window visible
	}
	
	/**
	 * Runs any operations associated with the controller.
	 */
	public void run(){
			view.addComponentListener(new ComponentAdapter(){
				public void componentResized(ComponentEvent e) {				
					view.updateScreen();
				}
			});

	}
	
	/**
	 * The getJFrame getter 
	 * @return The JFrame
	 */
	public JFrame getJFrame(){
		return this.jFrame;
	}

	
	
}
