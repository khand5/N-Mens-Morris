package sixmensmorris;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Creates the information that the controller will access in order to communicate to the user. The controller will call the view and this is what will draw the graphics to the application window.
 * @author Kelvin Lin, Jeremy Klotz
 * @version 1
 */
public class BoardView extends Screen{

	Board board;
	
	private int N; // Number of squares
	private int[] states; // array of integers that holds each state
	private final Color[] COLORS = { Color.BLACK, Color.BLUE, Color.RED }; // if a third colour was introduced, add it here

	private final double RECTANGLE_WIDTH_SCALING = 0.19; 													//Rectangle width scaling
	private final double RECTANGLE_HEIGHT_SCALING = 0.19; 													//Rectangle height scaling
	private final double CIRCLE_SCALING = 0.07; 															//Circle scaling
	private final double HORIZONTAL_LINE_SCALING = (this.CIRCLE_SCALING/this.RECTANGLE_WIDTH_SCALING)*0.9; 	//Horizontal line scaling
	private final double VERTICAL_LINE_SCALING = (this.CIRCLE_SCALING/this.RECTANGLE_HEIGHT_SCALING)*0.9;	//Vertical line scaling
	
	private Circle[] circles;		
	// array of circles , will be used multiple times
	
	/**
	 * Construct a board from the board model, where N is the number of pieces.
	 * @param N is the number of pieces.
	 */
	public BoardView(int N) {
		this.N = N;
		board = new Board(this.N);
		this.states = board.getBoardState();
		this.circles = new Circle[N*8]; // 8 nodes per square
	}
	
	/**
	 * Construct the board from the model using a current state.
	 * @param N				The number of pieces
	 * @param boardState	The state of the board
	 */
	public BoardView(int N, int[] boardState) {
		this.N = N;
		board = new Board(this.N, boardState);
		this.states = board.getBoardState();
		this.circles = new Circle[N*8];
	}
	
	/**
	 * This method will allow us to make sure the user can only place one piece per node on the board.
	 * @param number	The location of the piece
	 * @return a boolean value that determines if a piece is already placed in a location FALSE, a piece is already there.
	 */
	public boolean pieceNotTaken(int number){
		return states[number] == 0;
	}
	
	/**
	 * Return the state of the board (player Red or player Blue).
	 * @return the state of the board (player Red or player Blue).
	 */
	public int[] getBoardStates(){
		return this.states;
	}
	
	/**
	 * Set the state of the board.
	 * @param number is the specific index of the array of states
	 * @param state will be the previous state of the board and will be updated
	 */
	public void setBoardState(int number, int state){
		this.board.setPieceState(number, state);
		this.states = board.getBoardState();
		
	}
	/**
	 * Returns the current state of the board from accessing getPieceState. Number is the number used to index the array of states.
	 * @param number is the number used to index the array of states.
	 * @return The current state of the board from accessing getPieceState.
	 */
	public int getBoardState(int number){
		return this.board.getPieceState(number);
	}
	
	/**
	 * Return the array of all circles in the board
	 * @return the array of all circles in the board
	 */
	public Circle[] getCircles(){
		return this.circles;
	}
	
	/**
	 * Set the states of the game.
	 * @param states is the array of states (black, red, blue)
	 */
	public void setStates(int[] states){
		this.states = states;
	}
	
	/**
	 * Draw the entire board.
	 * 
	 * This method is used for consistency among other views.
	 * @param g is used to access and manipulate Java's awt library. 
	 */
	private void draw(Graphics g) {
		drawBoard(g, N);
	}
	
