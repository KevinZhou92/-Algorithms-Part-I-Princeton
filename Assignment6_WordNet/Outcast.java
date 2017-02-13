import edu.princeton.cs.algs4.*;

public class Outcast {
   private WordNet WORDNET;
   
   // constructor takes a WordNet object
   public Outcast(WordNet wordnet) {
         this.WORDNET = wordnet;
   }    
   
   public String outcast(String[] nouns) {
      if (nouns == null || nouns.length == 0) {
         throw new IllegalArgumentException("!");
      }
      
      String leastRelated = null;
      int maxDistance = Integer.MIN_VALUE;
      int sum = 0;
      
      for (String noun : nouns) {
         for (String noun1 : nouns) {
            sum += WORDNET.distance(noun, noun1);
         }
         if (sum > maxDistance) {
            maxDistance = sum;
            leastRelated = noun;
         }
         sum = 0;
      }
      return leastRelated;
   }
   
   public static void main(String[] args) {
      WordNet wordnet = new WordNet(args[0], args[1]);
      Outcast outcast = new Outcast(wordnet);
      for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
      }
   }
}