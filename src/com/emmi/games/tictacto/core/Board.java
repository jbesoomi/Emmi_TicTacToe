package com.emmi.games.tictacto.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.emmi.games.tictacto.players.Player;

public class Board {
	
	public static final Logger LOGGER = Logger.getLogger(Board.class.getName());
	
	private Cell[][] board;
	
	private List<Cell> availableCells;
	
	private List<Cell> usedCells;
	
	private int boardWidth;
	
	/**
	 * 
	 * @param boardWidth
	 */
	public Board(int boardWidth){
		this.boardWidth = boardWidth;
		this.initBoard();
	}
	
	/**
	 * Initialize Game Board 
	 */
	public void initBoard(){
		
		this.board = new Cell[this.boardWidth][this.boardWidth];
		this.availableCells = new ArrayList<Cell>();
		this.usedCells = new ArrayList<Cell>();
		
		Cell cell = null;
		for(int row = 0; row < this.boardWidth; row++){
			for(int column = 0; column < this.boardWidth; column++){
				cell = new Cell(row, column, this.boardWidth);
				this.board[row][column] = cell;
				this.availableCells.add(cell);
			}
		}
	}
	
	
	/**
	 * Attach given cell to given player.
	 * If given player wins the game with this move, return true;
	 * @param cell
	 * @param player
	 * @param oponent
	 * @return
	 */
	public boolean attachCellToPlayer(Cell cell, Player player, Player oponent){
		cell.setPlayer(player);
		this.availableCells.remove(cell);
		this.usedCells.add(cell);
		boolean gameWon = this.checkGameWon(player);
		if(!gameWon){
			player.notifyPickedCell(cell);
			oponent.notifyPickedCell(cell);
		}
		return gameWon;
	}
	
	/**
	 * Checks if Game ended with Tie - No empty cells left
	 * @return
	 */
	public boolean checkGameTie(){
		if(this.availableCells == null || this.availableCells.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if game end and store winner if exists
	 * 
	 * @return
	 */
	public boolean checkGameWon(Player player){
		
		Cell cell = null;
		boolean won = true;
		
		// check rows
		for(int row = 0; row < this.boardWidth; row++){
			cell = null;
			won = true;
			for(int column = 0; column < this.boardWidth; column++){
				cell =  this.board[row][column];
				if(cell.getPlayer() == null || player != cell.getPlayer()){
					won = false;
					break;
				}
			}
			if(won){
				return true;
			}
		}
		
		
		// check columns
		for(int column = 0; column < this.boardWidth; column++){
			cell = null;
			won = true;
			for(int row = 0; row < this.boardWidth; row++){
				cell =  this.board[row][column];
				if(cell.getPlayer() == null || player != cell.getPlayer()){
					won = false;
					break;
				}
			}
			if(won){
				return true;
			}
		}
		
		// check left-to-right diagonal
		cell = null;
		won = true;
		for(int i = 0; i < this.boardWidth; i++){
			cell =  this.board[i][i];
			if(cell.getPlayer() == null || player != cell.getPlayer()){
				won = false;
				break;
			}
		}
		if(won){
			return true;
		}
		
		// check right-to-left diagonal
		cell = null;
		won = true;
		for(int i = 0; i < this.boardWidth; i++){
			cell =  this.board[i][this.boardWidth-1-i];
			if(cell.getPlayer() == null || player != cell.getPlayer()){
				won = false;
				break;
			}
		}
		if(won){
			return true;
		}
		
		return false;
		
	}
	
	
	/**
	 * Print current board
	 */
	public void printBoard(){
	    System.out.println();
	    String rowLine;
	    for(int row = 0; row < this.boardWidth; row++){
	    	//print cells line
	    	rowLine = "";
			for(int column = 0; column < this.boardWidth; column++){
				rowLine += " "+this.board[row][column].getSymbol();
				if(column < this.boardWidth - 1){
					rowLine +=" |";
				}
			}
			System.out.println(rowLine);
			if(row < this.boardWidth - 1){
				// print border line
				for(int column = 0; column < rowLine.length(); column++){
					if(rowLine.charAt(column) == '|'){
						System.out.print("|");
					}else{
						System.out.print("-");
					}
				}
				System.out.println();
			}
		}
	    System.out.println();
	}
	
	public int getTotalSize(){
		return this.boardWidth * this.boardWidth;
	}
	
	
	

	public Cell[][] getBoard() {
		return board;
	}

	public void setBoard(Cell[][] board) {
		this.board = board;
	}

	public List<Cell> getAvailableCells() {
		return availableCells;
	}

	public void setAvailableCells(List<Cell> availableCells) {
		this.availableCells = availableCells;
	}

	public List<Cell> getUsedCells() {
		return usedCells;
	}

	public void setUsedCells(List<Cell> usedCells) {
		this.usedCells = usedCells;
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public void setBoardWidth(int boardWidth) {
		this.boardWidth = boardWidth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((availableCells == null) ? 0 : availableCells.hashCode());
		result = prime * result + Arrays.hashCode(board);
		result = prime * result + boardWidth;
		result = prime * result
				+ ((usedCells == null) ? 0 : usedCells.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (availableCells == null) {
			if (other.availableCells != null)
				return false;
		} else if (!availableCells.equals(other.availableCells))
			return false;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		if (boardWidth != other.boardWidth)
			return false;
		if (usedCells == null) {
			if (other.usedCells != null)
				return false;
		} else if (!usedCells.equals(other.usedCells))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Board [board=" + Arrays.toString(board) + ", availableCells="
				+ availableCells + ", usedCells=" + usedCells + ", boardWidth="
				+ boardWidth + "]";
	}
	
}


