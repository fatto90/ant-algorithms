package br.dcc.ufrj.asrankvrp.ant;

import java.util.ArrayList;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.world.City;

public class ASrankAnt extends Ant {

	public ASrankAnt(int id,City homeCity, int capacity) {
		super(id, homeCity, capacity);
	}

	@Override
	public City nextMove(City city, double sample) {
		ArrayList<City> neighbors = city.getNeighbors();
		City neighbor = null;
		double acumulator = 0;
		double[] probabilities = new double[neighbors.size()];
		double sum = 0;
		
		//TODO corrigir a busca pelo proximo nó... 
		//a formiga está terminando o tour antes da hora...
		
		for (int i = 0; i < neighbors.size() / 4; i++) {
			neighbor = neighbors.get(i);
			
			if (!this.pathContains(neighbor)){
				sum += neighbor.getAlphaPheromone() * neighbor.getBetaHeuristic();			
			}
		}

		for (int i = 0; i < neighbors.size() / 4; i++) {
			neighbor = neighbors.get(i);
			if (!this.pathContains(neighbor)){
				probabilities[i] = neighbor.getAlphaPheromone() * neighbor.getBetaHeuristic() / sum;			
			}
		}
		
		for (int i = 0; i < neighbors.size() / 4; i++) {
			neighbor = neighbors.get(i);
			acumulator += probabilities[i];
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
		// TODO Auto-generated method stub
		return 0;
	}

}
