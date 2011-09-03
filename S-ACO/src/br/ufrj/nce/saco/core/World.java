package br.ufrj.nce.saco.core;

public class World {
	
	private double ro = 0.1;
	private double [][]pheromoneTrail = {
									 //	00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18
										{0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //00
										{1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //01
										{1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //02
										{0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //03
										{0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0}, //04
										{0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0}, //05
										{0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //06
										{0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, //07
										{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}, //08
										{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}, //09
										{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, //10
										{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}, //11
										{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0}, //12
										{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0}, //13
										{0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0}, //14
										{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0}, //15
										{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1}, //16
										{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1}, //17
										{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0}, //18										
	};
	
	private int source = 0;
	private int destination = 18;
	
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public void updatePheromoneTrail(){
		
		for (int i = 0; i < this.pheromoneTrail.length; i++){

			for (int j = 0; j < this.pheromoneTrail.length; j++){
				this.pheromoneTrail[i][j] = (1 - ro) * this.pheromoneTrail[i][j];
			}
			
		}
	}
	
	public void addPheromone(int nodeSource, int nodeDestination, double amount) throws Exception{
		if (nodeSource == nodeDestination){
			throw new Exception("Node source equal node destination [" + nodeSource + ", " +nodeDestination + "]");
		}
		this.pheromoneTrail[nodeSource][nodeDestination] += amount;
	}
	
	public double getPheromoneAmount(int nodeSource, int nodeDestination){
		return this.pheromoneTrail[nodeSource][nodeDestination];
	}
	
	public double[] getPheromoneNeighbourhood(int node){
		return this.pheromoneTrail[node];
	}
	
	public String getBestPath(){
		String path = "0, ";
		double temp = 0;
		int node = 0;
		
		for (int i = 0; i < pheromoneTrail.length - 1; i = node) {
			
			for (int j = i + 1; j < pheromoneTrail.length; j++) {
				if (temp < pheromoneTrail[i][j]){
					node = j;
					temp = pheromoneTrail[i][j];
				} 
			}
			path += node + ", ";
			temp = 0;
		}
		
		return path;
	}
	
}
