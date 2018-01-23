import java.util.*;
import java.io.*;

// main function of homework2

public class homework {

	public static Problem readInput(File fileName) throws IOException{
		Scanner in = new Scanner(fileName);
		// board size
		int n = in.nextInt();
		int numFruitType = in.nextInt();
		double leftTime = in.nextDouble();
		char[][] board = new char[n][n];
		in.nextLine();
		for (int i = 0; i < n; i++) {
			String line = in.nextLine();
			for (int j = 0; j < n; j++) {
				board[i][j] = line.charAt(j);
			}
		}
		return new Problem(board, numFruitType, leftTime);
	}

	public static int readK(File fileName) throws IOException{
		Scanner in = new Scanner(fileName);
		// board size
		int n = in.nextInt();
		return n;
	}

	public static void writeOutput(int[] nextMove, char[][] board) throws IOException{
		FileWriter writer = new FileWriter("output.txt");  
    	BufferedWriter buffer = new BufferedWriter(writer); 
		buffer.write((char)('A' + nextMove[1]));
		buffer.write("" + (nextMove[0] + 1));
		buffer.newLine();
		StringBuilder builder = new StringBuilder();
		int n = board.length;
		for(int i = 0; i < n; i++) {
		    for(int j = 0; j < n; j++) {
		      builder.append(board[i][j]);
		    }
		    if (i != board.length - 1) {
		    	//append new line at the end of the row
		    	builder.append("\n");
		    }
		}
		buffer.write(builder.toString()); 
		buffer.close();  
	}

	public static void main(String[] args) {
		String fileName = "input.txt";
		try {
			File file = new File(fileName);
    		Problem problem = readInput(file);
    		char[][] board = problem.board;
    		int numFruitType = problem.numFruitType;
    		double leftTime = problem.leftTime;

    		File caFile = new File("calibration.txt");
    		int k = readK(caFile); // no. of nodes can be searched in one second
    		// System.out.println("k: " + k);
    		// int maxLevel = 4;
    		double timeOneStep = 10;
    		MyAgent player = new MyAgent(k, leftTime, timeOneStep);
    		Node root = new Node(0, true, 0, 0, new Board(board));
    		player.search(root);
    		writeOutput(player.getNextMove(), player.getBoardAfterMove());
		}
		catch (FileNotFoundException exc) {
	        System.out.println("File not found: " + fileName);
	    }
	    catch (IOException exc) {
         exc.printStackTrace();
		}
	}
}
