package sixmensmorris;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Defines a view for the menu screen.
 * This is the first GUI that appears that the user can interact with.
 * @author Kelvin Lin, Hasan Siddiqui, Jeremy Klotz
 * @version 1
 */
public class MenuView extends Screen{	
	
	private JLabel title;
	
	// User will click one of these two buttons to choose a state to enter.
	// Either debugging state, then go to play state, OR go straight to the play state.
	private JButton playGame, playGameAI;
	private JButton debug;
	private JButton loadGame;
	private int state;
	private int defaultFontSize = 36;
	private int defaultScreenWidth = 500;
	private int N; //Total number of pieces on the board
		
	//Used to store data about the game
	private int[] boardState;
	private int turn;
	private int gameState;
	private boolean removePiece;
	private boolean ExistsAI;
	private int AI_COLOR;
	
	/**
	 * Method to construct the output that the controller will handle everything inside of it.
	 * @param N	The number of pieces on the board
	 */
	public MenuView(int N){
		
		this.N = N;
		
		title = new JLabel("Six Men's Morris"); // title 
		
		playGame = new JButton("Play Game");
		playGame.addMouseListener(new MouseAdapter(){
			/**
			 * Determine if the playGame button was pressed.
			 */
			public void mouseClicked(MouseEvent e){
				playGameMouseClicked(e);
			}
		});
		
		playGameAI = new JButton("Play Game with Computer");
		playGameAI.addMouseListener(new MouseAdapter(){
			/**
			 * Determine if the playGame button was pressed.
			 */
			public void mouseClicked(MouseEvent e){
				playGameAIMouseClicked(e);
			}
		});
		
		debug = new JButton("Debug");
		
		/**
		 * Determine if the Debug button was pressed
		 */
		debug.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				debugMouseClicked(e);
			}
		});	 	
		
		loadGame = new JButton("Load Game");
		loadGame.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				loadGameMouseClicked(e);
			}
		});
		
		/* The next chunk of code Formats the menu screen to look like
		 *   		Six Men's Morris title
		 *   
		 *   		Play Game button 
		 *   
		 *   		Play Game with AI button
		 *   
		 *   		Debug button 
		 *   */
		Box box = Box.createVerticalBox();
		box.add(new JLabel(" "));
		box.add(new JLabel(" "));
		box.add(title);
		box.add(new JLabel(" "));
		box.add(new JLabel(" "));
		box.add(playGame);
		box.add(new JLabel(" "));
		box.add(new JLabel(" "));
		box.add(playGameAI);
		box.add(new JLabel(" "));
		box.add(new JLabel(" "));
		box.add(debug);
		box.add(new JLabel(" "));
		box.add(new JLabel(" "));
		box.add(loadGame);
		this.add(box);
	}
	
	/**
	 * Return the state of the application
	 * @return The state of the application
	 */
	public int getState(){
		return this.state;
	}
	
	/**
	 * If the mouse was clicked on the play game button, call the board controller and display the regular game on screen.
	 * @param e The MouseEvent
	 */
	private void playGameMouseClicked(MouseEvent e){
		BoardController boardController = new BoardController(N);
		boardController.setVisible(true);
		SwingUtilities.getWindowAncestor(this).dispose();
	}
	
	/**
	 * If the mouse was clicked on the play game with computer button, call the board controller and display the regular game on screen with AI enabled.
	 * @param e The MouseEvent
	 */
	private void playGameAIMouseClicked(MouseEvent e){
		BoardController boardController = new BoardController(N, true);
		boardController.setVisible(true);
		SwingUtilities.getWindowAncestor(this).dispose();
	}	
	
	/**
	 * If the mouse was click on the debug button, call the debug controller and display the debugging section on the screen.
	 * @param e The MouseEvent
	 */
	private void debugMouseClicked(MouseEvent e){
		DebugController debugController = new DebugController(N);
		debugController.setVisible(true);
		SwingUtilities.getWindowAncestor(this).dispose();
	}
	
	private void loadGameMouseClicked(MouseEvent e){
		try {
			FileReader fr = new FileReader("./savedGame.txt");
			BufferedReader br = new BufferedReader(fr);
			ArrayList<Integer> boardState = new ArrayList<Integer>();
			String stringRead = br.readLine();
			if(stringRead != null){
				gameState = Integer.parseInt(stringRead);
				turn = Integer.parseInt(br.readLine());
				removePiece = Boolean.parseBoolean(br.readLine());
				ExistsAI = Boolean.parseBoolean(br.readLine());
				AI_COLOR = Integer.parseInt(br.readLine());
				stringRead = br.readLine();
				while(stringRead != null){
					boardState.add(Integer.parseInt(stringRead));
					stringRead = br.readLine();
				}
				br.close();
				
				this.boardState = new int[boardState.size()];
				for(int i = 0; i < boardState.size(); i++){
					this.boardState[i] = boardState.get(i);
				}
				BoardController boardController = new BoardController(N, this.boardState, this.turn, this.gameState, this.removePiece);
				if (ExistsAI) {
					boardController.initAI(AI_COLOR);
				}
				boardController.setVisible(true);
				SwingUtilities.getWindowAncestor(this).dispose();
			} else{
				BoardController boardController = new BoardController(N);
				boardController.setVisible(true);
				SwingUtilities.getWindowAncestor(this).dispose();
			}
		} catch (NumberFormatException | IOException e1) {
			new ErrorDialog(new JFrame(), "Corrupt save file.", "An error occured. Please go save the game again.");
			BoardController boardController = new BoardController(N);
			boardController.setVisible(true);
			SwingUtilities.getWindowAncestor(this).dispose();
		}
	}
	
	/**
	 * This method formats all of the required components to the menu screen.
	 * @param g The Graphics object 
	 */
	private void draw(Graphics g) {
		// declare the font and pass it through to each component
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, this.getWidth()*defaultFontSize/defaultScreenWidth);
		title.setFont(font);
		
		title.setFont(font);
		playGame.setFont(font);
		playGameAI.setFont(font);
		debug.setFont(font);
		loadGame.setFont(font);
		//align components to center of the screen
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT); 
		playGame.setAlignmentX(JButton.CENTER_ALIGNMENT);
		playGameAI.setAlignmentX(JButton.CENTER_ALIGNMENT);
		debug.setAlignmentX(JButton.CENTER_ALIGNMENT);
		loadGame.setAlignmentX(JButton.CENTER_ALIGNMENT);
	}
	
	/**
	 * Updates the screen
	 */
	public void updateScreen(){
		this.invalidate();
		this.repaint();
	}
	
	@Override
	/**
	 * Draws the required components onto the screen.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.draw(g);
	}

}
