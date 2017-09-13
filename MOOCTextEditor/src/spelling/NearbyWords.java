/**
 * 
 */
package spelling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * @author UC San Diego Intermediate MOOC team
 *
 */
public class NearbyWords implements SpellingSuggest {
	// THRESHOLD to determine how many words to look through when looking
	// for spelling suggestions (stops prohibitively long searching)
	// For use in the Optional Optimization in Part 2.
	private static final int THRESHOLD = 1000; 

	Dictionary dict;

	public NearbyWords (Dictionary dict) 
	{
		this.dict = dict;
	}

	/** Return the list of Strings that are one modification away
	 * from the input string.  
	 * @param s The original String
	 * @param wordsOnly controls whether to return only words or any String
	 * @return list of Strings which are nearby the original string
	 */
	public List<String> distanceOne(String s, boolean wordsOnly )  {
		   List<String> retList = new ArrayList<String>();
		   insertions(s, retList, wordsOnly);
		   substitution(s, retList, wordsOnly);
		   deletions(s, retList, wordsOnly);
		   return retList;
	}

	
	/** Add to the currentList Strings that are one character mutation away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void substitution(String s, List<String> currentList, boolean wordsOnly) {
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the 
				// letters in the String
				StringBuffer sb = new StringBuffer(s);
				sb.setCharAt(index, (char)charCode);

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
			}
		}
	}
	
	/** Add to the currentList Strings that are one character insertion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void insertions(String s, List<String> currentList, boolean wordsOnly ) {
		// TODO: Implement this method  
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length()+1; index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				
				// chop up string via substring:
				String sb;
				if(index != s.length()+1){ //lead with most probably case
					sb= s.substring(0,index) + (char)charCode + s.substring(index);
				}
				else{//special case for last insertion
					sb= s+ (char)charCode;
				}

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void deletions(String s, List<String> currentList, boolean wordsOnly ) {
		// TODO: Implement this method
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){					
				// chop up string via substring:
				String sb;
				
				if(index != s.length()){
					sb= s.substring(0,index) + s.substring(index+1);
				}
				else{ //special case for last letter
					sb= s.substring(0,s.length());
				}
				

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param word The misspelled word
	 * @param numSuggestions is the maximum number of suggestions to return 
	 * @return the list of spelling suggestions
	 */
	@Override
	public List<String> suggestions(String word, int numSuggestions) {

		// initial variables
		Queue<String> queue = new LinkedList<String>();     // String to explore
		HashSet<String> visited = new HashSet<String>();   // to avoid exploring the same  
														   // string multiple times
		List<String> retList = new LinkedList<String>();   // words to return
		List<String> suggestions= new LinkedList<String>(); // holds temporary suggestions
		
		// insert first node
		queue.add(word);
		
		
		String currWord;
					
		// TODO: Implement the remainder of this method, see assignment for algorithm
		while(!queue.isEmpty() && retList.size() < numSuggestions && visited.size() < THRESHOLD){
			//  remove the word from the start of the queue and assign to curr
			currWord= queue.remove(); // processing the first word in the queue
			//  get a list of neighbors (strings one mutation away from curr)
			suggestions= distanceOne(currWord, true); // creates a list of distance one word form currWord
			//  for each n in the list of neighbors
			for(String s : suggestions){// adds any new words not visited to the queue
				if(!visited.contains(s)){//    if n is not visited		 
					  //     add n to the visited set
					visited.add(currWord); // add the word to list of visited words
					  //     add n to the back of the queue
					queue.add(s);
					   //    if n is a word in the dictionary
					if(dict.isWord(s)){
					   //       add n to the list of words to return
						retList.add(s);
					}
					
				}
			}
						
			//if(!retList.contains(currWord)){ //if the return list doens't contain the word, add it
			//	retList.add(currWord);
			//}
			// note that distanceOne is set to return only dictionary words
			
					
		}
		
		return retList;

	}	

   public static void main(String[] args) {
	    /**basic testing code to get started
	   String word = "propogate";
	   // Pass NearbyWords any Dictionary implementation you prefer
	   Dictionary d = new DictionaryHashSet();
	   DictionaryLoader.loadDictionary(d, "data/dict.txt");
	   NearbyWords w = new NearbyWords(d);
	   List<String> l = w.distanceOne(word, true);
	   System.out.println("One away word Strings for for \""+word+"\" are:");
	   System.out.println(l+"\n");

	   word = "dag";
	   List<String> suggest = w.suggestions(word, 4);
	   System.out.println("Spelling Suggestions for \""+word+"\" are:");
	   System.out.println(suggest);
	   */
	   
	   
	   /** TESTING INSERTION METHOD
	   System.out.println("Testing Insertion...");
	   String word= "";
	   Dictionary d= new DictionaryHashSet();
	   DictionaryLoader.loadDictionary(d, "data/dict.txt");
	   NearbyWords w = new NearbyWords(d);
	   List<String> l = new LinkedList();
	   w.insertions(word,l, true);
	   for(String s : l){
		   System.out.println(s);
	   }
	   */
	   
	   /** TESTIN DELETION METHOD
	   System.out.println("Testing Deletion...");
	   String word= "";
	   Dictionary d= new DictionaryHashSet();
	   DictionaryLoader.loadDictionary(d, "data/dict.txt");
	   NearbyWords w = new NearbyWords(d);
	   List<String> l = new LinkedList();
	   w.deletions(word,l, true);
	   for(String s : l){
		   System.out.println(s);
	   }
	   */
	   
	   System.out.println("Testing findPath...");
	   WPTree wpt= new WPTree();
	   String word1= "foal";
	   String word2= "needless";
	   List<String> list= wpt.findPath(word1, word2);
	   for(String s: list){
		   System.out.println(s);
	   }
	   
   }

}
