package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;
	//Tag for debugging
	String TAG= this.getClass().getCanonicalName();

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		// TODO: Implement this method
		head= new LLNode<E>(null);
		tail= new LLNode<E>(null);
		head.next=tail;
		tail.prev=head;
		size=0;
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element ) 
	{
		// TODO: Implement this method
		
				if(element == null){
					throw new NullPointerException(TAG 
							+ " cannot add null element to MyLinkedList.");
				}
				
				LLNode<E> newElement= new LLNode<E>(element);
				//Special case for first element:
				if(size==0){
					newElement.prev= head;
					newElement.next= tail;
					
					tail.prev=newElement;
					head.next= newElement;
					size++;
					return false;
				}
				//add pointers for new element:
				newElement.prev= tail.prev;
				newElement.next= tail;
				
				//change pointers of existing elements:
				newElement.prev.next= newElement;
				tail.prev= newElement;
				
				size++;
				
				return false;
	}
	
	/**
	 * Helper method that gets a node at
	 * specified index.
	 * @param The index of the node.
	 */
	private LLNode<E> getNode(int index){
		if(index > size-1 || index < 0){
			throw new IndexOutOfBoundsException(TAG 
					+ " index '" + index + "' is out of bounds.");
		}
		
		// TODO: Implement this method.
		//if in first half of chain, start from front
		LLNode<E> currNode;
		
		if((size-1)-index >= size/2){
			currNode=head;
			for(int i=0; i <= index; i++){
				currNode=currNode.next;
			}
			return currNode;
		}
		//if in second half of chain, start from tail
		else{
			currNode=tail;
			for(int i=(size-1); i >= index; i--){
				currNode=currNode.prev;
			}
			return currNode;
		}
	}
	

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) 
	{
		return getNode(index).data;	
	}

	/**
	 * Add an element to the list at the specified index
	 * @param The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		// TODO: Implement this method
		
		if(element == null){
			throw new NullPointerException(TAG 
					+ " cannot add null element to MyLinkedList.");
		}
		
		LLNode<E> newElement= new LLNode<E>(element);
		
		//Special case for first element:
		if(size==0){
			newElement.prev= head;
			newElement.next= tail;
			
			tail.prev=newElement;
			head.next= newElement;
			size++;
			return;
		}
		
		LLNode<E> oldNode= getNode(index); //the node that is being displaced
		
		
		//Add appropriate pointers to new element:
		newElement.prev= oldNode.prev;
		newElement.next= oldNode;
		
		//change pointers of adjacent elements
		(oldNode.prev).next= newElement;
		oldNode.prev= newElement;
		
		//increment size
		size++;
		
	}


	/** Return the size of the list */
	public int size() 
	{
		// TODO: Implement this method
		return size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{
		// TODO: Implement this method		
		
		LLNode<E> selectedNode= getNode(index);
		
		(selectedNode.prev).next= selectedNode.next;
		(selectedNode.next).prev= selectedNode.prev;
		
		size--;
		
		return selectedNode.data;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		// TODO: Implement this method
		
		if(element == null){
			throw new NullPointerException(TAG 
					+ " cannot add null element to MyLinkedList.");
		}
		
		LLNode<E> selectedNode= getNode(index);
		E oldData= selectedNode.data;
		selectedNode.data= element;
		
		return oldData;
	}   
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	// TODO: Add any other methods you think are useful here
	// E.g. you might want to add another constructor

	public LLNode(E e) 
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}
	
	public LLNode(E e, LLNode<E> prev, LLNode<E> next){
		this.data = e;
		this.prev = prev;
		this.next = next;
	}
	
	//LLNode objects are equal if their data is equal
	public boolean equals(LLNode<E> other){
	    boolean result;
	    if((other.data == null) || (getClass() != other.getClass())){
	        result = false;
	    } // end if
	    else{
	        LLNode<E> otherNode = (LLNode<E>)other;
	        result = (this.data).equals(other.data);
	    } // end else

	    return result;
	}

}
