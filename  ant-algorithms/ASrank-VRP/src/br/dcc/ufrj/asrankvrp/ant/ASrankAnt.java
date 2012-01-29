package br.dcc.ufrj.asrankvrp.ant;

import java.util.ArrayList;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.world.City;

public class ASrankAnt extends Ant {

	public ASrankAnt(int id,City homeCity, int capacity, int dimension) {
		super(id, homeCity, capacity, dimension);
	}

	@Override
	public City chooseNextMove(City city, double sample) {
		ArrayList<City> neighbors = city.getNeighbors();
		City neighbor = null;
		double acumulator = 0;
		double sum = 0;
		int size = neighbors.size() / 4;
		int totalIterations = 0;
		
		for (int i = 0, j = 0; j < size && i < neighbors.size(); i++) {
			neighbor = neighbors.get(i);			
			if (!this.isCityVisited(neighbor)){
				sum += neighbor.getAtractivity();
				j++;
			}
			totalIterations++;
		}
		
		for (int i = 0; i < totalIterations; i++) {
			neighbor = neighbors.get(i);
			
			if (!this.isCityVisited(neighbor)){
				acumulator += neighbor.getAtractivity() / sum;
			}
			
			if (acumulator > sample){
				if (this.currentCapacity - neighbor.getDemand() >= 0){
					return neighbor;
				} else {
					currentCapacity = this.totalCapacity;
					return this.getHomeCity();
				}
			}
		}
		return null;
	}

	@Override
	public double dropPheromone() {		
		return 1 / this.getTour().getDistance();
	}

}
