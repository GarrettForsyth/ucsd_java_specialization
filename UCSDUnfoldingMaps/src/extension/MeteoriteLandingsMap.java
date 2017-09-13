package extension;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PFont;
//import controllP5 library to use some interactive
//components such as text fields
import controlP5.*;


/** MeteoriteLandingsMap
 * An application with an interactive map displaying meteorite landing data. 
 * @author Garrett Forsyth
 * January 4 2017
 */

public class MeteoriteLandingsMap extends PApplet{
	
	//RSS feed with meteorite landing data from NASA
	private String meteoriteURL = "https://data.nasa.gov//api/views/gh4g-9sfh/rows.rss?accessType=DOWNLOAD";
	private String meteoriteCSV = "Meteorite_Landings.csv";
	
	//a file with country info
	private String countryFile= "countries.geo.json";
	
	//the map
	private UnfoldingMap map;
	
	//Markers for meteorite landings
	private List<Marker> meteoriteMarkers;
	//List of country markers
	private List<Marker> countryMarkers;
	
	//Variables for display
	//set some default values
	private int maxYear=2017;
	private int minYear=1700;
	private double minMass=0;;
	
	//for interactivity
	private ControlP5 cp5;
	
	//for filtering
	private AllFilters af;
	
	//fonts
	PFont zigBlack;
	
	////////////////////////////////////////////////
	////////////////////////////////////////////////
	
	public void setup(){
		// Initializing map
		// Note: OPENGL is and API for rendering 2D
		// and 3D vector graphics. Typically used to
		// interact with a graphics processing unit to achieve
		// hardware-accelerated rendering
		size(900,660, OPENGL);
		
		//map and default events
		map= new UnfoldingMap(this,200,50,650,550);
		MapUtils.createDefaultEventDispatcher(this,map);
		
		//Read in countries from CSV file
		List<Feature> countries= GeoJSONReader.loadData(this,countryFile);
		countryMarkers= MapUtils.createSimpleMarkers(countries);
		
		//Read in meteorites from CSV file
		List<PointFeature> meteorites= ParseFeed.parseMeteoriteCSV(this,meteoriteCSV);
		meteoriteMarkers= new ArrayList<Marker>();
		
		for(PointFeature meteorite : meteorites){
			meteoriteMarkers.add(new MeteoriteMarker(meteorite));
		}
		
		//add markers to the map
		map.addMarkers(meteoriteMarkers);
		
		//set tweening true
		map.setTweening(true);
		
		//controllP5 object
		cp5= new ControlP5(this);
		int xbase1= 200;
		int ybase1=610;
		
		cp5.addTextfield("input1")
			.setPosition(xbase1+300,ybase1)
			.setSize(40,30)
			.setFocus(true);
		
		cp5.addTextfield("input2")
		.setPosition(xbase1+377,ybase1)
		.setSize(40,30)
		.setFocus(true);
		
		cp5.addTextfield("input3")
		.setPosition(xbase1+560,ybase1)
		.setSize(90,30)
		.setFocus(true);
		
		//set up filters
	    af= new AllFilters();
		af.addFilter(new YearAfterFilter(minYear));
		af.addFilter(new YearBeforeFilter(maxYear));
		af.addFilter(new GreaterThanMassFilter(minMass));
	
		System.out.println("Reading in : " + meteoriteMarkers.size() + " meteorites.");
		
		//font
		//zigBlack= createFont("Dialog",32);
		//textFont(zigBlack);
		//fill(0);
		
		
			
	} // end setup
	
	//Note: this method is looping unless instructed not to
	public void draw(){
		background(0); //sets background colour
		map.draw(); //draws map (including markers)
		addKey();
	}
	
	//used by cp5
	public void controlEvent(ControlEvent theEvent){
		if(theEvent.isAssignableFrom(Textfield.class)){
			println("controlEvent: accessing a string from controler '" +
					theEvent.getName()+"':"
					+theEvent.getStringValue());
		}
	}
	
	//There is a lot of common code in the following methods, input1,input2,input3
	//This means there is probably a better way to write this part (maybe look at later)
	
	//input for min year
	public void input1(String theText){
		minYear=Integer.parseInt(theText);
		updateFilters();
		println("a textfield event for controller 'input' : "+theText);
	}
	
	//input for max year
	public void input2(String theText){
		maxYear=Integer.parseInt(theText);
		updateFilters();
	}
	
	//input for min mass
	public void input3(String theText){
		minMass=Double.parseDouble(theText);
		updateFilters();
		println("a textfield event for controller 'input' : "+theText);
	}
	
	private void updateFilters(){
		//update filter when member variable is changed
		af= new AllFilters();
		af.addFilter(new YearAfterFilter(minYear));
		af.addFilter(new YearBeforeFilter(maxYear));
		af.addFilter(new GreaterThanMassFilter(minMass));
						
		ArrayList<Marker> filteredList= new ArrayList<Marker>();
		filteredList= filterBy((ArrayList<Marker>)meteoriteMarkers,(Filter) af);
				
		showMarkers();
		hideUnfilteredMarkers(filteredList);
	}
	
	private void addKey(){
		
		//This part creates the text above and below the map
		fill(255,25525);
		int xbase1= 200;
		int ybase1=610;
		rect(xbase1, ybase1,650,30);
		rect(xbase1, 15,650,30);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		String str= "Currently displaying meteorites between the years of     " +  
					"   and "  + "            and more massive than " + minMass + " (g).";
		
		text(str, xbase1+10, ybase1+15);
		
		String str2= "Showing meteorites ocurring between " + minYear
				+ " and " + maxYear + " above the mass of "
				+ minMass + " (g).";
		
		text(str2, xbase1+10, 27);
		////////////////////////////////////////////////////
		//This part creates the legend on the side of the map
		fill(255, 250, 240);
				
		int xbase = 25;
		int ybase = 50;
				
		rect(xbase, ybase, 150, 250,25);
				
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(18);
		text("Meteorite Key", xbase+18, ybase+25);
		
		int xSymBase= xbase+15;
		int ySymBase= ybase+50;
		
		fill(255,2552,255);
		ellipse(xSymBase, ySymBase, 5, 5);
		
		int xTextBase= xSymBase+15;
		int yTextBase= ySymBase;
		
		textSize(12);
		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("Meteorite Marker", xTextBase, yTextBase);
		
		
		
	}
	
	//method to filter a list of MeteoriteMarkers
	private ArrayList<Marker> filterBy(ArrayList<Marker> originalList, Filter f){
		ArrayList<Marker> filteredList= new ArrayList<Marker>();
			for(Marker mm : originalList){
				if(f.satisfies((MeteoriteMarker)mm)){
					filteredList.add(mm);
				}
			}
		return filteredList;
	}
	
	private void showMarkers(){
		for(Marker marker : meteoriteMarkers){
			marker.setHidden(false);
		}
	}
	
	private void hideUnfilteredMarkers(ArrayList<Marker> filteredList){
		for(Marker marker : meteoriteMarkers){
			if(!filteredList.contains(marker)){
				marker.setHidden(true);
			}
		}
	}
	
	public void mouseMoved() {
	    Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
	    if (hitMarker != null) {
	        // Select current marker 
	        hitMarker.setSelected(true);
	    } else {
	        // Deselect all other markers
	        for (Marker marker : map.getMarkers()) {
	            marker.setSelected(false);
	        }
	    }
	}

	

}
