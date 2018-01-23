import java.util.*;

public class Board {

	private char[][] board;
	private Map<int[], Integer> fruitBlocks;
	private int n;
	private int numPickedFruits;

	Board(char[][] board) {
		this.board = Helper.copyBoard(board);
		this.n = board.length;
		this.fruitBlocks = calculateBlocks();
		this.numPickedFruits = 0;
	}

	// construct Board class by copying another class
	Board(Board boardState) {
		this.board = Helper.copyBoard(boardState.board);
		this.n = boardState.n;
		this.fruitBlocks = new HashMap<int[], Integer>(boardState.fruitBlocks);
		this.numPickedFruits = 0;
	}

	private Map<int[], Integer> calculateBlocks() {
		char[][] newBoard = Helper.copyBoard(board);
		Map<int[], Integer> map = new HashMap<>();
		int label = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (newBoard[i][j] != '*') {
					int numCollectedFruits = collectFruits(newBoard, new int[]{i, j}, newBoard[i][j]);
					map.put(new int[]{i, j}, numCollectedFruits);
					label++;
				}
			}
		}
		return map;
	}

	// collect all fruits 4-connected to current fruit using DFS
	private int collectFruits(char[][] board, int[] startCoor, char currFruit) {
		int i = startCoor[0];
		int j = startCoor[1];
		// stop when out of boundary or not same type fruit or empty
		if (i >= n || i < 0 || j >= n || j < 0 || board[i][j] == '*' || board[i][j] != currFruit) {
			// System.out.println("return!");
			return 0;
		}
		board[i][j] = '*';
		// coorList.add(new int[]{i, j});
		int[] xDiff = new int[]{0, 1, 0, -1};
		int[] yDiff = new int[]{-1, 0, 1, 0};
		int count = 0;
		for (int ii = 0; ii < 4; ii++) {
			count += collectFruits(board, new int[]{i + xDiff[ii], j + yDiff[ii]}, currFruit); 
		}
		return count + 1;
	}

	private void applyGravity() {
		for (int j = 0; j < n; j++) {
			Queue<Character> queue = new LinkedList<>();
			int numStar = 0;
			for (int i = 0; i < n; i++) {
				if (board[i][j] != '*') {
					queue.offer(board[i][j]);
				} else {
					board[numStar][j] = '*';
					numStar++;
				}
			}
			for (int k = numStar; k < n; k++) {
				board[k][j] = queue.poll();
			}
		}
	}

	// set picked fruit to '*', modify board, recalculate fruitBlocks and remainingCoor accordingly;
	public void setBoardAfterPick(int[] coor) {
		this.numPickedFruits = collectFruits(board, new int[]{coor[0], coor[1]}, board[coor[0]][coor[1]]); 
		applyGravity();
		fruitBlocks = calculateBlocks();
	}

	public Map<int[], Integer> getFruitBlocks() {
		return this.fruitBlocks;
	}

	public char[][] getBoard() {
		return this.board;
	}

	// used for cut-off test
	public int getMaxRemainingScore() {
		return 0;
	}

	public int getNumPickedFruits() {
		return this.numPickedFruits;
	}
}