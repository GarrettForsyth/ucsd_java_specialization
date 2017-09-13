package roadgraph;

import java.util.Comparator;

public class MinEdgeComparator implements Comparator<MapEdge>{
    @Override
    public int compare(MapEdge me1, MapEdge me2) {
        if(me1.getDitsance() < me2.getDitsance()) return -1;
        if(me1.getDitsance() > me2.getDitsance()) return  1;
        return 0;
    }
}
