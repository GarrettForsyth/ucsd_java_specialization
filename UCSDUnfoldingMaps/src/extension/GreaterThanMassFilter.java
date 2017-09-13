package extension;

public class GreaterThanMassFilter implements Filter{
	private double myMass;
	
	public GreaterThanMassFilter(double mass){
		myMass= mass;
	}
	
	public boolean satisfies(MeteoriteMarker mm){
		return mm.getMass() >= myMass;
	}
}
