package br.ufrj.dcc.util;

public class Pheromone {
	
	private double[][] pheromones;
	private double rate = 0.1;
	
	public Pheromone(double initialValue, int size) {
		this.pheromones = new double[size][size];
		
		for (int i = 0; i < pheromones.length; i++) {
			for (int j = 0; j < pheromones.length; j++) {
				pheromones[i][j] = (double) size / initialValue;
			}
		}
	}
	
	public void setRate(double rate){
		if (this.rate < 0 || this.rate > 1){
			throw new IllegalArgumentException("A taxa de evaporação deve pertencer ao intervalo [0, 1]");
		}
		this.rate = rate;
	}
	
	public void addValue(int i, int j, double value){
		this.pheromones[i][j] = this.pheromones[i][j] * (1 - this.rate) + value;
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
	
	public int size(){
		return this.pheromones.length;
	}
	
}
