package project.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import project.visualization.Guard;
import project.visualization.Museum;

public class PolyGuardFile {

	private Map<Museum, List<Guard>> museums;
	
	public PolyGuardFile(String filename){
		File file = new File(getClass().getResource(filename).getPath());
		try {
			this.museums = readFile(file);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}		
	}
	
	public List<Museum> getMuseums(){
		return new ArrayList<Museum>(museums.keySet());
	}
	
	public List<Guard> getGuards(Museum museum){
		return museums.getOrDefault(museum, new ArrayList<Guard>());
	}
	
	private static Map<Museum, List<Guard>> readFile(File file) throws FileNotFoundException{
		Scanner scanner = new Scanner(file);
		Map<Museum, List<Guard>> museums = new HashMap<Museum, List<Guard>>();
		while(scanner.hasNextLine()){
			String[] part = scanner.nextLine().split("; ");
			Museum museum = readMuseum(part[0]);
			List<Guard> guards = readGuards(part[1], museum);
			museums.put(museum, guards);
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
