import edu.princeton.cs.algs4.*;

public class WordNet {
    private Digraph G;
    // maps synset id to synsets string
    private ST<Integer, String> idMap;
    // maps nouns to set of synset ids
    private ST<String, SET<Integer>> nounMap;
    private SAP sap;
    
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
      if (synsets == null || hypernyms == null) {
         throw new IllegalArgumentException("Check input!");
      }
      
      In s_in = new In(synsets);
      In h_in = new In(hypernyms);
      idMap = new ST<Integer, String>();
      nounMap = new ST<String, SET<Integer>>();
     
      
      while (s_in.hasNextLine()) {
         String[] line = s_in.readLine().split(",");
         int id = Integer.parseInt(line[0]);
         String[] nouns = line[1].split(" ");
         
         idMap.put(id, line[1]);
         for (String noun : nouns) {
            if (nounMap.contains(noun)) {
               nounMap.get(noun).add(id);
            } else {
               SET<Integer> temp = new SET<Integer>();
               temp.add(id);
               nounMap.put(noun, temp);
            }
         }
      }
      int V = idMap.size();
      
      G = new Digraph(V); 
      while (h_in.hasNextLine()) {
         String[] line2 = h_in.readLine().split(",");
         int root = Integer.parseInt(line2[0]);
         for (int i = 1; i < line2.length; i++) {
            G.addEdge(root, Integer.parseInt(line2[i]));
         }
      }
      
      sap = new SAP(G); // construct all the shortest path in digraph; 
      
       DirectedCycle finder = new DirectedCycle(G);
       if (finder.hasCycle()) {
          throw new IllegalArgumentException("It's a cycled graph!");
       }
   }

   // returns all WordNet nouns
   public Iterable<String> nouns() {
      return nounMap.keys();
   }
   
   // is the word a WordNet noun?
   public boolean isNoun(String word) {
      if (word == null) {
         throw new NullPointerException("Alert! Please Input Something!");
      }
      return nounMap.contains(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
      checkNoun(nounA);
      checkNoun(nounB);
      return sap.length(nounMap.get(nounA), nounMap.get(nounB));
      
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
      checkNoun(nounA);
      checkNoun(nounB);
      int ancestorId = sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));
      return idMap.get(ancestorId);
   }
   
   private void checkNoun(String noun) {
     if (null == noun) throw new NullPointerException();
     if (!isNoun(noun)) throw new IllegalArgumentException();
   }
   
   public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length   = wn.distance(v, w);
            String ancestor = wn.sap(v, w);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }
    }
   
}