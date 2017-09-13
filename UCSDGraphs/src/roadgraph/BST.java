package roadgraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * 
 * Created a BST (for practice) to help store the map nodes.
 * It's noted that in this implementation, it's expected that
 * lots of nodes will be added and compared and few will be
 * deleted, which is good because the BST is less efficient
 * at deleting nodes.
 * 
 * 
 * Features of BST in the AVERAGE case:
 * search hit : 1.39 lgN
 * insert     : 1.39 lgN
 * delete     : sqrt(N)
 * ordered iteration
 * uses object's 'compareTo()' 
 * 
 *
 * @param <Key>
 * @param <Value>
 */

public class BST <Key extends Comparable <Key>, Value> {
    
    /*
     *  The root of the BST
     */
    private Node root;
      
    /**
     * Local class representing nodes of the BST
     *
     */
    private class Node{
        private Key key;
        private Value val;
        private Node left;
        private Node right;
        private int count;
        public Node(Key key, Value val){
            this.key = key;
            this.val = val;
        }
    }
    
    /*
     * Adds a node to the tree in it's proper place.
     * Uses recursion.
     */   
    public void put(Key key, Value val){
       root = put(root, key, val);
    }
    
    //overloaded helper function
    private Node put(Node x, Key key, Value val){
        // x == null when the end of a branch is reached
        if (x == null){
            x = new Node(key, val);
            x.count = 1 + size(x.left) + size(x.right);
            return x;
        }
        // if not the end, check which way to go:
        int cmp = key.compareTo(x.key);
        if      (cmp < 0)
            x.left = put(x.left, key, val);
        else if (cmp > 0)
            x.right = put(x.right, key, val);
        //if the value is already in the tree:
        else x.val = val;
        //Update the node's count instance
        x.count = 1 + size(x.left) + size(x.right);
        return x;           
    }
    
    /*
     * Returns a value given a key.
     * Compares sought after key with a node,
     * if it's less, it goes left down the tree
     * if it's more, it goes right down the tree
     * 
     */
    public Value get(Key key){
        Node x = root;
        while ( x != null){
            int     cmp = key.compareTo(x.key);            
            if            (cmp < 0) x = x.left;
            else if      (cmp > 0) x = x.right;
            else                  return x.val;
        }
        return null;      
    }
    
    /*
     * Returns a key in a tree. Null if not there.
     * Compares sought after key with a node,
     * if it's less, it goes left down the tree
     * if it's more, it goes right down the tree
     * 
     */
    public Key getKey(Key key){        
        Node x = root;
        while ( x != null){
            int cmp = key.compareTo(x.key);
            if           (cmp < 0) x = x.left;
            else if      (cmp > 0) x = x.right;
            else                  return x.key;          
        }
        return null;      
    }
    
    /**
     * Returns set of key elements
     * @return
     */
    public Set<Key> getKeySet(){
        Set<Key> q = new HashSet<Key>();
        inOrder(root, q);
        return q;
    }
    
    private void inOrder(Node x , Set<Key> q){
        if (x == null) return ;
        inOrder(x.left, q);
        q.add(x.key);
        inOrder(x.right, q);
    }
    
    
    
    
    /*
     * Traverse left
     * Enqueue
     * Traverse right
     */
    public Iterable<Key> iterator(){
        Queue<Key> q = new LinkedList<Key>();
        inOrder(root, q);
        return q;
    }
    
    private void inOrder(Node x , Queue<Key> q){
        if (x == null) return ;
        inOrder(x.left, q);
        q.offer(x.key);
        inOrder(x.right, q);
    }
    
    /*
     * Returns size of tree
     */    
    public int size(){
        return size(root);
    }
    /*
     * Returns the size of the subtree.
     */
    private int size(Node x){
        if(x == null) return 0;
        return x.count;
    }
    /*
     * Returns the rank of the tree
     */
    public int rank(Key key){
        return rank(key, root);
    }
    /*
     * Search recursively until a key is found that is
     * less than the sought after key. Then returns the
     * number of nodes less than key.
     * to give the number of nodes under it.
     */
    private int rank(Key key, Node x){
        if (x == null) return 0;  //if node is not in the tree
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else    return size(x.left);
    }
    
    public void deleteMin(){
        root = deleteMin(root);
    }
    private Node deleteMin(Node x){
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.count = 1 + size(x.left) + size(x.right);
        return x;                
    }
    
    
    /*
     * Deletion implemented following 
     * Hibbard deletion
     */
    public void delete(Key key){
        root = delete(root, key);
    }
    
    private Node delete(Node x, Key key){
        if (x == null) return null;
        //search for key as per usual
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left = delete(x.left, key);
        else if      (cmp > 0) x.right = delete(x.right, key);
        else {
            //in the case a child node is missing:
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            
            //replace deleted node with its successor
            //(the smallest node in it's right subtree)
            Node t = x;
            x = getMin(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        //update subtree counts
        x.count = size(x.left) + size(x.right) + 1;
        return x;
        
    }
    
    public Node getMax(){
        if( root == null) return null;
        return root;
    }
    
    public Node getMin(){
        return getMin(root);
    }
    private Node getMin(Node x){
        if ( x == null) return x;
        if (x.left != null) getMin(x.left);
        return x;
        
    }
    
}
