package br.ufrj.nce.saco.core.world;

import java.util.Arrays;
import java.util.Random;

import br.ufrj.nce.saco.core.ant.SingleAnt;
import br.ufrj.nce.saco.core.pheromone.Pheromone;

public class World {

	private double alpha = 2;
	private int antsAmount = 1;
	private double evaporationRate = 0;
	private boolean allLogsOn = false;
	private boolean printAntTrack = false;
	private boolean printBestPath = false;
	private boolean pheromoneUpdateIsConstant = false;
	
	private Random rand;
	private SingleAnt[] ants;
	private Pheromone pheromone;

	public World() {
		rand = new Random();
	}

	public World(long seed) {
		rand = new Random(seed);
	}
	
	public World(int nest, int food, int size) {
		rand = new Random();
		this.pheromone = new Pheromone(nest, food, size);
	}

	public World(int nest, int food, int size, long seed) {
		rand = new Random(seed);
		this.pheromone = new Pheromone(nest, food, size);
	}
	
	public int getAntsAmount() {
		return antsAmount;
	}

	public void setAntsAmount(int antsAmount) {
		this.antsAmount = antsAmount;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getEvaporationRate() {
		return evaporationRate;
	}

	public void setEvaporationRate(double evaporationRate) {
		this.evaporationRate = evaporationRate;
	}

	public boolean isAllLogsOn() {
		return allLogsOn;
	}

	public void setAllLogsOn(boolean allLogsOn) {
		this.allLogsOn = allLogsOn;
	}

	public boolean isPrintAntTrack() {
		return printAntTrack;
	}

	public void setPrintAntTrack(boolean printAntTrack) {
		this.printAntTrack = printAntTrack;
	}

	public boolean isPrintBestPath() {
		return printBestPath;
	}

	public void setPrintBestPath(boolean printBestPath) {
		this.printBestPath = printBestPath;
	}

	public boolean isPheromoneUpdateIsConstant() {
		return pheromoneUpdateIsConstant;
	}

	public void setPheromoneUpdateIsConstant(boolean pheromoneUpdateIsConstant) {
		this.pheromoneUpdateIsConstant = pheromoneUpdateIsConstant;
	}

	public void reset() throws Exception {
		createAnts();
		this.pheromone.reset();
	}
	
	private void createAnts(){
		this.ants = new SingleAnt[this.antsAmount];

		for (int i = 0; i < antsAmount; i++) {
			this.ants[i] = new SingleAnt(pheromone.getSourceNode(), pheromone.getDestinationNode());
			this.ants[i].setAlpha(this.alpha);
			this.ants[i].setLogsOn(this.allLogsOn);
			this.ants[i].setPheromoneUpdateConstantEnabled(this.pheromoneUpdateIsConstant);			
		}
	}
	
	public void createAnts(int antsAmount){
		this.antsAmount = antsAmount;
		this.createAnts();
	}

	public void run() throws Exception {

		for (int i = 0; i < ants.length; i++) {
			
			if (this.printAntTrack){
				System.out.println("Ant: " + i + " - path: [" + ants[i].getPath() + "]");
			}
			
			double[] pheromoneNeighborhood = pheromone.getPheromoneNeighborhood(ants[i].getCurrentNode());
			
			if (this.allLogsOn){
				System.out.println("-----------------------------------------------------------------------");
				System.out.println("Ant: " + i + " - Posição atual: " + ants[i].getCurrentNode());
				System.out.println("Feromônio da vizinhança: " + Arrays.toString(pheromoneNeighborhood));				
			}			
			ants[i].move(pheromoneNeighborhood, this.rand.nextDouble());
			
			if (this.allLogsOn){
				System.out.println("Ant: " + i + " - Próximo movimento: " + ants[i].getCurrentNode());
			}			

			if (ants[i].isPheromoneAvaible()) {
				pheromone.addPheromone(ants[i].getPreviousNode(), ants[i].getCurrentNode(), ants[i].getPheromoneAmount());
			}
			
			if (this.allLogsOn){
				System.out.println("Atualização dos feromônios da formiga " + i + ". Nó atual: " + ants[i].getPreviousNode() + ", Próximo nó: " + ants[i].getCurrentNode() + ", Feromônio a ser depositado: " + ants[i].getPheromoneAmount());
			}			

			if (ants[i].getCurrentNode() == ants[i].getTargetNode()){
				
				if (ants[i].getCurrentNode() == pheromone.getDestinationNode()) {
					
					if (this.allLogsOn){
						System.out.println("Atingiu destino");
					}
					
					ants[i].removeLoops();
					ants[i].setTargetNode(pheromone.getSourceNode());					
					ants[i].setModeBackward();
					
				} else {
					if (this.allLogsOn){
						System.out.println("Atingiu origem");
					}
					
					ants[i].setTargetNode(pheromone.getDestinationNode());
					ants[i].resetPath();
					ants[i].setModeForward();
				}
			}
		}
		pheromone.updatePheromoneTrail(this.evaporationRate);
	}

	public String worldPrint() {
		return pheromone.getPheromoneMatrix();
	}

	public String getBestPath() {
		return pheromone.printBestPath();
	}
	
	public int getBestPathSize() {
		return pheromone.getBestPathSize();
	}

	public void clearPheromone(int source, int destination) {
		pheromone.clearPheromone(source, destination);
	}

	public void addPheromone(int source, int destination, int amount) {
		pheromone.addPheromone(source, destination, amount);
	}
	
	public void putPheromone(int source, int destination) {
		pheromone.putPheromone(source, destination);
	}

}
