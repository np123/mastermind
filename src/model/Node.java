package model;

import java.awt.Color;
import java.awt.Point;


public class Node {

	private final int x;
	private final int y;	
	private final int position;
	private Color colour = Color.LIGHT_GRAY;
	
	public Node(int x, int y, int position){
		this.x = x;
		this.y = y;
		this.position = position;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}	
	
	public int getPosition(){
		return this.position;
	}
	
	public void setColor(Color c){
		this.colour = c;
	}
	
	public Color getColor(){
		return this.colour;
	}
	
	public int distanceTo(Point p){
		return (int) Math.sqrt(Math.pow(this.getX() - p.getX(), 2) + Math.pow(this.getY() - p.getY(), 2));
	}
}
