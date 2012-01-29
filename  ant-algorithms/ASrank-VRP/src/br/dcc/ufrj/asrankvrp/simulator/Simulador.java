package br.dcc.ufrj.asrankvrp.simulator;

import br.dcc.ufrj.asrankvrp.world.ASrankWorld;

public class Simulador {

	public static final String FILE_PATH = "C:\\Users\\Fabio\\Desktop\\dataset\\";
	public static final String FILE_NAME = "att48.vrp";
	public static final int QTDE_SIMULACOES = 1;
	public static final int RODADAS_ESTAVEIS = 100;
	public static final int ANT_AMOUNT = 1000;
	public static final int RANK_SIZE = 20;

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando execução do aplicativo... ");
		System.out.println("Quantidade de simulações: " + QTDE_SIMULACOES);
		System.out.println("Quantidade de rodadas estáveis: " + RODADAS_ESTAVEIS);
		System.out.println("Quantidade de formigas: " + ANT_AMOUNT);
		System.out.println("Rank size: " + RANK_SIZE);

		double bestSolution = 0;
		double initialPheromone = 0;
		ASrankWorld world = new ASrankWorld(50);
		world.createWorld(FILE_PATH.concat(FILE_NAME));
		world.createAnts(ANT_AMOUNT);
		world.setRankSize(RANK_SIZE);
		initialPheromone = ANT_AMOUNT / world.getInitialPathSize();

		world.createPheromones(initialPheromone);
		
		long t2 = System.currentTimeMillis();
		for (int i = 1, j = 0; j <= RODADAS_ESTAVEIS; i++, j++) {
			
			//long t1 = System.currentTimeMillis();
			world.run();

			if (world.getBestPath().getLength() < bestSolution || bestSolution == 0) {
				bestSolution = world.getBestPath().getLength();
				j = 1;
			}
			//System.out.println("[" + i + ", " + j + "] - Tbs: " + world.getBestTourSize() + " Tempo: " + (System.currentTimeMillis() - t1) + " milisegundos");
		}

		System.out.println("\n*******************************************************************************************");
		System.out.println("Seed: " + world.getSeed());
		System.out.println(world.getBestPath().lengthSubpath(0, 51));		
		System.out.println("Melhor Tour: " + world.getBestPath());
		System.out.println("Tamanho Melhor Tour: " + world.getBestPath().getLength());
		System.out.println("Tempo total da simulação: " + (System.currentTimeMillis() - t2) + " milisegundos");
	}
}
