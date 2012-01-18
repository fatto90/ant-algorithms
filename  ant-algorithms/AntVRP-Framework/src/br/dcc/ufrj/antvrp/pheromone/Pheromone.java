package br.dcc.ufrj.antvrp.pheromone;

import java.util.ArrayList;

import br.dcc.ufrj.antvrp.world.City;


public class Pheromone {
	
	private double [][]values;
	private double evaporateTax = 0.1;
	private ArrayList<City> cities;
	
	public Pheromone(ArrayList<City> cities, double startValue){
		this.cities = cities;
		values = new double[cities.size()][cities.size()];
		
		for (int i = 0; i < cities.size(); i++) {
			for (int j = 0; j < cities.size(); j++) {
				values[i][j] = startValue;
			}
		}
	}
	
	public double[] getNeighbourPheromone(City city){
		int i = this.cities.indexOf(city);
		return this.values[i];
	}
	
	public void evaporate(){
		
		for (int i = 0; i < cities.size(); i++) {
			for (int j = 0; j < cities.size(); j++) {
				values[i][j] = values[i][j] * (1 - this.evaporateTax);
			}
		}
	}
	
	public void evaporate(double evaporateTax){
		
		if (evaporateTax <= 0){
			throw new IllegalArgumentException("Valor não permitido para evaporateTax: ".concat(String.valueOf(evaporateTax)));
		}
		
		for (int i = 0; i < cities.size(); i++) {
			for (int j = 0; j < cities.size(); j++) {
				values[i][j] = values[i][j] * (1 - evaporateTax);
			}
		}
	}

	public double getEvaporateTax() {
		return evaporateTax;
	}

	public void setEvaporateTax(double evaporateTax) {
		if (evaporateTax <= 0){
			throw new IllegalArgumentException("Valor não permitido para evaporateTax: ".concat(String.valueOf(evaporateTax)));
		}

		this.evaporateTax = evaporateTax;
	}
	
	public void addPheromone(int sourceNode, int destinationNode, double value){
		
		if (sourceNode < 0 || sourceNode >= cities.size()){
			throw new IllegalArgumentException("Valor não permitido para sourceNode: ".concat(String.valueOf(sourceNode)));
			
		}
		
		if (destinationNode < 0 || destinationNode >= cities.size()){
			throw new IllegalArgumentException("Valor não permitido para destinationNode: ".concat(String.valueOf(destinationNode)));
			
		}
		
		values[sourceNode][destinationNode] += value;
		values[destinationNode][sourceNode] += value;
		
		
	}

}
