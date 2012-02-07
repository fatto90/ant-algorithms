package br.dcc.ufrj.antvrp.world;

import java.util.ArrayList;

import br.dcc.ufrj.antvrp.util.Util;

public class Customer implements Cloneable{
	
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
	private double atractivity;
	
	private Customer[] listCandidatesVector;
	private Customer[] neighborsVector;
	
	
	private boolean depot;
	
	private ArrayList<Customer> listCandidates;
	private ArrayList<Customer> neighbors;
	
	public Customer(int id, int lat, int lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.listCandidates = new ArrayList<Customer>();
		this.neighbors = new ArrayList<Customer>();
	}
	
	@Override
	protected Customer clone() throws CloneNotSupportedException {
		Customer city = new Customer(this.id, this.lat, this.lon);
		city.distance = this.distance;
		city.heuristic = this.heuristic;
		city.pheromone = this.pheromone;
		city.alphaPheromone = this.alphaPheromone;
		city.betaHeuristic = this.betaHeuristic;		
		city.demand = this.demand;
		city.depot = this.depot;
		city.atractivity = this.atractivity;
		city.listCandidatesVector = listCandidatesVector;
		city.neighborsVector = neighborsVector;
		return city;
	}
	
	public void addNeigbour(Customer newNeighbor) {
		Customer neighbor = null;
		double distance = 0;
		
		try{
			if (newNeighbor.getId() == this.id){
				Customer nextNeighbor = newNeighbor.clone();
				nextNeighbor.setDistance(0);
				neighbors.add(nextNeighbor);
				return;
			}
			
			distance = Util.hypot(this, newNeighbor);
			Customer nextNeighbor = newNeighbor.clone();
			nextNeighbor.setDistance(distance);
			neighbors.add(nextNeighbor);
			
			if (nextNeighbor.isDepot()){
				return;
			}
			
			for (int i = 0; i < listCandidates.size(); i++) {
				neighbor = listCandidates.get(i);
				
				if (distance < neighbor.getDistance()){
					listCandidates.add(i, nextNeighbor);
					return;
				}
			}
			
			if (neighbor == null || distance >= neighbor.getDistance()){
				listCandidates.add(nextNeighbor);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}
	
	public void setPheromone(double pheromone) {
		this.pheromone = pheromone;
		this.alphaPheromone = Math.pow(pheromone, alpha);
		this.atractivity = alphaPheromone * betaHeuristic;
	}
	
	public double getDistance() {
		return distance;
	}

	public double getDistance(int idCustomer) {
		if (this.id == idCustomer){
			return 0;
		} else {
			return this.neighborsVector[idCustomer - 1].getDistance();
		}
	}
	
	public double getDistance(Customer neighbor) {
		if (this.id == neighbor.getId()){
			return 0;
		} else {
			return this.neighborsVector[neighbor.getId() - 1].getDistance();
		}
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

	public ArrayList<Customer> getListCandidates() {
		return listCandidates;
	}

	public double getPheromone() {
		return pheromone;
	}

	public double getBetaHeuristic() {
		return betaHeuristic;
	}

	public double getAlphaPheromone() {
		return alphaPheromone;
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
		this.atractivity = alphaPheromone * betaHeuristic;
	}

	public Customer getNeighbor(int cityId) {
		return this.neighborsVector[cityId - 1];//this.getNeighbors().get(cityId - 1);
	}

	public double getAtractivity() {
		return atractivity;
	}
	
	public void createVectors(){
		listCandidatesVector = new Customer[listCandidates.size()];
		neighborsVector = new Customer[neighbors.size()];
		
		for(int i = 0, j = 0; i < neighborsVector.length; i++, j++){
			
			if (i < listCandidatesVector.length){
				listCandidatesVector[i] = listCandidates.get(i);
			}
			neighborsVector[j] = neighbors.get(j);
		}
	}
	
	public Customer getCandidate(int index){
		return this.listCandidatesVector[index];
	}
	
	public int getListCandidateSize(){
		return this.listCandidatesVector.length;
	}
	
}
