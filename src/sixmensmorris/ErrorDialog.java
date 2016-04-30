package sixmensmorris;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

/**
 * Defines a dialog box to display error messages to the user. Contains an access program to respond to the user’s input.
 * @author Kelvin Lin, Jeremy Klotz
 * @version 1
 */
public class ErrorDialog extends JDialog implements ActionListener{
	
	/**
	 * Initializes a dialog to display any errors found during the execution of the application
	 * @param parent The current JFrame / window the application is running in.
	 * @param title The title of the window. 
	 * @param message An error message to display to the user
	 */
	public ErrorDialog(JFrame parent, String title, String message){
		super(parent, title, true);
		this.setLocation(parent.getX() + parent.getWidth()/ 4, parent.getY() + parent.getHeight() / 4);		
		this.setResizable(false);
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 25);//this.getWidth() * FONT_SIZE / this.DEFAULT_SCREEN_WIDTH);
		
		JLabel error = new JLabel("<html><b>Important Message!</b></html>"); // error message that will occur
		error.setFont(font); // set the font to predetermined font above.
		getContentPane().add(error, BorderLayout.NORTH); 
		JButton okButton = new JButton("OK"); // User will click this button after placing all desired pieces 
		okButton.addActionListener(this); // Have the application be able to tell when it has been click and respond to click
		okButton.setFont(font); // give proper font
		getContentPane().add(okButton, BorderLayout.SOUTH);
		
		JTextPane errorMessages = new JTextPane(); // create a text component
		errorMessages.setContentType("text/html"); // make the error message read text in HTML format
		errorMessages.setText(message);			   // display appropriate message based on what user did wrong.
		errorMessages.setEditable(false);		   // this message can't be edited
		
		errorMessages.setFont(font);				// use predetermined font as declared above
		getContentPane().add(errorMessages);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // get rid of error information on closing the window
		
		this.pack();
		 
		this.setVisible(true);							// make the error visible
	}
	
	/**
	 * Responds to the user's input
	 */
	public void actionPerformed(ActionEvent e){
		this.setVisible(false); // Makes the dialog invisible
		this.dispose();			//Closes the dialog
	}
	
}
