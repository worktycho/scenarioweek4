package project.main;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
		//PolyGuardFile file_2 = new PolyGuardFile("guards.txt");
		Museum museum = file.getMuseums().get(0);
		//System.out.println(Functions.hasLineOfSight(new Point2D.Double(4.9, -1.5), new Point2D.Double(3, -0.3), museum.points));
		//Guard guard = new Guard(museum.getPoints().get(0).getX(), -museum.getPoints().get(0).getY(), museum);
		//Guard guard = new Guard(5.0, 2.0, museum);
		//Guard guard = new Guard(4.95, 1.8, museum);		
		//Guard guard = new Guard(2, 0.3, museum);
		Guard guard = new Guard(0.4, 0.1, museum);
		museum.setLayout(new GridBagLayout());
		museum.add(guard);		
		//museum.run();
		for(Museum m : file.getMuseums()){
			//m.run();
		}
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
