package br.dcc.ufrj.antvrp.ant;

import br.dcc.ufrj.antvrp.util.Tour;
import br.dcc.ufrj.antvrp.world.Customer;

public abstract class Ant implements Comparable<Ant> {

	public abstract Customer chooseNextMove(Customer city, double sample);
	public abstract double dropPheromone();

	private int id;
	private Customer firstCustomer;
	private Tour tour; 
	private int dimension = 0;
	protected int totalCapacity = 0;
	protected int currentCapacity = 0;
	private boolean []visitedCities;

	public Ant(int id, Customer firstCustomer, int capacity, int dimension) {
		this.tour = new Tour(firstCustomer);
		this.id = id;
		this.firstCustomer = firstCustomer;
		this.totalCapacity = capacity;		
		this.currentCapacity = capacity;
		this.dimension = dimension;
		this.visitedCities = new boolean[dimension];
		this.visitedCities[firstCustomer.getId() - 1] = true;
	}

	public int getId() {
		return id;
	}

	public Customer getFirstCustomer() {
		return firstCustomer;
	}

	public void walk(Customer city, double distance) {
		this.tour.add(city);
		this.tour.addDistance(distance);
		this.currentCapacity -= city.getDemand();
		this.visitedCities[city.getId() - 1] = true;		
	}

	public void resetTour() {
		this.tour.reset();
		this.currentCapacity = this.totalCapacity;
		this.visitedCities = new boolean[this.dimension];
		this.visitedCities[this.firstCustomer.getId() - 1] = true;
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
	
	public boolean tourContains(Customer city){
		for (Customer temp : this.tour.getCustomers()) {
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
	
	public boolean isCustomerVisited(Customer city){
		return this.visitedCities[city.getId() - 1];
	}
}
