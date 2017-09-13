package roadgraph;

import java.util.Comparator;

public class DijkstraComparator implements Comparator<MapNode>{
        
        @Override
        public int compare(MapNode mn1, MapNode mn2) {
            if(mn1.getDistanceToStart() < mn2.getDistanceToStart()) return -1;
            if(mn1.getDistanceToStart() > mn2.getDistanceToStart()) return  1;
            return 0;
        }

    }