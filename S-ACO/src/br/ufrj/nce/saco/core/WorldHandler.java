package br.ufrj.nce.saco.core;

import java.util.Random;

public class WorldHandler {

	private Ant[] ants;
	private World world;
	private Random rand;

	public WorldHandler() {
		rand = new Random();
	}

	public WorldHandler(long seed) {
		rand = new Random(seed);
	}

	public void initialize(int antsAmount) {
		this.ants = new Ant[antsAmount];
		this.world = new World();

		for (int i = 0; i < antsAmount; i++) {
			this.ants[i] = new Ant(0, 18);
		}
	}

	public void run(int print) throws Exception {

		for (int i = 0; i < ants.length; i++) {

			int currentNode = ants[i].getCurrentNode();
			double[] pheromoneNeighbourhood = world.getPheromoneNeighbourhood(currentNode);
			ants[i].move(pheromoneNeighbourhood, this.rand.nextDouble());
			
			if(ants[i].getPheromoneAmount() > 0){
				world.addPheromone(currentNode, ants[i].getCurrentNode(), ants[i].getPheromoneAmount());
				world.addPheromone(ants[i].getCurrentNode(), currentNode, ants[i].getPheromoneAmount());
			}
		}
		world.updatePheromoneTrail();
	}

	public void worldPrint() {
		String linha = "";
		for (int i = 0; i < world.getPheromoneNeighbourhood(i).length; i++) {
			for (int j = 0; j < world.getPheromoneNeighbourhood(i).length; j++) {
				linha += world.getPheromoneNeighbourhood(i)[j] + ", ";
			}
			System.out.println(linha);
			linha = "";
		}
	}
	
	public String getBestPath(){
		return world.getBestPath();
	}
	

}
