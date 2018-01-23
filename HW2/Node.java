import  java.util.*;

public class Node {
	int depth;
	boolean isPlayerMax;
	int maxScore;
	int minScore;
	Board boardState;

	Node(int depth, boolean isPlayerMax, int maxScore, int minScore, Board boardState) {
		this.depth = depth;
		this.isPlayerMax = isPlayerMax;
		this.maxScore = maxScore;
		this.minScore = minScore;
		this.boardState = boardState;
	}

	public String toString() {
		String string = "Node\n[\n";
		string += ("Depth: " + depth);
		string += ("\nPlayer: " + (isPlayerMax ? "Max" : "Min"));
		string += ("\nMaxScore: " + maxScore);
		string += ("\nMinScore: " + minScore);
		string += ("\nBoardState: \n");
		char[][] board = boardState.getBoard();
		for (char[] row : board) {
        	for (char element : row) {
        		string += (element + " ");
        	}
        	string += "\n";
        }
        string += "]\n";
        return string;
	}
 }