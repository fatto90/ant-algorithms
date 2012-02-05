package br.dcc.ufrj.asranksavvrp.world;

import java.util.Arrays;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.util.Tour;
import br.dcc.ufrj.antvrp.world.Customer;
import br.dcc.ufrj.antvrp.world.World;
import br.dcc.ufrj.asranksavvrp.ant.ASrankSavAnt;

public class ASrankSavWorld extends World {

	private int fParam = 2;
	private int gParam = 2;
	private static double RO = 0.1; 
	private int rankSize;

	public void createWorld(String path) throws Exception {
		super.createWorld(path);
	}

	public ASrankSavWorld() {
		super();
	}

	public ASrankSavWorld(long seed) {
		super(seed);
	}

	@Override
	public void createAnts(int total) {
		this.ants = new Ant[total];
		for (int i = 0; i < total; i++) {
			this.ants[i] = new ASrankSavAnt(i, this.getFirstDepot(), this.getCapacity(), this.getDimension());
		}
		
		this.rankSize = total / 5;
	}

	@Override
	protected void tourConstruction() throws Exception{
		Customer currCustomer = null;
		Customer nextCustomer = null;
		double distance = 0;

		for (Ant ant : ants) {
			ant.resetTour();
			
			do {
				currCustomer = ant.getTour().getCurrentCustomer();				
				nextCustomer = ant.chooseNextMove(currCustomer, this.getSampleDouble());
				if (nextCustomer != null){
					nextCustomer = this.getCustomer(nextCustomer.getId());
					distance = nextCustomer.getNeighbor(currCustomer.getId()).getDistance();
					ant.walk(nextCustomer, distance);
				}
			} while (nextCustomer != null);				
			
			distance = currCustomer.getNeighbor(ant.getFirstCustomer().getId()).getDistance();
			ant.walk(ant.getFirstCustomer(), distance);

			ant.getTour().opt2IntraRoutes();
			if (this.getBestTour() == null || this.getBestTour().getDistance() > ant.getTour().getDistance()){
				this.setBestTour(ant.getTour().clone());
			}
		}
	}
	
	@Override
	protected void pheromoneUpdate() {
		Ant ant = null;
		Customer lastTourCustomer = null;
		Ant []rank = this.ants;
		Arrays.sort(rank);		
		int w = rankSize;
		double pheromone = 0;
		
		for (Customer i: this.customers){
			for(Customer j: i.getListCandidates()){
				j.evapore(RO);
			}
		}
		
		for(int i = 0; i < this.getBestTour().getSize(); i++){
			Customer customer = this.getBestTour().getCustomers()[i];
			if (lastTourCustomer != null){
				if (lastTourCustomer.getId() != customer.getId()){
					this.addPheromone(lastTourCustomer, customer, (double)w / (double) this.getBestTour().getDistance());				
				}
			}
			lastTourCustomer = customer;
		}
		
		lastTourCustomer = null;
 
		for (int r = 1; r < w; r++) {
			ant = (Ant) rank[r - 1];
			
			for (int i = 0; i < ant.getTour().getSize(); i++) {
				Customer tourCustomer = ant.getTour().getCustomers()[i];
				if (lastTourCustomer != null){
					pheromone = ant.dropPheromone() * (w - r);
					this.addPheromone(lastTourCustomer, tourCustomer, pheromone);
				}
				
				lastTourCustomer = tourCustomer;
			}
		}
	}

	@Override
	public Tour getInitialTour() {
		int dimension = this.getDimension();
		Customer depot = this.depots[0];
		Customer customer = this.depots[0];
		Tour tour = new Tour(customer, dimension);
		int currentCapacity = 0;
		for(Customer candidate : customer.getListCandidates()){
			if(currentCapacity >= this.getCapacity()){
				tour.add(depot);
				currentCapacity = 0;
				customer = depot;
			} 
			if (!tour.contains(candidate)){
				candidate = this.getCustomer(candidate.getId());
				tour.add(candidate);
				customer = candidate;
				currentCapacity++;
			}
		}
		
		tour.add(depot);
		return tour;
	}

	protected void computeHeuristics() {
		Customer depot = null;
		double heuristic = 0;

		for (Customer city : this.getCities()) {
			if (city.isDepot()) {
				depot = city;
				break;
			}
		}

		for (Customer city : this.customers) {
			for (Customer neighbor : city.getListCandidates()) {
				heuristic = depot.getDistance(city.getId()) + depot.getDistance(neighbor.getId()) - gParam * city.getDistance(neighbor.getId()) + fParam * Math.abs(depot.getDistance(city.getId()) - depot.getDistance(neighbor.getId()));
				neighbor.setHeuristic(heuristic);
			}
		}
	}

	public int getRankSize() {
		return rankSize;
	}

	public void setRankSize(int rankSize) {
		this.rankSize = rankSize;
	}

	public double getTourLength(String string) {
		double result = 0;
		String[] customers = string.replaceAll(" ", "").split(",");
		
		for(int i = 0; i < customers.length - 1; i++){
			int a = Integer.parseInt(customers[i]);
			int b = Integer.parseInt(customers[i + 1]);
			result += this.getCustomer(a).getDistance(b);
		}
		
		return result;
	}

	@Override
	protected double getIntialValue(int antsAmount) {
		return antsAmount / this.getInitialTour().getDistance();
	}
}
