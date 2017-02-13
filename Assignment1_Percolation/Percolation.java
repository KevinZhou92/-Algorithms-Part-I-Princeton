/*----------------------------------------------------------------
 *  Author:        Kevin Zhou
 *  Written:       11/18/2016
 *  Last updated:  11/17/2016
 *
 *  Compilation:   javac Percolation.java
 *  Execution:     java Percolation
 *  
 *  Test Percolation of a system
 *
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private boolean[][] opened;
    private int top = 0;
    private int bottom;
    private int size;
    private WeightedQuickUnionUF qf;
    private WeightedQuickUnionUF qfback;

   
   // create n-by-n grid, with all sites blocked,'-1' denote blocked
   public Percolation(int n) {
      if (n <= 0)
         throw new IllegalArgumentException("Given N <= 0 || T <= 0");
      size = n;
      bottom = size * size + 1;
      qf = new WeightedQuickUnionUF(size * size + 2);
      qfback = new WeightedQuickUnionUF(size * size + 1);
      opened = new boolean[size][size];
   }
   
   // convert coordinates to 1d 
   private int getIndex(int x, int y) {
      return size*(x - 1) + y;
   }
   
   // open site (row, col) if it is not open already
   public void open(int i, int j) {
      opened[i - 1][j - 1] = true;
      
      if (i == 1) {
            qf.union(getIndex(i, j), top);
            qfback.union(getIndex(i, j), top);
        }
        if (i == size) {
            qf.union(getIndex(i, j), bottom);
        }
        if (j > 1 && isOpen(i, j - 1)) {
            qf.union(getIndex(i, j), getIndex(i, j - 1));
            qfback.union(getIndex(i, j), getIndex(i, j - 1));
        }
        if (j < size && isOpen(i, j + 1)) {
            qf.union(getIndex(i, j), getIndex(i, j + 1));
            qfback.union(getIndex(i, j), getIndex(i, j + 1));
        }
        if (i > 1 && isOpen(i - 1, j)) {
            qf.union(getIndex(i, j), getIndex(i - 1, j));
            qfback.union(getIndex(i, j), getIndex(i - 1, j));
        }
        if (i < size && isOpen(i + 1, j)) {
            qf.union(getIndex(i, j), getIndex(i + 1, j));
            qfback.union(getIndex(i, j), getIndex(i + 1, j));
        }
   }       
   
    // is site (row, col) open?
   public boolean isOpen(int row, int col) {
        return opened[row-1][col-1]; 
   } 
   
   public boolean isFull(int row, int col) {
      if (0 < row && row <= size && 0 < col && col <= size) {
         return qf.connected(top, getIndex(row, col)) && qfback.connected(top, getIndex(row, col));
     } else {
         throw new IndexOutOfBoundsException();
     }
   }  // is site (row, col) full?
   
   public boolean percolates() {
      return qf.connected(top, bottom);
   }              // does the system percolate?
}