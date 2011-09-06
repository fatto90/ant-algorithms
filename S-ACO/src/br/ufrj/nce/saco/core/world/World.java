package br.ufrj.nce.saco.core.world;

import java.util.Random;

import br.ufrj.nce.saco.core.ant.SingleAnt;
import br.ufrj.nce.saco.core.pheromone.Pheromone;
import br.ufrj.nce.saco.utils.Constants;

public class World {

	private SingleAnt[] ants;
	private Pheromone pheromone;
	private Random rand;

	public World() {
		rand = new Random();
	}

	public World(long seed) {
		rand = new Random(seed);
	}

	public void initialize(int antsAmount) throws Exception {
		this.ants = new SingleAnt[antsAmount];
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

		for (int i = 0; i < antsAmount; i++) {
			this.ants[i] = new SingleAnt(pheromone.getSourceNode());
			this.ants[i].setAlpha(Constants.ALPHA);
		}
	}

	public void run() throws Exception {

		for (int i = 0; i < ants.length; i++) {
			
			if (Constants.TRACE_ON){
				System.out.println("Ant: " + i + " - path: [" + ants[i].getPath() + "]");
			}
			
			double[] pheromoneNeighborhood = pheromone.getPheromoneNeighborhood(ants[i].getCurrentNode());
			ants[i].move(pheromoneNeighborhood, this.rand.nextDouble());

			if (ants[i].isPheromoneAvaible()) {
				pheromone.addPheromone(ants[i].getPreviousNode(), ants[i].getCurrentNode(), ants[i].getPheromoneAmount());
			}

			if (ants[i].getCurrentNode() == pheromone.getSourceNode() && ants[i].getPathSize() == 1){
				ants[i].switchMode();
			}

			if (ants[i].getCurrentNode() == pheromone.getDestinationNode()) {
				ants[i].removeLoops();
				ants[i].switchMode();
			}
		}
		pheromone.updatePheromoneTrail(Constants.EVAPORATION_RATE);
	}

	public void worldPrint() {
		String linha = "";
		for (int i = 0; i < pheromone.getPheromoneNeighborhood(i).length; i++) {
			for (int j = 0; j < pheromone.getPheromoneNeighborhood(i).length; j++) {
				linha += pheromone.getPheromoneNeighborhood(i)[j] + ", ";
			}
			System.out.println(linha);
			linha = "";
		}
	}

	public String getBestPath() {
		return pheromone.getBestPath();
	}
	
	public int getBestPathSize() {
		return pheromone.getBestPathSize();
	}
	
	public String chooseBestPath(){
		return pheromone.chooseBestPath(this.pheromone.getSourceNode(), this.pheromone.getSourceNode());
	}
	
	public int countBestPath(){
		return pheromone.countBestPath(this.pheromone.getSourceNode(), this.pheromone.getSourceNode());
	}

	public void removePheromone() {
		pheromone.removePheromone(4, 14);
		pheromone.removePheromone(5, 15);
		
	}

	public void addPheromone() {
		pheromone.addPheromone(4, 14, 1);
		pheromone.addPheromone(5, 15, 1);
	}

}
