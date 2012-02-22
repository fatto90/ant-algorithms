package br.dcc.ufrj.asranksavvrp.simulator;

import br.dcc.ufrj.antvrp.simulator.Simulator;
import br.dcc.ufrj.antvrp.util.Util;
import br.dcc.ufrj.antvrp.world.World;
import br.dcc.ufrj.asranksavvrp.world.ASrankSavWorld;

public class AsRankSavSimulator extends Simulator{
	
	private int rankSize = 20;
	
	protected void setTamanhoRank() throws Exception{
		int rankSize = Util.getIntegerStdin("Informe o tamanho do ranking de formigas: ");
		this.rankSize = rankSize; 
	}

	public int getRankSize() {
		return rankSize;
	}
	
	private void printHeader(){
		System.out.println("============================================================================");
		System.out.println("Universidade Federeal do Rio de Janeiro");
		System.out.println("Instituto de Matematica - Departamento de Ciencia da Computacao");
		System.out.println("Projeto Final de Curso");
		System.out.println("Aluno: Fabio Augusto Antunes Barbosa DRE:102025586");
		System.out.println("Orientador: Joao Carlos Pereira da Silva");		
		System.out.println("Implementacao do Algoritmo ASrankSav para o Problema de Roteamento de Veiculos.");
		System.out.println("============================================================================\n\n");
	}
	
	private void printResults(World world, long tempo){
		System.out.println("\n*******************************************************************************************");
		System.out.println("Melhor Tour: " + world.getBestTour());
		System.out.println("Numero de Veiculos: " + world.getBestTour().getRoutes().size());
		System.out.println("Tamanho Melhor Tour: " + world.getBestTour().getDistance());
		System.out.println("  Tamanho Pior Tour: " + world.getWorstTour().getDistance());
		System.out.println("Tempo total da simulacao: " + tempo + " milissegundos\n");
	}
	
	private void printInputs(World world)  throws Exception{
		System.out.println("Iniciando execucao do aplicativo... ");
		System.out.println("Quantidade de simulacoes: " + this.getNumeroSimulacoes());
		System.out.println("Quantidade de rodadas estaveis: " + this.getRodadasEstaveis());
		System.out.println("Quantidade de formigas: " + this.getAntAmount());
		System.out.println("Rank size: " + this.getRankSize());
		System.out.println("Seed: " + world.getSeed() + "\n");
	}
	
	private void printIntance(ASrankSavWorld world) {
		System.out.println("Nome: " + world.getName());
		System.out.println("Comentarios: " + world.getComment());
		System.out.println("Tipo: " + world.getType());
		System.out.println("Dimensao: " + world.getDimension());
		System.out.println("Edge Weight Type: " + world.getEdgeWeightType());
		System.out.println("Capacidade: " + world.getCapacity() + "\n");
		
	}	

	public static void main(String[] args) throws Exception {
		AsRankSavSimulator simulator = new AsRankSavSimulator();
		simulator.printHeader();
		
		//simulator.setFileName();
		if (simulator.isDefaultMode()){
			simulator.setNumeroSimulacoes();
			simulator.setQuantidadeFormigas();
			simulator.setTamanhoRank();
		}
		
		ASrankSavWorld world = new ASrankSavWorld(0);
		world.createWorld(simulator.getFileName());
		
		simulator.printInputs(world);		
		simulator.printIntance(world);
		
		world.createAnts(simulator.getAntAmount());
		world.setRankSize(simulator.getRankSize());
		world.createPheromones();
		
		for(int s = 0; s < simulator.getNumeroSimulacoes(); s++){
			long t2 = System.currentTimeMillis();
			double bestSolution = 0;
			
			for (int j = 0; j <= simulator.getRodadasEstaveis(); j++) {
				world.run();
				if (world.getBestTour().getDistance() < bestSolution || bestSolution == 0) {
					bestSolution = world.getBestTour().getDistance();
					//System.out.println(bestSolution);
					j = 0;
				}
			}
			
			simulator.printResults(world, System.currentTimeMillis() - t2);
			world.reset();
		}
	}
}
