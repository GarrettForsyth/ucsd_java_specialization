package roadgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import geography.GeographicPoint;

/**
     * 2) Christofides algorithm
     * 
     *      Note : this is for an undirected graph, so any data for one way streets may cause hiccups
     * 
     *      FROM WIKIPDEDIA:
     * 
     *    1   Create a minimum spanning tree T of G.
     *    2   Let O be the set of vertices with odd degree in T. By the handshaking lemma, O has an even number of vertices.
     *    3   Find a minimum-weight perfect matching M in the induced subgraph given by the vertices from O.
     *    4   Combine the edges of M and T to form a connected multigraph H in which each vertex has even degree.
     *    5   Form an Eulerian circuit in H.
     *    6   Make the circuit found in previous step into a Hamiltonian circuit by skipping repeated vertices (shortcutting).
     *    
     *    In graph theory, a branch of mathematics, the handshaking lemma is the statement that every finite undirected graph 
     *    has an even number of vertices with odd degree (the number of edges touching the vertex).
     */

public class Christofides{
    
    private List<MapNode> vertices;
    private List<MapEdge> edges;
    
    public Christofides(List<MapNode> v, List<MapEdge> e){
        vertices = v;
        edges = e;
        
        // 1   Create a minimum spanning tree T of G.
        MapGraph mst = kruskalMinSpanTree();
        
        // 2   Let O be the set of vertices with odd degree in T. By the 
        // handshaking lemma, O has an even number of vertices.
        
        
        //  3   Find a minimum-weight perfect matching M in the induced 
        //      subgraph given by the vertices from O.
        //      (i.e. there MUST be an even number of odd vertices via
        //      the handshake lemma, so pair these off such that the
        //      edges that cover them cover the least distance)
        //      This could be done more precisely using Edmonds or 
        //      Blossom's algorithm, but using a Greedy method is simpler 
        //      and gives a reasonable approximation.
        // 4    Combine the edges of M and T to form a connected multigraph H
        //      in which each vertex has even degree.
        
        mst = perfectMatching(mst);
        
        // 5   Form an Eulerian circuit in H.
        //     This is done by implementing Fleury's algorithm
        //     We know the graph only has even vertices from above.
        
        /*
        System.out.println("Testing DFSCount.. ");
        boolean[] visited = new boolean[vertices.size()];
        System.out.println(DFSCount(vertices.get(0), visited, vertices));
        */
        
        List<MapNode> eulerianCircuit = getEulerianPath(mst);

        double dist = 0;
        int j = 0;
        
        System.out.println("Printing Eulerian circuit..");
        
        for(MapNode mn : eulerianCircuit){
            System.out.println(mn.getPosition());
            if (j > 0){
                dist += eulerianCircuit.get(j).getPosition().distance(eulerianCircuit.get(j-1).getPosition());
            }
            j++;
        }
        System.out.println("Total distance of tour was : " + dist);
        
        
        
        
        
    }
    
    /*
     * Eulerain Path for a directed graph algorithm:
     * 
     * Start with an empty stack and an empty circuit (eulerian path).
     *
     * If current vertex has no out-going edges (i.e. neighbors) - add it to circuit, 
     * remove the last vertex from the stack and set it as the current one. Otherwise
     * (in case it has out-going edges, i.e. neighbors) - add the vertex to the stack,
     * take any of its neighbors, remove the edge between that vertex and selected neighbor,
     *  and set that neighbor as the current vertex.
     * Repeat step 2 until the current vertex has no more out-going edges (neighbors) and the stack is empty.
     */
    private List<MapNode> getEulerianPath(MapGraph g){
        List<MapNode> vert = g.getVerticesList();       
        Stack<MapNode> s = new Stack<>();
        List<MapNode> path = new LinkedList<>();
        //we've already matched the MST so can start anywhere:
        MapNode curr = vert.get(0);
        s.push(curr);
        
        while(curr.getNodeNeighbors().size() != 0 || !s.isEmpty()){
            if(curr.getNodeNeighbors().size() == 0){
                path.add(curr);
                curr = s.pop();
            }
            else{      
                s.push(curr);
                
                MapNode mn = curr.getNodeNeighbors().get(0);
                g.removeEdge(new MapEdge(curr, mn));
                curr = vert.get(vert.indexOf(mn));

            }
        }
        return path;
    }
    

    
    /*
     *   Takes as input an undirected graph K(V ) with edge costs we ≥ 0
     *    satisfying the triangle inequality and outputs a perfect matching
     *   M
     */
    private  MapGraph perfectMatching(MapGraph g){
        
                // tree is an undirected graph in which any two vertices are connected
                // by exactly one path. In other words, any acyclic connected graph is a
                // tree. A forest is a disjoint union of trees.

                List<MapNode> activeComponents = getActiveComponents(g);
                System.out.println(activeComponents.size());

                while (!activeComponents.isEmpty()){
                        MapNode mn = activeComponents.get(activeComponents.size() - 1);

                        List<MapNode> path = findPath(mn, activeComponents, g);

                        connectActiveNodes(path, g);
                        //remove connected nodes from active component list
                        activeComponents.remove(activeComponents.indexOf(path.get(path.size()-1)));
                        activeComponents.remove(activeComponents.indexOf(path.get(0)));
                    
                }
         return g;
    }
    /*
     * Connects two active components by creating a path
     * of edges between them.
     */
    private void connectActiveNodes(List<MapNode> path, MapGraph g){
        MapEdge newEdge = null;
        for(int i = 0; i < path.size()-1; i ++){

            newEdge = new MapEdge(path.get(i), path.get(i+1));
            g.addEdge(newEdge);
            //undirected graph so add going the other way too
            newEdge = new MapEdge(path.get(i+1), path.get(i));
            g.addEdge(newEdge);
        }
    }
    /*
     * Finds the shortest path between the active components.
     */
    private List<MapNode> findPath(MapNode mn, List<MapNode> activeComponents, MapGraph g){
        double minDistance = Double.POSITIVE_INFINITY;
        double routeDistance = 0;
        List<GeographicPoint> minMatchRoute = null;
        for( MapNode m : activeComponents){
            if(!m.equals(mn)){
                List<GeographicPoint> route = g.aStarSearch(m.getPosition(),mn.getPosition());
                routeDistance = getRouteDistance(route);
                if( routeDistance < minDistance){
                    minDistance = routeDistance;
                    minMatchRoute = route;
                }
            }
        }
        List<MapNode> minMatchRouteNodes = new LinkedList<>();
        BST<MapNode, ArrayList<MapEdge>> bst = g.getIntersections();
        for(int i =0; i < minMatchRoute.size(); i++ ){
            minMatchRouteNodes.add(bst.getKey(new MapNode(minMatchRoute.get(i))));
        }
        return minMatchRouteNodes;
    }
    /*
     * Get's the total distance traveled in a route
     */
    private double getRouteDistance(List<GeographicPoint> route){
        double distance = 0;
        for(int i = 1; i < route.size(); i ++ ){
            distance += route.get(i).distance(route.get(i-1));
        }
        return distance;
    }
    
