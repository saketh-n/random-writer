// Random Writer generates randomized text based on the style of a given 
// input text. It is based on the Random Writer idea proposed by Claude
// Shannon.
import java.util.*;
import java.io.*;

public class RandomWriter {
   
   public static void main(String[] args) throws FileNotFoundException {
      Map<String, ArrayList<Character>> seeds = 
         new TreeMap<String, ArrayList<Character>>();
      Random r = new Random();
      Scanner console = new Scanner(System.in);
      Scanner input = getFile(console);
      // this is the length in chars of the initial seed
      int k = validInteger(console, "seed");
      // this is the length in chars of the output printed
      int outputLength = validInteger(console, "output");
      readTo(input, seeds, k);
      genRandomText(seeds, outputLength, r, k);
      
      // Allows the user to run the program again
      System.out.println("Go again? (y)");
      if (console.next().toLowerCase().charAt(0) == 'y') {
         main(args);
      }
   }   
   
   // Prompts for input file, prompts again if invalid file is given
   public static Scanner getFile(Scanner console) throws FileNotFoundException {
      System.out.print("Please enter a file to analyze: ");
      File newFile = new File(console.next());
      while (!newFile.exists()) {
         return getFile(console);
      }
      return new Scanner(newFile);
   }
   
   // Keeps prompting asking the user for length of word, until a valid 
   // integer (>= 0) is entered.
   public static int validInteger(Scanner console, String word) {
      int x = -1;
      while (x < 0) {
         System.out.print("Please enter the length of the " + word + ". ");
         System.out.println("Must be >= 0");
         while (!console.hasNextInt()) {
            System.out.print("Please enter the length of the " + word + ". ");
            System.out.println("Must be >= 0");
         }
         x = console.nextInt();
      }
      
      return x;
   }
      
   // reads content of input to a String
   // Creates a list of k-length word 'seeds' which are used to generate
   // randomized text, following the same writing pattern as the provided input
   public static void readTo(Scanner input, 
                             Map<String, ArrayList<Character>> seeds,
                             int k) {
      
      // Copies file to a String, so it is easier to parse
      String word = "";
      while (input.hasNextLine()) {
         word += input.nextLine() + " ";
      }
      word = word.replace("_","");
      
      if (k > word.length()) {
         String error = "\nThe seed is " + (k - word.length());
         error += " letters longer than input";
         error += ". Please try again";
         throw new IllegalArgumentException(error);
      }
      
      for (int i = 0; i < word.length() - k; i++) {
            String key = word.substring(i, i+k);
            if (seeds.containsKey(key)) {
               seeds.get(key).add(word.charAt(i+k));
            } else {
               seeds.put(key, new ArrayList<Character>());
               seeds.get(key).add(word.charAt(i+k));
            }
      }
   }
   
   // generates randomized text based on a provided list of k-length 'seeds'
   // r is to generate the randomness, outputLength is output length of 
   // randomized text in chars.
   public static void genRandomText(Map<String, ArrayList<Character>> seeds
                                    , int outputLength, Random r, int k) {
      
      ArrayList<String> keys = new ArrayList<String>(seeds.keySet());
      
      String seed = keys.get(r.nextInt(keys.size()));
      String output = seed;
      
      // starts at seed.length(), so output is around outputLength letters long
      for (int i = seed.length(); i < outputLength; i++) {
         // It generates a new seed on each iteration
         // The new seed is the last k characters in output
         seed = output.substring(output.length() - k, output.length());
         if (seeds.containsKey(seed)) {
            ArrayList<Character> nextChars = seeds.get(seed);
            // Picks a random character from nextChars
            
            int randIndex = r.nextInt(nextChars.size());
            output += nextChars.get(randIndex).toString();
         } else {
            // generates new key for use in next iteration of loop
            output += keys.get(r.nextInt(keys.size()));
            // To make sure output is around outputLength letters long
            i = output.length();
         }
         
      }
      
      System.out.println(output.substring(0, outputLength)); 
   } 
}