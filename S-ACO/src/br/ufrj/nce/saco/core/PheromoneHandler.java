package br.ufrj.nce.saco.core;

import java.util.Random;

public class PheromoneHandler {

	private Ant[] ants;
	private Pheromone pheromone;
	private Random rand;

	public PheromoneHandler() {
		rand = new Random();
	}

	public PheromoneHandler(long seed) {
		rand = new Random(seed);
	}

	public void initialize(int antsAmount) {
		this.ants = new Ant[antsAmount];
		this.pheromone = new Pheromone();

		for (int i = 0; i < antsAmount; i++) {
			this.ants[i] = new Ant(0, 18);
		}
	}

	public void run(int print) throws Exception {

		for (int i = 0; i < ants.length; i++) {

			int currentNode = ants[i].getCurrentNode();
			double[] pheromoneNeighbourhood = pheromone.getPheromoneNeighbourhood(currentNode);
			ants[i].move(pheromoneNeighbourhood, this.rand.nextDouble());
			
			if(ants[i].getPheromoneAmount() > 0){
				pheromone.addPheromone(currentNode, ants[i].getCurrentNode(), ants[i].getPheromoneAmount());
				pheromone.addPheromone(ants[i].getCurrentNode(), currentNode, ants[i].getPheromoneAmount());
			}
		}
		pheromone.updatePheromoneTrail();
	}

	public void worldPrint() {
		String linha = "";
		for (int i = 0; i < pheromone.getPheromoneNeighbourhood(i).length; i++) {
			for (int j = 0; j < pheromone.getPheromoneNeighbourhood(i).length; j++) {
				linha += pheromone.getPheromoneNeighbourhood(i)[j] + ", ";
			}
			System.out.println(linha);
			linha = "";
		}
	}
	
	public String getBestPath(){
		return pheromone.getBestPath();
	}
	

}
