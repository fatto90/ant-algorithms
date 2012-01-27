package br.dcc.ufrj.antvrp.world;

import java.util.ArrayList;

public class City implements Cloneable{
	
	private int lat;
	private int lon;
	private int id;
	private int alpha = 2;
	private int beta = 2;
	private int demand = 0;
	
	private double heuristic;
	private double pheromone;
	private double distance;	
	private double betaHeuristic;
	private double alphaPheromone;
	
	private boolean depot;
	
	private ArrayList<City> neighbors;
	
	public City(int id, int lat, int lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.neighbors = new ArrayList<City>();
	}
	
	@Override
	protected City clone() throws CloneNotSupportedException {
		City city = new City(this.id, this.lat, this.lon);
		city.distance = this.distance;
		city.heuristic = this.heuristic;
		city.pheromone = this.pheromone;
		city.alphaPheromone = this.alphaPheromone;
		city.betaHeuristic = this.betaHeuristic;		
		city.demand = this.demand;
		city.depot = this.depot;
		return city;
	}
	
	public void addNeigbour(City newNeighbor) {
		City neighbor = null;
		double distance = 0;
		
		if (newNeighbor.getId() == this.id){
			return;
		}
		
		try{
			distance = calculateDistance(this, newNeighbor);
			
			for (int i = 0; i < neighbors.size(); i++) {
				neighbor = neighbors.get(i);
				
				if (distance < neighbor.getDistance()){
					
					City nextNeighbor = newNeighbor.clone();
					nextNeighbor.setDistance(distance);
					
					neighbors.add(i, nextNeighbor);				
					return;
				}
			}
			
			if (neighbor == null || distance >= neighbor.getDistance()){
				
				City nextNeighbor = newNeighbor.clone();
				nextNeighbor.setDistance(distance);
				
				neighbors.add(nextNeighbor);				
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}
	
	public void setPheromone(double pheromone) {
		this.pheromone = pheromone;
		this.alphaPheromone = Math.pow(pheromone, alpha);
	}
	
	public double calculateDistance(City city, City neighbor){
		//return Math.sqrt(Math.pow((city.getLat() - neighbor.getLat()), 2) + Math.pow(city.getLon() - neighbor.getLon(), 2));
		
		double lon = city.getLon() - neighbor.getLon();
		double lat = city.getLat() - neighbor.getLat();
		return Math.hypot(lon, lat);
	}

	public double getDistance() {
		return distance;
	}

	public double getDistance(int idCity) {
		
		if (this.id == idCity){
			return 0;
		}
		
		for(City neighbor: this.neighbors){
			if (neighbor.getId() == idCity){
				return neighbor.getDistance();
			}			
		}
		
		return -1;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getLat() {
		return lat;
	}

	public int getLon() {
		return lon;
	}

	public int getId() {
		return id;
	}

	public ArrayList<City> getNeighbors() {
		return neighbors;
	}

	public double getPheromone() {
		return pheromone;
	}

	public double getBetaHeuristic() {
		return betaHeuristic;
	}
/*
	public void setBetaHeuristic(double betaHeuristic) {
		this.betaHeuristic = betaHeuristic;
	}
*/
	public double getAlphaPheromone() {
		return alphaPheromone;
	}

	public void setAlphaPheromone(double alphaPheromone) {
		this.alphaPheromone = alphaPheromone;
	}

	public int getAlpha() {
		return this.alpha;
	}

	public int getBeta() {
		return beta;
	}

	public void setBeta(int beta) {
		this.beta = beta;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}

	public boolean isDepot() {
		return depot;
	}

	public void setDepot(boolean depot) {
		this.depot = depot;
	}

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;		
		this.betaHeuristic = Math.pow(heuristic, beta);
	}
	
	@Override
	public String toString() {		
		return String.valueOf(this.id);
	}

	public void evapore(double tax) {
		this.pheromone = this.pheromone * (1 - tax);
		this.alphaPheromone = Math.pow(this.pheromone, alpha);
	}
}