    /*
     * Checks a list of map nodes to see if there are any active components.
     * Define an active component to be
     * any connected component C of F with an odd number of nodes, else call the component
     * inactive. The while-loop terminates when all connected components C of F are inactive.
     */
    private List<MapNode> getActiveComponents(MapGraph g){
        List<MapNode> oddVertices =   new LinkedList<>();
        for(MapNode me : g.getVerticesList()){
            //if there is an odd number of neighbors
            if(me.getNodeNeighbors().size() % 2 != 0){
                oddVertices.add(me);
                //add to the oddVertices list
            }
        }
        return oddVertices;
    }
    /*
     * Finds the minimum spanning tree in the graph of intersections:
     * (i.e. the way to connect all intersections using the least amount
     *  of road distance)
     *  
     *  From  https://www.hackerearth.com/practice/algorithms/graphs/minimum-spanning-tree/tutorial/
     *  "Kruskal’s Algorithm builds the spanning tree by adding edges one by 
     *  one into a growing spanning tree. Kruskal's algorithm follows greedy 
     *  approach as in each iteration it finds an edge which has least weight 
     *  and add it to the growing spanning tree"
     */
    private MapGraph kruskalMinSpanTree(){
        
        //Kruskal's algorithm:
        
        //sort the edges of G in increasing order by length
        Collections.sort(edges, new MinEdgeComparator());
       // keep a subgraph S of G, initially empty
        MapGraph s = new MapGraph();
        
        // Structure used to track whether vertices  are part of
        // the same component
        QuickUnionUF edgeComponents = new QuickUnionUF(vertices.size());
        
       // for each edge e in sorted order
        for(MapEdge me : edges){
           // if the end points of me are disconnected in S
            Set<GeographicPoint> sVertices = s.getVertices();
            //if either vertex is missing, the end points MUST not be connected, so add and connect:
            if(!sVertices.contains(me.getStartNode().getPosition()) || !sVertices.contains(me.getEndNode().getPosition())){
                //add start node if missing
                if(!sVertices.contains(me.getStartNode())){
                    s.addVertex(me.getStartNode().getPosition());
                }
                //add end node if missing
                if(!sVertices.contains(me.getEndNode())){
                    s.addVertex(me.getEndNode().getPosition());
                }
                
             // add e to S
              // Unions the nodes using their positions in the list to keep track of components
              // each component is defined by the smallest element attached to it
              edgeComponents.union(vertices.indexOf(me.getStartNode()), vertices.indexOf(me.getEndNode()));
              s.addEdge(me); 
            }
            //if both vertices already exist, check to see if the components are
            //connected. This is done by utilizing a union find structure.
            else{               
                if(!edgeComponents.connected(vertices.indexOf(me.getStartNode()), vertices.indexOf(me.getEndNode()))){
                    MapEdge newEdge = new MapEdge(me.getStartNode(), me.getEndNode());
                    edgeComponents.union(vertices.indexOf(me.getStartNode()), vertices.indexOf(me.getEndNode()));
                    s.addEdge(newEdge);
                }
            }
            
        }
        
        //now add each edge's pair to keep it undirected
        for( MapEdge me : s.getEdgesList()){
            s.addEdge(new MapEdge(me.getEndNode(), me.getStartNode()));
        }
       // return S
        return s;
    }  
    
    /*
     * QuickUnion Structure used to track which
     * components the MapNodes are attached to.
     * (We only wish to connect nodes of different
     * components)
     */
    private class QuickUnionUF{
        
        private int[] id;
        
        public QuickUnionUF(int N){
            id = new int[N];
            for (int i = 0; i < N; i++) id[i]=i;
        }
        
        private int root(int i){
            while(i != id[i]){
                id[i] = id[id[i]];
                i = id[i];
            }
            return i;
        }
        
        public boolean connected(int p,int q){
            return root(p) == root(q);
        }
        
        public void union(int p, int q){
            int i = root(p);
            int j = root(q);
            id[i] = j;
        }
        
    }
    
    
   

}