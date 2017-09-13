package roadgraph;

import java.util.Objects;

import geography.GeographicPoint;

/**
 * MapeNode.java
 * 
 * A POJO to represent an edge (or street) in the MapGraph.
 */

public class MapEdge {
    private final MapNode start;
    private final MapNode end;
    private String roadType;
    private String streetName;
    private final double distance;
    

    
    /*
     * Various constructors requiring at least start and end points:
     */
    
    public MapEdge(MapNode start, MapNode end){
        if(start==null || end== null){
            throw new IllegalArgumentException("Null coordinate in MapEdge constructor.");
        }
        this.start=start;
        this.end=end;
        this.roadType="";
        this.streetName="";
        this.distance=(start.getPosition()).distance(end.getPosition());
    }
    
    public MapEdge(MapNode start, MapNode end, String streetName, String roadType){
        this(start, end);
        if(streetName!=null){ this.streetName=streetName;}
        else{this.streetName="";}  
        if(roadType!=null){ this.roadType=roadType;}
        else{this.roadType="";}
    }
    
    /*
     * Getter methods:
     */
    public MapNode getStartNode(){
        return this.start;
    }
    
    public MapNode getEndNode(){
        return this.end;
    }
    
    public String getStreetName(){
        return this.streetName;
    }
    public String getRoadType(){
        return this.roadType;
    }
    public double getDitsance(){
        return this.distance;
    }
    /*
     * Start and end locations should never change once set
     * and by consequence, neither should distance.
     * Setter methods:
     */
    public void setStreetName(String name){
        this.streetName=name;
    }
    public void setRoadType(String roadType){
        this.roadType= roadType;
        return;
    }

    /*
     * Two MapEdges are equal if the have the same
     * start AND end locations.
     * Equals to method and hashCode() method:
     */
    public boolean equals(Object o) {
        if ( o == this) return true;
        if ( o == null) return false;
        if (o instanceof MapEdge) {
          MapEdge me= (MapEdge) o;
          if (this.start.equals(me.start) &&
                  this.end.equals(me.end)) return true;
        }
        return false;
        
     }
    /*
     * Always change hashmap() with equals
     */
    public int hashCode() {
        return Objects.hash(start,end);
    }
    /*
     * More relevant  toString() :
     */
    public String toString(){
        String str= "[";
        if(this.streetName != null){
            str+= "Street Name: " + this.streetName + ", ";
        }
        str += "Start: " + start.toString();
        str += ",  End: " + end.toString();
        str += ", Distance: " + this.distance + "]";
        return str;
    }

}
