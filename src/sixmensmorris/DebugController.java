package sixmensmorris;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

/**
 * Creates the window and all labels or buttons needed to access and update the view which in turn will change values in the model.
 * @author Kelvin Lin, Jeremy Klotz
 * @version 1
 */
public class DebugController extends JFrame {
	
	// instantiate types of variables that will be used
	private JFrame jFrame;			
	private BoardView boardView;
	
	// instantiate number of pieces per player, if this was 9 men's morris, it would change to 9 etc.
	private final int NUMBER_OF_PIECES = 6;

	// Blue state is index 1, Red is 2
	private final int BLUE_STATE = 1;
	private final int RED_STATE = 2;
	
	// Declare default window size and font size
	private final int FONT_SIZE = 25;

	private final int DEFAULT_SCREEN_WIDTH = 500;
	private final int DEFAULT_SCREEN_HEIGHT = 500;
	
	// use a series of JRadio buttons and put them into a button group so that only one button can
	// be pressed at a time
	private JRadioButton blue, red, black;
	private ButtonGroup buttonGroup;
	
	// create a regular button that will change the application state to the regular gameplay
	// only will make transition if no errors occur
	private JButton playGame;

	private int N; //Total number of pieces on the board
	
	/**
	 * Construct the state based on the number of layers.
	 * @param N Is the number of layers / rectangles.
	 */
	public DebugController(int N) {

		this.N = N;
		
		// Instantiate the main window of the debugging section, with window name, size and close operation
		jFrame = new JFrame("Six Men's Morris");
		jFrame.setSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Box box = Box.createHorizontalBox();
		// create default font and make it scalable to window size
		Font font = new Font(Font.MONOSPACED, Font.PLAIN,
				this.jFrame.getWidth() * FONT_SIZE / this.DEFAULT_SCREEN_WIDTH);

		// place 3 Jradio buttons for red, blue, and black into a box for a section of the window.
		// when red is clicked, whatever space the user clicks will place a red circle, same for blue and black.
		Box buttonBox = Box.createVerticalBox();
		blue = new JRadioButton("Blue", true);
		blue.setFont(font);
		blue.setForeground(Color.BLUE);
		red = new JRadioButton("Red", false);
		red.setFont(font);
		red.setForeground(Color.RED);
		black = new JRadioButton("Black", false);
		black.setFont(font);
		buttonBox.add(blue);
		buttonBox.add(red);
		buttonBox.add(black);
		// group the JRadio buttons together
		buttonGroup = new ButtonGroup();
		buttonGroup.add(blue);
		buttonGroup.add(red);
		buttonGroup.add(black);
		// create the Play game button
		playGame = new JButton("Play Game");
		playGame.setFont(font);
		buttonBox.add(playGame);
		// add the box of buttons to the entire box of the window
		box.add(buttonBox);
		
		
		playGame.addMouseListener(new MouseAdapter(){
			/**
			 * Checks to see if the play game button was clicked.
			 */
			public void mouseClicked(MouseEvent e){
				playGameMouseClicked(e);
			}
		});	 
		
		// create a regular board view and add it to the box that the button box was added to
		boardView = new BoardView(N);
		box.add(boardView);
		// add the big box to the window
		jFrame.add(box);
		// make the window visible
		jFrame.setVisible(true);

		boardView.addComponentListener(new ComponentAdapter() {
			/**
			 * Check to see if components need to resized, if so resize them based on window dimensions.
			 * Components are the board, buttons and each label associated to the buttons. 
			 */
			public void componentResized(ComponentEvent e) {
				updateView();
				resizeText();
			}
		});
		
		
		// add a mouse listener to the actual board in the window. 
		boardView.addMouseListener(new MouseClickEventHandler());
	}
	
