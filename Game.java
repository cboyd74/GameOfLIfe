package application;

import java.util.ArrayList;

public class Game {
	private ArrayList<ArrayList<Cell>> currBoard;
	private ArrayList<ArrayList<Cell>> board;
	private ArrayList<ArrayList<Cell>> board_2;
	private int gen;

	public Game(int width, int height) {
		gen = 0; // Generation set to 0
		init(width, height); // Initialize everything
	}

	/**
	 * Initializes game. Width and height will be used to create 2D arrays.
	 * 
	 * @param width
	 * @param height
	 */
	private void init(int width, int height) {
		this.board = new ArrayList<ArrayList<Cell>>();
		this.board_2 = new ArrayList<ArrayList<Cell>>();
		for (int i = 0; i < height; i++) {
			board.add(new ArrayList<Cell>());
			board_2.add(new ArrayList<Cell>());
			for (int j = 0; j < width; j++) {
				board.get(i).add(new Cell());
				board_2.get(i).add(new Cell());
			}
		}
		currBoard = board;
	}

	public ArrayList<ArrayList<Cell>> getBoard() {
		return this.currBoard;
	}

	/**
	 * Creates new generation by applying Game of Life rules.
	 */
	public void nextGen() {
		ArrayList<ArrayList<Cell>> newGen;
		// When generating new generation, alternate between board and board_2
		if (currBoard.equals(board)) {
			newGen = board_2;
		} else {
			newGen = board;
		}
		for (int i = 0; i < currBoard.size(); i++) {
			ArrayList<Cell> curr = currBoard.get(i);
			for (int j = 0; j < curr.size(); j++) {
				// Counts the live neighbors of cell at i, j
				int neighborsAlive = countNeighbors(i, j);
				Cell currCell = curr.get(j);
				if (currCell.isAlive()) { // live cell
					if (neighborsAlive < 2) { // isolation death
						newGen.get(i).get(j).setAlive(false);
						newGen.get(i).get(j).setAge(0);
					} else if (neighborsAlive > 3) { // over population
						newGen.get(i).get(j).setAlive(false);
						newGen.get(i).get(j).setAge(0);
					} else {
						newGen.get(i).get(j).setAlive(true);
						newGen.get(i).get(j).setAge(currCell.getAge() + 1);
					}
				} else { // dead cell
					if (neighborsAlive == 3) { // dead cell comes to life
						newGen.get(i).get(j).setAlive(true);
						newGen.get(i).get(j).setAge(0);
					} else {
						newGen.get(i).get(j).setAlive(false);
						newGen.get(i).get(j).setAge(0);
					}
				}
			}
		}
		this.gen++; // update generation number
		this.currBoard = newGen;
	}

	private int countNeighbors(int x, int y) {
		int count = 0;

		// neighbors at i-1
		if (x > 0) {
			if (currBoard.get(x - 1).get(y).isAlive()) {
				count++;
			}
			if (y > 0) {
				if (currBoard.get(x - 1).get(y - 1).isAlive()) {
					count++;
				}
			}
			if (y < this.currBoard.get(x).size() - 1) {
				if (currBoard.get(x - 1).get(y + 1).isAlive()) {
					count++;
				}
			}
		}

		// neighbors at i+1
		if (x < this.currBoard.size() - 1) {
			if (currBoard.get(x + 1).get(y).isAlive()) {
				count++;
			}
			if (y > 0) {
				if (currBoard.get(x + 1).get(y - 1).isAlive()) {
					count++;
				}
			}
			if (y < this.currBoard.get(x).size() - 1) {
				if (currBoard.get(x + 1).get(y + 1).isAlive()) {
					count++;
				}
			}
		}

		// neighbors at i
		if (y > 0) {
			if (currBoard.get(x).get(y - 1).isAlive()) {
				count++;
			}
		}
		if (y < this.currBoard.get(x).size() - 1) {
			if (currBoard.get(x).get(y + 1).isAlive()) {
				count++;
			}
		}
		return count;
	}

	public int getGen() {
		return this.gen;
	}
}
