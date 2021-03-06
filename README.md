# SudokuValidator
Program that utliztes Java Threads in order to validate a Sudoku solution.

Assignment #1 12/02/2021
Jordan Tiedemann
COSC 3407 - Operating Systems

GENERAL USAGE NOTES
(* -> Fully Implemented )
(- -> Incomplete        )
(x -> Planned           )
------------------------------------------------------------------------------------------------------------------
* main method
  * three integer arrays are initialized representing a sudoku solution. Numbers can be changed to test various solutions.
	* by default sudoku and sudoku2 are valid solutions. sudoku3 is an invalid solution.
	* the test results will be printed to console when run
	- the integer arrays are assumed to be size 81 and fully intialized 
	x guards to validate that each integer within the sudoku array are correctly initialized

* enum Region
	* The Region enum is an integer that represents the region to be tested.
	* There are 9 rows, 9 columns and 9 3x3 box regions for a total of 27 checks.
* SudokuValidator
	* this object takes in a board (a 1d integer array representing a sudoku solution) and keep a reference to the solution.
* isValidSudoku
	* this method generates 27 threads using the SudokuChecker class that checks each corresponding region enum
	* generates a boolean array to store the results of each thread validation
	* once each thread is completed- checks the solution array to return is the sudoku is valid or invalid
* SudokuChecker
	* this nested inner class implements the Runnable class to make use of threads in Java
	* parameters store a reference to the board (1d int array) and solution array, as well as a region enum to control which region is checked.
	* performs some guard checks before traversing the 1d array in the region corresponding the the enum specified
	* generates a boolean array that tracks which integer values are found during the iteration of the region to ensure no duplicates are found
	* sets the boolean value in the solution array to false if duplicate values are found or if an invalid number is found
  
NOTES ABOUT USING THE PROGRAM: 
- sudoku integer array are initialized in main and can be changed for each test you want to perform
- can test any number of sudoku solutions by declaring and initializing the integer array, creating the SudokuValidator object, and then calling the isValidSudoku method
- Sudoku solution integer array are assumed to be size 81 and fully initialized (program does not currently perform null checking when validating the integer array solution)

KNOWN ISSUES: 
- Sudoku solutions that are an integer array of size 81 with uninitialized or null integers within the array will generate errors
------------------------------------------------------------------------------------------------------------------
