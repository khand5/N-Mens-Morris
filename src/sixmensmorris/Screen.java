package sixmensmorris;
import javax.swing.JPanel;

/**
 * Declares a function that will be used in classes that extend from it.
 * @author Kelvin Lin , Jeremy Klotz
 * @version 1
 */
public abstract class Screen extends JPanel {
	public abstract void updateScreen(); //Updates the screen
}
