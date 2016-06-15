package model;

import java.awt.Color;
import java.util.ArrayList;

public class Board {

	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private ArrayList<Node> nodeList = new ArrayList<Node>();
	private static Board actualBoard;
	private int guessNumber = 0;
	private static Color selectedColor;
	
	public Board(int boardWidth, int boardHeight, boolean actual){
		if (!actual) return;
		actualBoard = this;
		for (int y = 13*boardHeight/15; y > boardHeight/15; y -= boardHeight/15){
			for (int x = 30; x < 7*boardWidth/10; x+= boardWidth/9){
				nodeList.add(new Node(x + 14, y + 14, x % (boardWidth/9)));
			}			
		}
	}

	/**
	 * Distinguishing is used to allow creation of multiple boards
	 * Used to make adding AI/solver algorithm easier
	 * @return instance of the board
	 */
	public static Board actual(){
		return actualBoard;
	}
	
	/**
	 * Resets the state of the game
	 */
	public void reset(){
		guessNumber = 0;
		pieces = new ArrayList<Piece>();
		selectedColor = Color.LIGHT_GRAY;		
	}
	
	public static Color getColor() {
		return selectedColor;
	}

	public static void setColor(Color selectedColor) {
		Board.selectedColor = selectedColor;
	}

	public void addPiece(int x, int y, int pos, Color c){
		int index = pieces.lastIndexOf(new Piece(-1,-1,pos,null));
		if (index == -1) pieces.add(new Piece(x, y, pos, c));
		else pieces.set(index, new Piece(x,y,pos,c));
	}
	
	public Piece getPiece(int x){
		if (pieces == null || pieces.isEmpty() || x >= pieces.size()) return null;
		return pieces.get(x);
	}
	
	public Piece getPieceOrdered(int x){
		int index = pieces.indexOf(new Piece(-1,-1,x,null));
		if (index == -1) return null;
		else return pieces.get(index);
	}
	
	/**
	 * @return number of occupied Nodes on the board
	 */
	public int getNumPieces(){		
		return pieces.size();
	}
	
	public static ArrayList<Node> getNodes(){		
		return actualBoard.nodeList;
	}
	
	public static Node getNode(int pos){
		return actualBoard.nodeList.get(pos);
	}
	
	/**
	 * @return the number of guesses made
	 */
	public int guessNum(){
		return guessNumber;
	}
	
	/**
	 * @return value indicating whether submitted guess was valid
	 */
	public boolean makeGuess(){
		Color[] row = new Color[6];
		for (int x = guessNumber * 6; x < guessNumber * 6 + 6; x++){
			int pc = pieces.indexOf(new Piece(-1,-1,x,null));
			if (pc != -1) row[x % 6] = pieces.get(pc).getColor();
		}		
		for (Color c : row)
			if (c == null || c == Color.LIGHT_GRAY) return false;		
		guessNumber++;
		return true;
	}
	
}
