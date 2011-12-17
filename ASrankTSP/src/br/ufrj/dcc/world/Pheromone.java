package br.ufrj.dcc.world;

public class Pheromone {
	
	private double[][] pheromones;
	
	public Pheromone(double initialValue, int size) {
		this.pheromones = new double[size][size];
		
		for (int i = 0; i < pheromones.length; i++) {
			for (int j = 0; j < pheromones.length; j++) {
				pheromones[i][j] = (double) size / initialValue;
			}
		}
	}
	
	public double getValue(int i, int j){
		return this.pheromones[i][j];
	}
	
	public void setValue(int i, int j, double value){
		this.pheromones[i][j] = value;
		this.pheromones[j][i] = value;
	}

	public void removeValue(int i, int j, double value){
		this.pheromones[i][j] = value;
		this.pheromones[j][i] = value;
	}
	
	public void evaporate(double rate){
		for (int i = 0; i < pheromones.length; i++) {
			for (int j = 0; j < pheromones.length; j++) {
				pheromones[i][j] = (1 - rate) * pheromones[i][j];
			}
		}
	}

	public double[] getPheromones(int i) {
		return this.pheromones[i];
	}
}
