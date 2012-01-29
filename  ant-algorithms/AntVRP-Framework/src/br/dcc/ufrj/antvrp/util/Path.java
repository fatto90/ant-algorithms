package br.dcc.ufrj.antvrp.util;

import java.util.ArrayList;
import java.util.Arrays;

import br.dcc.ufrj.antvrp.world.City;

public class Path {
	
	private ArrayList<City> cities = null;
	private ArrayList<Path> routes = null;
	private City startCity = null;
	private double length = 0;
	
	public Path(City startCity) {
		this.cities = new ArrayList<City>();
		this.routes = new ArrayList<Path>();
		this.cities.add(startCity);		
		this.startCity = startCity;

		this.routes.add(new Path());
		this.routes.get(0).cities.add(startCity);
		this.routes.get(0).setStartCity(startCity);
	}
	
	public Path(){
		this.cities = new ArrayList<City>();
		this.routes = new ArrayList<Path>();
	}
	
	public ArrayList<City> getCities(){
		return this.cities;
	}
	
	public void reset(){
		this.cities = new ArrayList<City>();
		this.cities.add(this.startCity);
		this.length = 0;
		
		this.routes.add(new Path());
		this.routes.get(0).cities.add(startCity);
		this.routes.get(0).setStartCity(startCity);
	}
	
	public void add(City city){		
		City lastCity = this.cities.get(this.cities.size() - 1);
		this.length += Util.hypot(lastCity, city);
		this.cities.add(city);
		
		if (startCity.getId() == city.getId()){
			this.routes.add(new Path());
		}
		
		this.routes.get(this.routes.size() - 1).cities.add(city);
			
	}
	
	public City getCurrentCity(){
		return this.cities.get(this.cities.size() - 1);
	}
	
	public City getCity(int cityIndex){
		return this.cities.get(cityIndex);
	}
	
	public Path subpath(int beginIndex, int endIndex){
		Path result = new Path(this.startCity);
		
		for(int i = beginIndex; i <= endIndex; i++){
			result.add(this.cities.get(i));
		}
		
		return result;
	}
	
	public Path subpathInvert(int beginIndex, int endIndex){
		Path result = new Path(this.startCity);
		
		for(int i = endIndex; i >= beginIndex; i++){
			result.add(this.cities.get(i));
		}
		
		return result;
	}
	
	public double lengthSubpath(int beginIndex, int endIndex){
		double result = 0;
		City a = this.cities.get(beginIndex);
		City b = null;
		
		for (int i = beginIndex + 1; i <= endIndex; i++){
			b = this.cities.get(i);
			result += Util.hypot(a, b);
			a = b;
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.cities.toArray());
	}

	public double getLength() {
		return length;
	}
	
	public int getRoutesLength(){
		return this.routes.size();
	}
	
	public City getStartCity() {
		return startCity;
	}

	public void setStartCity(City startCity) {
		this.startCity = startCity;
	}
}
