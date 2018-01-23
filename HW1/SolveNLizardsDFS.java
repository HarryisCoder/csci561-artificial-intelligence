import java.util.*;

public class SolveNLizardsDFS {

	// private Set<Integer> colSet;
	// private Set<Integer> rowSet;
	// private Set<Integer> lDiagSet;
	// private Set<Integer> rDiagSet;
	private int[][] table;
	private int numLiz;
	private int numTrees;
	private int numRooms;
	// private int countLiz;
	private int n;

	SolveNLizardsDFS(int[][] table, int numLiz, int numTrees) {
		this.table = table;
		this.n = table.length;
		this.numLiz = numLiz;
		// this.countLiz = 0;
		this.numTrees = numTrees;
		this.numRooms = n * n - numTrees;
		// for (int i = 0; i < n; i++) {
		// 	for (int j = 0; j < n; j++) {
		// 		if (table[i][j] == 2) {
		// 			this.numTrees++;
		// 		}
		// 	}
		// }
		// this.colSet = new HashSet<>();
		// this.rowSet = new HashSet<>();
		// this.lDiagSet = new HashSet<>();
		// this.rDiagSet = new HashSet<>();
	}

	private int[] addTrees(int i, int j, Set<Integer> rowSet, Set<Integer> colSet, Set<Integer> rDiagSet, Set<Integer> lDiagSet) {
		int countTrees = 0;
		// int nextStarRow = startRow;
		// int nextStarCol = startCol;
		while (i < n && j < n && table[i][j] == 2) {
			countTrees++;
			// System.out.println("tree!\nnumTrees: " + numTrees + "\n");
			// if (countLiz != 0) {
			boolean bool1 = rowSet.remove(i);
			boolean bool2 = colSet.remove(j);
			boolean bool3 = rDiagSet.remove(i + j);
			boolean bool4 = lDiagSet.remove(i - j);
				// if (solveNLizardsDFS(i, j + 1, countLiz)) {
				// 	return true;
				// }
				// System.out.println("Jump out!");
				// numTrees++;
				// if (bool1) {rowSet.add(i);}
				// if (bool2) {colSet.add(j);}
				// if (bool3) {rDiagSet.add(i + j);}
				// if (bool4) {lDiagSet.add(i - j);}
			j++;
			if (j == n) {
				j = 0;
				i++;
			}
			// }
		}
		return new int[]{i, j, countTrees};
	}

	private boolean solveNLizardsDFS(int startRow, int startCol, int countLiz, Set<Integer> rowSet, Set<Integer> colSet, Set<Integer> rDiagSet, Set<Integer> lDiagSet) {
		if (startCol == n) {
			startCol = 0;
			startRow++;
		}
		// System.out.println("n: " + n + " countLiz: " + countLiz  + " numRooms: " + numRooms);
		// System.out.println("rowSet:" + rowSet + " colSet:" + colSet + " rDiagSet:" + rDiagSet + " lDiagSet:" + lDiagSet);
		// homework.printTable(table);
		if (numRooms < numLiz - countLiz) {
			// System.out.println("Kill");
			return false;
		}
		int[] nextLizCoor = addTrees(startRow, startCol, rowSet, colSet, rDiagSet, lDiagSet);
		int i = nextLizCoor[0];
		int j = nextLizCoor[1];
		while (i < n && j < n) {
			if (table[i][j] == 0 && homework.isValid(i, j, rowSet, colSet, rDiagSet, lDiagSet)) {
				table[i][j] = 1;
				// System.out.println("[" + i + ", " + j + "]");
				numRooms--;
				Set<Integer> tempRowSet = new HashSet<>(rowSet);
				Set<Integer> tempColSet = new HashSet<>(colSet);
				Set<Integer> tempRDiagSet = new HashSet<>(rDiagSet);
				Set<Integer> tempLDiagSet = new HashSet<>(lDiagSet);
				tempRowSet.add(i);
				tempColSet.add(j);
				tempRDiagSet.add(i + j);
				tempLDiagSet.add(i - j);

				// System.out.println();
				countLiz++;
				if (countLiz == numLiz) {
					return true;
				}
				if (solveNLizardsDFS(i, j + 1, countLiz, tempRowSet, tempColSet, tempRDiagSet, tempLDiagSet)){
					return true;
				}
				// numTrees -= nextLizCoor[2];
				table[i][j] = 0;
				numRooms++;
				countLiz--;
			} 
			j++;
			if (j == n) {
				j = 0;
				i++;
			}
			nextLizCoor = addTrees(i, j, rowSet, colSet, rDiagSet, lDiagSet);
			i = nextLizCoor[0];
			j = nextLizCoor[1];
			// }
				// return solveNLizards(table, i, j + 1, countLiz, p, rowSet, colSet, rDiagSet, lDiagSet);
		}
		// System.out.println("to the end!");
		return false;
	}

	public boolean getResult() {
		int startRow = 0;
		int startCol = 0;
		int countLiz = 0;
		Set<Integer> colSet = new HashSet<>();
		Set<Integer> rowSet = new HashSet<>();
		Set<Integer> lDiagSet = new HashSet<>();
		Set<Integer> rDiagSet = new HashSet<>();
		return this.solveNLizardsDFS(startRow, startCol, countLiz, rowSet, colSet, rDiagSet, lDiagSet);
	}
}