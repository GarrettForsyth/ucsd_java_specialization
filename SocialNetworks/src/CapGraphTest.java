import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import graph.CapGraph;
import graph.Graph;
import util.GraphLoader;

public class CapGraphTest {

    @Test
    public void testLoadGraph() {
        CapGraph testGraph = new CapGraph();     
        GraphLoader.loadGraph(testGraph, "data//twitter_higgs.txt");
        assertNotNull(testGraph);
    }
    
    @Test
    public void testAddVertex() {
        CapGraph testGraph = new CapGraph();     
        for ( int i = 0; i < 100; i ++ ){
            testGraph.addVertex(i);
        }
        assertEquals( 100 , testGraph.exportGraph().size() );
    }
    
    @Test
    public void testAddVertexTwice() {
        CapGraph testGraph = new CapGraph();     
        for ( int i = 0; i < 100; i ++ ){
            testGraph.addVertex(i);
        }
        for ( int i = 0; i < 100; i ++ ){
            testGraph.addVertex(i);
        }
        assertEquals( 100 , testGraph.exportGraph().size() );
    }
    
    @Test
    public void testAddEdge() {
        CapGraph testGraph = new CapGraph();   
        for ( int i = 0; i < 100; i ++ ){
            testGraph.addVertex(i);
        }
        for ( int i = 0; i < 50; i ++ ){
            testGraph.addEdge(i, i + 50);
        }
        assertEquals(testGraph.exportGraph().get(0).size() , 1 );
    }
    
    @Test
    public void testAddEdgeTwice() {
        CapGraph testGraph = new CapGraph();   
        for ( int i = 0; i < 100; i ++ ){
            testGraph.addVertex(i);
        }
        for ( int i = 0; i < 50; i ++ ){
            testGraph.addEdge(i, i + 50);
        }
        for ( int i = 0; i < 50; i ++ ){
            testGraph.addEdge(i, i + 50);
        }
        // only one since implemented using a hash SET. 
        assertEquals( 1 , testGraph.exportGraph().get(0).size() );
    }
    
    @Test
    public void testCreateEgoNet(){
        CapGraph testGraph = new CapGraph();
        GraphLoader.loadGraph(testGraph, "data//small_test_graph.txt");
        Graph egoNetTest = testGraph.getEgonet( 6 );
        assertEquals( 14 , testGraph.exportGraph().size() ); 
                    
        
    }
    
    @Test
    public void testGetCSSs(){
        CapGraph testGraph = new CapGraph();
        GraphLoader.loadGraph(testGraph, "data//scc//test_4.txt");
        
        List<Graph> css = testGraph.getSCCs();
        
        System.out.println(css.size());
        
        List<HashMap<Integer,HashSet<Integer>>> cssMaps = new ArrayList<>();
        
        for( Graph g : css){
            cssMaps.add(g.exportGraph());
        }
        
        
        for (HashMap< Integer, HashSet<Integer>> egoMap : cssMaps ){
            for ( Map.Entry<Integer, HashSet<Integer>> me : egoMap.entrySet()){
                System.out.println((me.getKey() + "/" + me.getValue()));               
            }
            System.out.println("");
        }
        
        
        
        
        
    }

}
