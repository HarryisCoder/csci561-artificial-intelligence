import java.util.*;
import java.io.*;

public class homework {

	public static boolean isValid(int i, int j, Set<Integer> rowSet, Set<Integer> colSet, Set<Integer> rDiagSet, Set<Integer> lDiagSet) {
		if (rowSet.contains(i) || colSet.contains(j) || rDiagSet.contains(i + j) || lDiagSet.contains(i - j)) {
			return false;
		}
		return true;
	}

	public static void printTable(int[][] table){
        for (int[] row : table) {
        	for (int element : row) {
        		System.out.print(element + " ");
        	}
        	System.out.println();
        }
	}

	public static void printResult(boolean result, int[][] table) throws IOException{
		FileWriter writer = new FileWriter("output.txt");  
    	BufferedWriter buffer = new BufferedWriter(writer); 
		if (result) {
			buffer.write("OK");
			buffer.newLine();
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < table.length; i++) {
			    for(int j = 0; j < table.length; j++) {
			      builder.append(table[i][j]);
			    }
			    if (i != table.length - 1) {
			    	//append new line at the end of the row
			    	builder.append("\n");
			    }
			}
			buffer.write(builder.toString()); 
		} else {
			buffer.write("FAIL");  
		}
		buffer.close();  
}

	public static Problem readFile(File fileName) throws IOException{

        Scanner in = new Scanner(fileName);
        String mode = in.next();
		int n = in.nextInt();
		int[][] table = new int[n][n];
		int p = in.nextInt();
		in.nextLine();
		for (int i = 0; i < n; i++) {
			String line = in.nextLine();
			for (int j = 0; j < n; j++) {
				table[i][j] = (int)(line.charAt(j) - '0');
			}
		}
		return new Problem(table, p, mode);
	}

	public static boolean iniCheck(int[][] table, int[] num, int numLiz) {
		if (table == null || table.length == 0 || table[0].length == 0) {
			System.out.println("Empty table detected!");
			return false;
		}
		int numTrees = 0;
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				if (table[i][j] == 2) {
					numTrees++;
				}
			}
		}
		int numRooms = table.length * table.length - numTrees;
		num[0] = numTrees;
		num[1] = numRooms;
		if (numRooms < numLiz) {
			System.out.println("Not enough rooms for lizards");
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
    	String fileName = "input.txt";
    	try {
	        File file = new File(fileName);
    		Problem problem = readFile(file);
    		int[][] table = problem.table;
    		String mode = problem.mode;
    		int numLiz = problem.numLiz;
    		int[] num = new int[2];
			boolean result = iniCheck(table, num, numLiz);
			if (!result) {
				printResult(result, table);
				return;
			}
			if (numLiz == 0) {
				printResult(result, table);
				return;
			}
			int numTrees = num[0];
			int numRooms = num[1];
			if (mode.equals("DFS")) {
				SolveNLizardsDFS solution = new SolveNLizardsDFS(table, numLiz, numTrees);
				result = solution.getResult();
			} else if (mode.equals("BFS")) {
				SolveNLizardsBFS solution = new SolveNLizardsBFS(table, numLiz, numTrees);
				result = solution.getResult();
			} else if (mode.equals("SA")) {
				SolveNLizardsSA solution = new SolveNLizardsSA(table, numLiz, numRooms, numTrees);
				result = solution.getResult();
			}
			printResult(result, table);
		}
		catch (FileNotFoundException exc) {
	        System.out.println("File not found: " + fileName);
	    }
	    catch (IOException exc) {
         exc.printStackTrace();
      }
	}
}

class Problem {
	int[][] table;
	int numLiz;
	String mode;
	Problem(int[][] table, int numLiz, String mode) {
		this.table = table;
		this.numLiz = numLiz;
		this.mode = mode;
	}
}