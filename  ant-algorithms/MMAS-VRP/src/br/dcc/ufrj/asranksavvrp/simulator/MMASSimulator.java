package br.dcc.ufrj.asranksavvrp.simulator;

import br.dcc.ufrj.antvrp.simulator.Simulator;
import br.dcc.ufrj.antvrp.world.World;
import br.dcc.ufrj.asranksavvrp.world.MMASWorld;

public class MMASSimulator extends Simulator{
	
	private void printHeader(){
		System.out.println("============================================================================");
		System.out.println("Universidade Federeal do Rio de Janeiro");
		System.out.println("Instituto de Matemática - Departamento de Ciência da Computação");
		System.out.println("Projeto Final de Curso");
		System.out.println("Aluno: Fabio Augusto Antunes Barbosa DRE:102025586");
		System.out.println("Orientador: João Carlos Pereira da Silva");		
		System.out.println("Implementação do Algoritmo ASrankSav para o Problema de Roteamento de Veículos.");
		System.out.println("============================================================================\n\n");
	}
	
	private void printResults(World world, long tempo){
		System.out.println("\n*******************************************************************************************");
		System.out.println("Melhor Tour: " + world.getBestTour());
		System.out.println("Numero de Veículos: " + world.getBestTour().getRoutes().size());
		System.out.println("Tamanho Melhor Tour: " + world.getBestTour().getDistance());
		System.out.println("  Tamanho Pior Tour: " + world.getWorstTour().getDistance());
		System.out.println("Tempo total da simulação: " + tempo + " milissegundos\n");
	}
	
	private void printInputs(World world)  throws Exception{
		System.out.println("Iniciando execução do aplicativo... ");
		System.out.println("Quantidade de simulações: " + this.getNumeroSimulacoes());
		System.out.println("Quantidade de rodadas estáveis: " + this.getRodadasEstaveis());
		System.out.println("Quantidade de formigas: " + this.getAntAmount());
		System.out.println("Seed: " + world.getSeed() + "\n");
	}
	
	private void printIntance(MMASWorld world) {
		System.out.println("Nome: " + world.getName());
		System.out.println("Comentários: " + world.getComment());
		System.out.println("Tipo: " + world.getType());
		System.out.println("Dimensão: " + world.getDimension());
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
			
			for (int i = 1, j = 0; j <= simulator.getRodadasEstaveis(); i++, j++) {
				world.run();
				if (world.getBestTour().getDistance() < bestSolution || bestSolution == 0) {
					bestSolution = world.getBestTour().getDistance();
					j = 0;
					//System.out.println(bestSolution + " " + world.getBestTour());
				}
			}
			
			simulator.printResults(world, System.currentTimeMillis() - t2);
			world.reset();
		}
	}
}
