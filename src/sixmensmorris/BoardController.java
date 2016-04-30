package sixmensmorris;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * This is a controller for the board class. 
 * It acts as an intermediary between the Board model (Board.java), and the Board view (BoardView.java).
 * @author Kelvin Lin , Zichen Jiang, Jeremy Klotz
 * @version 1
 */
public class BoardController extends JFrame {
	private JFrame jFrame;
	private BoardView boardView;
	private int turn; // 0 = blue, 1 = red
	private Player blue, red;
	private boolean ExistsAI;
	private AI AI;

	private int state = 0; // 0 = place pieces, 1 = play game, 2 = blue wins, 3 = red wins, 4 = draw
	private String[] stateStrings = {"Placing Pieces", "Game in Progress", "Blue Wins", "Red Wins", "Game Drawn"};

	
	private final int NUMBER_OF_PIECES = 6;	// this can change to 9 if we are going to do 9 Men's Morris instead
	private final int BLUE_STATE = 1;
	private final int RED_STATE = 2;
	
	private int AI_TURN;
	private int AI_COLOUR;
	private int PLAYER_COLOUR;

	private final int FONT_SIZE = 25; // declaring a size for the font used in the application

	private final int DEFAULT_SCREEN_WIDTH = 500; // default width and height of screen (will scale if stretch/compress window)
	private final int DEFAULT_SCREEN_HEIGHT = 500;

	private int selectedColour = 0;	//Used to facilitate movement of pieces
	private int selectedPiece = -1; //Used to facilitate movement of pieces
	private JLabel blueLabel, blueCount, redLabel, redCount, title; // some labels to properly update the view
	
	private boolean removePiece = false; //Whether a piece can be removed from the board
	
	private JButton saveGame; //Save game button
	private JButton makeAIMove; // make AI move button
	
	private int maxNumberOfRepeats = 12; //Maximum number of repetitions
	
	/**
	 * Constructs the screen needed to play the game, and adds all EventListeners needed to obtain input from the user.
	 * @param N is the number of squares
	 */
	public BoardController(int N) {
		//Instantiate Random Turns
		Random random = new Random();
		turn = random.nextInt(2);

		// Instantiate Models
		blue = new Player(BLUE_STATE, NUMBER_OF_PIECES);
		red = new Player(RED_STATE, NUMBER_OF_PIECES);

		// Instantiate Views
		jFrame = new JFrame("Six Men's Morris");
		jFrame.setSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT); // (500,500)
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Box outerBox = Box.createVerticalBox();
		
		Font font = new Font(Font.MONOSPACED, Font.PLAIN,
				this.jFrame.getWidth() * FONT_SIZE / this.DEFAULT_SCREEN_WIDTH); // scale the font based on window size 
																				 // in case it is stretched/compressed
		
		//Create title JLabel and add it to the top of the screen
		title = new JLabel(stateStrings[state]);
		title.setFont(font);
		updateTitleColour();
		updateTitleText();
		outerBox.add(title);
		
