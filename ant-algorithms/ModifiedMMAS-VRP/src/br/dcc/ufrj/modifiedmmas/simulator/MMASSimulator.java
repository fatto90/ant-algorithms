package br.dcc.ufrj.modifiedmmas.simulator;

import br.dcc.ufrj.antvrp.simulator.Simulator;
import br.dcc.ufrj.antvrp.world.World;
import br.dcc.ufrj.modifiedmmas.world.MMASWorld;

public class MMASSimulator extends Simulator{
	
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
		System.out.println("Seed: " + world.getSeed() + "\n");
	}
	
	private void printIntance(MMASWorld world) {
		System.out.println("Nome: " + world.getName());
		System.out.println("Comentarios: " + world.getComment());
		System.out.println("Tipo: " + world.getType());
		System.out.println("Dimencao: " + world.getDimension());
		System.out.println("Edge Weight Type: " + world.getEdgeWeightType());
		System.out.println("Capacidade: " + world.getCapacity() + "\n");
		
	}	

	public static void main(String[] args) throws Exception {
		MMASSimulator simulator = new MMASSimulator();
		simulator.printHeader();
		simulator.antAmount = 200;
		
		if (simulator.isDefaultMode()){
			simulator.setNumeroSimulacoes();
			simulator.setQuantidadeFormigas();
		}
		
		MMASWorld world = new MMASWorld(0);
		world.createWorld(simulator.getFileName());
		
		simulator.printInputs(world);
		simulator.printIntance(world);
		
		world.createAnts(simulator.getAntAmount());
		world.createPheromones();
		
		for(int s = 0; s < simulator.getNumeroSimulacoes(); s++){
			long t2 = System.currentTimeMillis();
			double bestSolution = 0;
			
			for (int j = 0; j <= simulator.getRodadasEstaveis(); j++) {
				world.run();
				if (world.getBestTour().getDistance() < bestSolution || bestSolution == 0) {
					bestSolution = world.getBestTour().getDistance();
					j = 0;
				}
			}
			
			simulator.printResults(world, System.currentTimeMillis() - t2);
			world.reset();
		}
	}
}
