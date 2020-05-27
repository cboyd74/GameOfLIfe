package application;

import java.util.ArrayList;

public class Cell {
	private boolean alive;
	private int age;

	/**
	 * Creates new instance of Cell.
	 */
	public Cell() {
		this.alive = false; // Initially dead
		this.age = 0; // Initially age of 0
	}

	/**
	 * Sets this Cell's alive status to the opposite of its current
	 */
	public void setAlive() {
		if (this.alive == true) {
			this.alive = false;
		} else {
			this.alive = true;
		}
	}

	/**
	 * Sets this Cell's alive status to the given boolean value.
	 * 
	 * @param alive
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Returns this Cell's age
	 * 
	 * @return
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * Sets this Cell's age to the given int value. Used when copying Cells into new
	 * array during generation.
	 * 
	 * @param age
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Returns alive status of this cell
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return this.alive;
	}
}
