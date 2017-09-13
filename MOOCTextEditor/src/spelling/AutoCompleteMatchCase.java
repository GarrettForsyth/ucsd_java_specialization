package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.stream.events.Characters;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteMatchCase implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;  
    

    public AutoCompleteMatchCase()
	{
		root = new TrieNode();
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should convert the 
	 * string to all lower case before you insert it. 
	 * 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word)
	{
		//format check:
		if(word.isEmpty() || word ==null){
			return false;
		}
		
		//convert word to lower case array of chars
		//word=word.toLowerCase();
		char[] letters= word.toCharArray();
		
		//start at the root
		TrieNode currNode= root;
		
		for(char let : letters){ //iterate through each letter of the word											
			//if next letter is not a child, create it 
			Character ch= new Character(let);
			if(currNode.getChild(ch) == null){
				currNode.insert(ch);
			}
			currNode= currNode.getChild(ch);
			
			//NOTE : This section MUST come after to account for the last letter!
			//if at the correct node... 
			//System.out.println(currNode.getText() + "  EQUALS  " + word + "  " + currNode.getText().equals(word));
			if(currNode.getText().equals(word)){
				//if word is already in the dictionary return false
				if(currNode.endsWord()){
					//System.out.println("word already exists.");
					return false; //indicates no action done
				}
				else{//otherwise the node exist, but isn't considered  word
					//set to be considered a word and return true
					//System.out.println("add word successful.");
					currNode.setEndsWord(true);
					return true;
				}
			}			
		}		
		return false;
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    //This method must transverse the entire tree and count
		//how many nodes 'end a word'
		int wordCount=0;
		TrieNode currNode= root;
		
		Queue<TrieNode> nodesToCheck= new LinkedList<TrieNode>();
		Set<Character> childNodeChars= new TreeSet<Character>();
		nodesToCheck.add(currNode);
		
		while(!nodesToCheck.isEmpty()){
			currNode= nodesToCheck.remove();
			//count if word
			if(currNode.endsWord()){
				wordCount++;
			}
			
			//add child to queue
			//childNodeChars.clear();
			childNodeChars= currNode.getValidNextCharacters();
			
			for(Character ch : childNodeChars){
				nodesToCheck.add(currNode.getChild(ch));
			}
			
		}		
		return wordCount;
	}
	
	
	/** Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week. */
	@Override
	public boolean isWord(String s) 
	{
		System.out.println("Is word method entered...");
		//Format
		if(s.isEmpty() || s ==null){
			return false;
		}
		
		 boolean firstLetCap=false;
    	 boolean anyUpperCase=false;
    	 boolean allUpper=true;
    	 
    	 boolean[] caseFlags= getCaseFlags(s);
    	 for(boolean b : caseFlags){
    		 if(b){
    			 anyUpperCase=true;
    		 }
    		 else{
    			 allUpper=false;
    		 }
    	 }
    	 //if only upper case is first letter, set firstLetCap to true
    	 if(anyUpperCase){
    		 firstLetCap=true;
    		 for(int i=1; i < caseFlags.length; i++){
    			 if(caseFlags[i] == true){
    				 firstLetCap=false;
    			 }
    		 }
    	 }
    	 
    	 System.out.println("ANY UPPER SET TO : " + anyUpperCase
    			 			+" ALL UPPER SET TO : " + allUpper 
    			 			+ " FIRST LET CAP SET TO : " + firstLetCap);
    	 
    	 if(allUpper || firstLetCap){
    		 s=s.toLowerCase();
    	 }
		
	    //Follows characters to check for existing node set to endWord
		//s=s.toLowerCase();
		char[] letters= s.toCharArray();
		

		
		TrieNode currNode= root;
		
		for(char let : letters){
					
			//If not the right node, keep looking at child nodes
			if(currNode.getChild(let)==null){
				return false; //fall off tree --> word does not exist
			}
			else{
				currNode= currNode.getChild(let);
			}
			//Check if node is correct node:
			if(currNode.getText().equals(s)){
				//If so, check if it 'ends a word'
				if(currNode.endsWord()){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}
	/**
	

	/** 
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 List<String> completions= new LinkedList<String>();
    	 boolean firstLetCap=false;
    	 boolean anyUpperCase=false;
    	 
    	 boolean[] caseFlags= getCaseFlags(prefix);
    	 for(boolean b : caseFlags){
    		 if(b){
    			 anyUpperCase=true;
    		 }
    	 }
    	 //if only upper case is first letter, set firstLetCap to true
    	 if(anyUpperCase){
    		 firstLetCap=true;
    		 for(int i=1; i < caseFlags.length; i++){
    			 if(caseFlags[i] == true){
    				 firstLetCap=false;
    			 }
    		 }
    	 }
    	 prefix=prefix.toLowerCase();
  	 
    	 if(prefix== null || numCompletions < 0){
 			return null;
 		 }
    	
    	     	 
    	 TrieNode stem= findStem(prefix);
    	 if(stem == null){
    		 return completions;
    	 }
    	 
    	 
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 TrieNode currNode;
    	 Queue<TrieNode> nodesToCheck= new LinkedList<TrieNode>();
    	 Set<Character> childNodeChar= new TreeSet<Character>();
    	 nodesToCheck.add(stem);    	 
    	 		
    	 //Create a list of completions to return (initially empty)
    	 //While the queue is not empty and you don't have enough completions:
    	 while(!nodesToCheck.isEmpty() && completions.size() < numCompletions){
    	 //remove the first Node from the queue
    	 	currNode= nodesToCheck.remove();
    	 //If it is a word, add it to the completions list
    	 	if(currNode.endsWord()){
    	 		  	 		
    	 		if(firstLetCap){
    	 			String suggestion=currNode.getText();
    	 			char c2[] = suggestion.toCharArray();
    	 	    	c2[0] = Character.toUpperCase(c2[0]);
    	 	    	suggestion= new String(c2);
    	 	    	completions.add(suggestion);
    	 		}
    	 		else if(anyUpperCase){    	 			
    	 			completions.add(currNode.getText().toUpperCase());
    	 		}
    	 		else{
    	 			completions.add(currNode.getText());
    	 		}
    	 	}
    	 // Add all of its child nodes to the back of the queue
    	 	childNodeChar= currNode.getValidNextCharacters();
    	 	for(Character ch : childNodeChar){
    	 		nodesToCheck.add(currNode.getChild(ch));
    	 	}
    	 }
    	 // Return the list of completions
    	 return completions;
     }
     
     private TrieNode findStem(String prefix){
    	 //special case for empty stem:
    	 if(prefix.isEmpty()){
    		 return root;
    	 }
    	char[] letters= prefix.toCharArray();	
 		TrieNode currNode= root;
 		//System.out.println("printing tree...");
 		//printTree();
 		
 		for(char let : letters){				
 			//If stem does not exist
 			if(currNode.getChild(let)==null){
 				return null; //fall off tree --> no suggestions
 			}
 			else{
 				currNode= currNode.getChild(let);
 			}
 			//Check if node is correct node:
 			if(currNode.getText().equals(prefix)){
 				//If so, return prefix
 				return currNode;
 			}
 		}
 		return null;
     }
     
    
     
     public boolean[] getCaseFlags(String word) {

 		boolean[] flags = new boolean[word.length()];

 		// for if should return array or null
 		boolean anyUpperCase = false;

 		for (int i = 0; i < flags.length; i++) {
 			// if isUpperCase
 			if (Character.isUpperCase(word.charAt(i))) {
 				flags[i] = true;
 			} else {
 				flags[i] = false;
 			}
 		}
 		return flags;
 	}

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}