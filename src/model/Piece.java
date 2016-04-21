package model;

import java.awt.Color;

public class Piece {

	private int x;
	private int y;
	private int position;
	private final Color color;
	
	public Piece (int x, int y, int position, Color color){
		this.x = x;
		this.y = y;
		this.position = position;
		this.color = color;
	}
	
	public int getX(){
		return x;
	}
	
	public int getPosition(){
		return position;
	}		
	
	public int getY(){
		return y;
	}
	
	public Color getColor(){
		return color;
	}
	
	public int getScreenX(){
		int index = this.getPosition();
		return Board.getNode(index).getX();
	}
	
	public int getScreenY(){
		int index = this.getPosition();
		return Board.getNode(index).getY();
	}
	
	@Override
	public boolean equals(Object other){
		Piece pc = (Piece) other;
		return this.getPosition() == pc.getPosition();
	}

}
