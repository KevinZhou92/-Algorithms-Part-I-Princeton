import edu.princeton.cs.algs4.*;


public class SAP {
   private Digraph G;
   
   // constructor takes a rooted DAG as argument
   public SAP(Digraph G) {
         if (G == null) {
            throw new NullPointerException();
         }
         this.G = new Digraph(G);
   }
   
   private int[] solution (Iterable<Integer> q1, Iterable<Integer> q2) {
      ST<Integer, Integer> visited1 = new ST<Integer, Integer>();
      ST<Integer, Integer> visited2 = new ST<Integer, Integer>();
      int shortestPath = Integer.MAX_VALUE;
      int shortestAncestor = -1;
      int depth1 = 0;
      int depth2 = 0;
      int[] result = new int[2];
      
      Queue<Integer>  queue1 = new Queue<Integer>();
      Queue<Integer>  queue2 = new Queue<Integer>();
      
      for(int i : q1) {
         checkRange(i);
         queue1.enqueue(Integer.valueOf(i));
      }
      
      for(int j : q2) {
         checkRange(j);
         queue2.enqueue(Integer.valueOf(j));
      }
      
      while (!queue1.isEmpty() || !queue2.isEmpty()) {
         while (!queue1.isEmpty()) {
            int size = queue1.size();
            for (int i = 0; i < size; i++) {
               int id = queue1.dequeue();
               for (int v : G.adj(id)) {
                  if (visited1.contains(Integer.valueOf(v))) {
                     continue;
                  }
                  queue1.enqueue(Integer.valueOf(v));
               } 
               if (visited1.contains(Integer.valueOf(id))) { // ensure we keep minimum value for each point
                  int existLength = visited1.get(Integer.valueOf(id));
                  if (existLength < depth1) {
                     continue;
                  }
               } 
               visited1.put(Integer.valueOf(id), Integer.valueOf(depth1));
            }
            depth1++;
         }
         
         while (!queue2.isEmpty()) {
            int size = queue2.size();
            for (int i = 0; i < size; i++) {
               int id = queue2.dequeue();
               for (int v : G.adj(id)) {
                  if (visited2.contains(Integer.valueOf(v))) {
                     continue;
                  }
                  queue2.enqueue(Integer.valueOf(v));
               }
              if (visited2.contains(Integer.valueOf(id))) { // ensure we keep minimum value for each point
                     int existLength = visited2.get(Integer.valueOf(id));
                     if (existLength < depth2) {
                        continue;
                     }
               } 
             visited2.put(Integer.valueOf(id), Integer.valueOf(depth2));
            
              
               //calculate the length
            if (visited1.contains(Integer.valueOf(id))) {
               int d1 = visited1.get(Integer.valueOf(id));
               int d2 = visited2.get(Integer.valueOf(id));
               if (d1 + d2 < shortestPath) {
                  shortestPath = d1 + d2; // the increment on depth happens after each search
                  shortestAncestor = id;
               }
            }
         }
         depth2++;
      }
   }
      
      if (shortestPath != Integer.MAX_VALUE) {
         result[0] = shortestPath;
         result[1] = shortestAncestor;
      } else {
         result[0] = -1;
         result[1] = -1;
      }
      
      return result;
   }
   // length of shortest ancestral path between v and w
   public int length(int v, int w) {
      if (v < 0 || w < 0) {
         throw new IndexOutOfBoundsException();
      }
      
      Queue<Integer>  q1 = new Queue<Integer>();
      Queue<Integer>  q2 = new Queue<Integer>();
      q1.enqueue(Integer.valueOf(v));
      q2.enqueue(Integer.valueOf(w));
      
      return solution(q1, q2)[0];
   }

   // a shortest common ancestor of vertices v and w
   public int ancestor(int v, int w) {
      if (v < 0 || w < 0) {
         throw new IndexOutOfBoundsException();
      }
      
      Queue<Integer>  q1 = new Queue<Integer>();
      Queue<Integer>  q2 = new Queue<Integer>();
      q1.enqueue(Integer.valueOf(v));
      q2.enqueue(Integer.valueOf(w));
      
      return solution(q1, q2)[1];
   }

   // length of shortest ancestral path of vertex subsets A and B
   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
      Queue<Integer>  q1 = new Queue<Integer>();
      Queue<Integer>  q2 = new Queue<Integer>();
      for (int i : subsetA) {
         q1.enqueue(Integer.valueOf(i));
      }
      for (int j : subsetB) {
         q2.enqueue(Integer.valueOf(j));
      }
      
      return solution(q1, q2)[0];
   }

   // a shortest common ancestor of vertex subsets A and B
   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
      Queue<Integer>  q1 = new Queue<Integer>();
      Queue<Integer>  q2 = new Queue<Integer>();
      for (int i : subsetA) {
         q1.enqueue(Integer.valueOf(i));
      }
      for (int j : subsetB) {
         q2.enqueue(Integer.valueOf(j));
      }
      
      return solution(q1, q2)[1];
}
   
   
    private void checkRange(int arg) {
        if (null == this.G) throw new java.lang.NullPointerException();
        if (arg >= this.G.V() || arg < 0) {
            throw new IndexOutOfBoundsException();
        }
    }


   // do unit testing of this class
   public static void main(String[] args) {
      In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        System.out.println("graph constructs finished");
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
      }
   }
}