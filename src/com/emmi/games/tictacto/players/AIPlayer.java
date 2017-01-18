package com.emmi.games.tictacto.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.emmi.games.tictacto.core.Board;
import com.emmi.games.tictacto.core.BoardLevel;
import com.emmi.games.tictacto.core.Cell;

public class AIPlayer extends Player {
	
	public static final Logger LOGGER = Logger.getLogger(AIPlayer.class.getName());
	
	private int boardWidth;
	
	private int totalSize;
	
	//Name of the file used to store loosing combinations
	private String fileName;
	
	//Store paths and their status
	private BoardLevel baseLevel;
	
	private String currentMovesPath = "";
	
	//holde current board level
	private BoardLevel currentLevel;
	
	/**
	 * 
	 * @param symbol
	 * @param boardWidth
	 * @param fileName
	 * @throws Exception
	 */
	public AIPlayer(String symbol, int boardWidth, String fileName) throws Exception{
		super(symbol);
		this.boardWidth = boardWidth;  
		this.totalSize = boardWidth * boardWidth;
		this.fileName = fileName;
		this.setupLoosingPaths();
	}
	
	@Override
	public Cell pickACell(Board board) {
		
		//Pause for 0.5 second delay [just to make it more realistic]
//        try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			LOGGER.log(Level.WARNING, "Failed to apply AI delay!");
//		}
		
		/*
		 * AI Picking Cell Strategy: 
		 * 1. Check available empty cells in game board.
		 * 		a. if cell is open (not dead-end from previous loosing combinations), 
		 * 				add it to possibleCells list
		 * 2. select a randomCell from possibleCells
		 * 
		 */
		
		List<Cell> possibleCells = new ArrayList<Cell>();
		for(Cell cell : board.getAvailableCells()){
			if(this.currentLevel.isTargetCellOpen(cell)){
				possibleCells.add(cell);
			}
		}
		int randomIndex = ThreadLocalRandom.current().nextInt(0, possibleCells.size());
		return possibleCells.get(randomIndex);
	}
	
	@Override
	public void notifyPickedCell(Cell cell) {
		/*
		 * When a cell is picked, add it to the moves sequential path
		 */
		this.addMove(cell);
	}
		
	/**
	 * Add cell to moves sequential path
	 * 
	 * @param cell
	 * @return
	 */
	public boolean addMove(Cell cell){
		if(this.currentMovesPath != ""){
			this.currentMovesPath += "-";
		}
		this.currentMovesPath += cell.toPairString();
		this.currentLevel = this.currentLevel.addNextLevel(cell, false);
		return true;
	}
	
	/**
	 * Initialize Loosing Combinations Paths (from source fileName)
	 * @throws Exception
	 */
	public void setupLoosingPaths() throws Exception{
		
		this.baseLevel = new BoardLevel(new Cell(0,0, this.totalSize), this.totalSize, 0, false);
		this.currentLevel = this.baseLevel;
		
		//read data from file
		this.readFromFile(this.fileName);
		
		//debugging prints
//		for(BoardLevel level : this.baseLevel.getNextBoardLevels()){
//			level.printDeadEnds("");
//		}
		
		
	}
	
	/**
	 * Add given path with it's equivalents paths. There could be 8 possibilities: 
	 * >> 0, 90, 180, 270 degrees rotated paths.
	 * >> Flipping of each rotated paths.
	 * This can reduce the total trials by 1/8 times 
	 * @param cellsPath
	 */
	public void addPathWithEquivalents(List<Cell> cellsPath){
		
		//For debugging prints
//		String  str= "";
//		for(Cell cell : cellsPath){
//			if(str != ""){
//				str += ",";
//			}
//			str += cell.getCellIndex();
//		}
//		System.out.println("============ "+str);
		
		// Add Original Path
		cellsPath = this.addPath(cellsPath, false, false);
		
		// Add Original Path - Flipped
		this.addPath(cellsPath, false, true);
		
		// Add 90 CCW rotated Path
		cellsPath = this.addPath(cellsPath, true, false);
		
		// Add 90 CCW rotated Path - Flipped
		this.addPath(cellsPath, false, true);
		
		// Add 180 CCW rotated Path (90CCW rotation of previously rotated 90CCW)
		cellsPath = this.addPath(cellsPath, true, false);
		
		// Add 180 CCW rotated Path (90CCW rotation of previously rotated 90CCW) - Flipped
		this.addPath(cellsPath, false, true);
		
		// Add 270 CCW rotated Path (90CCW rotation of previously rotated 180CCW)
		cellsPath = this.addPath(cellsPath, true, false);
		
		// Add 270 CCW rotated Path (90CCW rotation of previously rotated 180CCW) - Flipped
		this.addPath(cellsPath, false, true);
		
	}
	
	/**
	 * Add given path after applying required rotation/flipping. 
	 * Returns the resulted path (after applying rotation/flipping) 
	 * @param cellsPath
	 * @param rotate
	 * @param flip
	 * @return
	 */
	public List<Cell> addPath(List<Cell> cellsPath, boolean rotate, boolean flip){
		
		List<Cell> addedCellsPath = new ArrayList<Cell>();
		
		Cell cell = null;
		BoardLevel lastLevel = this.baseLevel;
		String str = "";
		for(int i=0; i < cellsPath.size(); i++){
			cell = cellsPath.get(i);
			if(rotate){
				cell = cell.getRotatedCell_90CCW();
			}
			if(flip){
				cell = cell.getFlippedCell();
			}
			lastLevel = lastLevel.addNextLevel(cell, (i == cellsPath.size() - 1));
			addedCellsPath.add(cell);
			
			//for debugging
			if(str != ""){
				str += "-";
			}
			str+= cell.getCellIndex();
		}
		//System.out.println("ADDED PATH: "+str);
		return addedCellsPath;
		
	}
	
