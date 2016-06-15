package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import model.Board;
import view.UserInterface;

/**
 * The GraphicsController class communicates with the user and
 * reacts to input by calling appropriate methods in the
 * model and view packages
 *
 */
public class GraphicsController extends MouseAdapter{	
	
	private view.UserInterface UI;
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private JButton submit;
	private JButton newGame;
	
	/**
	 * Instantiates a GraphicsController and configures the window
	 */
	public GraphicsController(){
		
		//Sets window height and width based on device graphics settings
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		int width = (int) (gd.getDefaultConfiguration().getBounds().getWidth()/2);
		int height = (int) (19*gd.getDefaultConfiguration().getBounds().getHeight()/20);
		
		new model.Board(4*width/5, height, true);
		
		view.Layout layout = new view.Layout(width, height);
		this.UI = new view.UserInterface();
		UI.setLayout(layout);		
		
		/*
		 * Creating the buttons used to control game actions
		 */
		
		JButton red = new JButton();
		red.setBackground(UserInterface.RED());		
		buttons.add(red);
		
		JButton blue = new JButton();
		blue.setBackground(UserInterface.BLUE());
		buttons.add(blue);
		
		JButton green = new JButton();
		green.setBackground(UserInterface.GREEN());
		buttons.add(green);
		
		JButton magenta = new JButton();
		magenta.setBackground(UserInterface.MAGENTA());
		buttons.add(magenta);
		
		JButton gold = new JButton();
		gold.setBackground(UserInterface.GOLD());
		buttons.add(gold);
		
		JButton black = new JButton();
		black.setBackground(Color.BLACK);
		buttons.add(black);		
		
		submit = new JButton();
		submit.setText("Check");
		submit.setPreferredSize(new Dimension(75,25));
		submit.addMouseListener(this);
		
		newGame = new JButton();
		newGame.setText("New Game");
		newGame.setPreferredSize(new Dimension(100,25));
		newGame.addMouseListener(this);
					
		UI.add(newGame, "New Game");
		for (JButton button : buttons){
			button.setPreferredSize(new Dimension(25,25));
			button.addMouseListener(this);
			UI.add(button,button.getText());
		}	
		UI.add(submit, "Check");	
		
		
		//Creates new JFrame and sets state to visible
		JFrame window = new JFrame();
		window.setSize(width, height);
		window.add(UI);
		UI.addMouseListener(this);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setAlwaysOnTop(true);
	}

	
	/**
	 * Determines if mouse was clicked while overtop a node
	 * Triggers update of the user interface
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		/* 
		 * Map the x and y coordinate of the click to the node
		 * Ignore if there is no node
		 * Otherwise process the move
		 * 
		 * */
				
		//Color selection
		if (buttons.contains(e.getSource())){
			model.Board.setColor(((JButton)e.getSource()).getBackground());
			return;
		}
		
		//Create new game
		if (e.getSource() instanceof JButton && ((JButton)e.getSource()).getText().equals("New Game")){
			model.Board.actual().reset();
			submit.setVisible(true);
			UI.reset();
			UI.revalidate();
			UI.update();
			return;
		}
		
		//Submit guess
		if (e.getSource() instanceof JButton && ((JButton)e.getSource()).getText().equals("Check")){
			if (Board.actual().makeGuess()){
				UI.guessMade = true;
				Board.setColor(Color.LIGHT_GRAY);
			}			
			UI.revalidate();
			UI.update();
			UI.guessMade = false;
			
			if (Board.actual().guessNum() >= 12) {
				submit.setVisible(false);
				UI.revalidate();
				UI.update();
			}
			return;
		}
		
		/*
		 * Check whether a Node was clicked, and if so, fill with selected colour
		 */
		
		Point clicked = new Point(e.getX(), e.getY());
		int position = -1;
		
		for (int x = model.Board.actual().guessNum() * 6; x < model.Board.actual().guessNum() * 6 + 6; x++){
			if (model.Board.getNode(x).distanceTo(clicked) < 25){
				position = x;	
				break;
			}
		}
		
		//If no colour selected or click is outside of nodes, do nothin
		if (position == -1 || model.Board.getColor() == Color.LIGHT_GRAY) return;
		
		//Update board to reflect the guess in progress
		Color color = model.Board.getColor();
		model.Board.actual().addPiece(model.Board.getNode(position).getX(),model.Board.getNode(position).getY(), position, color); 
		UI.update();
		
	}
}
