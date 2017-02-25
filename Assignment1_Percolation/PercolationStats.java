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


import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
   private double[] count;   // record each # of opened sites in each trial to percolate a system
   
   // perform trials independent experiments on an n-by-n grid
   public PercolationStats(int n, int trials) {
      if (n <= 0 || trials <= 0)
         throw new IllegalArgumentException("Given N <= 0 || T <= 0");
      int i = 0;           //# of current test
      count = new double[trials];
      while (i < trials) {
         Percolation pe = new Percolation(n);
         while (!pe.percolates()) {
            int row = StdRandom.uniform(1, n+1);
            int col = StdRandom.uniform(1, n+1);
            if (!pe.isOpen(row, col)) {
               pe.open(row, col);
               count[i]++;
            }
         }
         count[i] = count[i]/(n*n);
         i++;
      }
   }
   // sample mean of percolation threshold
   public double mean() {
      return StdStats.mean(count);
   }
   // sample standard deviation of percolation threshold
   public double stddev() {
      return StdStats.stddev(count);
   }
   // low  endpoint of 95% confidence interval
   public double confidenceLo() {
      return StdStats.mean(count) - 1.96*stddev()/Math.sqrt(count.length);
   }
   // high endpoint of 95% confidence interval
   public double confidenceHi() {
      return StdStats.mean(count) + 1.96*stddev()/Math.sqrt(count.length);
   }                  
   
   // test client (described below)
   public static void main(String[] args) {
      int n = Integer.parseInt(args[0]);
      int t = Integer.parseInt(args[1]);
      
      PercolationStats PS = new PercolationStats(n, t);
      System.out.println("mean                    = " + PS.mean() + "\n");
      System.out.println("stddev                  = " + PS.stddev() + "\n");
      System.out.println("95% confidence interval = " + PS.confidenceLo() + " , " + PS.confidenceHi());
      }   
}   
