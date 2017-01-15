import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
    private Move current;
    
    private class Move implements Comparable<Move> {
        private Board board;
        private Move previous;
        private int moveNo;
        
        public Move(Board initial, Move previousMove) {
            board = initial;
            previous = previousMove;
            if (previous == null) moveNo = 0;
            else {
                moveNo = previous.moveNo + 1;
            }
        }
        
        public int compareTo(Move that) {
            return this.board.manhattan() + this.moveNo - that.board.manhattan() - that.moveNo; 
        }
    }
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Please provide a valid board!");
        current =new Move(initial, null);
        Move current2 =new Move(initial.twin(), null);
        MinPQ<Move> pq = new MinPQ<Move>();
        MinPQ<Move> pq2 = new MinPQ<Move>();
        while ( !current.board.isGoal() && !current2.board.isGoal()) {
            current = expand(current, pq);
            current2 = expand(current2, pq2);  
        }
    } 
    
    private Move expand(Move current, MinPQ<Move> minPQ) {
        for (Board temp : current.board.neighbors()) {
           if (current.previous == null || !temp.equals(current.previous.board)) {
               minPQ.insert(new Move(temp, current));          
            }
        }
        return minPQ.delMin();
    }
        
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return current.board.isGoal()? true : false;
    }  
     
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return current.moveNo;
        else return -1;
    }                     
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> sequence = new Stack<Board>();
        Move lastNode = current;
        while (lastNode != null) {
            sequence.push(lastNode.board);
             lastNode =  lastNode.previous;
        } 
        return sequence;
    }     
    
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
    
        // solve the puzzle
        Solver solver = new Solver(initial);
    
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board + "---------");
        }
    }
}