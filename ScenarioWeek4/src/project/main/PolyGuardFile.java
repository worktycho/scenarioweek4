package project.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import project.visualization.Guard;
import project.visualization.Museum;

public class PolyGuardFile {

	private List<Museum> museums;
	
	public PolyGuardFile(String filename){
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
			String[] part = scanner.nextLine().split(";");
			Museum museum = readMuseum(part[0]);
			List<Guard> guards = readGuards(part[1], museum);
			for(Guard guard : guards){
				museum.add(guard);
			}
			museums.add(museum);
		}
		scanner.close();
		return museums;
	}
	
	private static Museum readMuseum(String line){
		line = line
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
	
	private static List<Guard> readGuards(String line, Museum museum){
		line = line
				.replace("(", "")
				.replace(")", "")
				.replace(",", "");
		Scanner lineScanner = new Scanner(line);
		List<Guard> guards = new ArrayList<Guard>();
		while(lineScanner.hasNext()){
			guards.add(new Guard(lineScanner.nextDouble(), lineScanner.nextDouble(), museum));
		}
		lineScanner.close();
		return guards;
	}
}
