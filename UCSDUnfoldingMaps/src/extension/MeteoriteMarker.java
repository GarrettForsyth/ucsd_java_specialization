package extension;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;


public class MeteoriteMarker extends SimplePointMarker{
	
	//size of marker, set in constructor
	//size is proportinal to mass
	protected double radius;
	
	//class constructor
	public MeteoriteMarker(PointFeature feature){
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		double mass = Double.parseDouble(properties.get("mass").toString());
		properties.put("radius", 3 + mass/1000);
		setProperties(properties);
		this.radius = 3 + getMass()/1000;
	}
	
	//draw method for marker
	public void draw(PGraphics pg, float x, float y){
		if(!hidden){
			// save previous styling
			pg.pushStyle();
			
			//determine the colour based on the year
			//colorDetermine(pg);
			
			//pg.ellipse(x, y, (float)radius,(float)radius);
			
			pg.ellipse(x, y, 5, 5);
			if(this.isSelected()){
				showTitle(pg,x,y);
			}
		}
		
	}
	
	public void showTitle(PGraphics pg, float x, float y)
	{
		String title = this.toString();
		pg.pushStyle();
		
		pg.rectMode(PConstants.CORNER);
		
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);
		
		
		
		
		pg.popStyle();
		
	}
	
	private void colorDetermine(PGraphics pg){
		//not using this part because 1) the year data for some of
		//the entries is not available and 2) there are FAR more 
		//recent meteors than past ones
		
		//roughly colours the marker based on the year it dropped
		//assuming a range of about 1750 (White) to 2017 (Black)
		
		int year= getYear();
		
		float ratio= ((2017-year)/267);
		int intRatio= (int)ratio;
		System.out.println(ratio);
		pg.fill(intRatio,intRatio,intRatio);
	}
	
	/** toString
	 * Returns an earthquake marker's string representation
	 * @return the string representation of an earthquake marker.
	 */
	public String toString(){
		String str= getName() + ", " + getRecClass() + ", " + getMass()
		+ "(g), " + getYear();
		return str;
	}
	
	/*
	 * getters for earthquake properties
	 */
	
	public String getName(){
		return getProperty("name").toString();
	}
	
	public String getID(){
		return getProperty("id").toString();
	}
	
	public String getRecClass(){
		return getProperty("recclass").toString();
	}
	
	public  double getMass(){
		return Double.parseDouble(getProperty("mass").toString());
	}
	
	public int getYear(){
		return Integer.parseInt(getProperty("year").toString());
	}
	
	public double getRadius() {
		return Double.parseDouble(getProperty("radius").toString());
	}
	
}
