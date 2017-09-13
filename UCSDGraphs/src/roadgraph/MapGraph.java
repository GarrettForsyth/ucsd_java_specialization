/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
    
    //Use an adjacent list because this is a 'sparse' graph
    //because in general each intersection should have 8 edges
    //connected to it or less.
    
    //Use BST with mapNode's as keys and its edges as values
    // BST chosen for quick look up and insertion and anticipating
    // not needing to delete many keys.
    private BST < MapNode, ArrayList<MapEdge>> intersections;
    //Flag used to break out of recursion in Nearest Neighbours' "followBranch' helper
    private boolean branchExit;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
	    intersections= new BST<MapNode, ArrayList<MapEdge>>();
	    branchExit = false;
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return intersections.size();
	}
	
	public BST<MapNode, ArrayList<MapEdge>> getIntersections(){
	    return intersections;
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
	    Set<GeographicPoint> vertices= new HashSet<>();
	    
	    //Iterate over the vertices, extract
	    //the GeograhpicPoint from each and add to list
	    for(MapNode mn : intersections.iterator()){
	        vertices.add(mn.getPosition());
	    }
		return vertices;
	}
	
	public List<MapNode> getVerticesList()
    {
        List<MapNode> vertices= new LinkedList<>();
        
        //Iterate over the vertices, extract
        //the GeograhpicPoint from each and add to list
        for(MapNode mn : intersections.iterator()){
            vertices.add(mn);
        }
        return vertices;
    }
	
	public List<MapEdge> getEdgesList()
    {
        List<MapEdge> edges= new LinkedList<>();
        
        //Iterate over the vertices, extract
        //the GeograhpicPoint from each and add to list
        for(MapNode mn : intersections.iterator()){
            edges.addAll(intersections.get(mn));
            
        }
        return edges;
    }
    
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
	    int count = 0;
	    //Iterate over the vertices, extract
        //the GeograhpicPoint from each and add to list
        for(MapNode mn : intersections.iterator()){
            count += intersections.get(mn).size();
        }
        return count;
	}

	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
	    if (location == null) return false;
	    MapNode newVertex= new MapNode(location);
	    
	    if (intersections.getKey(newVertex) != null) return false;	    
	    else intersections.put(newVertex, new ArrayList<MapEdge>());
		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double l) throws IllegalArgumentException {
	    if (from == null || to == null || roadName == null || roadType == null || l < 0){
            throw new IllegalArgumentException("addEdge has null arguements.");
        }
	    
	    //initialize:
	    MapNode fromNode= new MapNode(from);
	    MapNode toNode= new MapNode(to);  
	    
	    if (intersections.getKey(fromNode) == null || intersections.getKey(toNode) == null){
	        throw new IllegalArgumentException("addEdge failed Inersections does not"
	                + "contain either fromNode or toNode.");
	    };
	    MapEdge newEdge = new MapEdge(fromNode, toNode, roadName, roadType);
	    intersections.getKey(fromNode).addEdge(newEdge);
	    intersections.get(fromNode).add(newEdge);
	}
	
	public void addEdge(MapEdge me) throws IllegalArgumentException {
        if (me == null ){
            throw new IllegalArgumentException("addEdge has null arguements.");
        }
        
        if (intersections.getKey(me.getStartNode()) == null ||
                intersections.getKey(me.getEndNode()) == null){
            throw new IllegalArgumentException("addEdge failed Inersections does not"
                    + "contain either fromNode or toNode.");
        };
        
        intersections.getKey(me.getStartNode()).addEdge(me);
        intersections.get(me.getStartNode()).add(me);
    }
	
	public void removeEdge(MapEdge me) throws IllegalArgumentException {
	    if (me == null || !getEdgesList().contains(me)){
            throw new IllegalArgumentException("addEdge has null arguements.");
        }
	    
	    MapNode mn = intersections.getKey(me.getStartNode());
	    mn.removeEdge(me);
	    intersections.get(mn).remove(intersections.get(mn).indexOf(me));
	    
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
	    //Validate input:
	    if(start== null || goal== null){
	        return null;
	    }
	            
         //Create nodes from geographical points for comparison purposes
         //(two MapNode objects are equal if they have the same GeographicPoint)
         MapNode startNode= new MapNode(start);
         MapNode goalNode= new MapNode(goal);
	    
	    if (intersections.getKey(startNode) == null || intersections.getKey(goalNode) == null){
            throw new IllegalArgumentException("addEdge failed Inersections does not"
                    + "contain either fromNode or toNode.");
        }
	    
	    //Initialize:	    	    	    
	    Map<MapNode, ArrayList<MapNode>> parentMap = createParentMapBFS(startNode, goalNode, 
	                                                                    nodeSearched);
	    
	    //Reconstruct path:
	    return reconstructPath(startNode,goalNode, parentMap);
	        
	}

	/**
     * Helper method that returns the route from
     * the start to the goal.
     * @param MapNode corresponding to the start of the search
     * @param MapNode corresponding to the end of the search
     * @param hashMap of nodes' and their parents
     * @return a linked list of MapNodes corresponding to the shortest
     * path from the start node to the finish node.
     **/
	private LinkedList<GeographicPoint> reconstructPath(MapNode start, MapNode goal, 
	        Map<MapNode, ArrayList<MapNode>> parentMap)
	{	         
	    //list to return
        LinkedList<GeographicPoint> path = new LinkedList<>();  
        
        //start and goal and follow the parents up the parent map
        //adding each parent to the path list
        MapNode curr = goal;
        while (curr != start) {
            path.addFirst(curr.getPosition());
            curr = parentMap.get(curr).get(parentMap.get(curr).size() - 1);
        }
        path.addFirst(start.getPosition());
        return path;
	}
	
	/**
	 * Helper method that searches for path from creating a parentMap
	 * hashmap on the way (which is used later).
	 * @param Queue of MapNodes to check.
     * @param HashSet tracking the nodes already visited
     * @param map of a node to a list of it's parent's nodes (parent nodes
     * are nodes that have an edge directed to this node)
     * @param a GeographicPoint used to create a goal MapNode
     * @return False if no path is found, true otherwise.
	 **/
	private Map<MapNode, ArrayList<MapNode>> createParentMapBFS(MapNode start, MapNode goal, 
	                                 Consumer<GeographicPoint> nodeSearched)
	{
	    //initialize lists:
	    Map<MapNode, ArrayList<MapNode>> parentMap = new HashMap<>();
	    Queue<MapNode> toCheck= new LinkedList<>();
	    HashSet<MapNode> visited= new HashSet<>();
	    toCheck.add(start);
        visited.add(start);
	    MapNode curr;
	    
	    while (!toCheck.isEmpty()){
            curr = toCheck.remove();
            nodeSearched.accept(curr.getPosition());
            //check if goal has been reached:
            if (curr.equals(goal)){
                return parentMap;
            }
            //add all unvisited neighbors to the queue:
            for (MapEdge me : intersections.get(curr)){
                 MapNode mn= me.getEndNode();
                 if (!visited.contains(mn)){                   
                     visited.add(mn);
                     //if not on parent map, add it:
                     if(!parentMap.containsKey(mn)){
                         parentMap.put(mn, new ArrayList<MapNode>());
                     }
                     //add curr as parent:
                     parentMap.get(mn).add(curr);
                     //add gp to the queue:
                     toCheck.add(mn);              
                }
            }
        }
	    //return null if no list is found
        return null;
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
	  //Validate input:
        if(start== null || goal== null){
            return null;
        }
        
        if( start.equals(goal)) return new LinkedList<GeographicPoint>();
        
        //Create nodes from geographical points for comparison purposes
        //(two MapNode objects are equal if they have the same GeographicPoint)
        MapNode startNode= new MapNode(start);
        MapNode goalNode= new MapNode(goal);
        
        if (intersections.getKey(startNode) == null || intersections.getKey(goalNode) == null){
            throw new IllegalArgumentException("addEdge failed Inersections does not"
                    + "contain either fromNode or toNode.");
        }

	    //Initialize lists and distances to infinity
	    PriorityQueue<MapNode> pq= new PriorityQueue<>(new DijkstraComparator());
	    //enqueue {S,0} onto the PQ
        startNode.setDistanceToStart(0);
                     
        Map<MapNode, ArrayList<MapNode>> parentMap=  createPriorityParentMap(pq, goalNode, 
                                                        startNode, "Dijkstra", nodeSearched);   
                              
        return reconstructPath(startNode,goalNode, parentMap);
		
	}
	
	   /**
     * Helper method that searches for path from creating a parentMap
     * hashmap on the way (which is used later).
     * @param Queue of MapNodes to check.
     * @param HashSet tracking the nodes already visited
     * @param map of a node to a list of it's parent's nodes (parent nodes
     * are nodes that have an edge directed to this node)
     * @param a GeographicPoint used to create a goal MapNode
     * @return False if no path is found, true otherwise.
     **/
    private Map<MapNode, ArrayList<MapNode>> createPriorityParentMap(PriorityQueue<MapNode> toCheck,
            MapNode goalNode, MapNode startNode,String comp, Consumer<GeographicPoint> nodeSearched)
    {
        //initialize:
        Map<MapNode, ArrayList<MapNode>> parentMap= new HashMap<>();
        HashSet<MapNode> visited= new HashSet<>();
        toCheck.add(startNode);
        MapNode curr;
        /*
         * Ugly way to ensure all priorities start ant infinity
         */
        for(MapNode mn : intersections.getKeySet()){
            if(!mn.equals(startNode)){
                mn.setDistanceToGoal(Double.POSITIVE_INFINITY);
                mn.setDistanceToStart(Double.POSITIVE_INFINITY);
            }
        }
        
        //while PQ is not empty:
        int i=0;
        while (!toCheck.isEmpty()){
            //dequeue curr from front of queue
            curr = toCheck.remove();
            
          
            //if curr not visited:
            if(!visited.contains(curr)){
                //add curr to visted set   
                nodeSearched.accept(curr.getPosition());
                visited.add(curr);
                i++;
                if(curr.equals(goalNode)){
                    return parentMap;
                }
                //for each of curr's neighbors, n, not visited:
                for (MapEdge me : intersections.get(curr)){
                     MapNode mn = intersections.getKey(me.getEndNode());
                    if (!visited.contains(mn)){                       
                        
                        switch (comp){
                        
                        case ("Dijkstra") :
                            
                            if ( ( curr.getDistanceToStart() + me.getDitsance() )  < mn.getDistanceToStart() ){                          
                                mn.setDistanceToStart(curr.getDistanceToStart() +  me.getDitsance() );
                             // if node is new, make a spot for it in the parent map
                                if(!parentMap.containsKey(mn)){
                                    parentMap.put(mn, new ArrayList<MapNode>());
                                }
                                parentMap.get(mn).add(curr);
                                toCheck.add(mn);    
                            }
                        
                        case ("A*") :
                            
                            // if path is closer to goal update 
                            if ( curr.getDistanceToStart() + me.getDitsance() + 
                                 mn.getPosition().distance(goalNode.getPosition())
                                  <  mn.getDistanceToStart() + mn.getDistanceToGoal()){
                               mn.setDistanceToStart(curr.getDistanceToStart() +  me.getDitsance());
                               mn.setDistanceToGoal( mn.getPosition().distance(goalNode.getPosition()));;
                               if(!parentMap.containsKey(mn)){
                                   parentMap.put(mn, new ArrayList<MapNode>());
                               }
                               parentMap.get(mn).add(curr);
                               toCheck.add(mn);
                           }  
                        
                        }                                           
                    }
                }                              
            }                    
        }
        System.out.println("No path found.");
        return null;
    }
    
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
	  //Validate input:
        if(start== null || goal== null){
            return null;
        }
        
        if( start.equals(goal)) return new LinkedList<GeographicPoint>();
        
        //Create nodes from geographical points for comparison purposes
        //(two MapNode objects are equal if they have the same GeographicPoint)
        MapNode startNode= new MapNode(start);
        MapNode goalNode= new MapNode(goal);
        
        if (intersections.getKey(startNode) == null || intersections.getKey(goalNode) == null){
            throw new IllegalArgumentException("addEdge failed Inersections does not"
                    + "contain either fromNode or toNode.");
        }

        //Initialize lists and distances to infinity
        PriorityQueue<MapNode> pq= new PriorityQueue<>(new StarComparator());
        //enqueue {S,0} onto the PQ
        startNode.setDistanceToStart(0);
        startNode.setDistanceToGoal(0);
                     
        Map<MapNode, ArrayList<MapNode>> parentMap=  createPriorityParentMap(pq, goalNode, 
                                                        startNode, "A*", nodeSearched);        
                              
        return reconstructPath(startNode,goalNode, parentMap);
	}
	
    /**
     * 1) Nearest Neighbour Search
     *      The next node is chosen by traveling to the
     *      nearest unexplored neighbour. This method 
     *      usually finds a path about 25% longer than
     *      the shortest path, however under unfortunate
     *      start orientations, it could also produce
     *      the WORST possible path.
     */
    public  List<GeographicPoint> nearestNeighbour()
    {
        //Validate input:
        if ( intersections == null ) return null;
               
        // Note:
        // Picking a random object takes on average O(n/2)
        // which is pretty bad, but the greedy algorithm
        // benefits from running multiple times at different
        // starting points  and comparing for the best solution
        // so efficiency is sacrificed for a better answer here.
         
        //pick a random node to start at:
        int rand = new Random().nextInt(intersections.size());
        int i = 0;
        MapNode start = null;
        for(MapNode mn : intersections.iterator()){
            if (i == rand){
                start = mn;
                break;
            }
            i++;
        }
        
        // linked list chosen since efficient to add
        // when the total size of array will be unknown
        // (i.e. avoid resizing) and element look up
        // will probably not be needed
        List<MapNode> tour = new LinkedList<>();
        branchExit = false;
        followBranch(start, tour);
        
        //Change list into Geographic Points:
        List<GeographicPoint> geoTour = new LinkedList<>();
        for(MapNode mn : tour){
            geoTour.add(mn.getPosition());
        }
        
        /*
         * At this point all nodes have been covered and now must return home
         */
        List<GeographicPoint> returnHome = aStarSearch(tour.get(tour.size()-1).getPosition(), start.getPosition());
        
        for( int z = 1; z < returnHome.size(); z++){
            geoTour.add(returnHome.get(z));
        }

        return geoTour;
        
    }
    /**
     * Function is recursively called to explore branches
     * by following the closes nodes and then backtracking
     * when a dead end is reached.
     * A global variable, 'branchExit' is used to break the 
     * recursion.
     * @param node the node being processed
     * @param tour a list tracking the nodes visited
     */
    private void followBranch(MapNode node, List<MapNode> tour){
        PriorityQueue<MapEdge> minEdges = new PriorityQueue<>(new MinEdgeComparator());
        
        //for each attached edge
        for (MapEdge me : intersections.get(node)){
            
            //if the end node hasn't been visited, add to queue
            MapNode mn = intersections.getKey(me.getEndNode());           
            if (!tour.contains(mn)){  
                minEdges.add(me);
            }
        }
        
        //check edges in order of the cloest nodes:
        MapEdge minEdge = null;
        while(!minEdges.isEmpty() && branchExit == false){
                //add the node here, so it gets added multiple times
                //when the serach needs to backtrack after reaching
                //a dead end.
                tour.add(node);
                minEdge = minEdges.remove();
                followBranch(minEdge.getEndNode(), tour);   
        }
            //add node to the tour here to get the final back track of the node
            
            //checks to see if all nodes have been visited
            //costly costly costly...
            //point is back tracking is very inefficient so after all nodes are 
            //visited, exit and use A Star to find the fastest path back  to start
            if(tour.containsAll(intersections.getKeySet())){
                branchExit = true;
            }
            else{
                tour.add(node);
            }
    }



	
      public static void runTest(int i, String file, GeographicPoint start, GeographicPoint end) {
          MapGraph graph = new MapGraph();
    
    
          GraphLoader.loadRoadMap("data/graders/mod3/" + file, graph);
         // CorrectAnswer corr = new CorrectAnswer("data/graders/mod3/" + file + ".answer", false);
          List<GeographicPoint> path = graph.dijkstra(start, end);
      }
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.print("DONE. \nLoading the map...");
		
		System.out.println("DONE.");
		
		// You can use this method for testing.
        
        System.out.println("Testing..");
        
        /*
         * Testing results of NN algorithm on simple data.
         * Ran many times, keeping the best path found:
         */
             
        //Run NN many times and keep smallest result :
     //   double tourLength = 0;
     //   double minTourLength = Double.POSITIVE_INFINITY;
      //  List<GeographicPoint> minTour = null;
      //  List<GeographicPoint> testNN;
     //   for( int i = 0; i < 100; i++ ){
            
     //       testNN = firstMap.nearestNeighbour();
       //     tourLength = 0;
       //     int j = 0;
       //     for(GeographicPoint gp : testNN){
       //         if ( j > 0){
      //              tourLength += testNN.get(j).distance(testNN.get(j-1)); 
      //          }
       //         j++;             
       //     }
      //      if(tourLength < minTourLength){
      //          minTourLength = tourLength;
      //          minTour = testNN;
      //      }
      //  }
        
      //  System.out.println("The minimum tour length was " + minTourLength);
       // for(GeographicPoint gp : minTour){
       //     System.out.println(gp);
      // }
        
        /*
         * This code consistently gives 2700 as the shortest path.
         */
           
       /*
        * TESTING OF CHRISTOFIDES ALGORITHM:
        */       
    
        Christofides test = new Christofides(firstMap.getVerticesList(),firstMap.getEdgesList());
        
        /*
         * Notes on christofides algorithm:
         * 
         * distance found was : 3157 
         * Comparing this with the nearest neighbour implementation, it gave an 'incorrect result'
         * (i.e. , greedy found a shorter path)
         * 
         * By analyzing small cases, I believe that the difference in the results between the two
         * implementations, NearestNeighbor (Greedy), and Christofides algorithm (approximation)
         * are :
         * 
         * 1) I used a very approximate matching implementation to match the 'odd nodes' in the minimum
         * spanning tree. 
         * 
         * Improvements to this would be to implement either Edmonds' algorithm or Blossoms' algorithm,
         * which would yield perfect matching, 
         * but at the moment I found these to be too overwhelming to me, so I decided to use an easier
         * greedy-like algorithm. To get more accurate results I could run the greedy algorithm many
         * times and take the 'best' result, but then performance would suffer.
         * 
         * It should also be noted, that in the small testing cases, if the odd nodes are matched
         * 'better', then the path found would be the same as that of the NearestNeighbour algorithm found.
         * 
         */
       
        
        
        /*
        // A very simple test using real data
        MapGraph testMap = new MapGraph();
        GraphLoader.loadRoadMap("data/maps/utc.map", testMap);    
        
        
        System.out.println("Testing..");
        System.out.println(testMap.getNumEdges());
        System.out.println(testMap.getNumVertices());
        
        List<GeographicPoint> testroute = testMap.NearestNeighbour();
        for(GeographicPoint gp : testroute){
            System.out.println(gp);
       }
        */
        
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		//runTest(3, "map3.txt", new GeographicPoint(0, 0), new GeographicPoint(0, 4));
		
		// You can use this method for testing.
		GeographicPoint start= new GeographicPoint(1.0,1.0);
		GeographicPoint goal= new GeographicPoint(8.0,-1.0);
		
		System.out.println("Testing..");
		System.out.println(firstMap.getNumEdges());
		System.out.println(firstMap.getNumVertices());
		
		
		List<GeographicPoint> testBfs= firstMap.bfs(start,goal);
		for(GeographicPoint gp : testBfs){
		    System.out.println(gp);
		}
		*/
		
		
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		
		///////////////////
		
		//MapGraph graph = new MapGraph();

        //GraphLoader.loadRoadMap("data/graders/mod3/" + file, graph);
		
		///////////////////
		///MapGraph simpleTestMap = new MapGraph();
		//GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		//GeographicPoint testStart = new GeographicPoint(7.0, 3.0);
		//GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		//System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
	//	List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		
		//List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);

       // for(GeographicPoint gp : testroute2){
       //    System.out.println(gp);
       // }
		
	//	MapGraph testMap = new MapGraph();
	//	GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		
		// A very simple test using real data
	//	testStart = new GeographicPoint(32.869423, -117.220917);
	//	testEnd = new GeographicPoint(32.869255, -117.216927);
	//	System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
	//	testroute = testMap.dijkstra(testStart,testEnd);
	//	System.out.println(testroute.size());
	//	testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
	//	testStart = new GeographicPoint(32.8674388, -117.2190213);
	//	testEnd = new GeographicPoint(32.8697828, -117.2244506);
	//	System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
	//	testroute = testMap.dijkstra(testStart,testEnd);
	//	System.out.println(testroute.size());
	//	testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		 
		
		
		/* Use this code in Week 3 End of Week Quiz */
		/*
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");
		

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		//List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
        */
		
		
	}
	
}
