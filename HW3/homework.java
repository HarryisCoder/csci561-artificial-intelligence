import java.util.*;
import java.io.*;

// main function of homework2

public class homework {

	public static Problem readInput(File fileName) throws IOException{
		Scanner input = new Scanner(fileName);
		List<Clause> queries = new ArrayList<>();
		Set<Clause> kb = new HashSet<>();
		// m queries and n clauses in KB
		int m = input.nextInt();
		// System.out.println("m: " + m);
		input.nextLine();
		for (int i = 0; i < m; i++) {
			String line = input.nextLine();
			// System.out.println("line: " + line);
			queries.add(Helper.string2clause(line));
		}
		int n = input.nextInt();
		input.nextLine();
		// System.out.println("n: " + n);
		for (int i = 0; i < n; i++) {
			String line = input.nextLine();
			// System.out.println("line: " + line);
			kb.add(Helper.string2clause(line));
		}
		return new Problem(queries, kb);
	}

	// public static void writeOutput(int[] nextMove, char[][] board) throws IOException{
	// 	FileWriter writer = new FileWriter("output.txt");  
 //    	BufferedWriter buffer = new BufferedWriter(writer); 
	// 	buffer.write((char)('A' + nextMove[1]));
	// 	buffer.write("" + (nextMove[0] + 1));
	// 	buffer.newLine();
	// 	StringBuilder builder = new StringBuilder();
	// 	int n = board.length;
	// 	for(int i = 0; i < n; i++) {
	// 	    for(int j = 0; j < n; j++) {
	// 	      builder.append(board[i][j]);
	// 	    }
	// 	    if (i != board.length - 1) {
	// 	    	//append new line at the end of the row
	// 	    	builder.append("\n");
	// 	    }
	// 	}
	// 	buffer.write(builder.toString()); 
	// 	buffer.close();  
	// }

	public static void main(String args[]) {
		// String fileName = "input.txt";
		// if (args.length < 1) {
			// System.out.println("Please provide filename.");
		// 	return;
		// }
		String fileName = "input.txt";
		try {
			File file = new File(fileName);
    		Problem problem = readInput(file);
    		// System.out.println("queries num: " + problem.queries.size());
    		// System.out.println(problem.queries);
    		// System.out.println("KB num: " + problem.kb.size());
    		// System.out.println(problem.kb);

    		// System.out.println();

    		FileWriter writer = new FileWriter("output.txt");  
    		BufferedWriter buffer = new BufferedWriter(writer); 

    		for (int i = 0; i < problem.queries.size(); i++) {
    		// for (int i = 0; i < 1; i++) {
    			problem = readInput(file);
    			// System.out.println("Query: " + problem.queries.get(i) + " ?");
    			Solution solution = new Solution(problem.queries.get(i), problem.kb);
    			// System.out.println("Answer: " + solution.getResult());
    			buffer.write(solution.getResult());
				if (i != problem.queries.size() - 1) {
					buffer.newLine();
				}
    		}
    		buffer.close(); 
		}
		catch (FileNotFoundException exc) {
	        System.out.println("File not found: " + fileName);
	    }
	    catch (IOException exc) {
         exc.printStackTrace();
		}
	}
}
