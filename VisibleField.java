// Name:
// USC NetID:
// CS 455 PA3
// Spring 2019


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield), Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // Covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares:blank
   public static final int MINE_GUESS = -2;// yellow
   public static final int QUESTION = -3;//question mark
   
   // Uncovered states (all non-negative values): 0-8
   public static final int NO_ADJACENT_MINE = 0;
   // values in the range [0,8] corresponds to number of mines adjacent to this square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game) black
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game X sign
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose) red one
   // ----------------------------------------------------------   
  

   private int numCols;	//count the number of columns of the visible field
   private int numRows;	//count the number of rows of the visible field
   private int state[][]; // to tracing the state of every square in the visible field
   private int numMineLeft;// tracing how many the guessed mine left 
   private MineField underlyingField;//underlying mineField of visible field


   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the mines covered up, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
	  //initialize instance variables by using mineField parameter
	  this.numRows = mineField.numRows();
	  this.numCols = mineField.numCols();
      numMineLeft = mineField.numMines();
      this.underlyingField = mineField;
      
	  state = new int[numRows][numCols];
      
      for(int i = 0; i < numRows; i++)	//initialize all mines as COVERED status in the very beginning
      {
    	  for(int j = 0; j < numCols; j++)
    	  {
    		  state[i][j] = COVERED;
    	  }
      }

   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
	   
	   this.numRows = getMineField().numRows();
	   this.numCols = getMineField().numCols();
	   numMineLeft = getMineField().numMines();
	
	   for(int i = 0; i < numRows; i++) //same as the constructor does, set all square status as COVERED
	   {
		   for(int j = 0; j < numCols; j++)
		   {
			   state[i][j] = COVERED;
		   }
	   }  
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {

      return underlyingField;       
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return state[row][col];       
   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  So the value can
      be negative, if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
	   int guessedMine = 0;
	   for(int i = 0; i < numRows; i++)
	   {
		   for(int j = 0; j < numCols; j++)
		   {
			   if(getStatus(i,j) == MINE_GUESS)
			   {
				   guessedMine++;
			   }
		   }
	   }
      return numMineLeft - guessedMine;       
   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      
	   if(state[row][col] == COVERED)
       {
    	  state[row][col] = MINE_GUESS;
    	  //numMineLeft--;
       }
       else if(state[row][col] == MINE_GUESS)
       {
     	  state[row][col] = QUESTION;
     	  //numMineLeft++;
       }
       else if(state[row][col] == QUESTION)
       {
    	  state[row][col] = COVERED;
       }
       else
       {
    	  return;  
       }
    	
  }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
	  
	 if(!underlyingField.inRange(row, col))
	 {
		 return false;
	 }
	 else if(getMineField().hasMine(row, col))
	 {
		 state[row][col] = EXPLODED_MINE;
		 return false;
	 }
	 else
	 {
		 unCoverNextSquare(row,col);
		 return true;
	 }
	
   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game over
    */
   public boolean isGameOver() {
	   //we have two situations lead to the end of the game 
	  boolean gameWin = false; // one is the winning situation 
	  boolean gameLose = false;//another is the lose situation
	  int coveredNumber = numCols*numRows; // the total covered number of squares is the number of columns * rows
	  
	  for(int i = 0; i <numRows; i++)//loop through the matrix, once find exploded mine then the game is over right away
	  {
		  for(int j = 0; j < numCols; j++)
		  {
			  if(state[i][j] == EXPLODED_MINE)
			  {
				  gameLose = true;// the game loses, and set the game lose interface
				  gameLoseSet();//set the game losing interface
				  return gameLose;
			  }
			  else if(isUncovered(i,j))//once the game is not lose, then only win or the game is not end yet
			  {
				  coveredNumber--;
			  }			  
		  }
	  }
	  if(coveredNumber == getMineField().numMines())// if the covered number equals to mine's number, 
	  {											   //then it indicates the game wins
		  gameWin=true;
		  gameWinSet();						//set the wining interface
		  return gameWin; 
	  }

      return false;       //not win neither lose then return false to indicate the game is still in process
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
	   if(getStatus(row,col) > COVERED)
	   {
		   return true;
	   }
	   else
	   {
		   return false;   
	   }
	      
   }
   
 
   // <put private methods here>
   private void unCoverNextSquare(int row, int col){
	
	   int numOfAdjacentMines = getMineField().numAdjacentMines(row, col);
	
	   if(isGameOver()) //if game is over during the uncovering process then return 
	   {
		   return;
	   }
	   else if(!underlyingField.inRange(row, col)|| getStatus(row,col) == QUESTION || getStatus(row,col) == MINE_GUESS || isUncovered(row,col) )
	   {			// if game is not over then check the square to be checked if is in range and if is
		   return; // marked as QUESTION as well as if is uncovered already
	   }
	   else if(numOfAdjacentMines == NO_ADJACENT_MINE)
	   {
			state[row][col] = NO_ADJACENT_MINE; //mark the next square as no adjacent mine
			unCoverNextSquare(row-1,col); //top 
			unCoverNextSquare(row-1,col+1); // top-right
			unCoverNextSquare(row,col+1);//right
			unCoverNextSquare(row+1,col+1);//right - bottom 
			unCoverNextSquare(row+1,col);// bottom
			unCoverNextSquare(row+1,col-1);//left-bottom
			unCoverNextSquare(row,col-1);//left
			unCoverNextSquare(row-1,col-1);	//left-top
			
			return;
	   }
	   else
	   {
			state[row][col] = numOfAdjacentMines;
			return;
	   }
	
   }
/**
 * When the game wins, show all unguessed mines as MINE_GUESS status
 * 
 * */
	private void gameWinSet()
	 {	
		for(int i = 0; i < numRows; i++)
		  {
			  for(int j = 0; j < numCols; j++)
			  {
				if(getMineField().hasMine(i,j))
				{
					state[i][j] = MINE_GUESS;
				}
			  }
			  
		  }
	
	 }
/**
 * when the game loses, show the mines' location by setting their status as MINE,
 * and if the user has ever set any wrong guessing mines,
 * show them up by setting the wrong guesses as INCORRECT_GUESS status
 * */
	private void gameLoseSet() 
	 {
		for(int i = 0; i < numRows; i++)
		{
			for(int j = 0; j < numCols; j++)
			{
				if((state[i][j] == COVERED || state[i][j] == QUESTION) && getMineField().hasMine(i,j))
				{
					state[i][j] = MINE; //if the user ignored this location which has a mine, then set this location as MINE status
				}
				else if((state[i][j] == MINE_GUESS) && (!getMineField().hasMine(i, j))) // if the user set it as mine but the loc does not a mine 
				{												//then set it as INCORRECT_GUESS status
					state[i][j] = INCORRECT_GUESS;			}
			}
		}
		
	 }
   
}
