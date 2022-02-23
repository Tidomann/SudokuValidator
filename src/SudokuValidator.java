//Jordan Tiedemann
//COSC 3407 - Assignment 1
public class SudokuValidator {
    private final int max = 9;
    private final int NUM_THREADS = 27;
    private int[] board;

    enum Region{
        firstRow(1),
        secondRow(2),
        thirdRow(3),
        fourthRow(4),
        fifthRow(5),
        sixthRow(6),
        seventhRow(7),
        eighthRow(8),
        ninthRow(9),
        firstCol(10),
        secondCol(11),
        thirdCol(12),
        fourthCol(13),
        fifthCol(14),
        sixthCol(15),
        seventhCol(16),
        eighthCol(17),
        ninthCol(18),
        firstBox(19),
        secondBox(20),
        thirdBox(21),
        fourthBox(22),
        fifthBox(23),
        sixthBox(24),
        seventhBox(25),
        eighthBox(26),
        ninthBox(27);

        private int value;
        Region(final int value){
            this.value = value;
        }
        public int getValue(){ return value; }
    }

    public static void main(String[] args) {

        int[] sudoku = new int[]{
                6, 2, 4, 5, 3, 9, 1, 8, 7,
                5, 1, 9, 7, 2, 8, 6, 3, 4,
                8, 3, 7, 6, 1, 4, 2, 9, 5,
                1, 4, 3, 8, 6, 5, 7, 2, 9,
                9, 5, 8, 2, 4, 7, 3, 6, 1,
                7, 6, 2, 3, 9, 1, 4, 5, 8,
                3, 7, 1, 9, 5, 6, 8, 4, 2,
                4, 9, 6, 1, 8, 2, 5, 7, 3,
                2, 8, 5, 4, 7, 3, 9, 1, 6
        };

        int[] sudoku2 = new int[]{
                7, 9, 2, 1, 5, 4, 3, 8, 6,
                6, 4, 3, 8, 2, 7, 1, 5, 9,
                8, 5, 1, 3, 9, 6, 7, 2, 4,
                2, 6, 5, 9, 7, 3, 8, 4, 1,
                4, 8, 9, 5, 6, 1, 2, 7, 3,
                3, 1, 7, 4, 8, 2, 9, 6, 5,
                1, 3, 6, 7, 4, 8, 5, 9, 2,
                9, 7, 4, 2, 1, 5, 6, 3, 8,
                5, 2, 8, 6, 3, 9, 4, 1, 7
        };

        int[] sudoku3 = new int[]{
                5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5
        };



        SudokuValidator test1 = new SudokuValidator(sudoku);
        SudokuValidator test2 = new SudokuValidator(sudoku2);
        SudokuValidator test3 = new SudokuValidator(sudoku3);
        System.out.println("Board 1 is valid: " + test1.isValidSudoku());
        System.out.println("Board 2 is valid: " + test2.isValidSudoku());
        System.out.println("Board 3 is valid: " + test3.isValidSudoku());

    }

    /**
     * Constructor for SudokuValidator.
     * @param board A 1d array that represents the sudoku board
     */
    public SudokuValidator(int[] board){
        this.board = board;
    }

