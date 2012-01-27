package br.dcc.ufrj.asrankvrp.ant;

import java.util.ArrayList;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.world.City;

public class ASrankAnt extends Ant {

	public ASrankAnt(int id,City homeCity, int capacity) {
		super(id, homeCity, capacity);
	}

	@Override
	public City chooseNextMove(City city, double sample) {
		ArrayList<City> neighbors = city.getNeighbors();
		City neighbor = null;
		double acumulator = 0;
		double sum = 0;
		int size = neighbors.size() / 4;
		int totalIterations = 0;
		double[] probabilities = null;
		
		for (int i = 0, j = 0; j < size && i < neighbors.size(); i++) {
			neighbor = neighbors.get(i);			
			if (!this.pathContains(neighbor)){
				sum += neighbor.getAlphaPheromone() * neighbor.getBetaHeuristic();
				j++;
			}
			totalIterations++;
		}		
		
		probabilities = new double[totalIterations];

		for (int i = 0; i < probabilities.length; i++) {
			neighbor = neighbors.get(i);
			if (!this.pathContains(neighbor)){
				probabilities[i] = neighbor.getAlphaPheromone() * neighbor.getBetaHeuristic() / sum;
			}
		}
		
		for (int i = 0; i < probabilities.length; i++) {
			neighbor = neighbors.get(i);
			
			if (!this.pathContains(neighbor)){
				acumulator += probabilities[i];
			}
			
			if (acumulator > sample){
				
				if (this.capacityCurrentValue - neighbor.getDemand() >= 0){
					return neighbor;
				} else {
					capacityCurrentValue = this.capacityInitialValue;
					return this.getHomeCity();
				}
			}
		}
		
		return null;
	}

	@Override
	public double dropPheromone() {		
		return 1 / this.getTourLength();
	}

}
