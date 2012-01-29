package br.dcc.ufrj.antvrp.ant;

import br.dcc.ufrj.antvrp.util.Path;
import br.dcc.ufrj.antvrp.world.City;

public abstract class Ant implements Comparable<Ant> {

	public abstract City chooseNextMove(City city, double sample);
	public abstract double dropPheromone();

	private int id;
	private City homeCity;
	private Path path; 
	private int dimension = 0;
	protected int capacityInitialValue = 0;
	protected int capacityCurrentValue = 0;
	private boolean []visitedCities;

	public Ant(int id, City homeCity, int capacity, int dimension) {
		this.path = new Path(homeCity);
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
		this.path.add(city);		
		this.capacityCurrentValue -= city.getDemand();
		this.visitedCities[city.getId() - 1] = true;		
	}

	public void resetPath() {
		this.path = new Path(homeCity);
		this.capacityCurrentValue = this.capacityInitialValue;
		this.visitedCities = new boolean[this.dimension];
		this.visitedCities[this.homeCity.getId() - 1] = true;
	}

	public int compareTo(Ant ant) {
		if (this.path.getLength() > ant.path.getLength()) {
			return 1;

		} else if (this.path.getLength() == ant.path.getLength()) {
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

	public double getsTourLength() {
		return this.path.getLength();
	}
	
	public boolean isCityVisited(City city){
		return this.visitedCities[city.getId() - 1];
	}
}
