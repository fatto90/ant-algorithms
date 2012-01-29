package br.dcc.ufrj.antvrp.ant;

import br.dcc.ufrj.antvrp.util.Tour;
import br.dcc.ufrj.antvrp.world.City;

public abstract class Ant implements Comparable<Ant> {

	public abstract City chooseNextMove(City city, double sample);
	public abstract double dropPheromone();

	private int id;
	private City homeCity;
	private Tour tour; 
	private int dimension = 0;
	protected int totalCapacity = 0;
	protected int currentCapacity = 0;
	private boolean []visitedCities;

	public Ant(int id, City homeCity, int capacity, int dimension) {
		this.tour = new Tour(homeCity);
		this.id = id;
		this.homeCity = homeCity;
		this.totalCapacity = capacity;		
		this.currentCapacity = capacity;
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

	public void walk(City city, double distance) {
		this.tour.add(city);
		this.tour.addDistance(distance);
		this.currentCapacity -= city.getDemand();
		this.visitedCities[city.getId() - 1] = true;		
	}

	public void resetTour() {
		this.tour.reset();
		this.currentCapacity = this.totalCapacity;
		this.visitedCities = new boolean[this.dimension];
		this.visitedCities[this.homeCity.getId() - 1] = true;
	}

	public int compareTo(Ant ant) {
		if (this.tour.getDistance() > ant.tour.getDistance()) {
			return 1;

		} else if (this.tour.getDistance() == ant.tour.getDistance()) {
			return 0;

		} else {
			return -1;

		}
	}
	
	public boolean tourContains(City city){
		for (City temp : this.tour.getCities()) {
			if(temp.getId() == city.getId()){
				return true;
			}
		}
		return false;
	}

	public Tour getTour() {
		return tour;
	}

	public double getsTourLength() {
		return this.tour.getDistance();
	}
	
	public boolean isCityVisited(City city){
		return this.visitedCities[city.getId() - 1];
	}
}
