About:
* This is a simple TicTacTo console game, where a human player plays against the computer.
* The computer player learns from its previous losses and avoids following a dead-end (loosing) path.
* This is done by Jehad Bisoomi.

Notes:
* Tracking previous loosing moves-sequence is done by storing data to or reading it from a given text file.
* Each loosing moves-sequence read from a given file is stored along with all its equivalent paths: Rotated Paths (0, 90,180,270) (4 cases), and flipped paths of each case (2 cases for each rotation degree). Thus, each loosing combination line produces another 7 equivalent paths (8 paths in total), which reduces learning trials by 1/8 times. 
* The computer player is assumed to be always the first player. 
* The computer payer is given "@" symbol, while the human player is given "X" symbol.


Project Components:
* GameManager.java: manages the game runtime.
* GameSettings.java: holds the game settings.
* Board.java: holds game board information.
* Cell.java: holds board cell information.
* BoardLevel.java: used to hold and manage old, current and possible moves paths.
* Player.java: player general structure (parent).
* AIPlayer.java: implements the computer player strategies.
* HumanPlayer.java: implements the human player actions.
* Constants.java:  holds game constants. 
