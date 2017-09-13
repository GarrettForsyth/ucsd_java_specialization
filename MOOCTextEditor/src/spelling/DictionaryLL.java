package spelling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * A class that implements the Dictionary interface using a LinkedList
 *
 */
public class DictionaryLL implements Dictionary 
{

	private LinkedList<String> dict;
	
    // TODO: Add a constructor
	
	public DictionaryLL(){
		this.dict= new LinkedList<String>();
	}


    /** Add this word to the dictionary.  Convert it to lowercase first
     * for the assignment requirements.
     * @param word The word to add
     * @return true if the word was added to the dictionary 
     * (it wasn't already there). */
    public boolean addWord(String word) {
    	// TODO: Implement this method
    	word= word.toLowerCase();
    	if(this.isWord(word)){
    		return false;
    	}
    	dict.add(word);
        return true;
    }


    /** Return the number of words in the dictionary */
    public int size()
    {
        // TODO: Implement this method
        return dict.size();
    }

    /** Is this a word according to this dictionary? */
    public boolean isWord(String s) {
        //TODO: Implement this method
    	s=s.toLowerCase();
    	if(dict.contains(s)){
    		return true;
    	}
    	return false;
    }
    /** Reads in an external dictionary expecting
     * every line to be a word.
     * 
     * @param filename
     
    private void readInDictionary(String filename){
    	
    	BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));
			
			String line= br.readLine();
    		
    		while(line !=null){
    			this.addWord(line);
    			line= br.readLine();
    		}
    		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    }
    */

    
}
