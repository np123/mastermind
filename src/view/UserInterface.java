package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
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

	static BufferedImage boardImage = null;
	static BufferedImage backImage = null;

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

		try {
			boardImage = ImageIO.read(new File("resources/texture3.bmp"));
			backImage = ImageIO.read(new File("resources/texture2.bmp"));
		} catch (IOException e) {			
			System.out.println("Error: Image files missing from resource");
			e.printStackTrace();
		}

		colorSet = new Table<Color>(6);
		colorSet.insert(Color.RED, 0);
		colorSet.insert(Color.BLUE, 1);
		colorSet.insert(Color.GREEN, 2);
		colorSet.insert(Color.MAGENTA, 3);
		colorSet.insert(Color.YELLOW, 4);
		colorSet.insert(Color.BLACK, 5);

		
		
	}

	public UserInterface(){
		Random codeGen = new Random();
		code = new Table<Color>(6);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 0);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 1);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 2);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 3);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 4);
		code.insert(colorSet.colorAt(codeGen.nextInt(6)), 5);	
	}
	

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

		int num = model.Board.actual().guessNum();
		if (guessMade == true){
			num--;
		}
		
		for (int i = 0; i < num; i++){
			drawGuess(g, i, drawScore(i));
		}
		if (model.Board.actual().guessNum() == 12) drawAns(g);
	}

	private void drawBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setPaint(new TexturePaint(backImage, new Rectangle(75,75)));		
		g2d.fillRect(0,0,windowWidth,windowHeight);		
	}

	private void drawBoard(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;								

		g2d.setPaint(new TexturePaint(boardImage, new Rectangle(tileSize,tileSize)));
		g2d.fillRect(startWidth, startHeight, boardWidth, boardHeight);
	}	

	private void drawRows(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Color cream = new Color (255,129,55);
		g2d.setColor(cream);
		for (int i = boardHeight/15; i < 9*boardHeight/10; i += boardHeight/15){
			g2d.fillRoundRect(10, i, 7*boardWidth/10, boardHeight/20, 15, 15);
		}
	}

	private void drawNodes(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;		

		for (model.Node n : model.Board.getNodes()){
			g2d.setColor(n.getColor());
			g2d.fillOval(n.getX() - 14, n.getY() - (boardHeight/20)/2, 28, 28);
		}
	}

	private void drawPieces(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (int x = 0; x < model.Board.actual().getNumPieces(); x++){			
			g2d.setColor(model.Board.actual().getPiece(x).getColor());
			g2d.fillOval(model.Board.actual().getPiece(x).getScreenX() - 14, model.Board.actual().getPiece(x).getScreenY() - (boardHeight/20)/2, 28, 28);
		}
	}

	private void drawAns(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		for (int x = 72; x < 78; x++){			
			g2d.setColor(code.colorAt(x % 6));
			model.Piece pc = new model.Piece(model.Board.getNode(x).getX(),model.Board.getNode(x).getY(), x, code.colorAt(x % 6));
			g2d.fillOval(pc.getScreenX() - 14, pc.getScreenY() - (boardHeight/20)/2, 28, 28);
		}
	}
	
	public int[] drawScore(int guess){

		int yes = 0, pos = 0;

		Table<Color> row = new Table<Color>(6);

		for (int x = guess * 6; x < guess*6 + 6; x++){
			if (Board.actual().getPiece(x) == null) return null;
			Color c = Board.actual().getPieceOrdered(x).getColor();			
			//System.out.println("Color : " + c + " position " + x % 6);
			row.insert(c, x % 6);
		}

		//System.out.println("Guess Colors");
		int[] matches = new int[6];
		for (int i = 0; i < 6; i++){			
			System.out.println("TEST" + row.colorAt(i).toString());
			if (code.colorAt(i).equals(row.colorAt(i))){
				matches[colorSet.get(code.colorAt(i))]++;
				yes++;
			}
		}
		//System.out.println("End Guess Colors");

		for (Color c1 : code.keys()){										
			//System.out.println(c1.toString());
			//System.out.print("match: " + matches[colorSet.get(c1)] + "  wrong spot");
			if (code.count(c1) - row.count(c1) <= 0){
				//System.out.println(code.count(c1) - matches[colorSet.get(c1)]);
				pos += (code.count(c1) - matches[colorSet.get(c1)]);
			} else if (row.count(c1) < code.count(c1)){
				pos += (row.count(c1) - matches[colorSet.get(c1)]);
			}
		}

		System.out.println("Number of matches: " + yes);
		System.out.println("Number in wrong spot: " + pos);

		return new int[]{yes,pos};
	}


	private void drawGuess(Graphics g, int guess, int[] matches){
		Graphics2D g2d = (Graphics2D) g;

		if (matches == null) return;

		int right = matches[0];
		int pos = matches[1];

		for (int x = 0; x < 2; x++){
			for (int y = 0; y < 3; y++){				
				if (right > 0) {
					g2d.setColor(Color.RED);
					right--; 
				} else if (right <= 0 && pos > 0){
					g2d.setColor(Color.WHITE);
					pos--;
				} else {
					g2d.setColor(Color.BLACK);
				}				
				g2d.fillOval(3*windowWidth/5 + y*10, (13-guess)*windowHeight/15 + x*10, 10, 10);
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