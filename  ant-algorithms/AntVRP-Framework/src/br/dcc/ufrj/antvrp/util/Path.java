package br.dcc.ufrj.antvrp.util;

import java.util.ArrayList;

import br.dcc.ufrj.antvrp.world.City;

public class Path {
	
	ArrayList<City> cities = null;
	City startCity = null;
	double length = 0;
	
	public Path(City startCity) {
		this.cities = new ArrayList<City>();
		this.cities.add(startCity);		
		this.startCity = startCity;
	}
	
	public ArrayList<City> getCities(){
		return this.cities;
	}
	
	public void reset(){
		this.cities = new ArrayList<City>();
		this.cities.add(this.startCity);
		this.length = 0;
	}
	
	public void add(City city){		
		City lastCity = this.cities.get(this.cities.size() - 1);
		this.length += Util.hypot(lastCity, city);
		this.cities.add(city);
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
			b = this.cities.get(beginIndex);
			result += Util.hypot(a, b);
		}
		
		return result;
	}
}
