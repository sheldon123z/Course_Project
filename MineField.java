// Name:
// USC NetID:
// CS 455 PA3
// Spring 2019
import java.util.Random;
import java.util.Arrays;
/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {
   
   // <put instance variables here>
   private int numRows;
   private int numCols;
   private int numMines;
   private boolean[][] mineField;
   private Random generater;
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will corresponds to the number of 'true' values in mineData.
    * @param mineData  the data for the mines; must have at least one row and one col.
    */
   public MineField(boolean[][] mineData) {
	   /*initialize the instance variable*/
	   numRows = mineData.length;
	   numCols = mineData[0].length;
	   mineField = new boolean[numRows][numCols]; 
	   generater = new Random();
	   
	   //initialize the mainfield array a input
	   for(int row = 0; row < mineData.length; row++)
	   {
		   for(int col = 0;col < mineData[0].length; col++)
		   {
			   mineField[row][col] = mineData[row][col];
			   
			   if(hasMine(row,col))
			   {
				   numMines++;
			   }
		   }
	   }
      
   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
	   
      this.numRows = numRows;
      this.numCols = numCols;
      this.numMines = numMines;
      mineField = new boolean[numRows][numCols]; 
      generater = new Random();
      
      for(int i = 0; i < numRows; i++)
      {
    	  for(int j = 0; j< mineField[1].length; j++)
    	  {
    		  mineField[i][j] = false;
    	  }
      }
         
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col)
    */
   public void populateMineField(int row, int col) {
	   //loop through the array and set all locations as false
      for(int i=0; i < mineField.length; i++)
      {
    	  for(int j = 0; j < mineField[0].length; j++)
    	  {
    		  if(mineField[i][j])
    		  {
    			  mineField[i][j] = false;
    		  }
    	  } 	      	  
      }
      int counter = 0;
      while(counter != this.numMines)
      {
    	  int randRow = generater.nextInt(this.numRows);
    	  int randCol = generater.nextInt(this.numRows);
    	  if((randRow == row) && (randCol == col) || mineField[randRow][randCol] == true)
    	  {
    		  continue;
    	  }
    	  else
    	  {
    		  //set a mine to the position
    		  mineField[randRow][randCol] = true; 
    		  counter++;
    	  }
      }
   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state the minefield is in at the beginning of a game.
    */
   public void resetEmpty() {
	   for(int i = 0; i < mineField.length; i++)
	   {
		   for(int j = 0; j < mineField[0].length; j++)
		   {
			   mineField[i][j] = false;
		   }
	   }
     
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
	  //Using a integer to track the number of adjacent mines 
	  int adjacentMines = 0;
	  
	  if(hasMine(row-1,col-1) && inRange(row-1,col-1)) //check left top corner square, the coordinate should be in range 
	  {													//or just skip checking
		  adjacentMines++;
	  }
	  if(hasMine(row,col-1) && inRange(row,col-1)) // check left
	  {
		  adjacentMines++;
	  }
	  if(hasMine(row+1,col-1) && inRange(row+1,col-1))//check left bottom
	  {
		  adjacentMines++;
	  }
	  if(hasMine(row-1,col) && inRange(row,col))// check top 
	  {
		  adjacentMines++;
	  }
	  if(hasMine(row+1,col) && inRange(row+1,col))//check bottom
	  {
		  adjacentMines++;
	  }
	  if(hasMine(row-1,col+1) && inRange(row-1,col+1))//check right top corner
	  {
		  adjacentMines++;
	  }
	  if(hasMine(row,col+1) && inRange(row,col+1))//check right 
	  {
		  adjacentMines++;
	  }
	  if(hasMine(row+1,col+1) && inRange(row+1,col+1))//check right bottom corner
	  {
		  adjacentMines++;
	  }
	
      return adjacentMines;       //return adjacent mines
    
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
	   if(row < this.numRows && row >= 0 && col < this.numCols && col >= 0)
	   {
		   return true;
	   }
      return false;      
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {   
	   
      return this.numRows;       
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
	   
      return this.numCols;       
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
	   if(inRange(row,col))
	   {
		 return mineField[row][col];
	   }
	   return false;
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
      return numMines;       
   }

   
   // <put private methods here>
   
         
}