	/**
	 * Process loosing combination line:
	 * It should be of the following format (0,1)-(1,2)....etc
	 * @param line
	 */
	public void processLoosingCombinationLine(String line){
		
		if(line == null){
			return;
		}
		
		// Trim line to remove any trailing white spaces
		line = line.trim();
		
		// replace first non-digit characters from string 
		//  (to remove leading empty string resulting from string.split() )
		line = line.replaceFirst("[\\D]+", "");
		
		// Split line by non digits (to keep digits only)
		String[] indexesArray = line.split("[\\D]+");
		
		// if indexesArray is not of even-length (pairs of integers), then it's invalid line
		if(indexesArray == null || indexesArray.length % 2 != 0 ){
			LOGGER.log(Level.WARNING, "Skipping Invalid Line - invalid Pairs Format : "+line);
			return;
		}
		
		
		/*
		 * pairs (cells) count = indexesArray.length / 2
		 * if cells count > totalSize : invalid Line 
		 */
		int pathLength = (indexesArray.length / 2);
		if(pathLength > this.totalSize){
			LOGGER.log(Level.WARNING, "Skipping Invalid Line - invalid Moves Count : "+line);
			return;
		}
		
		/*
		 * reaching here: indexesArray should be holding rows and columns values in sequence
		 */
		int row = 0;
		int column = 0;
		List<Cell> cellsPath = new ArrayList<Cell>();
		Cell cell = null;
		int moveCounts = 0;
		for(int i=0; i< indexesArray.length; i++){
			//System.out.print("*R*"+indexesArray[i]+" ");
			row = Integer.valueOf(indexesArray[i++]);
			//System.out.print("*C*"+indexesArray[i] + " ");
			column = Integer.valueOf(indexesArray[i]);
			
			// if row/column > boardWidth - 1, then it's invalid line
			if(row > this.boardWidth - 1 || column > this.boardWidth -1 ){
				LOGGER.log(Level.WARNING, "Skipping Invalid Line - invalid Pair Values: "+line);
				return;
			}
			
			cell = new Cell(row, column, this.boardWidth);
			
			moveCounts++;
			// if this is odd move (first move, third move, ....etc) => It's AI player
			// else, it's the other player
			if(moveCounts % 2 != 0){
				cell.setPlayer(this);
			}
			
			//System.out.println(">>"+cell.getCellIndex());
			cellsPath.add(cell);
			
		}
		//Add cellsPath with it's equivalents (rotated and flipped paths)
		this.addPathWithEquivalents(cellsPath);
	}
	
	
	@Override
	public void notifyLoose() {
		try {
			this.appendLineToFile(this.fileName, this.currentMovesPath);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to NotifyLoose!", e);
			e.printStackTrace();
		}
	}

	// Append new line to given text file
	public void appendLineToFile(String fileName, String line)
			throws Exception {

		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;

		try {

			// Open target file (using fileName)
			File file = new File(fileName);

			// Check if file exists: if not, create new one
			if (!file.exists()) {
				file.createNewFile();
			}

			if (line == null) {
				return;
			}

			if (!line.endsWith("\n")) {
				line += "\n";
			}

			// Get a fileWriter object with setting "append" flag to True (to
			// append new lines)
			fileWriter = new FileWriter(file.getAbsoluteFile(), true);

			// Get a bufferendWriter object from fileWriter
			bufferedWriter = new BufferedWriter(fileWriter);

			// Write (append) line to the file
			bufferedWriter.write(line);

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to Write to Storage File", e);
			throw e;
		} finally {
			// Close opened streams
			try {

				if (bufferedWriter != null) {
					bufferedWriter.close();
				}

				if (fileWriter != null) {
					fileWriter.close();
				}

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Failed to Close Writing Streams", e);
				throw e;
			}
		}

	}

	// Read lines from given text file, process each line on the run
	public void readFromFile(String fileName) throws Exception {

		FileInputStream fileInputStream = null;
		BufferedReader bufferReader = null;

		try {

			// Open input file (using fileName)
			File file = new File(fileName);

			// Check if file exists: if not, throw exception
			if (!file.exists()) {
				LOGGER.log(Level.WARNING, "Input File Doesn't Exist! Creating new one");
				file.createNewFile();
//				Exception e = new Exception("Input file is not found! "
//						+ "Please make sure file exists and is accessable! "
//						+ "[Given fileName: " + fileName + "]");
//				LOGGER.log(Level.SEVERE, null, e);
//				throw e;
			}

			// Get Input Stream from file
			fileInputStream = new FileInputStream(file);

			// Get BufferReader of the fileInputStream
			bufferReader = new BufferedReader(new InputStreamReader(
					fileInputStream));

			String line;

			// Read Line By Line using the bufferedReader
			while ((line = bufferReader.readLine()) != null) {
				//System.out.println(line);
				// Process Lossing Combination Line
				this.processLoosingCombinationLine(line);
			}

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to Read From Storage File", e);
			throw e;
		} finally {
			// Close opened streams
			try {

				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (bufferReader != null) {
					bufferReader.close();
				}

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Failed to Close Reading Streams", e);
				throw e;
			}
		}
	}
	
	/**
	 * For debugging: print paths
	 */
	public void printAll(){
		for(BoardLevel level: this.baseLevel.getNextBoardLevels()){
			level.printAll("");
		}
		System.out.println("-------- "+this.currentMovesPath);
	}


}