    /**
     * This method uses threads the check each row, column, and box region. Using the enum structure and the boolean
     * array, each thread can check the specified region to see if the solution for that region is valid. Each worker
     * thread is assigned the task of determining the validity of a particular region of the Sudoku puzzle.
     * @return if a region is invalid returns false, otherwise returns true
     */
    public boolean isValidSudoku(){
        if (board.length != 81){
            return false;
        }
        //Create our boolean array to be passed into each thread
        boolean[] solution = new boolean[27];
        //Initialize the boolean array
        //Assume that the solution is valid
        for(int i = 0; i < 27; ++i){
            solution[i] = true;
        }
        //Create threads for each region of the sudoku puzzle
        //9 rows, 9 columns, 9 3x3 boxes
        Thread[] threads = new Thread[NUM_THREADS];
        //For each region, initialize the thread
        int i = 0;
        for(Region region : Region.values()){
            threads[i] = new Thread(new SudokuChecker(board, region, solution));
            ++i;
        }
        //Start the Threads
        for(int j = 0; j < threads.length; ++j){
            threads[j].start();
        }
        //Wait for all threads to die
        for(int j = 0; j < threads.length; j++){
            try {
                threads[j].join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        //Iterate through the solution array to see if one of the regions evaluated to invalid
        for(int j = 0; j < solution.length; ++j){
            if(!solution[j]){//if one of the regions is invalid
                return false;
            }
        }
        return true; //all regions evaluated valid

    }


    /**
     * Nested Inner Class
     */
    public class SudokuChecker implements Runnable{
        private Region check;
        private int[] board;
        private boolean[] solutions;
        public SudokuChecker(int[] board, Region check, boolean[] solutions){
            this.board = board;
            this.check = check;
            this.solutions = solutions;
        }

        @Override
        public void run() {
            //Region boundary check
            if(check.getValue() < 1)
                return;
            if(check.getValue() > solutions.length)//Assumes 9x9 Sudoku Board
                return;

            //Row Checking
            if(check.getValue() <= max){
                //Create array to test that all elements are unique values
                boolean[] unique = new boolean[max];
                //initialize unique array to false
                for(int i = 0; i < max; ++i){
                    unique[i] = false;
                }

                //Traverse the row specified by the region
                for(int i = max * (check.getValue()-1); i < (max * check.getValue()); ++i){
                    //If the value on the board is not within range, sudoku is not valid
                    if(board[i] <= 0 || board[i] > max){
                        solutions[check.getValue()-1] = false;
                        return;
                    }
                    //If we previously found this value in the row return false
                    if(unique[board[i]-1]){
                        solutions[check.getValue()-1] = false;
                        return;
                    }
                    //Mark this value as found
                    unique[board[i]-1] = true;
                }
            }

            //Column Checking
            if(check.getValue() > max && check.getValue() <= 2*max){
                //Create array to test that all elements are unique values
                boolean[] unique = new boolean[max];
                //initialize unique array to false
                for(int i = 0; i < max; ++i){
                    unique[i] = false;
                }

                //Traverse the col specified by the region
                for(int i = 0; i < max; ++i){
                    //Region Value for Columns start at 10
                    //IE Column 1 = 10, subtract 9 to get column value 1
                    int index = (check.getValue() - 9 - 1) + (max*i);
                    //If the value on the board is not within range, sudoku is not valid
                    if(board[index] <= 0 || board[index] > max){
                        solutions[check.getValue()-1] = false;
                        return;
                    }
                    //If we previously found this value in the row return false
                    if(unique[board[index]-1]){
                        solutions[check.getValue()-1] = false;
                        return;
                    }
                    //Mark this value as found
                    unique[board[index]-1] = true;
                }
            }

            //Box Checking
            if(check.getValue() > 2*max){
                //Create array to test that all elements are unique values
                boolean[] unique = new boolean[max];
                //initialize unique array to false
                for(int i = 0; i < max; ++i){
                    unique[i] = false;
                }
                //Region value will always be greater >= 19
                //determine the row that we are checking (row 0, 1 ,2)
                int boxRow = (check.getValue()-19) / 3;
                int boxCol = (check.getValue()-19) % 3;
                //Traverse the box specified by the region
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        //boxRow + boxCol = 1st index of box
                        int index = boxRow*27 + boxCol*3 + row*9 + col;
                        //If the value on the board is not within range, sudoku is not valid
                        if(board[index] <= 0 || board[index] > max){
                            solutions[check.getValue()-1] = false;
                            return;
                        }
                        //If we previously found this value in the row return false
                        if(unique[board[index]-1]){
                            solutions[check.getValue()-1] = false;
                            return;
                        }
                        //Mark this value as found
                        unique[board[index]-1] = true;
                    }
                }
            }
        }//run end
    }//SudokuChecker end
}
