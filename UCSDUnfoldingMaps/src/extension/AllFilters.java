package extension;

import java.util.ArrayList;

public class AllFilters implements Filter{
	ArrayList<Filter> filters;
	
	public AllFilters(){
		filters= new ArrayList<Filter>();
	}
	
	public void addFilter(Filter f){
		filters.add(f);
	}
	
	public boolean satisfies(MeteoriteMarker mm){
		for(Filter f : filters){
			if(! f.satisfies(mm)){
				return false;
			}
		}
		return true;
	}
}
