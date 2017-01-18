package com.emmi.games.tictacto.core;

public class GameSettings {
	
	// Default Settings
	private String storageFileName = "3X3.txt";
	private int boardSize = 3;
	
	public String getStorageFileName() {
		return storageFileName;
	}
	
	public void setStorageFileName(String storageFileName) {
		this.storageFileName = storageFileName;
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + boardSize;
		result = prime * result
				+ ((storageFileName == null) ? 0 : storageFileName.hashCode());
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
		GameSettings other = (GameSettings) obj;
		if (boardSize != other.boardSize)
			return false;
		if (storageFileName == null) {
			if (other.storageFileName != null)
				return false;
		} else if (!storageFileName.equals(other.storageFileName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[storageFileName = " + storageFileName
				+ ", boardSize = " + boardSize + "]";
	}
	
}
