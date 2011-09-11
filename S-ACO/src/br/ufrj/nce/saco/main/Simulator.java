package br.ufrj.nce.saco.main;

import br.ufrj.nce.saco.core.world.World;
import br.ufrj.nce.saco.utils.Constants;

public class Simulator {
	
	public World world;

	public static void main(String[] args) throws Exception {
		Simulator simulator = new Simulator();
		simulator.setup();
		
		int[] pathsSize = new int[3];
		
		int caminhoMedio = 0;
		long time = 0;

		try {
			time = System.currentTimeMillis();
			
			for (int i = 0; i < Constants.SIMULATIONS; i++) {
				
				simulator.execute(Constants.STEPS_1);

				int pathSize = simulator.world.getBestPathSize();
				caminhoMedio += pathSize;				
				pathsSize[pathSize]++;				
				
				if (Constants.PRINT_BEST_PATH){
					System.out.println("Melhor caminho: " + simulator.world.getBestPath());
				}
				simulator.world.reset();
			}
			
			System.out.println("Tempo total: " + (System.currentTimeMillis() - time) + " milisegundos.");
			for (int i = 0; i < pathsSize.length; i++) {
				if (i > 0){
					System.out.println("Tamanho: " + i + " = " + (double) pathsSize[i] / Constants.SIMULATIONS);
				}
				
			}
			
			System.out.println("Total: " + Constants.SIMULATIONS);
			System.out.println("Melhor caminho médio: " + (double) caminhoMedio / Constants.SIMULATIONS);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void execute(int steps) throws Exception{
		for (int j = 0; j < steps; j++) {
			this.world.run();
		}
	}
	
	private void setup(){
		this.world = new World(0, 2, 3);
		this.world.putPheromone(0, 1);
		this.world.putPheromone(0, 2);
		this.world.putPheromone(1, 2);

		this.world.setAlpha(Constants.ALPHA);
		this.world.setEvaporationRate(Constants.EVAPORATION_RATE);
		this.world.setAllLogsOn(Constants.PRINT_LOGS_ON);
		this.world.setPheromoneUpdateIsConstant(Constants.PHEROMONE_UPDATE_CONSTANT);
		this.world.setPrintAntTrack(Constants.PRINT_TRACK);
		this.world.setPrintBestPath(Constants.PRINT_BEST_PATH);
		
		this.world.createAnts(Constants.ANTS);		

		/*
		this.pheromone = new Pheromone(0, 18, 19);
		this.pheromone.addPheromone(0, 1, 1);
		this.pheromone.addPheromone(0, 2, 1);
		this.pheromone.addPheromone(1, 3, 1);
		this.pheromone.addPheromone(2, 4, 1);
		this.pheromone.addPheromone(2, 5, 1);
		this.pheromone.addPheromone(3, 6, 1);
		this.pheromone.addPheromone(4, 7, 1);
		this.pheromone.addPheromone(4, 9, 1);
		this.pheromone.addPheromone(4, 14, 1);
		this.pheromone.addPheromone(5, 7, 1);
		this.pheromone.addPheromone(5, 10, 1);
		this.pheromone.addPheromone(5, 15, 1);
		this.pheromone.addPheromone(6, 8, 1);
		this.pheromone.addPheromone(7, 12, 1);
		this.pheromone.addPheromone(8, 11, 1);
		this.pheromone.addPheromone(9, 14, 1);
		this.pheromone.addPheromone(10, 15, 1);
		this.pheromone.addPheromone(11, 13, 1);
		this.pheromone.addPheromone(12, 14, 1);
		this.pheromone.addPheromone(12, 15, 1);
		this.pheromone.addPheromone(13, 16, 1);
		this.pheromone.addPheromone(14, 17, 1);
		this.pheromone.addPheromone(15, 17, 1);
		this.pheromone.addPheromone(16, 18, 1);
		this.pheromone.addPheromone(17, 18, 1);
		
		/*
		this.pheromone = new Pheromone(01, 74, 81);
		this.pheromone.addPheromone(00, 01, 1);
		this.pheromone.addPheromone(00, 10, 1);
		this.pheromone.addPheromone(10, 11, 1);
		this.pheromone.addPheromone(10, 20, 1);
		this.pheromone.addPheromone(20, 21, 1);
		this.pheromone.addPheromone(20, 30, 1);
		this.pheromone.addPheromone(30, 31, 1);
		this.pheromone.addPheromone(30, 40, 1);
		this.pheromone.addPheromone(40, 41, 1);
		this.pheromone.addPheromone(40, 50, 1);
		this.pheromone.addPheromone(50, 51, 1);
		this.pheromone.addPheromone(01, 11, 1);
		this.pheromone.addPheromone(11, 12, 1);
		this.pheromone.addPheromone(11, 21, 1);
		this.pheromone.addPheromone(21, 22, 1);
		this.pheromone.addPheromone(21, 31, 1);
		this.pheromone.addPheromone(31, 32, 1);
		this.pheromone.addPheromone(31, 41, 1);
		this.pheromone.addPheromone(41, 51, 1);
		this.pheromone.addPheromone(12, 13, 1);
		this.pheromone.addPheromone(12, 22, 1);
		this.pheromone.addPheromone(22, 23, 1);
		this.pheromone.addPheromone(22, 32, 1);
		this.pheromone.addPheromone(32, 33, 1);
		this.pheromone.addPheromone(13, 14, 1);
		this.pheromone.addPheromone(13, 23, 1);
		this.pheromone.addPheromone(23, 24, 1);
		this.pheromone.addPheromone(23, 33, 1);
		this.pheromone.addPheromone(33, 34, 1);
		this.pheromone.addPheromone(14, 15, 1);
		this.pheromone.addPheromone(14, 24, 1);
		this.pheromone.addPheromone(24, 25, 1);
		this.pheromone.addPheromone(24, 34, 1);
		this.pheromone.addPheromone(34, 35, 1);
		this.pheromone.addPheromone(34, 44, 1);
		this.pheromone.addPheromone(44, 45, 1);
		this.pheromone.addPheromone(44, 54, 1);
		this.pheromone.addPheromone(54, 55, 1);
		this.pheromone.addPheromone(54, 64, 1);
		this.pheromone.addPheromone(64, 65, 1);
		this.pheromone.addPheromone(64, 74, 1);
		this.pheromone.addPheromone(74, 75, 1);
		this.pheromone.addPheromone(05, 15, 1);
		this.pheromone.addPheromone(32, 42, 1);
		this.pheromone.addPheromone(33, 43, 1);
		*/

	}
	

}
