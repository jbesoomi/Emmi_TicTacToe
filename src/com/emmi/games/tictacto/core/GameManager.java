package com.emmi.games.tictacto.core;

import java.util.logging.Logger;

import com.emmi.games.tictacto.players.AIPlayer;
import com.emmi.games.tictacto.players.HumanPlayer;
import com.emmi.games.tictacto.players.Player;

public class GameManager {
	
	public static final Logger LOGGER = Logger.getLogger(GameManager.class.getName());
	
	private GameSettings gameSettings;
	
	
	private Board board;
	
	private Player humanPlayer;
	private Player aiPlayer;
	
	private Player currentPlayer;
	private Player otherPlayer;
	
	
	public GameManager(){
		this.setGameSettings(new GameSettings());
	}
	
	public GameManager(GameSettings gameSettings){
		this.setGameSettings(gameSettings);
	}
	
	/**
	 * Initialize Game
	 * 
	 * @throws Exception
	 */
	public void initGame() throws Exception{
		System.out.println("Initializing Game...");
		this.board = new Board(this.gameSettings.getBoardSize());
		this.aiPlayer = new AIPlayer("@", this.gameSettings.getBoardSize(), this.gameSettings.getStorageFileName());
		this.humanPlayer = new HumanPlayer("X");
		//Assume First Player is Always AI
		this.setCurrentPlayer(this.aiPlayer);
		this.setOtherPlayer(this.humanPlayer);
		System.out.println("Game Ready!");
	}
	
	/**
	 * Switch Current Player
	 */
	public void togglePlayers(){
		if(this.getCurrentPlayer() == this.aiPlayer){
			this.setCurrentPlayer(this.humanPlayer);
			this.setOtherPlayer(this.aiPlayer);
		}else{
			this.setCurrentPlayer(this.aiPlayer);
			this.setOtherPlayer(this.humanPlayer);
		}
	}
	
	/**
	 * Start new Game
	 * 
	 * @throws Exception
	 */
	public void startNewGame() throws Exception{
		//initialize game
		this.initGame();
		System.out.println("\nGame Started!");
		
		//Print initial board
		this.board.printBoard();
		
		//Game Loop: break if game ends with tie or a player win
		Cell cell;
		boolean gameWon = false;
		while(!this.board.checkGameTie()){
			
			//Let current player pick a cell from board available cells
			System.out.println("Player ["+this.getCurrentPlayer().getSymbol()+"] turn.. ");
			cell = this.getCurrentPlayer().pickACell(board);
			
			//Apply move by player to the picked cell
			gameWon = board.attachCellToPlayer(cell, this.getCurrentPlayer(), this.getOtherPlayer());
			System.out.println("Player ["+this.getCurrentPlayer().getSymbol()+"] picked cell: "+(cell.getCellIndex()+1));
			
			//Print Current Board
			this.board.printBoard();
			
			//If game won by a player, end it
			if(gameWon){
				System.out.println("\n\n >>> Game Ended: "
						+ "Player ["+this.getCurrentPlayer().getSymbol()+"] won the game! <<<\n\n");
				
				//Notify players with result
				this.getCurrentPlayer().notifyWin();
				this.getOtherPlayer().notifyLoose();
				break;
			}
			
			//Switch Current Player
			this.togglePlayers();
		}
		
		//If reached here with no player won the game: Then it's a tie
		if(!gameWon){
			System.out.println("\n\n === Game Ended As Tie - No Winner! === \n\n");
			//Notify players with result
			this.getCurrentPlayer().notifyTie();;
			this.getOtherPlayer().notifyTie();
		}
		
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public void setGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public Player getHumanPlayer() {
		return humanPlayer;
	}

	public void setHumanPlayer(Player humanPlayer) {
		this.humanPlayer = humanPlayer;
	}

	public Player getAiPlayer() {
		return aiPlayer;
	}

	public void setAiPlayer(Player aiPlayer) {
		this.aiPlayer = aiPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Player getOtherPlayer() {
		return otherPlayer;
	}

	public void setOtherPlayer(Player otherPlayer) {
		this.otherPlayer = otherPlayer;
	}
	
	
	
}
