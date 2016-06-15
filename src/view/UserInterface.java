package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

import model.Board;

public class UserInterface extends JPanel {

	public boolean guessMade = false;

	final static int windowHeight;
	final static int windowWidth;

	final static int boardWidth;
	final static int boardHeight;

	final static int startWidth;
	final static int startHeight;	

	final static int tileSize;

	private final static Table<Color> colorSet;
	private Table<Color> code;

	private static final Color RED = new Color(198,0,0);
	private static final Color BLUE = new Color(11,0,169);
	private static final Color GREEN = new Color(0,186,12);
	private static final Color MAGENTA = new Color(248,9,255);
	private static final Color GOLD = new Color(228, 194, 25);

	static {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		windowWidth = (int) (gd.getDefaultConfiguration().getBounds().getWidth()/2);
		windowHeight = (int) (19*gd.getDefaultConfiguration().getBounds().getHeight()/20);

		boardWidth = 4*windowWidth/5;
		boardHeight = windowHeight;		

		startWidth = 0;
		startHeight = 0;

		tileSize = 60;

		colorSet = new Table<Color>(6);
		colorSet.insert(RED, 0);
		colorSet.insert(BLUE, 1);
		colorSet.insert(GREEN, 2);
		colorSet.insert(MAGENTA, 3);
		colorSet.insert(GOLD, 4);
		colorSet.insert(Color.BLACK, 5);
	}

	public static Color RED(){
		return RED;
	}
	public static Color BLUE(){
		return BLUE;
	}
	public static Color GREEN(){
		return GREEN;
	}
	public static Color MAGENTA(){
		return MAGENTA;
	}
	public static Color GOLD(){
		return GOLD;
	}


	public UserInterface(){
		this.reset();
	}

	/**
	 * Resets the game choosing a random color code
	 */
	public void reset(){
		Random codeGen = new Random();
		code = new Table<Color>(6);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 0);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 1);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 2);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 3);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 4);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 5);
	}	

	/**
	 * Triggers UI update
	 */
	public void update(){
		super.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBackground(g);
		drawBoard(g);
		drawRows(g);
		drawNodes(g);		
		drawPieces(g);
		drawState(g);

		int num = model.Board.actual().guessNum();
		if (guessMade == true){
			num--;
		}

		/*
		 * Draw result of the current (and previous) guess
		 */
		for (int i = 0; i < num; i++){
			int[] score = drawScore(i);
			drawGuess(g, i, score);
			if (score[0] == 6) drawWin(g);
		}
		
		//Draws the actual code if all guesses have been used
		if (model.Board.actual().guessNum() == 12) drawAns(g);
	}

	//Draws the background
	private void drawBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(0,0,windowWidth,windowHeight);			
	}

	//Draws the board
	private void drawBoard(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;										
		g2d.setColor(new Color(43,64,104));
		g2d.fillRoundRect(startWidth, startHeight + 20, boardWidth, boardHeight - boardHeight/10, 25, 25);
	}	

	//Draws all of the rows on the board
	private void drawRows(Graphics g) {
		Color cream = new Color (255,129,55);
		g.setColor(cream);
		for (int i = boardHeight/15; i < 9*boardHeight/10; i += boardHeight/15){			
			g.fillRoundRect(10, i, 7*boardWidth/10, boardHeight/20, 15, 15);
			if (i == boardHeight/15) g.setColor(Color.DARK_GRAY);
		}
	}

	//Draws all of the color nodes on the board
	private void drawNodes(Graphics g) {
		for (model.Node n : model.Board.getNodes()){
			g.setColor(n.getColor());
			g.fillOval(n.getX() - 14, n.getY() - (boardHeight/20)/2, 28, 28);
		}
	}

	//Draws the guessed colors
	private void drawPieces(Graphics g) {		
		for (int x = 0; x < model.Board.actual().getNumPieces(); x++){			
			g.setColor(model.Board.actual().getPiece(x).getColor());
			g.fillOval(model.Board.actual().getPiece(x).getScreenX() - 14, model.Board.actual().getPiece(x).getScreenY() - (boardHeight/20)/2, 28, 28);
		}
	}

	//Displays the number of guesses used
	private void drawState(Graphics g){
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesNewRoman", Font.BOLD, 24));
		g.drawString("Guess: " + model.Board.actual().guessNum(), boardWidth + 4, boardHeight/10);
	}

	//Reveals the color code
	private void drawAns(Graphics g){
		for (int x = 72; x < 78; x++){			
			g.setColor(code.colorAt(x % 6));
			model.Piece pc = new model.Piece(model.Board.getNode(x).getX(),model.Board.getNode(x).getY(), x, code.colorAt(x % 6));
			g.fillOval(pc.getScreenX() - 14, pc.getScreenY() - (boardHeight/20)/2, 28, 28);
		}
	}

	//Displays a string indicating a win
	private void drawWin(Graphics g){
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesNewRoman", Font.BOLD, 25));
		g.drawString("You Win!!", boardWidth + 5, boardHeight/5);
	}

	//Computes the result of the current guess
	private int[] drawScore(int guess){

		int yes = 0, pos = 0;
		Table<Color> row = new Table<Color>(6);

		for (int x = guess * 6; x < guess*6 + 6; x++){
			if (Board.actual().getPiece(x) == null) return null;
			Color c = Board.actual().getPieceOrdered(x).getColor();
			row.insert(c, x % 6);
		}

		int[] matches = new int[6];
		for (int i = 0; i < 6; i++){			
			if (code.colorAt(i).equals(row.colorAt(i))){
				matches[colorSet.get(code.colorAt(i))]++;
				yes++;
			}
		}

		for (Color c1 : code.keys()){										
			if (code.count(c1) - row.count(c1) <= 0){
				pos += (code.count(c1) - matches[colorSet.get(c1)]);
			} else if (row.count(c1) < code.count(c1)){
				pos += (row.count(c1) - matches[colorSet.get(c1)]);
			}
		}
		return new int[]{yes,pos};
	}

	//Draws the result of the current guess
	private void drawGuess(Graphics g, int guess, int[] matches){

		if (matches == null) return;

		int right = matches[0];
		int pos = matches[1];

		for (int x = 0; x < 2; x++){
			for (int y = 0; y < 3; y++){				
				if (right > 0) {
					g.setColor(Color.RED);
					right--; 
				} else if (right <= 0 && pos > 0){
					g.setColor(Color.WHITE);
					pos--;
				} else {
					g.setColor(Color.BLACK);
				}				
				g.fillOval(3*windowWidth/5 + y*10, (13-guess)*windowHeight/15 + x*10, 10, 10);
			}
		}

	}

}

class Table<Key>{

	final int maxSize;

	private ArrayList<Key> keys = new ArrayList<Key>();
	private int[] position;
	private int[] count;	

	public Table(int size){
		this.maxSize = size;
		count = new int[maxSize];
		position = new int[maxSize];
	}

	public void insert(Key key, int val){
		if(!keys.contains(key)) keys.add(key);		
		position[val] = keys.indexOf(key);
		count[keys.indexOf(key)]++;
	}

	public int get(Key key){
		return keys.indexOf(key);		
	}

	public Key colorAt(int pos){
		return keys.get(position[pos]);
	}

	public int count(Key key){
		if (!keys.contains(key)) return 0;
		else return count[keys.indexOf(key)];
	}

	public Iterable<Key> keys(){
		return keys;
	}

	public int size(){
		return keys.size();
	}

}