package br.dcc.ufrj.asrankvrp.simulator;

import br.dcc.ufrj.asrankvrp.world.ASrankWorld;

public class Simulador {

	public static final String FILE_PATH = "C:\\Users\\Fabio\\Desktop\\dataset\\";
	public static final String FILE_NAME = "att48.vrp";
	public static final int QTDE_SIMULACOES = 1;
	public static final int RODADAS_ESTAVEIS = 100;
	public static final int ANT_AMOUNT = 3000;
	public static final int RANK_SIZE = 35;

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando execução do aplicativo... ");
		System.out.println("Quantidade de simulações: " + QTDE_SIMULACOES);
		System.out.println("Quantidade de rodadas estáveis: " + RODADAS_ESTAVEIS);
		System.out.println("Quantidade de formigas: " + ANT_AMOUNT);
		System.out.println("Rank size: " + RANK_SIZE);
		
		for(int s = 0; s < QTDE_SIMULACOES; s++){
			long t2 = System.currentTimeMillis();
			double bestSolution = 0;
			double initialPheromone = 0;
			ASrankWorld world = new ASrankWorld(50);
			world.createWorld(FILE_PATH.concat(FILE_NAME));
			world.createAnts(ANT_AMOUNT);
			world.setRankSize(RANK_SIZE);
			initialPheromone = ANT_AMOUNT / world.getInitialTourSize();
			world.createPheromones(initialPheromone);
			
			System.out.println(world.getTourLength("1, 47, 21, 30, 43, 18, 27, 20, 37, 7, 28, 8, 19, 36, 13, 12, 1, 34, 41, 17, 23, 2, 9, 10, 38, 31, 44, 46, 33, 16, 40, 4, 1, 14, 22, 39, 32, 25, 11, 45, 35, 5, 26, 42, 3, 29, 6, 48, 1, 15, 24, 1"));
			
			for (int i = 1, j = 0; j <= RODADAS_ESTAVEIS; i++, j++) {
				world.run();
				if (world.getBestTour().getDistance() < bestSolution || bestSolution == 0) {
					bestSolution = world.getBestTour().getDistance();
					System.out.println("Mudou na rodada " + j + " de " + i + " Best size: " + world.getBestTour().getDistance() + " - " + world.getBestTour());
					j = 0;
				}
			}
			
			System.out.println("\n " + s + " *******************************************************************************************");
			
			System.out.println("Seed: " + world.getSeed());
			System.out.println("Melhor Tour: " + world.getBestTour());
			System.out.println("Numero de Veículos: " + world.getBestTour().getRoutes().size());
			System.out.println("Tamanho Melhor Tour: " + world.getBestTour().getDistance());
			System.out.println("Tempo total da simulação: " + (System.currentTimeMillis() - t2) + " milissegundos");
		}
	}
}
