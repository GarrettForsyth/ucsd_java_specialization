/**
 * 
 */
package textgen;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author UC San Diego MOOC team
 *
 */
public class MyLinkedListTester {

	private static final int LONG_LIST_LENGTH =10; 

	MyLinkedList<String> shortList;
	MyLinkedList<Integer> emptyList;
	MyLinkedList<Integer> longerList;
	MyLinkedList<Integer> list1;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Feel free to use these lists, or add your own
		//shortList --> {A,B}
		//emptyList -->{}
		//longerList -->{1,2,..9}
		//list1 --> {65,21,42}

	    shortList = new MyLinkedList<String>();
		shortList.add("A");
		shortList.add("B");
		emptyList = new MyLinkedList<Integer>();
		longerList = new MyLinkedList<Integer>();
		for (int i = 0; i < LONG_LIST_LENGTH; i++)
		{
			longerList.add(i);
		}
		list1 = new MyLinkedList<Integer>();
		list1.add(65);
		list1.add(21);
		list1.add(42);
		
	}

	
	/** Test if the get method is working correctly.
	 */
	/*You should not need to add much to this method.
	 * We provide it as an example of a thorough test. */
	@Test
	public void testGet()
	{

		//test empty list, get should throw an exception
		try {
			emptyList.get(0);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			
		}
		
		// test short list, first contents, then out of bounds
		assertEquals("Check first", "A", shortList.get(0));
		assertEquals("Check second", "B", shortList.get(1));
		
		try {
			shortList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			shortList.get(2);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		// test longer list contents
		for(int i = 0; i<LONG_LIST_LENGTH; i++ ) {
			assertEquals("Check "+i+ " element", (Integer)i, longerList.get(i));
		}
		
		// test off the end of the longer array
		try {
			longerList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			longerList.get(LONG_LIST_LENGTH);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
		
	}
	
	
	/** Test removing an element from the list.
	 * We've included the example from the concept challenge.
	 * You will want to add more tests.  */
	@Test
	public void testRemove()
	{
		int a = list1.remove(0);
		assertEquals("Remove: check a is correct ", 65, a);
		assertEquals("Remove: check element 0 is correct ", (Integer)21, list1.get(0));
		assertEquals("Remove: check size is correct ", 2, list1.size());
		
		// TODO: Add more tests here
		try {
			emptyList.remove(0);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
		
		try {
			emptyList.remove(-19);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
		
		
		longerList.remove(1);
		longerList.remove(8);
		//check points going forwards then backwards 
		assertEquals("Remove: check pointers ", (Integer)6 ,(Integer)longerList.get(5));
		assertEquals("Remove: check pointers ", (Integer)2 ,(Integer)longerList.get(1));
	}
	
	/** Test adding an element into the end of the list, specifically
	 *  public boolean add(E element)
	 * */
	@Test
	public void testAddEnd()
	{
		//shortList --> {A,B}
		//emptyList -->{}
		//longerList -->{1,2,..9}
		//list1 --> {65,21,42}
        // TODO: implement this test
		
		shortList.add("C");
		assertEquals("Check end", "C", shortList.get(2));
		assertEquals("Check front", "A", shortList.get(0));
		assertEquals("Check size", (Integer)3, (Integer)shortList.size);
		
		assertEquals("Check pointer", shortList.get(2), shortList.tail.prev.data);
		
	
		
		try{
		shortList.add(null);
		fail("Check null pointer.");
		}
		catch(NullPointerException e){
		}
		
		
		
		
	}

	
	/** Test the size of the list */
	@Test
	public void testSize()
	{
		// TODO: implement this test
		assertEquals("Check size", (Integer)2, (Integer)shortList.size);
	}

	
	
	/** Test adding an element into the list at a specified index,
	 * specifically:
	 * public void add(int index, E element)
	 * */
	@Test
	public void testAddAtIndex()
	{
        // TODO: implement this test
		shortList.add(1,"C");
		
		assertEquals("Check added index, ", "C", shortList.get(1));
		assertEquals("Check front of list", "A", shortList.get(0));
		assertEquals("Check size", (Integer)3, (Integer)shortList.size);
		
		shortList.add(0,"Z");
		assertEquals("Check added index, ", "Z", shortList.get(0));
		assertEquals("Check size", (Integer)4, (Integer)shortList.size);
		
		try{
			shortList.add(1,null);
			fail("Check null pointer.");
			}
		catch(NullPointerException e){
		}
		
		try{
			shortList.add(6,"E");
			fail("Check out of bounds.");
			}
		catch(IndexOutOfBoundsException e){
		}
		
		try{
			shortList.add(-5,"E");
			fail("Check out of bounds.");
			}
		catch(IndexOutOfBoundsException e){
		}
		
	}
	
	/** Test setting an element in the list */
	@Test
	public void testSet()
	{
	    // TODO: implement this test
		
		int test= longerList.set(0, 10);
		assertEquals("Check old value, ", 0, test);
		assertEquals("Check new value, ", (Integer)10, (Integer)longerList.get(0));
		
		try{
			longerList.set(1,null);
			fail("Check null pointer.");
			}
		catch(NullPointerException e){
		}
		
		try{
			shortList.set(-5,"E");
			fail("Check out of bounds.");
			}
		catch(IndexOutOfBoundsException e){
		}
		try{
			shortList.set(10,"E");
			fail("Check out of bounds.");
			}
		catch(IndexOutOfBoundsException e){
		}
		
	    
	}
	
	
	// TODO: Optionally add more test methods.
	
}
