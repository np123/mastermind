package model;

import java.awt.Color;
import java.util.ArrayList;

public class Board {

	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private ArrayList<Node> nodeList = new ArrayList<Node>();
	private static Board actualBoard;
	private int guessNumber = 0;
	private Color selectedColor;
	
	public Board(int boardWidth, int boardHeight, boolean actual){
		if (!actual) return;
		actualBoard = this;
		for (int y = 13*boardHeight/15; y > boardHeight/15; y -= boardHeight/15){
			for (int x = 30; x < 7*boardWidth/10; x+= boardWidth/9){
				nodeList.add(new Node(x + 14, y + 14, x % (boardWidth/9)));
			}			
		}
	}

	public static Board actual(){
		return actualBoard;
	}
	
	public static Color getColor() {
		return actualBoard.selectedColor;
	}

	public static void setColor(Color selectedColor) {
		actualBoard.selectedColor = selectedColor;
	}

	public void addPiece(int x, int y, int pos, Color c){
		pieces.add(new Piece(x, y, pos, c));
	}
	
	public Piece getPiece(int x){
		if (pieces == null || pieces.isEmpty() || x >= pieces.size()) return null;
		return pieces.get(x);
	}
	
	public int getNumPieces(){		
		return pieces.size();
	}
	
	public static ArrayList<Node> getNodes(){		
		return actualBoard.nodeList;
	}
	
	public static Node getNode(int pos){
		return actualBoard.nodeList.get(pos);
	}
	
	public int guessNum(){
		return guessNumber;
	}
	
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
