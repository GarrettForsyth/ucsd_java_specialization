package basicgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** A class that implements a directed graph. 
 * The graph may have self-loops, parallel edges. 
 * Vertices are labeled by integers 0 .. n-1
 * and may also have String labels.
 * The edges of the graph are not labeled.
 * Representation of edges via an adjacency matrix.
 * 
 * @author UCSD MOOC development team and YOU
 *
 */
public class GraphAdjMatrix extends Graph {

	private final int defaultNumVertices = 5;
	private int[][] adjMatrix;
	
	/** Create a new empty Graph */
	public GraphAdjMatrix () {
		adjMatrix = new int[defaultNumVertices][defaultNumVertices];
	}
	
	/** 
	 * Implement the abstract method for adding a vertex.
	 * If need to increase dimensions of matrix, double them
	 * to amortize cost. 
	 */
	public void implementAddVertex() {
		int v = getNumVertices();
		if (v >= adjMatrix.length) {
			int[][] newAdjMatrix = new int[v*2][v*2];
			for (int i = 0; i < adjMatrix.length; i ++) {
				for (int j = 0; j < adjMatrix.length; j ++) {
					newAdjMatrix[i][j] = adjMatrix[i][j];
				}
			}
			adjMatrix = newAdjMatrix;
		}
	}
	
	/** 
	 * Implement the abstract method for adding an edge.
	 * Allows for multiple edges between two points:
	 * the entry at row v, column w stores the number of such edges.
	 * @param v the index of the start point for the edge.
	 * @param w the index of the end point for the edge.  
	 */	
	public void implementAddEdge(int v, int w) {
		adjMatrix[v][w] += 1;
	}
	
	/** 
	 * Implement the abstract method for finding all 
	 * out-neighbors of a vertex.
	 * If there are multiple edges between the vertex
	 * and one of its out-neighbors, this neighbor
	 * appears once in the list for each of these edges.
	 * 
	 * @param v the index of vertex.
	 * @return List<Integer> a list of indices of vertices.  
	 */	
	public List<Integer> getNeighbors(int v) {
		List<Integer> neighbors = new ArrayList<Integer>();
		for (int i = 0; i < getNumVertices(); i ++) {
			for (int j=0; j< adjMatrix[v][i]; j ++) { //if entry > 1 in matrix 
				neighbors.add(i);
			}
		}
		return neighbors;
	}
	
	/** 
	 * Implement the abstract method for finding all 
	 * in-neighbors of a vertex.
	 * If there are multiple edges from another vertex
	 * to this one, the neighbor
	 * appears once in the list for each of these edges.
	 * 
	 * @param v the index of vertex.
	 * @return List<Integer> a list of indices of vertices.  
	 */
	public List<Integer> getInNeighbors(int v) {
		List<Integer> inNeighbors = new ArrayList<Integer>();
		for (int i = 0; i < getNumVertices(); i ++) {
			for (int j=0; j< adjMatrix[i][v]; j++) {
				inNeighbors.add(i);
			}
		}
		return inNeighbors;
	}
	
	/** 
	 * Implement the abstract method for finding all 
	 * vertices reachable by two hops from v.
	 * Use matrix multiplication to record length 2 paths.
	 * 
	 * @param v the index of vertex.
	 * @return List<Integer> a list of indices of vertices.  
	 */	
	public List<Integer> getDistance2(int v) {
		// XXX Implement this method in week 2
		//done via helper method(s)
		List<Integer> distance2 = new ArrayList<Integer>();
		int[][] twoHopMat= multiplyMatrix(adjMatrix, adjMatrix);
		for (int i = 0; i < getNumVertices(); i ++) {
			for (int j=0; j< twoHopMat[v][i]; j ++) { //if entry > 1 in matrix 
				distance2.add(i);
			}
		}
		return distance2;
	}
	/** 
	 *Helper function that multiplies two matrices.
	 *Although there surely exist more efficient methods
	 *in some library, this was done for the practice.
	 * 
	 * @param mat1 matrix one, mat2 matrix 2
	 * @return productMat, the product of the two.  
	 */
	
	private int[][] multiplyMatrix(int[][] mat1, int [][] mat2){
		//In matrix mult, the lengths of 
		//mat1 rows = mat 2 col and mat1 col = mat2 row
		if(mat1.length != mat2[0].length 
				|| mat1[0].length != mat2.length){
			throw new IllegalArgumentException("Invalid matrix sizes.");
		}
		//size of n1 by n2 x m1 m2 = n1 x m2
		int[][] productMat=new int[mat1.length][mat2[0].length];
		
		for(int i=0; i < mat1.length; i++){
			for(int j=0; j < mat2[0].length; j++){
				productMat[i][j]= dotProduct(mat1[i], getCol(mat2, j));
			}
		}
		return productMat;
		
	}
	
	private int dotProduct(int[] a, int[] b){
		if(a.length != b.length){
			throw new IllegalArgumentException("Invalid array sizes.");
		}
		int total=0;
		for(int i=0; i < a.length; i++){
			total += a[i]*b[i];
		}
		return total;
	}
	//returns an array that is a column of a matrix 
	private int[] getCol(int[][] mat, int colNum){
		int numRows= mat.length;
		int[] cols= new int[numRows];
		
		for(int i=0; i< numRows; i++){
			cols[i]= mat[i][colNum];
		}
		return cols;
	}
	
	/**
	 * Generate string representation of adjacency matrix
	 * @return the String
	 */
	public String adjacencyString() {
		int dim = getNumVertices();
		String s = "Adjacency matrix";
		s += " (size " + dim + "x" + dim + " = " + dim* dim + " integers):";
		for (int i = 0; i < dim; i ++) {
			s += "\n\t"+i+": ";
			for (int j = 0; j < dim; j++) {
			s += adjMatrix[i][j] + ", ";
			}
		}
		return s;
	}

}
