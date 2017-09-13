package roadgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import geography.GeographicPoint;

/**
 * MapeNode.java
 * 
 * A class to represent a Node in the MapGraph.
 */

public class MapNode implements Comparable<MapNode>{
    private List<MapEdge> neighbors;
    private GeographicPoint position;
    
    //used for priority:
    private double distanceToStart;
    private double distanceToGoal;
    
    /*
     * Constructor for no neighbors
     */
    public MapNode(GeographicPoint loc){
        if(loc == null){
            throw new IllegalArgumentException("Cannont instantiate with null location.");
        }
        position = loc;
        neighbors = new ArrayList<MapEdge>();
        distanceToStart = Double.POSITIVE_INFINITY;
        distanceToGoal = Double.POSITIVE_INFINITY;
    }
    
    /*
     * Getter for position:
     */
    public GeographicPoint getPosition(){
        return this.position;
    }
    
    /*
     * Getter for neighbors:
     */
    public List<MapEdge> getNeighbors(){
        return  neighbors;
    }
    
    public List<MapNode> getNodeNeighbors(){
        List<MapNode> nodeNeighbors= new ArrayList<>(neighbors.size());
        for(MapEdge me : neighbors){
            nodeNeighbors.add(me.getEndNode());
        }
        return nodeNeighbors;
    }
    /*
     * Remove Neighbour
     */
    public boolean removeEdge(MapEdge me){
        if(me != null && neighbors.contains(me)){
            neighbors.remove(neighbors.indexOf(me));
            return true;
        }
        return false;
    }
    
    
    /*
     * Getter for distanceToStart:
     */
    public double getDistanceToStart(){
        return this.distanceToStart;
    }
    /*
     * Getter for distanceToStart:
     */
    public double getPriority(){
        return (this.distanceToStart+this.distanceToGoal);
    }
    
    /*
     * Getter for distanceToGaol:
     */
    public double getDistanceToGoal(){
        return this.distanceToGoal;
    }
    
    /*
     * Setter for distanceToStart:
     */
    public void setDistanceToStart(double newDist){
        this.distanceToStart = newDist;
    }
    /*
     * Setter for distanceToGaol:
     */
    public void setDistanceToGoal(double newDist){
        this.distanceToGoal = newDist;
    }
    
    
    /*
     * Add edge of various flavours:
     */
    
    public void addEdge(MapEdge newEdge){
        if(!neighbors.contains(newEdge)){
            neighbors.add(newEdge);
        }
        return;
    }
    
    public void addEdge(MapNode neighborLocation){
        MapEdge newEdge= new MapEdge(this, neighborLocation);
        this.addEdge(newEdge);
    }
       
    public void addEdge(MapNode neighborNode, String streetName, String roadType){
        MapEdge newEdge= new MapEdge(this,
                         neighborNode, streetName, roadType);
        this.addEdge(newEdge);
    }
    
    /*
     * Two MapNodes are equal if they are at the same position.
     * Satisify 1) Reflexive
     *          2) symmetric
     *          3) transitive
     * Equals to method and hashCode() method:
     */
    public boolean equals(Object o) {
        if( o == this) return true;
        if (o == null ) return false;
        if (o instanceof MapNode) {
          MapNode mn= (MapNode) o;
          if (this.position.equals(mn.position)) return true;
        }
        return false;     
     }
    
    public int hashCode() {
        return Objects.hash(position);
    }
    
    /*
     * toString method:
     */
    public String toString() {       
       String str= "Map Node Position: " + position.toString();
       return str;       
    }

    /*
     * MapNode objects will be ordered by greatest
     * latitude, and then by greatest longitude.
     */
    @Override
    public int compareTo(MapNode o) {
        if( this.getPosition().getX() < o.getPosition().getX()) return -1;
        if( this.getPosition().getX() > o.getPosition().getX()) return  1;
        
        if( this.getPosition().getY() < o.getPosition().getY()) return -1;
        if( this.getPosition().getY() > o.getPosition().getY()) return  1;
        return 0;
    }
    

    
    
    
}
