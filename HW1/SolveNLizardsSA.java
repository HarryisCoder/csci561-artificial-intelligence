import java.util.*;

public class SolveNLizardsSA {
	private int[][] tableConstant;
	private int[][] table;
	private int numLiz;
	private int[] rooms;
	private int[] lizards;
	private boolean printAllowed0;
	private boolean printAllowed1;
	private int n;
	private int numTrees;

	SolveNLizardsSA (int[][] table, int numLiz, int numRooms, int numTrees) {
		this.tableConstant = table;
		this.n = table.length;
		this.table = new int[n][n];
		this.numLiz = numLiz;
		this.rooms = new int[numRooms - numLiz];
		this.lizards = new int[numLiz];
		// for test only
		this.printAllowed0 = false;
		this.printAllowed1 = false;
		this.numTrees = numTrees;
	}

	private void getRandomInit() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				table[i][j] = tableConstant[i][j];
				if (table[i][j] != 2) {
					list.add(i * n + j); // id starts from 0
				}
			}
		}
		Collections.shuffle(list);
		for (int i = 0; i < numLiz; i++) {
			int id = list.get(i);
			lizards[i] = id;
			table[id / n][id % n] = 1;
		}
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (table[i][j] == 0) {
					rooms[k++] = i * n + j;
				}
			}
		}
		return;
	}

	private boolean acceptWithP(int diffE, double temperature) {
		double prob = Math.pow(Math.E, -diffE/(temperature));
		if (printAllowed1) {
			System.out.println("diffE: " + diffE);
			System.out.println("prob: " + prob);
		}
		if (Math.random() < prob) {
			return true;
		}
		return false;
	}

	private int getConflictNumOne2(int startRow, int startCol, Map<Integer, Integer> lizConfNum) {
		int count = 0;
		Set<Integer> colSet = new HashSet<>();
		Set<Integer> rowSet = new HashSet<>();
		Set<Integer> lDiagSet = new HashSet<>();
		Set<Integer> rDiagSet = new HashSet<>();
		int i = startRow;
		int j = startCol;
		int lizId1 = i * n + j;
		rowSet.add(i);
		colSet.add(j);
		rDiagSet.add(i + j);
		lDiagSet.add(i - j);
		while (i < n && j < n) {
			if (!(i == startRow && j == startCol) && table[i][j] == 1) {
				if (!homework.isValid(i, j, rowSet, colSet, rDiagSet, lDiagSet)) {
					count++;
					int lizId2 = i * n + j;
					if (!lizConfNum.containsKey(lizId1)) {
						lizConfNum.put(lizId1, 1);
					} else {
						lizConfNum.put(lizId1, lizConfNum.get(lizId1) + 1);
					}
					if (!lizConfNum.containsKey(lizId2)) {
						lizConfNum.put(lizId2, 1);
					} else {
						lizConfNum.put(lizId2, lizConfNum.get(lizId2) + 1);
					}
				} 
			} else if (table[i][j] == 2) {
				rowSet.remove(i);
				colSet.remove(j);
				rDiagSet.remove(i + j);
				lDiagSet.remove(i - j);
			}
			j++;
			if (j == n) {
				j = 0;
				i++;
			}
		}
		return count;
	}

	private int getConflictNum2(Map<Integer, Integer> lizConfNum) {
		int sum = 0;
		List<int[]> lizCoor = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (table[i][j] == 1) {
					lizCoor.add(new int[]{i, j});
				}
			}
		}
		for (int i = 0; i < lizCoor.size(); i++) {
			int count = getConflictNumOne2(lizCoor.get(i)[0], lizCoor.get(i)[1], lizConfNum);
			sum += count;
		}
		return sum;
	}

	private int[] randomMove2(int nextMoveLizId) {
		int numRooms = rooms.length;
		Random rand = new Random();
		int roomIdx = rand.nextInt(numRooms);
		int roomId = rooms[roomIdx];
		table[roomId / n][roomId % n] = 1;
		table[nextMoveLizId / n][nextMoveLizId % n] = 0;
		rooms[roomIdx] = nextMoveLizId;
		return new int[]{roomIdx, roomId};
	}

	private void cancelRandomMove2(int[] pair, int nextMoveLizId) {
		int roomIdx = pair[0];
		int roomId = pair[1];
		table[roomId / n][roomId % n] = 0;
		table[nextMoveLizId / n][nextMoveLizId % n] = 1;
		rooms[roomIdx] = roomId;
		return;
	}

	public static int findMax(Map<Integer, Integer> lizConfNum) {
		int max = 0;
		int nextMoveLizId = 0;
		for (Map.Entry<Integer, Integer> entry : lizConfNum.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				nextMoveLizId = entry.getKey();
			}
		}
		List<Integer> maxs = new ArrayList<>();
		for (Map.Entry<Integer, Integer> entry : lizConfNum.entrySet()) {
			if (entry.getValue() == max) {
				maxs.add(entry.getKey());
			}
		}
		Collections.shuffle(maxs);
		return maxs.get(0);
	}

	public boolean getResult() {
		int iter = 0;
		int iter2 = 0;
		double temperature = 0;
		Map<Integer, Integer> lizConfNum = new HashMap<>();
		int numConflict = getConflictNum2(lizConfNum);
		// handle the case when array rooms is empty
		if (rooms.length == 0) {
			getRandomInit();
			numConflict = getConflictNum2(lizConfNum);
			if (numConflict == 0) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						if (table[i][j] == 1) {
							tableConstant[i][j] = 1;
						}
					}
				}
				return true;
			}
			return false;
		}
		if (n + numTrees < numLiz) {
			return false;
		}
		int nextNumConflict = 0;

		// int timeLimit = 60; //unit in seconds
		// long start = System.currentTimeMillis();
		// long end = start + timeLimit * 1000;
		double stopTemperature = 0.00001;
		boolean result = false;
		int count = 0;
		int lowConflictCount = 0;
		int lowConflictCountStop = 20000;
		while (count++ < 10) {
			iter = 0;
			getRandomInit();
			nextNumConflict = 0;
			lizConfNum = new HashMap<>();
			numConflict = getConflictNum2(lizConfNum);
			// homework.printTable(table);
			// System.out.println("count: " + count);
			
			while (numConflict != 0) {
				if (numConflict <= 2) {
					lowConflictCount++;
				} else {
					lowConflictCount = 0;
				}
				int nextMoveLizId = findMax(lizConfNum);
				int[] pair = randomMove2(nextMoveLizId);
				Map<Integer, Integer> tempLizConfNum = new HashMap<>();
				nextNumConflict = getConflictNum2(tempLizConfNum);
				temperature = 70 * Math.pow(0.999, iter);
				if (printAllowed1) {
					System.out.println("iteration: " + iter2);
					System.out.println("conflict no.: " + numConflict);
					System.out.println("temperature: " + temperature);
				}
				if (nextNumConflict <= numConflict || acceptWithP(nextNumConflict - numConflict, temperature)) {
					numConflict = nextNumConflict;
					lizConfNum = new HashMap<Integer, Integer>(tempLizConfNum);
				} else {
					cancelRandomMove2(pair, nextMoveLizId);
				}

				// if (System.currentTimeMillis() > end) {
				// 	if (printAllowed0) {
				// 		System.out.println("Oops, " + timeLimit + " s over!");
				// 	}
				// 	break;
				// }
				if (temperature < stopTemperature) {
					if (printAllowed0) {
						System.out.println("Oops, T decreases to 0!");
					}
					break;
				}
				if (lowConflictCount > lowConflictCountStop) {
					if (printAllowed0) {
						System.out.println("Oops, stuck in local!");
					}
					break;
				}
				iter++;
				iter2++;
			}
			if (numConflict == 0) {
				result = true;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						if (table[i][j] == 1) {
							tableConstant[i][j] = 1;
						}
					}
				}
				break;
			}
			// if (System.currentTimeMillis() > end) {
			// 	break;
			// }
		}
		if (result) {
			// if (printAllowed0) {
		// 		// System.out.println("iteration: " + iter);
		// 		// System.out.println("temperature: " + temperature);
		// 		System.out.println("Count: " + count);
				// System.out.println("Run Time: " + (System.currentTimeMillis() - start));
			// }
		}
		return result;
	}
}