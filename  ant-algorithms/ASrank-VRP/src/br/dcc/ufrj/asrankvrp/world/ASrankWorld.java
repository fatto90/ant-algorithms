package br.dcc.ufrj.asrankvrp.world;

import java.util.ArrayList;
import java.util.Arrays;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.world.City;
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
		this.ants = new ArrayList<Ant>();
		for (int i = 0; i < total; i++) {
			this.ants.add(new ASrankAnt(i, this.getFirstDepot(), this.getCapacity(), this.getDimension()));
		}
		
		this.rankSize = total / 5;
	}

	@Override
	protected void tourConstruction() throws Exception{
		City currCity = null;
		City nextCity = null;

		for (Ant ant : ants) {
			ant.resetPath();
			
			do {
				currCity = ant.getPath().getCurrentCity();				
				nextCity = ant.chooseNextMove(currCity, this.getSampleDouble());
				if (nextCity != null){
					nextCity = this.getCity(nextCity.getId());
					ant.walk(nextCity);
				}
			} while (nextCity != null);				
			
			ant.walk(ant.getHomeCity());
			
			if (this.getBestTourSize() > ant.getTourLength() || this.getBestTourSize() == 0){
				this.setBestTourSize(ant.getTourLength());
				this.setBestTour(ant.getPath());
			}
		}
	}
	
	@Override
	protected void pheromoneUpdate() {
		City lastPathCity = null;
		Object []rank = this.ants.toArray();
		Arrays.sort(rank);
		int w = rankSize;
		double pheromone = 0;
		
		for (City i: this.cities){
			for(City j: i.getNeighbors()){
				j.evapore(RO);
			}
		}
		
		for(City j: this.getBestTour().getCities()){
			if (lastPathCity != null){
				this.addPheromone(lastPathCity, j, (double)w / (double) this.getBestTourSize());				
			}
			lastPathCity = j;
		}
		
		lastPathCity = null;		
 
		for (int r = 1; r < w; r++) {
			Ant ant = (Ant) rank[r - 1];
			
			for (City pathCity : ant.getPath().getCities()) {				
				if (lastPathCity != null){
					pheromone = ant.dropPheromone() * (w - r);
					this.addPheromone(lastPathCity, pathCity, pheromone);
				}
				
				lastPathCity = pathCity;
			}
		}
	}

	@Override
	public double getInitialPathSize() {
		int dimension = this.getDimension();
		double pathSize = 0;
		ArrayList<Integer> initialTour = new ArrayList<Integer>();
		City nextCity = this.getFirstDepot();
		initialTour.add(nextCity.getId());

		for (int i = 0; i < dimension; i++) {
			for (City neighborCity : nextCity.getNeighbors()) {
				if (!initialTour.contains(neighborCity.getId())) {
					initialTour.add(neighborCity.getId());
					pathSize += neighborCity.getDistance();

					for (City tempCity : this.getCities()) {
						if (tempCity.getId() == neighborCity.getId()) {
							nextCity = tempCity;
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
		City depot = null;
		double heuristic = 0;

		for (City city : this.getCities()) {
			if (city.isDepot()) {
				depot = city;
			}
		}

		for (City city : this.cities) {
			for (City neighbor : city.getNeighbors()) {
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
}
