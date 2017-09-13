package roadgraph;

import java.util.Comparator;

public class StarComparator implements Comparator<MapNode>{
    @Override
    public int compare(MapNode mn1, MapNode mn2) {
        if(mn1.getPriority() < mn2.getPriority()) return -1;
        if(mn1.getPriority() > mn2.getPriority()) return  1;
        return 0;
    }
}
