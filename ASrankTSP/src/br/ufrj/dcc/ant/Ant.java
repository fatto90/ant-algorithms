package br.ufrj.dcc.ant;

import br.ufrj.dcc.world.Path;

public class Ant {
	
	private int homeNode;
	private int currentNode;
	private Path path = new Path();
	private int alpha = 1;
	private int beta = 1;
	private Ant nextAnt;
		
	public Ant(int homeNode) {
		this.currentNode = homeNode;
		this.homeNode = homeNode;
		path.addNode(homeNode);
	}
	
	public int move(long []distances, double []pheromone, double sample){
		
		double probabilities[] = new double[distances.length];
		double sum = 0;
		double acumulator = 0;
		
		for (int i = 0; i < distances.length; i++) {
			if (!path.containsNode(i)){
				sum += Math.pow(distances[i], this.alpha) * Math.pow(pheromone[i], this.beta);	
			}
		}
		
		for (int i = 0; i < distances.length; i++) {
			if (!path.containsNode(i)){
				probabilities[i] = Math.pow(distances[i], this.alpha) * Math.pow(pheromone[i], this.beta)/sum;
			} else {
				probabilities[i] = 0;
			}
		}
		
		for (int i = 0; i < probabilities.length; i++) {
			acumulator += probabilities[i];
			if (acumulator > sample){
				this.path.addNode(i, distances[i]);
				this.currentNode = i;
				return i;
			}
		}		
		
		return -1;
	}

	public int getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(int currentPosition) {
		this.currentNode = currentPosition;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	public int getHomeNode(){
		return this.homeNode;
	}

	public void moveHomeNode(long distance) {
		this.path.addNode(homeNode, distance);
		this.currentNode = homeNode;		
	}
	
	public Ant getNextAnt(){
		return this.nextAnt;
	}
	
	public void setNextAnt(Ant nextAnt){
		this.nextAnt = nextAnt;
	}
	
	public long getPathLength(){
		return this.path.getLength();
	}
}
