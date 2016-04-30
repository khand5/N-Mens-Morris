package sixmensmorris;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is the AI class for games with a computer/
 * The AI is implemented in a very basic way.
 * First, for placing stage, the AI will try to place pieces 
 * on the center parts of each line to be advantageous
 * Second, for the moving stage, the AI will move the first piece
 * available at a random available direction
 * Third, for removing pieces, the AI will remove the first available pieces
 *
 * @author Zichen Jiang
 * @version 1
 */


public class AI {
	private BoardView boardView;
	private int PLAYER_COLOR;
	private int AI_COLOR;
	
	/** Construct the AI class.
	 * 
	 * @param board boardView received from BoardController
	 * @ai colour of ai's piece
	 * @player colour of player's piece
	 * 
	 */
	public AI(BoardView board, int ai, int player) {
		this.boardView = board;
		PLAYER_COLOR = player;
		AI_COLOR = ai;
	}
	
	/**
	 * gives AI the most up-to-date board view
	 * 
	 * @param boardView board view class received from BoardController
	 */
	public void updateBoardView(BoardView boardView) {
		this.boardView = boardView;
	}
	
	/**
	 * Try to find the next available position to place a piece.
	 * Will try the middle points first, then the corner ones.
	 * 
	 * @return the position on the board to place the piece
	 */
	public int nextPlace() {
		int[] board = boardView.getBoardStates();
		
		for (int i=0; i<board.length; i+=2) {
			if (board[i] == 0) {
				return i;
			}
		}
		
		for (int i=1; i<board.length; i+=2) {
			if (board[i] == 0) {
				return i;
			}
		}

		return -1;
	}
	
	/**
	 * Find the next available piece from the player to remove.
	 * If only mill exist, remove a piece from the mill.
	 * Otherwise remove first piece encountered.
	 * 
	 * @return the position of the piece to remove
	 */
	public int nextRemove() {
		int[] board = this.boardView.getBoardStates();

		if (this.boardView.existsOnlyMills(1)) {
			for (int i=0; i<board.length; i++) {
				if (board[i] == PLAYER_COLOR) {
//					System.out.println("moving: "+ i);
					return i;
				}
			}
		}else {
			for (int i=0; i<board.length; i++) {
				if (board[i] == PLAYER_COLOR && !this.boardView.millExists(i)) {
//					System.out.println("moving: "+ i);
					return i;
				}
			}
		}

		return -1;
	}
	
	/**
	 * Try to find a piece available to move, and calculate where to move it.
	 * Will go through each piece and move the first available one at a random direction.
	 * 
	 * @return [a,b] - a: which piece to move; b: where to put it
	 */
	public int[] nextMove() {
		int[] board = this.boardView.getBoardStates();
		int prev, next; // prev and next are the ones on the same line/layer
		int out, in;	// up and down are the ones on the outter/lower layer
		int[] result = new int[] {-1,-1};
		
		for (int i=0; i<board.length; i++) {
			// get the position for the neighbor pieces for RED pieces
			if (board[i] == AI_COLOR) {
				// if it's even, need to check three directions
				if (i%2 == 0) {
					// if it's the top center one, need special case for left position
					if (i%8 == 0) { 
						prev = i+7;
						next = i+1;
					}else {
						prev = i-1;
						next = i+1;
					}
					
					out = i - 8 >= 0 ? i-8 : -1;
					in = i + 8 < board.length ? i + 8 : -1;
					
					int[] pos = new int[] {prev, next, out, in};
					ArrayList<Integer> validPos = new ArrayList<Integer>();
					
					for (int j=0; j<pos.length; j++){
						if (pos[j] > -1) { // if the position is valid, check if position is empty
							if (board[pos[j]] == 0) {
								validPos.add(pos[j]);
							}
						}
					}
					
					if (validPos.isEmpty()) {
						continue;
					}
					
					Random random = new Random();
					int target = random.nextInt(validPos.size());
					result[0] = i;
					result[1] = validPos.get(target);
					return result;					
					
				}else { // odd number, check two directions
					// if it's the top left one, need specia case for right position
					if ((i+1)%8 == 0) { 
						prev = i-1;
						next = i-7;
					}else {
						prev = i-1;
						next = i+1;
					}
//					System.out.println(prev + " ===== " + i + " ==== " + next);
					
					// if neither neighbour is empty, skip to the next piece
					if (board[prev]!=0 && board[next]!=0) {
						continue;
					}

					// if either of the neighbour ones is empty, place at the empty one
					else if (board[prev]!=0 ^ board[next]!=0) { 
						int place = board[prev] == 0 ? prev : next;
						result[0] = i;
						result[1] = place;
//						System.out.println("moving: "+ result[0] + "to: " + result[1]);
						return result;
					}

					// if both of the neighbours are empty, place at random
					else {
						Random random = new Random();
						int place = random.nextInt(2) == 1 ? prev : next;
						result[0] = i;
						result[1] = place;
//						System.out.println("moving: "+ result[0] + "to: " + result[1]);
						return result;
					}
				}
				
				
				
				
			}	

		}
		
		return result;
	}
}
