package br.dcc.ufrj.antvrp.ant;

import java.util.ArrayList;

import br.dcc.ufrj.antvrp.world.City;

public abstract class Ant implements Comparable<Ant> {

	private int id;
	private City homeCity;
	private ArrayList<City> path;
	protected int capacityInitialValue = 0;
	protected int capacityCurrentValue = 0;

	public abstract City nextMove(City city, double sample);

	public abstract double dropPheromone();

	public Ant(int id, City homeCity, int capacity) {
		path = new ArrayList<City>();
		this.id = id;
		this.homeCity = homeCity;
		this.path.add(homeCity);
		this.capacityInitialValue = capacity;		
		this.capacityCurrentValue = capacity;		
	}

	public int getId() {
		return id;
	}

	public City getHomeCity() {
		return homeCity;
	}

	public City getCurrentCity() {
		return path.get(path.size() - 1);
	}

	public void walk(City city) {
		this.path.add(city);
		this.capacityCurrentValue -= city.getDemand();
	}

	public void resetPath() {
		this.path = new ArrayList<City>();
		this.path.add(homeCity);
		this.capacityCurrentValue = this.capacityInitialValue;
	}

	public int compareTo(Ant ant) {
		if (this.path.size() > this.path.size()) {
			return 1;

		} else if (this.path.size() == ant.path.size()) {
			return 0;

		} else {
			return -1;

		}
	}
	
	public boolean pathContains(City city){
		for (City temp : this.path) {
			if(temp.getId() == city.getId()){
				return true;
			}
		}
		return false;
	}

	public ArrayList<City> getPath() {
		return path;
	}
}
