package br.ufrj.dcc.ant;

import br.ufrj.dcc.util.Path;

public class Ant implements Comparable<Ant> {
	
	private int currentNode;
	private int alpha = 1;
	private int beta = 1;
	
	private Path path = null;
	private Ant nextAnt;
		
	public Ant(int homeNode) {
		this.currentNode = homeNode;
		this.path = new Path(homeNode);
	}
	
	public int move(long []distances, double []pheromone, double sample){
		
		double probabilities[] = new double[distances.length];
		double sum = 0;
		double acumulator = 0;
		
		for (int i = 0; i < distances.length; i++) {
			if (!path.containsNode(i + 1)){
				sum += Math.pow(distances[i], this.alpha) * Math.pow(pheromone[i], this.beta);	
			}
		}
		
		for (int i = 0; i < distances.length; i++) {
			if (!path.containsNode(i + 1)){
				probabilities[i] = Math.pow(distances[i], this.alpha) * Math.pow(pheromone[i], this.beta)/sum;
			} else {
				probabilities[i] = 0;
			}
		}
		
		for (int i = 0; i < probabilities.length; i++) {
			acumulator += probabilities[i];
			if (acumulator > sample){
				this.path.addNode(i + 1, distances[i]);
				this.currentNode = i + 1;
				return i + 1;
			}
		}	
		
		throw new RuntimeException("Probabilidade não encontrada");
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
		return this.path.getStartNode();
	}

	public void moveHomeNode(long distance) {
		int homeNode = this.path.getStartNode();
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
	
	public double dropPheromone(){
		return (double) 1 / this.path.getLength();
	}

	@Override
	public int compareTo(Ant ant) {
		if (this.path.getLength() > ant.path.getLength()){
			return 1;
			
		} else if(this.path.getLength() == ant.path.getLength()) {
			return 0;
			
		}else{
			return -1;
			
		}
	}
	
	@Override
	public String toString() {
		return "[" + this.path.getStartNode() + "]" + this.path.getLength();
	}
}
