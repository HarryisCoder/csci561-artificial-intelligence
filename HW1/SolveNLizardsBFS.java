import java.util.*;

public class SolveNLizardsBFS {

	private int[][] table;
	private int numLiz;
	private int numTrees;
	private int n;

	SolveNLizardsBFS(int[][] table, int numLiz, int numTrees) {
		this.table = table;
		this.numLiz = numLiz;
		this.n = table.length;
		this.numTrees = numTrees;
		// for (int i = 0; i < n; i++) {
		// 	for (int j = 0; j < n; j++) {
		// 		if (table[i][j] == 2) {
		// 			this.numTrees++;
		// 		}
		// 	}
		// }
	}

	private void drawSolution(int[][] table, List<int[]> solution) {
		for (int[] coor : solution) {
			table[coor[0]][coor[1]] = 1;
		}
	}

	private void drawState(int[][] table, List<int[]> solution) {
		int[][] copy = new int[table.length][table.length];
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy.length; j++) {
				copy[i][j] = table[i][j];
			}
		}
		for (int[] coor : solution) {
			copy[coor[0]][coor[1]] = 1;
		}
		// homework.printTable(copy);
	}

	private boolean solveNLizardsBFS(int[][] table, int numLiz) {
		Queue<Node> queue = new LinkedList<>();
		Node root = new Node(numTrees, 0, 0, new ArrayList<int[]>(), new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>());
		queue.offer(root); 
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int ii = 0; ii < size; ii++) {
				Node node = queue.poll();
				// this path is already dead
				if (n - node.rowSet.size() + node.numTrees + node.lizCoor.size() < numLiz) {
					// System.out.println("Kill");
					continue;
				}
				int i = node.startRow;
				int j = node.startCol;
				while (i < n && j < n) {	
			    	// System.out.println("[" + i + ", " + j + "]");

					if (table[i][j] == 2) {
						node.numTrees--;
						node.rowSet.remove(i);
						node.colSet.remove(j);
						node.rDiagSet.remove(i + j);
						node.lDiagSet.remove(i - j);
						j++;
						if (j == n) {
							j = 0;
							i++;
						}
					}
					else if (homework.isValid(i, j, node.rowSet, node.colSet, node.rDiagSet, node.lDiagSet)) {
			    		// // System.out.println("Valid!");
						// node.lizCoor.add(new int[]{i, j});
						node.rowSet.add(i);
						node.colSet.add(j);
						node.rDiagSet.add(i + j);
						node.lDiagSet.add(i - j);
						List<int[]> temp = new ArrayList<>(node.lizCoor);
						temp.add(new int[]{i, j});
						Node child = new Node(node.numTrees, i, j, temp, node.rowSet, node.colSet, node.rDiagSet, node.lDiagSet);
						queue.offer(child);
						// drawState(table, child.lizCoor);
						// printTable(table);
						// // System.out.println();
						if (child.lizCoor.size() == numLiz) {
							this.drawSolution(table, child.lizCoor);
							return true;
						}
						node.rowSet.remove(i);
						node.colSet.remove(j);
						node.rDiagSet.remove(i + j);
						node.lDiagSet.remove(i - j);
						j++;
						if (j == n) {
							j = 0;
							i++;
						}
					} 
					else {
						j++;
						if (j == n) {
							j = 0;
							i++;
						}
					}
						// return solveNLizards(table, i, j + 1, k, p, rowSet, colSet, rDiagSet, lDiagSet);
				}

			}
		}
		return false;
	}

	public boolean getResult() {
		return solveNLizardsBFS(table, numLiz);
	}
}

class Node {
	// int numLiz;
	int numTrees;
	int startRow;
	int startCol;
	List<int[]> lizCoor;
	Set<Integer> rowSet;
	Set<Integer> colSet;
	Set<Integer> rDiagSet;
	Set<Integer> lDiagSet;
	Node(int numTrees, int startRow, int startCol, List<int[]> lizCoor, Set<Integer> rowSet, Set<Integer> colSet, Set<Integer> rDiagSet, Set<Integer> lDiagSet) {
		// this.numLiz = numLiz;
		this.numTrees = numTrees;
		this.startRow = startRow;
		this.startCol = startCol;
		this.lizCoor = new ArrayList<int[]>(lizCoor);
		this.rowSet = new HashSet<Integer>(rowSet);
		this.colSet = new HashSet<Integer>(colSet);
		this.rDiagSet = new HashSet<Integer>(rDiagSet);
		this.lDiagSet = new HashSet<Integer>(lDiagSet);
	}
}