		makeAIMove = new JButton("Make AI Move");
		makeAIMove.setFont(font);
		makeAIMove.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				makeAIMoveMouseClicked(e);
			}
		});
		makeAIMove.setVisible(false);
		
		updateAIButton();
		
		outerBox.add(makeAIMove);
		
		//Create horizontal components
		Box box = Box.createHorizontalBox(); // original box to contain all of the view's information
		Box blueVerticalBox = Box.createVerticalBox();
		Box redVerticalBox = Box.createVerticalBox();
		
		
		blueLabel = new JLabel("Blue:"); // use scalable font for all labels relevant to the blue player										 
		blueLabel.setFont(font);
		blueCount = new JLabel(String.valueOf(blue.getNumberOfUnplayedPieces())); // the label accesses the number of pieces left
																				  // and returns the integer value as a string for the label
		blueCount.setFont(font);		// use scalable font
		//create a sub box to be place in the window
		//add previously define labels to the sub box, and add this sub box to the original box
		blueVerticalBox.add(blueLabel);	
		blueVerticalBox.add(blueCount);
		box.add(blueVerticalBox);

		boardView = new BoardView(N); // call BoardView to display graphics
		box.add(boardView);		      // add graphics to the original box
		
		// creating the same labels needed for player blue, but this time for player red
		redLabel = new JLabel("Red:");
		redLabel.setFont(font);
		redCount = new JLabel(String.valueOf(red.getNumberOfUnplayedPieces()));
		redCount.setFont(font);
		
		redVerticalBox.add(redLabel);
		redVerticalBox.add(redCount);
		box.add(redVerticalBox);
		
		outerBox.add(box);
		
		saveGame = new JButton("Save Game");
		saveGame.setFont(font);
		saveGame.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				saveGameMouseClicked(e);
			}
		});
		
		outerBox.add(saveGame);
		
		jFrame.add(outerBox); // add the original box (everything) to the window
		jFrame.setVisible(true); // allows the window to display everything

		boardView.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				updateView();
				resizeText();
			}
		});

		boardView.addMouseListener(new MouseClickEventHandler());
		
	}
	
	/**
	 * BoardController constructor used for loading games.
	 * 
	 * It takes a boardState, a turn, and a state in addition to the number of layers available so that it can recreate the 
	 * previous state of the board.
	 * @param N				The number of pieces
	 * @param boardState	The state of the board
	 * @param turn			Which player's turn
	 * @param state			The game state
	 */
	public BoardController(int N, int[] boardState, int turn, int state, boolean removePiece){
		this(N, boardState);
		this.turn = turn;
		this.state = state;
		this.removePiece = removePiece;
		update(true);
	}
	
	/**
	 * Construct the screen needed to play the game given a certain state, and adds all EventListers needed to obtain input from the user.
	 * @author Kelvin Lin , Jeremy Klotz
	 * @param N is the number of squares 
	 * @param boardState this allows us to construct / update the board based on current state
	 */
	public BoardController(int N, int[] boardState) { 	
		this(N);

		for(int i = 0; i < boardState.length; i++){
			boardView.setBoardState(i, boardState[i]);
			if(boardState[i] == 1){
				blue.placePiece();
			} else if(boardState[i] == 2){
				red.placePiece();
			}
			
		}

		boardView.checkWinner();
	}
	
	/** 
	 * Initialize AI on new game
	 * @param N is the number of squares 
	 * @param ExistsAI is the boolean value to determine whether to enable AI
	 */	
	public BoardController(int N, boolean ExistsAI){
		this(N);
		if (ExistsAI) {
			this.initAI(-1);
		}
	}
	
	/**
	 * Initialize AI with given colour.
	 * If no colour given, randomly determin AI's colour.
	 * Also changes the text in makeAIMove button to tell user what colour the AI is.
	 * @param AI_colour predefined AI's colour, 1 for blue and 2 for red. -1 if undefined
	 */
	public void initAI(int AI_colour) {
//		System.out.println("enable AI");
		this.ExistsAI = true;

		// assign AI a colour, and the corresponding turn
		if (AI_colour < 0) {
			Random random = new Random();
			AI_TURN = random.nextInt(2);
			AI_COLOUR = AI_TURN + 1;
			PLAYER_COLOUR = (AI_COLOUR == 1) ? 2 : 1;
		}else {
			AI_TURN = AI_colour - 1;
			AI_COLOUR = AI_colour;
			PLAYER_COLOUR = (AI_COLOUR == 1) ? 2 : 1;
		}
		this.AI = new AI(this.boardView, AI_COLOUR, PLAYER_COLOUR);
		
//		System.out.println("AI colour:" + AI_COLOUR);
//		System.out.println("AI_TURN: " + AI_TURN);
//		System.out.println("Human colour:" + PLAYER_COLOUR);
		
		makeAIMove.setText("Make AI (" + (AI_COLOUR==1 ? "Blue" : "Red") + ") Move");
		makeAIMove.setForeground(AI_COLOUR==1 ? Color.BLUE : Color.RED);
		makeAIMove.setVisible(true);
		this.updateAIButton();
	}
	
	
	/**
	 * Updates the display.
	 * 
	 * @param updateState will update the game state if true
	 */
	private void update(boolean updateState){
		if(updateState){
			this.updateState();
		}
		if (ExistsAI) {
			this.updateAI();
		}
		this.updateLabels();
		this.updateTitleColour();
		this.updateTitleText();
		this.updateAIButton();
		this.updateView();
		
	}
	
	/**
	 * It is used to simply resize the text based on the dimensions of the window
	 * This allows for dynamic change, such that the user can play with any size of window
	 */
	private void resizeText() {
		Font font = new Font(Font.MONOSPACED, Font.PLAIN,
				this.jFrame.getWidth() * FONT_SIZE / this.DEFAULT_SCREEN_WIDTH);
		blueLabel.setFont(font);
		blueCount.setFont(font);
		redLabel.setFont(font);
		redCount.setFont(font);
		title.setFont(font);
		saveGame.setFont(font);
	}
	
	/**
	 * This method will update the labels of each player involved.
	 */
	private void updateLabels() {
		blueCount.setText(String.valueOf(blue.getNumberOfUnplayedPieces()));
		redCount.setText(String.valueOf(red.getNumberOfUnplayedPieces()));
		updateTitleColour();
		updateTitleText();
		this.updateAIButton();
	}
	
	/**
	 * Controller takes information from the view and calls methods from the java.awt library on it
	 */
	private void updateView() {
		boardView.invalidate(); // invalidates the board
		boardView.repaint(); 	// repaints the visual component of the view.
	}
	
	/**
	 * This method updates the title JLabel's colour
	 */
	private void updateTitleColour(){
		if(this.turn == 0){
			title.setForeground(Color.BLUE);
		} else{
			title.setForeground(Color.RED);
		}
	}
	
	/**
	 * This method updates the game state
	 * 
	 */
	private void updateState(){
		if(state == 0 && red.getNumberOfUnplayedPieces() == 0 && blue.getNumberOfUnplayedPieces() == 0){
			 this.state=1;
		} else if(state == 1 && boardView.getRepeats() > maxNumberOfRepeats){
			this.state = 4;
		} else if(state == 1 && boardView.checkWinner() != 1){
			this.state = boardView.checkWinner()==2?2:3;
		}
	}
	
	/**
	 * This method updates the title state according to the class.
	 * Also, if the state is not place pieces (state = 0), then it hides the blue and red piece counter labels.
	 */
	private void updateTitleText(){
		if(state != 0){
			this.blueCount.setVisible(false);
			this.blueLabel.setVisible(false);
			this.redLabel.setVisible(false);
			this.redCount.setVisible(false);
		}
		title.setText(this.stateStrings[this.state] + ((state <= 1)?(this.turn%2==0)?" (Blue Move)":" (Red Move)":""));		
	}
	
	/**
	 * Disable the makeAIMove button if on players turn.
	 * Enable the button if on AI's turn.
	 * 
	 */
	private void updateAIButton() {
		if (ExistsAI && turn == AI_TURN) {
			makeAIMove.setEnabled(true);
		}else{
			makeAIMove.setEnabled(false);
		}
	}
	
	/**
	 * Updates AI's board view.
	 */
	private void updateAI(){
		this.AI.updateBoardView(this.boardView);
	}
	
	/**
	 * This method encapsulates the place piece (state = 0) state.
	 * @param i		Where to place the piece
	 */
	private void placePieceState(int i){
		
		switch(turn%2){
		case 0:
				if(removePiece){
					removePiece(i);
				} else if(boardView.pieceNotTaken(i) && blue.getNumberOfUnplayedPieces() > 0){
					boardView.setBoardState(i, 1);
					blue.placePiece();
					if(boardView.millExists(i)){
						removePiece = true;
					} else{
						turn++; //Incrementing and decrementing turn at every subsequent turn ensures that there will never be overflow.
					}
				}
			break;
		case 1:
			if(removePiece){
				removePiece(i);
			} else if(boardView.pieceNotTaken(i) && boardView.pieceNotTaken(i)){
				if(red.getNumberOfUnplayedPieces() > 0){
					boardView.setBoardState(i, 2);
					red.placePiece();
					if(boardView.millExists(i)){
						removePiece = true;
					} else{
						turn--;
					}
				}
			}
			break;
		}
		update(true);
	}
	

	/**
	 * This method checks all the pieces of a certain colour on the board, and returns <code>true</code> if there are no possible
	 * moves to be played by the specified colour.
	 * @param colour	The colour to check
	 * @return			<code>true</code> if there are no possible moves, <code>false</code> otherwise.
	 */
	private boolean noPossibleMoves(int colour){
		for(int i = 0; i < boardView.getBoardStates().length; i++){
			if(boardView.getBoardState(i) == colour){
				for(int j = 0; j < boardView.getBoardStates().length; j++){
					if(j == i + 1 || j == i - 1 || j == i + 8 || j == i - 8 
					|| (j ==7 && i == 0) || (j==8 && i ==15) || (j ==0 && i == 7) || (j==15 && i ==8)){
						if(boardView.getBoardState(j) == 0){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	

	
	/**
	 * This method encapsulates the play game (state = 1) state.
	 * @param i		Where to move the piece
	 */
	private void movePiece(int i){

		//Move piece if legal
		if(noPossibleMoves(turn%2 + 1)){
			this.state = turn%2 == 0 ? 3 : 2;
		} else if(removePiece){
			removePiece(i);
		} else if(turn%2 == 0 && boardView.getBoardState(i) == 1 && selectedColour == 0){ //Select blue piece
			boardView.setBoardState(i, 0);
			selectedPiece = i; 
			selectedColour = 1;					
		} else if(turn%2 == 1 && boardView.getBoardState(i) == 2 && selectedColour == 0){ //Select red piece
			boardView.setBoardState(i, 0);
			selectedPiece = i;
			selectedColour = 2;
		} else if(selectedColour != 0 && boardView.getBoardState(i) == 0){ //Move the piece
			if(i == selectedPiece || i == selectedPiece + 1 || i == selectedPiece - 1 || i == selectedPiece + 8 || i == selectedPiece - 8 
					|| (i ==7 && selectedPiece == 0) || (i==8 && selectedPiece ==15) || (i ==0 && selectedPiece == 7) || (i==15 && selectedPiece ==8)){
				if(i != selectedPiece){ //If the piece is not placed back on the same square
					boardView.setBoardState(i, selectedColour);
					if(boardView.millExists(i)){
						removePiece = true;
					} else{
						selectedColour = 0;
						if(turn%2 == 0){
							turn++;
						} else{
							turn--;
						}
						selectedPiece = -1;
						
						// the turn ends
					}
				} else{
					boardView.setBoardState(i, selectedColour);
					selectedPiece = -1;
					selectedColour = 0;
				}
			} else{
				new ErrorDialog(jFrame, "Invalid move", "Please select a valid move");
			}
		}
	}
	
	/**
	 * This method allows the caller to remove a piece from the board
	 * @param i		Which piece to remove
	 */
	private void removePiece(int i){
		if(boardView.millExists(i) && !boardView.existsOnlyMills((turn+1)%2 +1)){
			new ErrorDialog(jFrame, "Invalid Move", "Please choose a piece not in a mill.");
		} else if(boardView.getBoardState(i) == 0 || boardView.getBoardState(i) == turn+1){
			new ErrorDialog(jFrame, "Invalid Move", "Please choose an opponent's piece to remove");
		} else{
			boardView.setBoardState(i, 0);
			removePiece = false;
			selectedColour = 0;
			update(true);
			if(turn%2 == 0){
				turn++;
			} else{
				turn--;
			}
			selectedPiece = -1;
			
			// end of turn
//			System.out.println("end of removePiece");
		}
	}
	
	/**
	 * @author Kelvin Lin
	 * @version 1:
	 * Controls what happens when a piece is clicked on the screen
	 */
	private class MouseClickEventHandler extends MouseAdapter {
		
		@Override
		/**
		 * This method allows for alternate colour pieces to be placed on the board after each click.
		 * @param e is the mouse being click
		 */
		public void mousePressed(MouseEvent e) {
//			System.out.println(!(ExistsAI && turn == AI_TURN));
			if (!(ExistsAI && turn == AI_TURN)) {
				Point point = new Point(e.getPoint().getX(), e.getPoint().getY()); // get the coordinates of the click
				Circle[] circles = boardView.getCircles(); // create an array that holds all circles from the board
				// iterate through the circle array
				// if the mouse is clicked on a point and is not occupied by another coloured circle
				// place a coloured circle based on the current state
				for(int i = 0; i < circles.length; i++){   
					if(circles[i].isMouseOver(point)){
						if(state == 0){
							placePieceState(i);
						} else if(state == 1){
							movePiece(i);
						}
					}
					update(boardView.getRepeats() > maxNumberOfRepeats);	
				}
			}
		}
		
	}


	/**
	 * This method processes the save game event.
	 * 
	 * It saves the state and turn into a text file called ./savedGame.txt. It then records the state of the board into the 
	 * text file
	 * @param e	The MouseEvent
	 */
	private void saveGameMouseClicked(MouseEvent e){
		try {
			FileWriter fw = new FileWriter("./savedGame.txt", false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(String.valueOf(this.state));
			bw.newLine();
			bw.write(String.valueOf(this.turn));
			bw.newLine();
			bw.write(String.valueOf(this.removePiece));
			bw.newLine();
			bw.write(String.valueOf(this.ExistsAI));
			bw.newLine();
			bw.write(String.valueOf(this.ExistsAI ? this.AI_COLOUR : -1));
			int[] board = this.boardView.getBoardStates();
			for(int i = 0; i < board.length; i++){
				bw.newLine();
				bw.write(String.valueOf(board[i]));
			}
			bw.close();
			new ErrorDialog(jFrame, "Saved.", "Your game has been saved.");
		} catch (IOException e1) {
			new ErrorDialog(jFrame, "Save Error.", "An error occured and your game was not saved.");
		}
	}
	
	/** This method process the AI move event.
	 * It will call different APIs from AI for different game state, 
	 * and use the returned values (positions) to place a piece, 
	 * move a piece, or remove a piece.
	 * @param e The MouseEvent
	 */
	private void makeAIMoveMouseClicked(MouseEvent e) {
		if (ExistsAI && turn == AI_TURN) {
			switch(state){
			case 0:
				// if need to remove piece during placing stage
				if (removePiece) {
					int move = AI.nextRemove();
//					System.out.println("removing: "+ move);
					if (move > -1)
						this.removePiece(move);
						update(true);
				} // if at the end of placing stage, red needs to move first
				else if (state == 1) {
					int[] move = AI.nextMove();
					if (move[0] > -1 && move[1] > -1) {
						movePiece(move[0]);
						movePiece(move[1]);
					}
				} // otherwise just place a piece
				else {
					int move = AI.nextPlace();
					if (move > -1)
						this.placePieceState(move);
				}
				break;
			case 1:
				if (ExistsAI && turn == AI_TURN) {
					int[] move = AI.nextMove();
					if (move[0] > -1 && move[1] > -1) {
						movePiece(move[0]);
						movePiece(move[1]);
						if (boardView.millExists(move[1])) {
							removePiece(AI.nextRemove());
						}
					}
					// if AI achieves a mill, let it remove a piece
					
				}
				break;
			default:
//				System.out.println("Something went wrong...");
			}
			update(boardView.getRepeats() > maxNumberOfRepeats);	
		}
	}
	
}