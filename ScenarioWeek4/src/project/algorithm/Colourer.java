package project.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.util.Equal;

public class Colourer {

	private List<double[][]> noColour;
	private List<double[][]> oneColour;
	private List<double[][]> twoColours;
	private List<double[][]> allColours;
	private List<double[]> a;
	private List<double[]> b;
	private List<double[]> c;
	private List<Character> abcFlag;		
	
	public Colourer(){
		
	}
	
	private int checkCommonPoints(double[][] triangle) {
		int flag = 0;
		for (int j = 0; j < 3; j++) {  			 // for each coordinate in triangles
			if (Equal.contains(a, triangle[j])) {
				flag = flag + 1;
			}
			if (Equal.contains(b, triangle[j])) {
				flag = flag + 1;
			}
			if (Equal.contains(c, triangle[j])) {
				flag = flag + 1;
			}
		}
		//System.out.println("Flag: " + flag);
		return flag;
	}
	
	private void checkAllTriangles(double[][][] triangles) {
		for (int i = 0; i< triangles.length; i++) {
			int commonPoints = checkCommonPoints(triangles[i]);
			if (commonPoints == 0) {
				noColour.add(triangles[i]);
				//System.out.println("noColour: " + i);
			}
			else if (commonPoints == 1) {
				oneColour.add(triangles[i]);
				//System.out.println("oneColour: " + i);
			}
			else if (commonPoints == 2) {
				twoColours.add(triangles[i]);
				//System.out.println("twoColour: " + i);
			}
			else if (commonPoints == 3){
				allColours.add(triangles[i]);
				//System.out.println("allColour: " + i);
			}
		}
		/*try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}	
	
	private void executeTriangle(double[][] triangle) {
		//System.out.println("Triangle: " + Arrays.toString(triangle[0]) + " " + 
				//Arrays.toString(triangle[1]) + " " + Arrays.toString(triangle[2]));
		if (!allColours.contains(triangle)) {
			//System.out.println("begins executeTr");
			for (int p = 0; p < a.size(); p++) {
				//System.out.println(a.get(p));
				//System.out.println(Arrays.deepToString(a.get(p)));
			}
			//System.out.println(Arrays.deepToString(a));
			for (int i = 0; i < 3; i++) {
				abcFlag.set(i, ' ');
			}
			for (int j = 0; j < 3; j++) {
					for (int n = 0; n < a.size(); n++) {						
						if(Equal.equal(a.get(n)[0], triangle[j][0]) && Equal.equal(a.get(n)[1], triangle[j][1])) { 
							abcFlag.set(j, 'a');
							//System.out.println("Adding a");
							break;
						}
					}
					for (int k = 0; k < b.size(); k++) {
						if(Equal.equal(b.get(k)[0], triangle[j][0]) && Equal.equal(b.get(k)[1], triangle[j][1])) { 
							abcFlag.set(j, 'b');
							//System.out.println("Adding b");
							break;
						}
					}
					for (int m = 0; m < c.size(); m++) {
						if(Equal.equal(c.get(m)[0], triangle[j][0]) && Equal.equal(c.get(m)[1], triangle[j][1])) { 
							abcFlag.set(j, 'c');
							//System.out.println("Adding c");
							break;
						}
					}
				}
			System.out.println("abcFlag Start " + abcFlag);
			if (!abcFlag.contains('a')) {
				int indexFlag = abcFlag.indexOf(' ');
				//System.out.println("Indexflag: " + indexFlag);
				abcFlag.set(indexFlag, 'a');
				a.add(triangle[indexFlag]);
				System.out.println("abcFlag " + abcFlag);
			}
			if (!abcFlag.contains('b')) {
				int indexFlag = abcFlag.indexOf(' ');
				abcFlag.set(indexFlag, 'b');
				b.add(triangle[indexFlag]);
				System.out.println("abcFlag " + abcFlag);
			}
			if (!abcFlag.contains('c')) {
				int indexFlag = abcFlag.indexOf(' ');
				abcFlag.set(indexFlag, 'c');
				c.add(triangle[indexFlag]);
				System.out.println("abcFlag " + abcFlag);
			}
			System.out.println("abcFlag End " + abcFlag);
		}
	}
	
	private void findNextTriangle(double[][][] museum) {
		if (twoColours.size() > 0) {
			executeTriangle(twoColours.get(0));
		}
		else if (oneColour.size() > 0) {
			executeTriangle(oneColour.get(0));
		}
		else if (noColour.size() > 0) {
			executeTriangle(noColour.get(0));
		}
		noColour.clear();
		oneColour.clear();
		twoColours.clear();
		allColours.clear();
		checkAllTriangles(museum); // for museum excl. allColours
	}

	public void colour(double[][][] museum) {
		this.noColour = new ArrayList<double[][]>(Arrays.asList(museum));
		this.oneColour = new ArrayList<double[][]>();
		this.twoColours = new ArrayList<double[][]>();
		this.allColours = new ArrayList<double[][]>();
		this.a = new ArrayList<double[]>();
		this.b = new ArrayList<double[]>();
		this.c = new ArrayList<double[]>();
		this.abcFlag = new ArrayList<Character>();	
		abcFlag.add(' ');
		abcFlag.add(' ');
		abcFlag.add(' ');		
		while (allColours.size() < museum.length) {
			findNextTriangle(museum);
			//System.out.println("Size: " + allColours.size());
		}
		//System.out.println("Finished");
		printCoords();
	}
	
	private void printCoords() {
		List<double[]> shortest = a;		
		if (shortest.size() > b.size()) {
			shortest = b;
		}
		if (shortest.size() > c.size()) {
			shortest = c;
		}
		String str = "";
		for(double[] coords : shortest){
			str += "(";
			for (double xOrY : coords) {
				str += xOrY;
				str += ",";
			}
			str = str.substring(0, str.length() - 1);
			str += "), ";
			
		}
		str = str.substring(0, str.length() - 2);
		System.out.println("Guards: " + shortest.size());
		System.out.println(str);
	}
}
