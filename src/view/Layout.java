package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.LinkedList;

public class Layout implements LayoutManager2 {

	protected static LinkedList<Component> buttons = new LinkedList<Component>();
	protected static LinkedList<Dimension> position = new LinkedList<Dimension>();
	public static int numComponents = 0;

	final int windowHeight;
	final int windowWidth;

	public Layout(int width, int height){
		windowWidth = width;
		windowHeight = height;
	}	

	@Override
	public void addLayoutComponent(String name, Component comp) {
		buttons.add(comp);
		position.add(comp.getPreferredSize());
		numComponents++;
	}

	@Override
	public void layoutContainer(Container parent) {		

		for (int i = 1; i < buttons.size() - 1; i++){
			Component current = buttons.get(i);
			Dimension dim = position.get(i);			

			current.setBounds(9*windowWidth/10 + dim.width/3 - (i % 2) * 50, windowHeight/2 + (i % 3 - 1) * dim.height*2, dim.width,dim.height);
		}
		
		Component guess = buttons.getLast();
		Dimension size = guess.getPreferredSize();
		guess.setBounds(4*windowWidth/5 - 6*size.width/5, windowHeight - (windowHeight * (model.Board.actual().guessNum() + 2)/15), size.width, size.height);

		Component newGame = buttons.getFirst();
		Dimension sz = newGame.getPreferredSize();
		newGame.setBounds(8*windowWidth/10 + sz.width/9, windowHeight/2 + 4 * sz.height, sz.width, sz.height);
		
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {		
		return new Dimension(windowWidth,windowHeight);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		buttons.remove(comp);

	}

	@Override
	public void addLayoutComponent(Component comp, Object arg1) {
		if (arg1 instanceof String){
			addLayoutComponent((String)arg1, comp);
		}
	}

	@Override
	public float getLayoutAlignmentX(Container arg0) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container arg0) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container arg0) {}

	@Override
	public Dimension maximumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

}
