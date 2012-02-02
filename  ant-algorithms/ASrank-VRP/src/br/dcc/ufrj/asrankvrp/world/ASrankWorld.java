package br.dcc.ufrj.asrankvrp.world;

import java.util.ArrayList;
import java.util.Arrays;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.world.Customer;
import br.dcc.ufrj.antvrp.world.World;
import br.dcc.ufrj.asrankvrp.ant.ASrankAnt;

public class ASrankWorld extends World {

	private int fParam = 2;
	private int gParam = 2;
	private static double RO = 0.1; 
	private int rankSize;

	public void createWorld(String path) throws Exception {
		super.createWorld(path);
	}

	public ASrankWorld() {
		super();
	}

	public ASrankWorld(long seed) {
		super(seed);
	}

	@Override
	public void createAnts(int total) {
		this.ants = new Ant[total];
		for (int i = 0; i < total; i++) {
			this.ants[i] = new ASrankAnt(i, this.getFirstDepot(), this.getCapacity(), this.getDimension());
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
			
			//ant.getTour().opt2IntraRoutes();
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
		
		for(Customer j: this.getBestTour().getCustomers()){
			if (lastTourCustomer != null){
				if (lastTourCustomer.getId() != j.getId()){
					this.addPheromone(lastTourCustomer, j, (double)w / (double) this.getBestTour().getDistance());				
				}
			}
			lastTourCustomer = j;
		}
		
		lastTourCustomer = null;
 
		for (int r = 1; r < w; r++) {
			ant = (Ant) rank[r - 1];
			
			for (Customer tourCustomer : ant.getTour().getCustomers()) {				
				if (lastTourCustomer != null){
					pheromone = ant.dropPheromone() * (w - r);
					this.addPheromone(lastTourCustomer, tourCustomer, pheromone);
				}
				
				lastTourCustomer = tourCustomer;
			}
		}
	}

	@Override
	public double getInitialTourSize() {
		int dimension = this.getDimension();
		double pathSize = 0;
		ArrayList<Integer> initialTour = new ArrayList<Integer>();
		Customer nextCustomer = this.getFirstDepot();
		initialTour.add(nextCustomer.getId());

		for (int i = 0; i < dimension; i++) {
			for (Customer neighborCustomer : nextCustomer.getListCandidates()) {
				if (!initialTour.contains(neighborCustomer.getId())) {
					initialTour.add(neighborCustomer.getId());
					pathSize += neighborCustomer.getDistance();

					for (Customer tempCustomer : this.getCities()) {
						if (tempCustomer.getId() == neighborCustomer.getId()) {
							nextCustomer = tempCustomer;
							break;
						}
					}
					break;
				}
			}
		}

		return pathSize;
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
}
