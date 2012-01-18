package br.dcc.ufrj.asrankvrp.world;

import java.util.ArrayList;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.world.City;
import br.dcc.ufrj.antvrp.world.World;
import br.dcc.ufrj.asrankvrp.ant.ASrankAnt;

public class ASrankWorld extends World {

	ArrayList<Ant> rank;
	private int fParam = 2;
	private int gParam = 2;
	

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
			this.ants.add(new ASrankAnt(i, this.getFirstDepot(), this.getCapacity()));
		}
		rank = new ArrayList<Ant>(this.ants);
	}

	@Override
	protected void tourConstruction() {
		City currCity = null;
		City nextCity = null;

		for (Ant ant : ants) {
			ant.resetPath();
			
			currCity = ant.getCurrentCity();
			nextCity = ant.nextMove(currCity, this.getSampleDouble());

			while(nextCity != null){
				nextCity = this.getCity(nextCity);
				ant.walk(nextCity);
				currCity = ant.getCurrentCity();
				nextCity = ant.nextMove(currCity, this.getSampleDouble());
			}
			ant.walk(ant.getHomeCity());

			for(City city : ant.getPath()){
				System.out.print(city.getId() + ", ");
			}
			System.out.println(" ");
		}

	}

	private City getCity(City nextCity) {
		for(City city : this.getCities()){
			if(city.getId() == nextCity.getId()){
				return city;
			}
		}
		return null;
	}

	@Override
	protected void pheromoneUpdate() {
		City depot = null;
		
		for(City city: this.getCities()){
			if (city.isDepot()){
				depot = city;
				continue;
			}
		}
		
		for(Ant ant : this.ants){
			for(City city : ant.getPath()){
				if (!city.isDepot()){
					
					// TODO city.setPheromone();
				}
			}
		}
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
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
				if (!initialTour.contains(neighborCity.getId())){
					initialTour.add(neighborCity.getId());
					pathSize += neighborCity.getDistance();
					
					for(City tempCity : this.getCities()){
						if (tempCity.getId() == neighborCity.getId()){
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
		
		for (City city: this.getCities()){
			if (city.isDepot()){
				depot = city;
			}
		}		
		
		for(City city: this.cities){
			
			for(City neighbor: city.getNeighbors()){
								
				heuristic = depot.getDistance(city.getId()) + 
					depot.getDistance(neighbor.getId()) - 
					gParam * city.getDistance(neighbor.getId()) +
					fParam * Math.abs(depot.getDistance(city.getId()) - depot.getDistance(neighbor.getId()));
				neighbor.setHeuristic(heuristic);
				System.out.println("");
			}
		}
		
		System.out.println("");
		
	}
	
}