	/**
	 * Draws a black rectangle (layer / square)  that updates based on window size.
	 * @param g is used to access and manipulate Java's awt library.
	 * @param rect will take in an initialized rectangle and update it accordingly.
	 * @see Rectangle
	 */
	private void drawRectangle(Graphics g, Rectangle rect) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(COLORS[0]);
		g2d.drawRect(rect.getTopLeftIntX(), rect.getTopLeftIntY(), rect.getIntWidth(), rect.getIntHeight()); // access coordinates from rectangle ADT to draw.
	}
	
	/**
	 * Draws a coloured circle based on current state of the board.
	 * @param g is used to access Java's awt library.
	 * @param circle takes in an initialized circle.
	 * @param state is the current state of the board 0 = black, 1 = blue , 2 = red
	 */
	private void drawCircle(Graphics g, Circle circle, int state) {
		Graphics2D g2d = (Graphics2D) g;
		switch (this.states[state]) {
		case 0:
			g2d.setColor(COLORS[0]);
			break;
		case 1:
			g2d.setColor(COLORS[1]);
			break;
		case 2:
			g2d.setColor(COLORS[2]);
			break;
		default:
			g2d.setColor(Color.GREEN);
		}
		g2d.fillOval(circle.getIntPointX() - circle.getIntRadius(), circle.getIntPointY() - circle.getIntRadius(),
				circle.getIntDiameter(), circle.getIntDiameter());
	}
	
	/**
	 * Draw a line from point a to b.
	 * This will be used to connect layers.
	 * @param g is used to access Java's awt library.
	 * @param a is the starting point.
	 * @param b is the ending point.
	 * @see drawMiddleLines
	 */
	private void drawLine(Graphics g, Point a, Point b) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(COLORS[0]);
		g2d.drawLine(a.getIntX(), a.getIntY(), b.getIntX(), b.getIntY());
	}
	
	/**
	 * Draw all circles needed for the board.
	 * @param g is used to access Java's awt library.
	 * @param rect is the rectangle corresponding to the layer.
	 * @param layer in this assignment is the first or second rectangle(more layers can be added).
	 */
	private void drawBoardCircles(Graphics g, Rectangle rect, int layer) {
		double radius = rect.getIntHeight() * this.CIRCLE_SCALING * (layer+1);
		int startingNode = 8 * layer;

		Point topMiddle = new Point((rect.getTopLeft().getX() + rect.getTopRight().getX()) / 2,
				rect.getTopLeft().getY());
		Point leftMiddle = new Point(rect.getTopLeft().getX(),
				(rect.getTopLeft().getY() + rect.getBottomLeft().getY()) / 2);
		Point bottomMiddle = new Point((rect.getBottomLeft().getX() + rect.getBottomRight().getX()) / 2,
				rect.getBottomLeft().getY());
		Point rightMiddle = new Point(rect.getTopRight().getX(),
				(rect.getTopRight().getY() + rect.getBottomRight().getY()) / 2);
		
		// draw all eight circles needed for each layer (square) of the board
		// at each corner, and each midpoint
		drawCircle(g, circles[startingNode] = new Circle(topMiddle, radius), startingNode);
		drawCircle(g, circles[startingNode + 1] = new Circle(rect.getTopRight(), radius), startingNode + 1);
		drawCircle(g, circles[startingNode + 2] = new Circle(rightMiddle, radius), startingNode + 2);
		drawCircle(g, circles[startingNode + 3] = new Circle(rect.getBottomRight(), radius), startingNode + 3);
		drawCircle(g, circles[startingNode + 4] = new Circle(bottomMiddle, radius), startingNode + 4);
		drawCircle(g, circles[startingNode + 5] = new Circle(rect.getBottomLeft(), radius), startingNode + 5);
		drawCircle(g, circles[startingNode + 6] = new Circle(leftMiddle, radius), startingNode + 6);
		drawCircle(g, circles[startingNode + 7] = new Circle(rect.getTopLeft(), radius), startingNode + 7);

	}
	
	/**
	 * Connect the layers of the board together through the midpoints of inner layers.
	 * @param g is used to access Java's awt library.
	 * @param rect is used to access the midpoints from the ADT.
	 * @see drawLine
	 * @see Rectangle
	 */
	private void drawMiddleLines(Graphics g, Rectangle rect) {
		double diameterWidth = rect.getIntWidth() * this.HORIZONTAL_LINE_SCALING;  // design choice of scaling.
		double diameterHeight = rect.getIntHeight()* this.VERTICAL_LINE_SCALING; // design choice of scaling.
		
		Point topMiddle = new Point((rect.getTopLeft().getX() + rect.getTopRight().getX()) / 2,
				rect.getTopLeft().getY());
		Point leftMiddle = new Point(rect.getTopLeft().getX(),
				(rect.getTopLeft().getY() + rect.getBottomLeft().getY()) / 2);
		Point bottomMiddle = new Point((rect.getBottomLeft().getX() + rect.getBottomRight().getX()) / 2,
				rect.getBottomLeft().getY());
		Point rightMiddle = new Point(rect.getTopRight().getX(),
				(rect.getTopRight().getY() + rect.getBottomRight().getY()) / 2);

		drawLine(g, topMiddle, new Point(topMiddle.getX(), topMiddle.getY() + diameterHeight)); // connect the mid point of the top of inner layer
		drawLine(g, leftMiddle, new Point(leftMiddle.getX() + diameterWidth, leftMiddle.getY())); // connect mid point of the left side of inner layer
		drawLine(g, bottomMiddle, new Point(bottomMiddle.getX(), bottomMiddle.getY() - diameterHeight)); // connect mid point of the bottom of the inner layer.
		drawLine(g, rightMiddle, new Point(rightMiddle.getX() - diameterWidth, leftMiddle.getY())); // connect the mid point of the right side of the inner layer. 
	}

	/**
	 * Draw the entire board based on pre-determined scaling constants.
	 * @param g is used to access Java's awt library.
	 * @param N is the number of layers / rectangles.
	 * @see Rectangle
	 * @see Point
	 */
	private void drawBoard(Graphics g, int N) {
		double rectangleWidthScaling = this.RECTANGLE_WIDTH_SCALING * this.getWidth(); // Rectangle width scaling factor
		double rectangleHeightScaling = this.RECTANGLE_HEIGHT_SCALING * this.getHeight(); // Rectangle height scaling factor
		
		// return integer value of the pre-determined scale
		int rectangleWidthScalingInt = (int) (rectangleWidthScaling);
		int rectangleHeightScalingInt = (int) (rectangleHeightScaling);
		
		// create three points needed to construct the rectangle (top left , right and bottom right points)
		Point A = new Point(rectangleWidthScalingInt, rectangleHeightScalingInt);										//Top Right Corner
		Point B = new Point(this.getWidth() - rectangleWidthScalingInt, rectangleHeightScalingInt); 					//Top Left Corner
		Point C = new Point(this.getWidth() - rectangleWidthScalingInt, this.getHeight() - rectangleHeightScalingInt);  //Bottom Right Corner
		
		// for each layer, connect the middle nodes
		for (int i = 0; i < N; i++) {
			Rectangle rect = new Rectangle(A, B, C);
			if (i < N - 1) {
				drawMiddleLines(g, rect);
			}
			drawRectangle(g, rect);
			drawBoardCircles(g, rect, i);
			
			rectangleWidthScalingInt = (int) (rectangleWidthScalingInt + rectangleWidthScaling);
			rectangleHeightScalingInt = (int) (rectangleHeightScalingInt + rectangleHeightScaling);
			A = new Point(rectangleWidthScalingInt, rectangleHeightScalingInt);
			B = new Point(this.getWidth() - rectangleWidthScalingInt, rectangleHeightScalingInt);
			C = new Point(this.getWidth() - rectangleWidthScalingInt, this.getHeight() - rectangleHeightScalingInt);
		}

	}
	
	/**
	 * Updates the screen. If whole screen needs to be repained, repaint the whole thing. Otherwise, repaint the errors.
	 */
	public void updateScreen(){
		this.invalidate();
		this.repaint();
	}
	
	/**
	 * Return whether a mill exists on the Board model at index i
	 * @param i		The index i
	 * @return		Whether a mill exists at index i
	 */
	public boolean millExists(int i){
		int[] mill = board.millExists(i);
		return mill[0] != -1;
	}
	
	/**
	 * Returns whether there are only mills left for a given colour
	 * @param colour	The colour to check
	 * @return			Whether there are only mills left for the given colour
	 */
	public boolean existsOnlyMills(int colour){
		return board.onlyMillsLeft(colour);
	}
	
	/**
	 * Returns the winner of the board
	 * @return	1 if blue is the winner, 2 if red is the winner, 0 otherwise
	 */
	public int checkWinner(){
		return board.checkWinner()+1; //+1 accounts for different state repersentation in BoardController class
	}
	
	/**
	 * Returns the number of repeated moves from the board model
	 * @return		The number of repeated moves
	 */
	public int getRepeats(){
		return board.getRepeats();
	}
	
	@Override
	/**
	 * Draw sections of the board only when they need to be.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

}
