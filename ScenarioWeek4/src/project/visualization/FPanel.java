package project.visualization;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import project.main.Main;
public class FPanel extends JPanel {

	protected int w = 400;
	protected int h = 300; 
	public static double scale = 50;
	//private Rectangle2D[] rects = new Rectangle2D[50];
	
	public FPanel() {
	    // mouse listener to detect scrollwheel events
	    addMouseWheelListener(new MouseWheelListener() {
	        @Override
	        public void mouseWheelMoved(MouseWheelEvent e) {
	        	scale -= (double)e.getWheelRotation();
	    		scale = (scale < 0) ? 0 : (scale > 130) ? 130 : scale;	   
	    		updatePreferredSize();
	            repaint();
	            System.out.println("Scale: " + scale);
	        }
	        
	        
	    });
	    addMouseMotionListener(new MouseMotionListener(){
	    	
			@Override
			public void mouseDragged(MouseEvent me) {
				Component c = me.getComponent();
				me.translatePoint(c.getLocation().x, c.getLocation().y);
				setLocation(me.getX() - Main.DIMENSION.width/2, me.getY() - Main.DIMENSION.height/2);
				//setLocation(me.getX() - c.getWidth()/2, me.getY() - c.getHeight()/2);
			}

			@Override
			public void mouseMoved(MouseEvent me) {
				
			}
	    	
	    });
	    
	}
	
	private void updatePreferredSize(){
		Dimension dimension = new Dimension(9000, 9000);
		setSize(dimension);
		setMinimumSize(dimension);
		setPreferredSize(dimension);
		for(Component c : getComponents()){
			c.setSize(dimension);
			c.setMinimumSize(dimension);
			c.setPreferredSize(dimension);
		}
		
	}
	
	
}