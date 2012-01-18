package br.dcc.ufrj.asrankvrp.simulator;

import br.dcc.ufrj.asrankvrp.world.ASrankWorld;

public class Simulador {
	
	public static final String FILE_PATH = "C:\\Users\\Fabio\\Desktop\\dataset\\";
	public static final String FILE_NAME = "att48.vrp";
	public static final int TOTAL_SIMULATIONS = 10;
	public static final int ANT_AMOUNT = 1;
	
	public static void main(String[] args) throws Exception {
		double initialPheromone = 0;
		ASrankWorld world = new ASrankWorld(50);
		world.createWorld(FILE_PATH.concat(FILE_NAME));		
		world.createAnts(ANT_AMOUNT);
		initialPheromone = ANT_AMOUNT/world.getInitialPathSize();
		world.createPheromones(initialPheromone);
		
		for (int i = 0; i < TOTAL_SIMULATIONS; i++) {
			world.run();
		}
	}

}
