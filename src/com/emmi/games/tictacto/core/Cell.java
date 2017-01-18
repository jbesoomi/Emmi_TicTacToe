package com.emmi.games.tictacto.core;

import com.emmi.games.tictacto.players.Player;

public class Cell {
	
	private int row;
	
	private int column;
	
	private int boardWidth;
	
	public Player player;
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @param boardWidth
	 */
	public Cell(int row, int column, int boardWidth){
		this.row = row;
		this.column = column;
		this.boardWidth = boardWidth;
	}
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @param boardWidth
	 * @param player
	 */
	public Cell(int row, int column, int boardWidth, Player player){
		this.row = row;
		this.column = column;
		this.boardWidth = boardWidth;
		this.player = player;
	}
	
	/**
	 * Returns the number of the cell in the grid
	 * Ranges from 0 to N-1
	 * (where N is the boardWidth)
	 * @return
	 */
	public int getCellIndex(){
		return (this.boardWidth * row) +  column;
	}
	
	/**
	 * Return a new (copied) Cell instance which is rotated by 90 degrees counter clock wise from current cell 
	 * @return
	 */
	public Cell getRotatedCell_90CCW(){
		// Rotation 90CCW of (x,y) = (N-1-y, x)
		// (where N is the boardWidth)
		return new Cell((this.boardWidth - 1 - this.column), this.row , this.boardWidth, this.player);
	}
	
	/**
	 * Return a new (copied)  Cell instance which is flipped horizontally from current cell
	 * @return
	 */
	public Cell getFlippedCell(){
		// Flip Horizontally of (x,y) = (x, N-1-y)
		// (where N is the boardWidth)
		return new Cell(this.row, (this.boardWidth - 1 - this.column) , this.boardWidth, this.player);
	}
	
	
	public String getSymbol(){
		if(this.player == null){
			return "" + (this.getCellIndex() + 1);
		}
		return this.player.getSymbol();
	}
	
	public String toPairString() {
		return "("+this.row+","+this.column+")";
	}
	
	@Override
	public String toString() {
		return ""+(this.getCellIndex());
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public void setBoardWidth(int boardWidth) {
		this.boardWidth = boardWidth;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + boardWidth;
		result = prime * result + column;
		result = prime * result + row;
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
		Cell other = (Cell) obj;
		if (boardWidth != other.boardWidth)
			return false;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
	
	
