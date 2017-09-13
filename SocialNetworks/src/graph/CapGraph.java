/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author Garrett Forsyth
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
    
    // This is a sparse graph, so use an adjacency list data structure
    // Users are represented as Integer ID's.
    // Note , users cannont be friends, or 'retweet' more than once
    // so a set implementation makes sense. 

    private HashMap< Integer, HashSet<Integer>> idAdjList;
    
    /* Constructor */
    public CapGraph(){
        idAdjList = new HashMap<>();
    }

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		//  return if vertex is already contained in graph
	    if ( idAdjList.containsKey( num ) ) return;
	    idAdjList.put( num , new HashSet<Integer>());
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
	    //  return if either vertex is not in the graph
        if ( !idAdjList.containsKey( from ) || !idAdjList.containsKey( to )) return;
        idAdjList.get(from).add(to);
	}
	
	/**
	 * @return the set of vertices for the graph.
	 */
	public Set<Integer> getVerticies(){
	    return new HashSet<>(idAdjList.keySet());
	}
	
	/**
	 * Returns set of neighbors of i.
	 * @param i is the user
	 * @return the list of id's connected to the user
	 */
	public HashSet<Integer> getNeighbors(int i){
	    return new HashSet<>(idAdjList.get(i));
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		
	    CapGraph egoNet = new CapGraph();
	    
	    // return empty graph if key is not contained
	    if ( !this.idAdjList.containsKey(center)) return egoNet;
	    
	    // add center point.
	    egoNet.addVertex( center );
	    
	    // go through center's neighbours and add to graph as well as
	    // their connection edge to center
	    for ( Integer i : this.idAdjList.get( center ) ){
	        egoNet.addVertex( i );
	        egoNet.addEdge( center , i);
	        
	        // find the interconnections by looking at intersection of two lists:
	        // first create a new list to not disturb the old one
	        List<Integer> intersection = new ArrayList<>(this.idAdjList.get( i ));
	        // before finding intersection, check if center is also i's neighbor (directed graph)
	        if ( intersection.contains( center ) ) egoNet.addEdge( i , center);
	        intersection.retainAll( this.idAdjList.get( center ) );	        
	        
	        for ( Integer j : intersection ){
	            // add the shared neighbor to the graph.
	            if ( !egoNet.getVerticies().contains(j) ) egoNet.addVertex( j );
	            egoNet.addEdge( i , j );
	        }
	    }
	    
	    return egoNet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
	    List<Graph> sccs = new ArrayList<Graph>();
		// 1. DFS (G) keeping track of the order in which vertices finish
	    Stack<Integer> vertices = new Stack<>();
	    vertices.addAll(this.getVerticies());
	    Stack<Integer> finished = DFS( this, vertices, null);
	    // 2. Compute the transpose of G.
	    CapGraph transpose = getTranspose(this);
	    // 3. DFS(G^T) exploring in the reverse order of finish time form step 1
		DFS(transpose, finished, sccs);
		return sccs;
	}
	
	/**
	 * Helper function to find SCC.
	 * 
	 * Uses a DFS to find all components (i.e. connected vertices) and
	 * returns a an ordered list reflecting this.
	 * 
	 * @param g The CapGraph 
	 * @param vertices list of vertices from the graph
	 * @return reversed order of how our DFS found vertices
	 */
	private Stack<Integer> DFS(CapGraph g, Stack<Integer> vertices, List<Graph> sccs){
	    // initialize visited set and finished stack:
	    HashSet<Integer> visited = new HashSet<>();
	    Stack<Integer> finished = new Stack<>();
	    
	    while ( !vertices.isEmpty()){
	       
	        Integer v = vertices.pop();
	        CapGraph scc = null;
	        if ( ! (sccs == null) ) {scc = new CapGraph();}
	        
	        if ( !visited.contains( v ) ){
	           
	            
	            DFSVisit(g, v, visited, finished, scc);   
	        }
	        if ( sccs!=null && !scc.getVerticies().isEmpty()) sccs.add(scc);
	        
	    }
	    return finished;
	}
	
	/**
	 * Recursively method that follows the edges of a vertices and 
	 * adds them to the finished stack in appropriate order.
	 * @param g
	 * @param v
	 * @param visited
	 * @param finished
	 */
	private void DFSVisit(CapGraph g, Integer v, HashSet<Integer> visited,
	                        Stack<Integer> finished, CapGraph scc){
	    // add v to visited set
	    visited.add(v);
	    if ( scc != null) scc.addVertex(v);
	    
	    for ( Integer n : g.getNeighbors(v)){
	        
	        if ( !visited.contains( n ) ){
	            if ( scc != null){
	                if (!scc.getVerticies().contains(n)) scc.addVertex(n);
	                scc.addEdge(v, n);                
	             }
	            DFSVisit( g , n, visited, finished, scc);
	        }
	    }
	    finished.push(v);	    
	}
	
	/**
	 * Returns a new graph that is the transpose of G
	 * (i.e. all the directed edges are flipped)
	 * @param g graph to be transposed
	 * @return the transposed graph
	 */
	private CapGraph getTranspose(Graph g){
	    CapGraph transpose = new CapGraph();
	    for ( Integer v : this.getVerticies()){
	        transpose.addVertex( v );
	        for ( Integer n : this.idAdjList.get((v))){
	            if ( !transpose.getVerticies().contains(n)){
	                transpose.addVertex(n);
	            }
	            transpose.addEdge( n, v);
	        }
	    }
	    return transpose;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// return a copy of the idAdjList to protect clients
	    // from altering the data
		return new HashMap<>(idAdjList);
	}

}
