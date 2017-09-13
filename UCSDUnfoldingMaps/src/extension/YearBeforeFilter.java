package extension;

public class YearBeforeFilter implements Filter {
	private int myYear;
	
	public YearBeforeFilter(int year){
		myYear= year;
	}
	
	public boolean satisfies(MeteoriteMarker mm){
		return mm.getYear() <= myYear;
	}
}
