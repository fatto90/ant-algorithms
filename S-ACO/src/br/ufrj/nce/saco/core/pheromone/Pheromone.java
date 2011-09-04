package br.ufrj.nce.saco.core.pheromone;

/**
 * This class was created to help handle the pheromone trail. 
 * The class instance have to be done specifying the amount of nodes at the class constructor.
 * 
 * @author Fabio Barbosa
 */

public class Pheromone {

	private double evaporation;
	private double[][] pheromoneTrail;
	private int sourceNode;
	private int destinationNode;
	
	
	/**  
	 * @param size This parameter specifies the total of nodes present on the simulation
	 */
	public Pheromone(int size) {
		this.pheromoneTrail = new double[size][size];
	}

	
	/** 
	 * @param evaporation This parameter specifies the evaporation rate
	 */
	public void setEvaporation(double evaporation) {
		this.evaporation = evaporation;
	}

	/** 
	 * Returns the source node value.
	 * The source node value is the node from all ants starts the simulation. In ACO this is the nest. 
	 * @return 
	 */
	public int getSourceNode() {
		return sourceNode;
	}
	

	/** 
	 * Specifies the source node value
	 * The source node value is the node from all ants starts the simulation. In ACO this is the nest.
	 * @param sourceNode Source node value
	 */
	public void setSourceNode(int sourceNode) {
		this.sourceNode = sourceNode;
	}

	/**
	 * Returns the destination node value
	 * The destination node value is the node witch represents the food in ACO. This is the node where all the ants want to achieve. 
	 * @return Destination node value
	 */
	public int getDestinationNode() {
		return destinationNode;
	}

	/** 
	 * Specifies the destination node value
	 * The destination node value is the node witch represents the food in ACO. This is the node where all the ants want to achieve.
	 * @param destinationNode Destination node value 
	 */
	public void setDestinationNode(int destinationNode) {
		this.destinationNode = destinationNode;		
	}
	
	/**
	 * This method is used to retrieve the amount of pheromone between two nodes  
	 * 
	 * @param nodeSource the source node
	 * @param nodeDestination the destination node
	 * @return
	 */
	public double getPheromoneAmount(int nodeSource, int nodeDestination) {
		return this.pheromoneTrail[nodeSource][nodeDestination];
	}

	public double[] getPheromoneNeighbourhood(int node) {
		return this.pheromoneTrail[node];
	}

	public void updatePheromoneTrail() {
		for (int i = 0; i < this.pheromoneTrail.length; i++) {
			for (int j = 0; j < this.pheromoneTrail.length; j++) {
				this.pheromoneTrail[i][j] = (1 - evaporation) * this.pheromoneTrail[i][j];
			}
		}
	}

	public void addPheromone(int nodeSource, int nodeDestination, double amount) throws Exception {
		if (nodeSource == nodeDestination) {
			throw new Exception("Node source is equal destination node [" + nodeSource + ", " + nodeDestination + "]");
		}
		this.pheromoneTrail[nodeSource][nodeDestination] += amount;
		this.pheromoneTrail[nodeDestination][nodeSource] += amount;
	}

	/**
	 * @return
	 */
	public String getBestPath() {
		String path = "0, ";
		double temp = 0;
		int node = 0;

		for (int i = 0; i < pheromoneTrail.length - 1; i = node) {

			for (int j = i + 1; j < pheromoneTrail.length; j++) {
				if (temp < pheromoneTrail[i][j]) {
					node = j;
					temp = pheromoneTrail[i][j];
				}
			}
			path += node + ", ";
			temp = 0;
		}
		return path;
	}

	/**
	 * @return
	 */
	public int getBestPathSize() {
		int size = 0;
		double temp = 0;
		int node = 0;

		for (int i = 0; i < pheromoneTrail.length - 1; i = node) {

			for (int j = i + 1; j < pheromoneTrail.length; j++) {
				if (temp < pheromoneTrail[i][j]) {
					node = j;
					temp = pheromoneTrail[i][j];
				}
			}
			temp = 0;
			size++;
		}
		return size;
	}

}