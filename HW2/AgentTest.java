import java.util.*;

// The agent that fight for me. Fight on!

public class AgentTest {

	// private Node root;
	private double leftTime;
	private int maxDepth;
	private int[] nextMove;
	private char[][] boardAfterMove;
	private int numPickedFruits;
	private int nodeCount;
	private long startTime;
	private double timeOneStep;
	private int b; // bracn factor(no. of blocks)

	AgentTest(int maxDepth, double leftTime, double timeOneStep) {
		// this.root = root;
		this.leftTime = leftTime;
		this.maxDepth = maxDepth;
		this.nodeCount = 0;
		this.timeOneStep = timeOneStep;
	}

	// minimax search with alpha-beta pruning
	public void search(Node root) {
		this.startTime =  System.currentTimeMillis();
		double alpha = -Double.MAX_VALUE;
		double beta = Double.MAX_VALUE;
		this.b = root.boardState.getFruitBlocks().size();
		// this.maxDepth = b == 1 ? 1 : (int)Math.floor(Math.log10(4669 * 5.0) / Math.log10(b)) + 1;
		// System.out.println("Max Depth: " + maxDepth);
		Result rst = minimaxValue(root, alpha, beta);
		if (rst == null) {
			// System.out.println("Time out, get greedy!");
			Map<int[], Integer> sortedFruitBlocks = sortFruitBlocks(root.boardState.getFruitBlocks());
			Map.Entry<int[], Integer>[] entryArr = new Map.Entry[sortedFruitBlocks.size()];
			sortedFruitBlocks.entrySet().toArray(entryArr);
			nextMove = entryArr[0].getKey();
		} else {
			nextMove = new int[]{rst.coor[0], rst.coor[1]};
		}
		// apply next move
		Board boardState = new Board(root.boardState);
		boardState.setBoardAfterPick(nextMove);
		// boardAfterMove = Helper.copyBoard(boardState.getBoard());
		boardAfterMove = boardState.getBoard();
		this.numPickedFruits = boardState.getNumPickedFruits();
		// System.out.println("numPickedFruits: " + numPickedFruits);

	}

	private double eval(Node node) {
		// int numLeftFruits = node.boardState.getRemainingCoor().size();
		// if (node.isPlayerMax) {
		// 	node.maxScore += numLeftFruits * numLeftFruits;
		// } else {
		// 	node.minScore += numLeftFruits * numLeftFruits;
		// }
		return (double)(node.maxScore - node.minScore);
	}

	private boolean cannotWin(Node node) {
		if (!node.isPlayerMax) {
			return false;
		}
		int maxScoreLeft = 0;
		int[] freq = new int[10];
		boolean rst = false;
		for (Map.Entry<int[], Integer> entry : node.boardState.getFruitBlocks().entrySet()) {
			int temp = entry.getValue();
			freq[node.boardState.getBoard()[entry.getKey()[0]][entry.getKey()[1]] - '0'] += temp;
		}
		for (int i = 0; i < 10; i++) {
		// 	System.out.println(i + ":" + freq[i]);
			maxScoreLeft += freq[i] * freq[i];
		}
		// if (node.isPlayerMax) {
			// System.out.println("Max");
			rst = node.maxScore + maxScoreLeft <= node.minScore;
		// } else {
			// System.out.println("Min");
			// rst = node.minScore + maxScoreLeft > node.maxScore;
		// }
		// if (rst) {
			// System.out.println("minScore " + node.minScore + " maxScore " + node.maxScore + " maxScoreLeft " + maxScoreLeft + " Cannot Win!");
		// }
		return rst;
	}

	private boolean cutOff(Node node) {
		// if (node.depth == maxDepth || node.boardState.getFruitBlocks().size() == 0 || cannotWin(node)) {
		if (node.depth == maxDepth || node.boardState.getFruitBlocks().size() == 0) {
			return true;
		}
		return false;
	}