	/**
	 * This method performs when the play game button is clicked. Checks to see if the user set up a possible board.
	 * If so, the actual game will start while keeping all pieces on the board. 
	 * @param e Is the mouse being clicked.
	 */
	private void playGameMouseClicked(MouseEvent e){
		
		if(boardIsLegal()){
			BoardController boardController = new BoardController(N, boardView.getBoardStates());
			boardController.setVisible(true);
			jFrame.dispose();
		}
	}
	
	/**
	 * Return a boolean value that determines if the board is of legal creation by the user.
	 * @return A boolean value that determines if the board is of legal creation by the user. 
	 * If the board isn't legal, create an object of type error dialog which in turn will place an error message to the view.
	 * TRUE == Legal 
	 */
	private boolean boardIsLegal(){
		String errorMessage = "<html>";
		errorMessage += checkNumbers();
		if(!errorMessage.equals("<html>")){
			new ErrorDialog(jFrame, "Error", errorMessage+"</html>");
			return false;
		}
		return true;
	}
	
	/**
	 * This method checks the debugging board to see how many pieces of each colour are present. 
	 * If an error has occurred , a proper message is added to the message passed through the error dialog
	 * If nothing is added to the message, then no error will appear and the application will transition to the play game state.
	 * @return A proper error message based on type of error. If there is no error, nothing is added to the message.
	 * 
	 */
	private String checkNumbers(){
		String errorMessage = "";
		int[] states = boardView.getBoardStates();
		int redCount = 0;
		int blueCount = 0;
		for(int i = 0; i < states.length; i++){
			if(states[i] == BLUE_STATE){
				blueCount++;
			} else if (states[i] == RED_STATE){
				redCount++;
			}
		}
		
		if(blueCount > NUMBER_OF_PIECES){
			errorMessage += "There are more than the allowed number of blue pieces.<br>";
		}
		
		if(redCount > NUMBER_OF_PIECES){
			errorMessage += "There are more than the allowed number of red pieces.<br>";
		}
		
		if(blueCount <= 1 && redCount >= 3){
			errorMessage += "The red player has too many pieces<br>";
		} else if(redCount <= 1 && blueCount >= 3){
			errorMessage += "The blue player has too many pieces<br>";
		}
		return errorMessage;
	}
	
	/**
	 * Adjusts the font for all text involved, making it able to be resized  based on the window size. 
	 */
	private void resizeText() {
		Font font = new Font(Font.MONOSPACED, Font.PLAIN,
				this.jFrame.getWidth() * FONT_SIZE / this.DEFAULT_SCREEN_WIDTH);
		blue.setFont(font);
		red.setFont(font);
		black.setFont(font);
		playGame.setFont(font);
	}
	
	/**
	 * Updates the view if and where it is needed.
	 */
	private void updateView() {
		boardView.invalidate();
		boardView.repaint();
	}
	
	/**
	 * Handles mouse click events within the Debug state.
	 * @author Kelvin Lin, Jeremy Klotz
	 * @version 1
	 */
	private class MouseClickEventHandler implements MouseListener {
		
		/**
		 * If the mouse is clicked on a board node, and one of the JRadio buttons (red, blue or black) is selected. 
		 * The method will place the color corresponding to the JRadio button into the clicked node of the board.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			//instantiate points based on where the mouse is pointing to
			//create an array of circles from the board view
			Point point = new Point(e.getPoint().getX(), e.getPoint().getY());
			Circle[] circles = boardView.getCircles();
			// for loop that iterates through array of  board circles
			for(int i = 0; i < circles.length; i++){
				// if the mouse is clicked over a circle in the board view and a color is selected
				// put that color in place of the current board circle.
				if(circles[i].isMouseOver(point)){
					if(blue.isSelected()){
						boardView.setBoardState(i, BLUE_STATE);
					} else if(red.isSelected()){
						boardView.setBoardState(i, RED_STATE);
					} else if(black.isSelected()){
						boardView.setBoardState(i, 0);
					}
				}
			}
			updateView(); // updates the view if anything has changed.
		}
		// the following methods are left empty but are still here incase we decide to detect other mouse operations.
		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}

}