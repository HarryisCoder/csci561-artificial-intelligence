public class Helper {
	
	public static char[][] copyBoard(char[][] oldBoard) {
		int n = oldBoard.length;
		char[][] newBoard = new char[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				newBoard[i][j] = oldBoard[i][j];
			}
		}
		return newBoard;
	}

	public static void printBoard(char[][] board) {
		for (char[] row : board) {
        	for (char element : row) {
        		System.out.print(element);
        	}
        	System.out.println();
        }
	}
}