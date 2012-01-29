package br.dcc.ufrj.antvrp.util;

import java.util.ArrayList;
import java.util.Arrays;

import br.dcc.ufrj.antvrp.world.City;

public class Tour {
	
	private ArrayList<City> cities = null;
	private ArrayList<Tour> routes = null;
	private City startCity = null;
	private double distance = 0;
	
	public Tour(City startCity) {
		this.cities = new ArrayList<City>();
		this.routes = new ArrayList<Tour>();
		this.cities.add(startCity);		
		this.startCity = startCity;

		this.routes.add(new Tour());
		this.routes.get(0).cities.add(startCity);
		this.routes.get(0).setStartCity(startCity);
	}
	
	public Tour(){
		this.cities = new ArrayList<City>();
		this.routes = new ArrayList<Tour>();
	}
	
	public ArrayList<City> getCities(){
		return this.cities;
	}
	
	public void reset(){
		this.cities = new ArrayList<City>();
		this.cities.add(this.startCity);
		this.distance = 0;
		
		this.routes.add(new Tour());
		this.routes.get(0).cities.add(startCity);
		this.routes.get(0).setStartCity(startCity);
	}
	
	public void add(City city){		
		this.cities.add(city);
	}
	
	public City getCurrentCity(){
		return this.cities.get(this.cities.size() - 1);
	}
	
	public City getCity(int cityIndex){
		return this.cities.get(cityIndex);
	}
	
	public Tour subTour(int beginIndex, int endIndex){
		Tour result = new Tour(this.startCity);
		
		for(int i = beginIndex; i <= endIndex; i++){
			result.add(this.cities.get(i));
		}
		
		return result;
	}
	
	public Tour subTourInvert(int beginIndex, int endIndex){
		Tour result = new Tour(this.startCity);
		
		for(int i = endIndex; i >= beginIndex; i++){
			result.add(this.cities.get(i));
		}
		
		return result;
	}
	
	public double lengthSubTour(int beginIndex, int endIndex){
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

	public double getDistance() {
		return distance;
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

	public void addDistance(double distance) {
		this.distance += distance;		
	}
	
	public double checkDistance(){
		
		City a = null;
		double distance = 0;
		
		for (City b: this.cities){
			if (a != null){
				distance += Util.hypot(a, b);
			}
			a = b;
		}
		
		return distance;
		
	}
}
