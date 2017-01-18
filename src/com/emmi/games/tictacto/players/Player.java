package com.emmi.games.tictacto.players;

import com.emmi.games.tictacto.core.Board;
import com.emmi.games.tictacto.core.Cell;

public abstract class Player {
	
	// to store player symbol
	private String symbol;
	
	
	public Player(String symbol){
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	//Must be override to determine picking new cell strategies
	public abstract Cell pickACell(Board board);
	
	// To take actions on winning
	public void notifyWin(){
		// Override if Action Needed
	}
	
	//To take actions on loosing
	public void notifyLoose(){
		// Override if Action Needed
	}
	
	//To take actions on tie
	public void notifyTie(){
		// Override if Action Needed
	}
	
	//To take actions when a cell is picked (by current player or oponent)
	public void notifyPickedCell(Cell cell){
		// Override if Action Needed
	}
	
}
