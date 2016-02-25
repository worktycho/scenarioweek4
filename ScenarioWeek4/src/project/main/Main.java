package project.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import project.visualization.FPanel;
import project.visualization.Functions;
import project.visualization.Guard;
import project.visualization.Museum;

public class Main {

	public static final Dimension DIMENSION = new Dimension(800, 600);
	
	public static void main(String[] arg){		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(DIMENSION);
		frame.setResizable(false);
		PolygonFile file = new PolygonFile("polygons.txt");
		Museum museum = file.getMuseums().get(4);
		//System.out.println(Functions.hasLineOfSight(new Point2D.Double(4.9, -1.5), new Point2D.Double(3, -0.3), museum.points));
		//Guard guard = new Guard(points.get(0).getX(), -points.get(0).getY(), this);
		//Guard guard = new Guard(2.0, 1.0, museum);
		//Guard guard = new Guard(4.95, 1.8, museum);
		//Guard guard = new Guard(4.9, 0.1, museum);
		//Guard guard = new Guard(0.4, 0.1, museum);
		Guard guard = new Guard(0.1, 0.1, museum);
		museum.setLayout(new GridBagLayout());
		//museum.add(guard);
		//museum.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel containerPanel = new JPanel();
		containerPanel.setMinimumSize(DIMENSION);
		containerPanel.setLayout(new GridLayout());
		containerPanel.add(museum);
		frame.setLayout(new GridLayout());
		frame.add(containerPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

	
	
	
}
