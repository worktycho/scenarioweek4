package project.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import project.visualization.Museum;

public class PolygonFile {

	private List<Museum> museums;
	
	public PolygonFile(String filename){
		File file = new File(getClass().getResource(filename).getPath());
		try {
			this.museums = readFile(file);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}		
	}
	
	public List<Museum> getMuseums(){
		return museums;
	}
	
	private static List<Museum> readFile(File file) throws FileNotFoundException{
		Scanner scanner = new Scanner(file);
		List<Museum> museums = new ArrayList<Museum>();
		while(scanner.hasNextLine()){
			museums.add(readMuseum(scanner));
		}
		scanner.close();
		return museums;
	}
	
	private static Museum readMuseum(Scanner scanner){
		String line = scanner.nextLine()
				.replace(":", "")
				.replace("(", "")
				.replace(")", "")
				.replace(",", "");
		//System.out.println(line);
		Scanner lineScanner = new Scanner(line);
		int id = lineScanner.nextInt();		
		List<Double> x = new ArrayList<Double>();
		List<Double> y = new ArrayList<Double>();
		while(lineScanner.hasNext()){
			x.add(lineScanner.nextDouble());
			y.add(-lineScanner.nextDouble());
		}
		lineScanner.close();
		return new Museum(id, x, y);
	}
}
