package extension;

public class YearAfterFilter implements Filter{
	private int myYear;
	
	public YearAfterFilter(int year){
		myYear= year;
	}
	
	public boolean satisfies(MeteoriteMarker mm){
		return mm.getYear() >= myYear;
	}

}
