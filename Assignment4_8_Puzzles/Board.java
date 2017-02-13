import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int n;
    private int[][] initialBoard;
   
    // construct a board from an n-by-n array of blocks (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length; // since it is a square matrix, we can directly use array.length to retrieve the length of the matrix
        initialBoard = copy(blocks);
    } 
    
    // create a copy of the input 
    private int[][] copy(int [][] blocks) {
        int[][] temp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[i][j] = blocks[i][j];
            }
        }
        return temp;
    }
   
    // board dimension n                                       
    public int dimension() {
        return n;
    } 
    
    // number of blocks out of place
    public int hamming() {
        int costH = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               if (initialBoard[i][j] != 0) {
                   if (initialBoard[i][j] != i * n + j + 1) {
                       costH = costH + 1;
                    }
               }
            }
        }
        return costH;
    } 
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
         int costM = 0;
         int temp = 0;
         for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (initialBoard[i][j] != 0) {
                    temp = Math.abs(j - (initialBoard[i][j] - 1) % n);
                    costM = costM + Math.abs(i - (initialBoard[i][j]-1)/n) + temp; 
                }          
            }
         }
         return costM;
    }  
    
    // is this board the goal board?
    public boolean isGoal() {
        boolean isGoal = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (initialBoard[i][j] != 0) {
                    if(initialBoard[i][j] != i * n + j + 1) { isGoal = false; }
                }
            }
        }
        return isGoal;
    } 
    
   // a board that is obtained by exchanging any pair of blocks
 public Board twin() {
        int pos1 = StdRandom.uniform(1, n*n);
        int pos2 = StdRandom.uniform(1, n*n);
        int temp;
        Board twinBoard = new Board(initialBoard);
        while (initialBoard[(pos1-1)/n][(pos1-1) % n] == 0){
            pos1 = StdRandom.uniform(1, n*n);
        }
        while (initialBoard[(pos2-1)/n][(pos2-1) % n] == 0 || pos2 == pos1){
            pos2 = StdRandom.uniform(1, n*n);
        }
        temp = initialBoard[(pos1-1)/n][(pos1-1)%n];
        twinBoard.initialBoard[(pos1-1)/n][(pos1-1)%n] = twinBoard.initialBoard[(pos2-1)/n][(pos2-1)%n];
        twinBoard.initialBoard[(pos2-1)/n][(pos2-1)%n] = temp;
        return twinBoard;
        
    }   
  /*
    public Board twin() {
    	// a board obtained by exchanging two adjacent blocks in the same row
    	int[][] newBoard = copy(initialBoard);
    	boolean flag = false;
    	for (int i = 0; i < n;i++){
    		for (int j = 0;j < n-1;j++){
    			if (newBoard[i][j]>0 && newBoard[i][j+1]>0) {
    				int tmp = newBoard[i][j];
    				newBoard[i][j] = newBoard[i][j+1];
    				newBoard[i][j+1] = tmp;
    				flag = true;
    				break;
    			}
    		}
    		if(flag) break;
    	}
    	return new Board(newBoard);
    }
    */
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null || !(y instanceof Board) || ((Board)y).n != n) return false;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (((Board) y).initialBoard[row][col] != initialBoard[row][col]) return false;
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        
        int[] spaceloc = spaceLocation();
        int row = spaceloc[0];
        int column = spaceloc[1];
       
        if (row > 0) neighbors.push(new Board(swap(row, column, row-1, column)));
        if (row < n-1) neighbors.push(new Board(swap(row, column, row+1, column)));
        if (column > 0) neighbors.push(new Board(swap(row, column, row, column-1)));
        if (column < n-1) neighbors.push(new Board(swap(row, column, row, column+1)));
        
        return neighbors;
    } 
    
    private int[] spaceLocation() {
            int[] location = new int[2];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                 if (initialBoard[i][j] == 0){
                     location[0] = i;
                     location[1] = j;
                 }
                }
            }
            return location;
    }
        
    private int[][] swap(int row1, int column1, int row2, int column2) {
        int[][] copy = copy(initialBoard);
        copy[row1][column1] = initialBoard[row2][column2];
        copy[row2][column2] = initialBoard[row1][column1];
        return copy;
    }
    
   
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dimension() + "\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++)
                str.append(String.format("%2d ", initialBoard[row][col]));
            str.append("\n");
        }
        return str.toString();
    }  
    
    public static void main(String[] args) {  
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);
    
    StdOut.println(initial);
    StdOut.println(initial.manhattan());
    StdOut.println("---------");
    for(Board ex:initial.neighbors()){
        StdOut.println(ex);
    }
    }
   
}