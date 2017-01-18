package com.emmi.games.tictacto;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.emmi.games.tictacto.core.GameManager;
import com.emmi.games.tictacto.core.GameSettings;

public class Driver {
	
	public static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		
		try {
			
			System.out.println("\n** Welcome to EMMI TicTacToe AI Game ***\n");
			GameSettings gameSettings = getUserCustomSettings();
			GameManager gameManager = new GameManager(gameSettings);
			
			boolean endGame = false;
			do{
				gameManager.startNewGame();
				System.out.println("\nWould you like to rematch using same settings? (y)");
				char input = scanner.next().charAt(0);
				if(input != 'y'){
					endGame = true;
				}
			}while(!endGame);
			
			System.out.println("\n * Game Ended * Hope to see you again :) - Bye ");
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Application Crashed!", e);
			e.printStackTrace();
		}
		

	}
	
	/**
	 * Get user customized game settings object
	 * @return
	 */
	public static GameSettings getUserCustomSettings(){
		
		GameSettings gameSettings = new GameSettings();
		
		System.out.println("Default Settings: "+gameSettings.toString());
		
		System.out.println("Do you want to use default settings? (y)");
		char input = scanner.next().charAt(0);
		if(input == 'y'){
			return gameSettings;
		}
		
		System.out.println("Please enter loosing combinations file name: (enter -1 to use default)");
		String fileName = scanner.next();
		if(fileName != null && !fileName.equalsIgnoreCase("-1")){
			gameSettings.setStorageFileName(fileName);
		}
		
		do{
			System.out.println("Please enter board edge legth ( at least 3): (enter -1 to use default)");
			int boardWidth = scanner.nextInt();
			if(boardWidth > 2){
				gameSettings.setBoardSize(boardWidth);
				break;
			}else if(boardWidth == -1){
				break;
			}else{
				System.out.println("Invalid edge length! value must be at least 3 !");
			}
		}while(true);
		
		return gameSettings;
	}
	
}