/*----------------------------------------------------------------
 *  Author:        Kevin Zhou
 *  Written:       12/18/2016
 *  Last updated:  12/18/2016
 *
 *  Compilation:   javac Subset.java
 *  Execution:     java 
 *  
 *  
 *
 *
 *----------------------------------------------------------------*/


import edu.princeton.cs.algs4.StdIn;

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> s = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            s.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            System.out.println(s.dequeue() + "");
        }
    }
}