	private Node applyAction(Node node, int[] moveCoor, int numBlockFruits) {
		int depth = node.depth + 1;
		// copy parent boardState
		Board boardState = new Board(node.boardState);
		boardState.setBoardAfterPick(moveCoor);
		// int numBlockFruits = chosenBlock.size();
		int maxScore, minScore;
		if (node.isPlayerMax) {
			maxScore = node.maxScore + numBlockFruits * numBlockFruits;
			minScore = node.minScore;
		} else {
			minScore = node.minScore + numBlockFruits * numBlockFruits;
			maxScore = node.maxScore;
		}
		// child of Max node is Min node, vice versu
		boolean isPlayerMax = !node.isPlayerMax;
		// Node parent = node;
		// List<Node> childs = new ArrayList<Node>();
		return new Node(depth, isPlayerMax, maxScore, minScore, boardState);
	}

	private Map<int[], Integer> sortFruitBlocks (Map<int[], Integer> map) {
		List<Map.Entry<int[], Integer>> list = new LinkedList<Map.Entry<int[], Integer>>(map.entrySet());
	    Collections.sort(list, new Comparator<Map.Entry<int[], Integer>>() {
	        // @Override
	        public int compare(Map.Entry<int[], Integer> a, Map.Entry<int[], Integer> b) {
	            return (b.getValue() - a.getValue());
	        }
	    });

	    Map<int[], Integer> result = new LinkedHashMap<int[], Integer>();
	    for (Map.Entry<int[], Integer> entry : list) {
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}

	private Result minimaxValue(Node node, double alpha, double beta) {
		 // System.out.println(node);
		double runTime = (System.currentTimeMillis() - this.startTime) / 1000.0;
		// double estNextStepTime = Math.pow(this.b, this.maxDepth - 1) / 4669;
		// System.out.println("estNextStepTime: " + estNextStepTime);
		if (this.leftTime <= 0 || runTime > this.timeOneStep || this.leftTime - runTime <= 0) {
			return null;
		}
		if (cutOff(node)) {
			 // System.out.println("Cut off!");
			double evaluation = eval(node);
			 // System.out.println("Eval: " + evaluation);
			return new Result(evaluation, null);
		}
		// List<Node> childs = node.childs;
		// initializa v to -inf
		int[] temp = new int[]{0,0};
		Result rst = new Result(node.isPlayerMax ? -Double.MAX_VALUE : Double.MAX_VALUE, temp);
		Map<int[], Integer> sortedFruitBlocks = sortFruitBlocks(node.boardState.getFruitBlocks());
		// System.out.println("sortedFruitBlocks: " + sortedFruitBlocks);
		// for (int i = 0; i < node.boardState.getFruitBlocks().size(); i++) {
		for (int[] moveCoor : sortedFruitBlocks.keySet()) {
			Node child = applyAction(node, moveCoor, sortedFruitBlocks.get(moveCoor));
			nodeCount++;
			
			// update minimax value
			Result v = minimaxValue(child, alpha, beta);
			if (v == null) {
				return null;
			}
			// if max node
			if (node.isPlayerMax) {
				//  System.out.println("min-node Result: " + v);
				double minValue = v.minimaxValue;
				if (minValue > rst.minimaxValue) {
					//  System.out.println("update movecoor: [" + moveCoor[0] + ", " + moveCoor[1] + "]");
					rst = new Result(minValue, new int[]{moveCoor[0], moveCoor[1]});
					//  System.out.println("update Result: " + rst);
				}
				if (minValue >= beta) {
					 // System.out.println("beta cut!");
					return rst;
				}
				alpha = Math.max(alpha, minValue);
			}
			// if min node
			else {
				double maxValue = v.minimaxValue;
				if (maxValue < rst.minimaxValue) {
					rst = new Result(maxValue, new int[]{moveCoor[0], moveCoor[1]});
				}
				if (maxValue <= alpha) {
					 // System.out.println("alpha cut!");
					return rst;
				}
				beta = Math.min(beta, maxValue);
			}
		}
		 // System.out.println("max-node Result: " + rst);
		return rst;
	}

	public int[] getNextMove() {
		return nextMove;
	}

	public char[][] getBoardAfterMove() {
		return boardAfterMove;
	}

	public int getNumPickedFruits() {
		return numPickedFruits;
	}

	public int getNodeCount() {
		return nodeCount;
	}
}