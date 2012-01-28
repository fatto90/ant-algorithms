package br.dcc.ufrj.antvrp.ant;

import br.dcc.ufrj.antvrp.util.Path;
import br.dcc.ufrj.antvrp.world.City;

public abstract class Ant implements Comparable<Ant> {

	private int id;
	private City homeCity;
	//private ArrayList<City> path;
	private Path path; 
	private boolean []visitedCities;
	protected int capacityInitialValue = 0;
	protected int capacityCurrentValue = 0;
	private double tourLength = 0;
	private int dimension = 0;

	public abstract City chooseNextMove(City city, double sample);

	public abstract double dropPheromone();

	public Ant(int id, City homeCity, int capacity, int dimension) {
		path = new Path(homeCity);
		this.id = id;
		this.homeCity = homeCity;
		this.capacityInitialValue = capacity;		
		this.capacityCurrentValue = capacity;
		this.dimension = dimension;
		this.visitedCities = new boolean[dimension];
		this.visitedCities[homeCity.getId() - 1] = true;
	}

	public int getId() {
		return id;
	}

	public City getHomeCity() {
		return homeCity;
	}

	public void walk(City city) {
		this.tourLength += this.path.getCurrentCity().getDistance(city.getId()); 
		this.path.add(city);		
		this.capacityCurrentValue -= city.getDemand();
		this.visitedCities[city.getId() - 1] = true;		
	}

	public void resetPath() {
		this.path = new Path(homeCity);
		this.capacityCurrentValue = this.capacityInitialValue;
		this.tourLength = 0;
		this.visitedCities = new boolean[this.dimension];
		this.visitedCities[this.homeCity.getId() - 1] = true;
	}

	public int compareTo(Ant ant) {
		if (this.getTourLength() > ant.getTourLength()) {
			return 1;

		} else if (this.getTourLength() == ant.getTourLength()) {
			return 0;

		} else {
			return -1;

		}
	}
	
	public boolean pathContains(City city){
		for (City temp : this.path.getCities()) {
			if(temp.getId() == city.getId()){
				return true;
			}
		}
		return false;
	}

	public Path getPath() {
		return path;
	}

	public double getTourLength() {
		return tourLength;
	}
	
	public boolean isCityVisited(City city){
		return this.visitedCities[city.getId() - 1];
	}
}
