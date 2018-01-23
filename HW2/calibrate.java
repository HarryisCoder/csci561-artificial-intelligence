import java.util.*;
import java.util.regex.*;
import java.io.*;

public class calibrate {
	public static void main(String args[]) {
		char[][] board = new char[27][27];
		int temp = 0;
		for (int i = 0; i < 27; i++) {
			for (int j = 0; j < 27; j++) {
				temp = temp == 0 ? 1 : 0; 
				board[i][j] = (char)('0' + temp);
			}
		}
		// Helper.printBoard(board);
		AgentTest player = new AgentTest(4, 1, 1);
		Node root = new Node(0, true, 0, 0, new Board(board));
		player.search(root);
		int k = player.getNodeCount();
		// System.out.println("k: " + k);
		try {
			FileWriter writer = new FileWriter("calibration.txt");  
			BufferedWriter buffer = new BufferedWriter(writer);
			buffer.write("" + k);
			buffer.close();
		}
		catch (IOException exc) {
        	exc.printStackTrace();
		}
	}
}