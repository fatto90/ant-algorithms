package br.dcc.ufrj.asrankvrp.simulator;

import br.dcc.ufrj.antvrp.util.Util;
import br.dcc.ufrj.antvrp.world.World;
import br.dcc.ufrj.asrankvrp.world.ASrankWorld;

public class Simulator {

	private static String fileName = "C:\\Users\\Fabio\\Desktop\\dataset\\att48.vrp";
	private static final int RODADAS_ESTAVEIS = 100;
	private static int numeroSimulacoes = 100;
	private static int antAmount = 1000;
	private static int rankSize = 20;
	
	private static void printHeader(){
		System.out.println("============================================================================");
		System.out.println("Universidade Federeal do Rio de Janeiro");
		System.out.println("Instituto de Matemática - Departamento de Ciência da Computação");
		System.out.println("Projeto Final de Curso");
		System.out.println("Aluno: Fabio Augusto Antunes Barbosa DRE:102025586");
		System.out.println("Orientador: João Carlos Pereira da Silva");		
		System.out.println("Implementação do Algoritmo ASrank para o Problema de Roteamento de Veículos.");
		System.out.println("============================================================================\n\n");
	}
	
	private static void getFileName() throws Exception{
		String fileName = Util.getStringStdin("Informe o dataset: ");
		if (!"0".equals(fileName)){
			Simulator.fileName = fileName; 
		}
	}
	
	private static void getNumeroSimulacoes() throws Exception{
		int numeroSimulacoes = Util.getIntegerStdin("Informe o número de resultados desejados: ");
		if (numeroSimulacoes != 0){
			Simulator.numeroSimulacoes = numeroSimulacoes; 
		}
	}
	
	private static void getQuantidadeFormigas() throws Exception{
		int antAmount = Util.getIntegerStdin("Informe a quantidade de formigas: ");
		if (antAmount  != 0){
			Simulator.antAmount = antAmount ; 
		}
	}
	
	private static void getTamanhoRank() throws Exception{
		int rankSize = Util.getIntegerStdin("Informe o tamanho do ranking de formigas: ");
		if (rankSize  != 0){
			Simulator.rankSize = rankSize; 
		}

	}
	
	private static void printResults(World world, long tempo){
		System.out.println("\n*******************************************************************************************");
		System.out.println("Melhor Tour: " + world.getBestTour());
		System.out.println("Numero de Veículos: " + world.getBestTour().getRoutes().size());
		System.out.println("Tamanho Melhor Tour: " + world.getBestTour().getDistance());
		System.out.println("Tempo total da simulação: " + tempo + " milissegundos\n");
	}
	
	private static void printInputs(World world){
		System.out.println("Iniciando execução do aplicativo... ");
		System.out.println("Quantidade de simulações: " + numeroSimulacoes);
		System.out.println("Quantidade de rodadas estáveis: " + RODADAS_ESTAVEIS);
		System.out.println("Quantidade de formigas: " + antAmount);
		System.out.println("Rank size: " + rankSize);
		System.out.println("Seed: " + world.getSeed() + "\n");
	}
	
	

	public static void main(String[] args) throws Exception {
		Simulator.printHeader();
		Simulator.getFileName();
		Simulator.getNumeroSimulacoes();
		Simulator.getQuantidadeFormigas();
		Simulator.getTamanhoRank();
		
		ASrankWorld world = new ASrankWorld();
		world.createWorld(fileName);
		
		Simulator.printInputs(world);		
		Simulator.printIntance(world);
		
		world.createAnts(antAmount);
		world.setRankSize(rankSize);
		world.createPheromones(antAmount);
		
		for(int s = 0; s < numeroSimulacoes; s++){
			long t2 = System.currentTimeMillis();
			double bestSolution = 0;
			
			for (int i = 1, j = 0; j <= RODADAS_ESTAVEIS; i++, j++) {
				world.run();
				if (world.getBestTour().getDistance() < bestSolution || bestSolution == 0) {
					bestSolution = world.getBestTour().getDistance();
					j = 0;
				}
			}
			
			Simulator.printResults(world, System.currentTimeMillis() - t2);
			world.reset();
		}
	}

	private static void printIntance(ASrankWorld world) {
		System.out.println("Nome: " + world.getName());
		System.out.println("Comentários: " + world.getComment());
		System.out.println("Tipo: " + world.getType());
		System.out.println("Dimensão: " + world.getDimension());
		System.out.println("Edge Weight Type: " + world.getEdgeWeightType());
		System.out.println("Capacidade: " + world.getCapacity());
		
	}
}
