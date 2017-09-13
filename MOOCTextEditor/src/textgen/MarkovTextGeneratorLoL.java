package textgen;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team 
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;
	
	//Tag for debugging
	String TAG= this.getClass().getCanonicalName();
	
	public MarkovTextGeneratorLoL(Random generator)
	{
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}
	
	
	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText)
	{
		// TODO: Implement this method
		if(!wordList.isEmpty()){
			return;
		}
		
		if(sourceText == null){
			throw new NullPointerException(TAG 
					+ " cannot pass Null sourceText to train.");
		}
		
		String[] words= sourceText.split("\\p{Space}+");
		
		//special care for first word:
		starter= words[0];
		ListNode prevWord=new ListNode(starter);

				
		for(int i=1; i < words.length; i++){ //starting at second element
			String word= words[i];		
			int index= indexOfNode(wordList,prevWord); //via helper method

			if(index != -1){
				(wordList.get(index)).addNextWord(word);
			}
			else{
				wordList.add(prevWord);
				prevWord.addNextWord(word);
			}
			prevWord= new ListNode(word);
		}
		
		//special case for last word:
		prevWord.addNextWord(starter);
		if(indexOfNode(wordList,prevWord) < 0){
			wordList.add(prevWord);
		}		
	}
	
	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {
	    // TODO: Implement this method
		if(wordList == null || wordList.isEmpty() || numWords <=0){
			return "";
		}
		
		String currWord= starter;
		String output=starter ;
		
		for(int i=0; i < numWords-1; i++){
			int index= indexOfNode(wordList, new ListNode(currWord));
			currWord= wordList.get(index).getRandomNextWord(rnGenerator);
			output+= " " + currWord;
		}
		
		return output;
	}
	
	
	// Can be helpful for debugging
	@Override
	public String toString()
	{
		String toReturn = "";
		for (ListNode n : wordList)
		{
			toReturn += n.toString();
		}
		return toReturn;
	}
	
	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText)
	{
		wordList= new LinkedList<ListNode>();
		starter= "";
		train(sourceText);
	}
	
	// TODO: Add any private helper methods you need here.
	
	//helper method to find index of a node
	private int indexOfNode(List<ListNode> list, ListNode node){
		for(int i=0; i < list.size(); i++){
			if(list.get(i).equals(node)){
				return i;
			}
		}
		return -1;
	}
	
	
	
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args)
	{
		////
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		System.out.println(textString);
		gen.train(textString);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
		String textString2 = "You say yes, I say no, "+
				"You say stop, and I say go, go, go, "+
				"Oh no. You say goodbye and I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"I say high, you say low, "+
				"You say why, and I say I don't know. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"Why, why, why, why, why, why, "+
				"Do you say goodbye. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"You say yes, I say no, "+
				"You say stop and I say go, go, go. "+
				"Oh, oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println(textString2);
		gen.retrain(textString2);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
	}

}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode
{
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	private List<String> nextWords;
	
	ListNode(String word)
	{
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord()
	{
		return word;
	}

	public void addNextWord(String nextWord)
	{
		nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator)
	{
		// TODO: Implement this method
	    // The random number generator should be passed from 
	    // the MarkovTextGeneratorLoL class
		int rand= generator.nextInt(nextWords.size());
	    return nextWords.get(rand);
	}

	public String toString()
	{
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
	
	public boolean equals(ListNode other){
	    boolean result;
	    if((other.word == null) || (getClass() != other.getClass())){
	        result = false;
	    } // end if
	    else{
	        ListNode otherNode = (ListNode)other;
	        result = (this.word).equals(other.word);
	    } // end else

	    return result;
	}
	
	public int hashCode() {
	    return java.util.Objects.hashCode(word);
	}
	
	
}


