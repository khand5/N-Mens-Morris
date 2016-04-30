package sixmensmorris;
/**
 * This class models a player.
 * Each player is determined by two integers. 
 * An integer that represents their color , and the number of pieces they have left to place.
 * @author Kelvin Lin, Jeremy Klotz
 * @version 1
 */
public class Player {
	
	// each player is determined by two integers.
	// An integer that represents their color , and the number of pieces they have left to place.
	private final int COLOR; 
	private int numberOfUnplayedPieces;
	
	/**
	 * Constructor method that takes in two parameters, color and number of unplayed pieces.
	 * @param color The colour of the piece
	 * @param numberOfUnplayedPieces The number of unplayed pieces
	 */
	public Player(int color, int numberOfUnplayedPieces){
		this.COLOR = color;
		this.numberOfUnplayedPieces = numberOfUnplayedPieces;
	}
	
	/** 
	 * 	Return the color of the player.
	 * @return The color of the player.
	 */
	public int getColor(){
		return this.COLOR;
	}
	
	/**
	 * Returns the number of pieces the player has yet to place.
	 * @return The number of pieces the player has yet to place.
	 */
	public int getNumberOfUnplayedPieces(){
		return this.numberOfUnplayedPieces;
	}
	
	/**
	 * Models the action of the user playing a piece on the board.
	 */
	public void placePiece(){
		this.numberOfUnplayedPieces--;
	}
	
}
