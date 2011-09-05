package br.ufrj.nce.saco.core.pheromone;

/**
 * This class was created to help handle the pheromone trail. 
 * The class instance have to be done specifying the amount of nodes at the class constructor.
 * 
 * @author Fabio Barbosa
 */

public class Pheromone {

	private double[][] pheromoneTrail;
	private int sourceNode;
	private int destinationNode;
	private int size;
	
	
	/**  
	 * @param size This parameter specifies the total of nodes present on the simulation
	 */
	public Pheromone(int size) {
		this.pheromoneTrail = new double[size][size];
		this.size = size;
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
	 * @return the pheromone amount between two nodes
	 */
	public double getPheromoneAmount(int nodeSource, int nodeDestination) {
		return this.pheromoneTrail[nodeSource][nodeDestination];
	}

	
	/**
	 * This method helps to retrieve the amount of pheromone from the neighbourhood of a specific node
	 * @param node the node witch we want retrieve the neighbourhood
	 * @return an double array containing the neighbourhood from the node specified in the parameter
	 */
	public double[] getPheromoneNeighbourhood(int node) {
		return this.pheromoneTrail[node];
	}

	
	/** This should be used to update all pheromone array called pheromone trail.
	 * The evaporation constant between 0 and 1 must to be specified. 
	 * @param evaporation the evaporation constant 
	 * @throws Exception If the evaporation constant not between 0.0 and 1.0
	 */
	public void updatePheromoneTrail(double evaporation) throws Exception {
		
		if (evaporation < 0 || evaporation > 1){
			throw new Exception("The evaporation constant must to be between 0.0 and 1.0.");
		}
		
		for (int i = 0; i < this.pheromoneTrail.length; i++) {
			for (int j = 0; j < this.pheromoneTrail.length; j++) {
				this.pheromoneTrail[i][j] = (1 - evaporation) * this.pheromoneTrail[i][j];
			}
		}
	}

	/**
	 * Adds pheromone to the pheromone trail
	 * 
	 * @param nodeSource the node where the ant moves from. This parameter must be between zero and
	 * the size parameter specified in the constructor.  
	 * @param nodeDestination the node where the ant moves to. This parameter must be between zero and
	 * the size parameter specified in the constructor.
	 * @param amount the amount of pheromone to be placed between source and destination nodes.
	 *  The amount of pheromone must be greater or equal zero.
	 *  
	 * @throws IllegalArgumentException Throws if node source or destination node be less than zero 
	 * or greater than size of pheromone trail or if the amount of pheromone less than zero or if 
	 * the source and the destination node are the same. 
	 */
	public void addPheromone(int nodeSource, int nodeDestination, double amount) throws IllegalArgumentException {
		if (nodeSource == nodeDestination) {
			throw new IllegalArgumentException("Node source must be different from the destination node [" + nodeSource + ", " + nodeDestination + "]");
		}
		if (nodeSource < 0 || destinationNode < 0) {
			throw new IllegalArgumentException("The source node or destination node must be greater than zero.");			
		}
		if (nodeSource >= this.size || nodeDestination >= this.size) {
			throw new IllegalArgumentException("The source node or destination node must be less than size.");			
		}
		if (amount < 0) {
			throw new IllegalArgumentException("The amount of pheromone node must be greater or equal than zero.");			
		}
		
		this.pheromoneTrail[nodeSource][nodeDestination] += amount;
		this.pheromoneTrail[nodeDestination][nodeSource] += amount;
	}

	/**
	 * Retrieves the best path pheromone trail
	 * @return a string with the best pheromone trail
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
	 * Retrieves the best pheromone trail, ie, the path whose the nodes have more pheromone than 
	 * the others nodes.
	 * @return an integer representing the length of the best pheromone trail. 
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