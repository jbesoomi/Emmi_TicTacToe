package com.emmi.games.tictacto.players;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.emmi.games.tictacto.core.Board;
import com.emmi.games.tictacto.core.Cell;

public class HumanPlayer extends Player{
	
	public static final Logger LOGGER = Logger.getLogger(HumanPlayer.class.getName());
	
	private Scanner scanner = new Scanner(System.in);

	public HumanPlayer(String symbol) {
		super(symbol);
	}
	
	@Override
	public Cell pickACell(Board board) {
		
		/*
		 * Pick a cell from user input
		 */
		
		int index;
		Cell cell = null;
        do{
        	try{
        		
        		cell = null;
        		
        		System.out.println("Please pick a Cell "+this.getCellsReadableString(board.getAvailableCells())+", or -1 to exit: ");
                index=scanner.nextInt();
                if(index == -1){
                	System.out.println("Game Canceled!");
                	System.exit(0);
                }
                index--;
            	for(Cell tmp : board.getAvailableCells()){
            		if(tmp.getCellIndex() == index){
            			cell = tmp;
            			break;
            		}
            	}
            	if(cell == null){
            		System.out.println("Invalid/Used Cell: "+(index+1));
            	}
        	}catch(InputMismatchException e){
        		scanner.next();
        		LOGGER.log(Level.WARNING, "Invalid Input!");
        		//skip last input
        		System.out.println("Invalid Input!");
        	}
        	
        }while(cell == null);
		return cell;
	}
	
	/**
	 * used to get a human-readable string representing available cells left
	 * 
	 * @param cellsList
	 * @return
	 */
	private String getCellsReadableString(List<Cell> cellsList){
		
		String res = "[";
		for(Cell cell : cellsList){
			if(res != "["){
				res += ",";
			}
			res += (cell.getCellIndex() + 1);
		}
		res += "]";
		return res;
	}

}
