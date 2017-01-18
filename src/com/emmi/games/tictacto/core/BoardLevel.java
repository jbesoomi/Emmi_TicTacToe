package com.emmi.games.tictacto.core;

import java.util.ArrayList;
import java.util.List;

import com.emmi.games.tictacto.common.Constants.CellState;

public class BoardLevel {
	
	private Cell currentCell;
	
	private int totalSize;
	
	private int level;
	
	private CellState state = CellState.OPEN;
	
	private List<BoardLevel> nextBoardLevels = new ArrayList<BoardLevel>();
	
	/**
	 * 
	 * @param currentCell
	 * @param totalSize
	 * @param level
	 * @param isDeadEnd
	 */
	public BoardLevel(Cell currentCell, int totalSize, int level, boolean isDeadEnd){
		this.currentCell = currentCell;
		this.totalSize = totalSize;
		this.level = level;
		if(isDeadEnd){
			this.state = CellState.DEAD_END;
		}else{
			this.checkIfDeadEnd();
		}
	}
	
	/**
	 * Add a new Level (move)
	 * 
	 * @param cell
	 * @param isDeadEnd
	 * @return
	 */
	public BoardLevel addNextLevel(Cell cell, boolean isDeadEnd){
		BoardLevel targetLevel = null;
		//check if already existing
		for(BoardLevel level : this.nextBoardLevels){
			if(level.getCurrentCell().getCellIndex() == cell.getCellIndex()){
				targetLevel = level;
				break;
			}
		}
		//else, create new
		if(targetLevel == null){
			targetLevel = new BoardLevel(cell, this.totalSize, this.level+1, isDeadEnd);
			this.nextBoardLevels.add(targetLevel);
		}
		
		//check if current level is dead-end after the new modification (will update dead state)
		this.checkIfDeadEnd();
		
		return targetLevel;
	}
	
	/**
	 * Check if targetCell is open 
	 * 
	 * @param targetCell
	 * @return
	 */
	public boolean isTargetCellOpen(Cell targetCell){
		for(BoardLevel level : this.nextBoardLevels){
			if(level.getCurrentCell().getCellIndex() == targetCell.getCellIndex()){
				if(level.getState() == CellState.DEAD_END){
					return false;
				}
				break;
			}
		}
		return true;
	}
	
	public boolean checkIfDeadEnd(){
		
		// If state is DEAD_END, return true
		if(this.state == CellState.DEAD_END){
			return true;
		}
		
		
		// If there are still some other nextBoardLevels available to try, return false 
		if(this.nextBoardLevels.size() < (totalSize-level)){
			return false;
		}
		
		// If any of the next possible paths are open, return false
		for(BoardLevel level : this.nextBoardLevels){
			if(!level.checkIfDeadEnd()){
				return false;
			}
		}
		
		this.state = CellState.DEAD_END;
		return true;
	}

	/**
	 * For Debugging: Print all paths
	 * @param path
	 */
	public void printAll(String path){
		if(path != ""){
			path += "-";
		}
		path += currentCell.getCellIndex();
		System.out.println(path+ " >> "+ this.getState().name());
		for(BoardLevel level : this.nextBoardLevels){
			level.printAll(path);
		}
	}
	
	/**
	 * For Debugging: Print dead-end paths
	 * @param path
	 */
	public void printDeadEnds(String path){
		if(path != ""){
			path += "-";
		}
		path += currentCell.getCellIndex();
		if(this.state == CellState.DEAD_END){
			System.out.println(path+ " >> "+ this.getState().name());
		}
		for(BoardLevel level : this.nextBoardLevels){
			level.printDeadEnds(path);
		}
	}

	public Cell getCurrentCell() {
		return currentCell;
	}

	public void setCurrentCell(Cell currentCell) {
		this.currentCell = currentCell;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public CellState getState() {
		return state;
	}

	public void setState(CellState state) {
		this.state = state;
	}

	public List<BoardLevel> getNextBoardLevels() {
		return nextBoardLevels;
	}

	public void setNextBoardLevels(List<BoardLevel> nextBoardLevels) {
		this.nextBoardLevels = nextBoardLevels;
	}
	
}
