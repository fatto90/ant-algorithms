package br.ufrj.nce.saco.core;

import java.util.Random;


public class WorldHandler {
	
	private Ant []ants;
	private World world;
	private Random rand;
	
	public WorldHandler() {
		rand = new Random();
	}
	
	public WorldHandler(long seed) {
		rand = new Random(seed);
	}
	
	public void initialize(int antsAmount){
		this.ants = new Ant[antsAmount];
		this.world = new World();
		
		for (int i = 0; i < antsAmount; i++){
			this.ants[i] = new Ant(1000, 0, 18);
		}		
	}
	
	public void run(int print) throws Exception{
		
		
		for (int i = 0; i < ants.length; i++) {
			
			int currentNode = ants[i].getCurrentNode();		
			double []PheromoneNeighbourhood = world.getPheromoneNeighbourhood(currentNode); 
			ants[i].move(PheromoneNeighbourhood, rand.nextDouble());
			world.addPheromone(currentNode, ants[i].getCurrentNode(), ants[i].getPheromoneAmount()) ;
			if( print == 1999){
				System.out.println("Formiga " + i + " - nó " + ants[i].getCurrentNode() + " - Path: [" + ants[i].getPathString() + "] ");
				
			}

		}
		world.updatePheromoneTrail();
		System.out.println("");
	}
	
	
	
	
	
	
}
