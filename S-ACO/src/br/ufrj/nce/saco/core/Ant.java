package br.ufrj.nce.saco.core;

public class Ant {
	
	private int currentNode;
	private int sourceNode;
	private int destinationNode;
	private int lastNode;
	private int pathPosition;
	private int lastPathSize;
	private int pathSize = 1;
	private int []path;
	public enum Mode {FORWARD, BACKWARD};
	private Mode mode;
	private int alpha = 2;
			
	public Ant(int maxPath, int sourceNode, int destinationNode){	
		this.path = new int[maxPath];
		this.mode = Mode.FORWARD;		
		this.destinationNode = destinationNode;
		this.currentNode = sourceNode;
		this.sourceNode = sourceNode;		
		this.lastNode = sourceNode;
		this.pathPosition = 0;
	}
	
	public int getCurrentNode(){
		return this.currentNode;
	}
	
	public Mode getMode(){
		return this.mode;
	}

	public void switchMode(){		
		this.mode = (this.mode == Mode.FORWARD ? Mode.BACKWARD : Mode.FORWARD);		
	}

	public int[] getPath(){
		return this.path;
	}
	
	public String getPathString(){
		
		String result = "";
		
		for (int i = 0; i < path.length; i++) {
			result += path[i] + ", ";
		}
		
		return result;
	}

	public void move(double []pheromoneTrail, double u) throws Exception{
		
		if (this.mode == Mode.BACKWARD){
			this.lastNode = this.currentNode;
			if (pathPosition > 0){
				path[pathPosition] = 0;
				this.currentNode = path[--pathPosition];
				
			} else {
				this.lastNode = this.currentNode;
				this.currentNode = path[0];
			}			
			
		} else {
			this.pathSize++;
			this.lastNode = this.currentNode;
			this.currentNode = findNextNode(pheromoneTrail, u);
			this.path[++pathPosition] = this.currentNode;
		}
		
		if (this.currentNode == this.sourceNode ){
			this.switchMode();
			this.pathSize = 1;
		}
		
		if (this.currentNode == this.destinationNode){
			this.switchMode();
			this.path = removeLoops(this.path);
			this.lastPathSize = this.pathSize;
			
		}
	}
	
	public double getPheromoneAmount(){
		return this.mode == Mode.BACKWARD? (1.0 / path.length): 0.0;
	}

	private int findNextNode(double []pheromoneTrail, double u){
		
		double totalPheromone = 0;
		double probabilities[] = new double [pheromoneTrail.length];  
		double sumProbabilities = 0;
		
		for (int i = 0; i < pheromoneTrail.length; i++){
			if (i != path[Math.max(0, pathPosition - 1)]){
				totalPheromone += Math.pow(pheromoneTrail[i], this.alpha);	
			}
			
		}
		
		for (int i = 0; i < pheromoneTrail.length; i++){
			if (i != path[Math.max(0, pathPosition - 1)]){
				probabilities[i] = Math.pow(pheromoneTrail[i], this.alpha) / totalPheromone;
			}
		}
		
		for (int i = 0; i < pheromoneTrail.length; i++){
			if (i != path[Math.max(0, pathPosition - 1)]){
				sumProbabilities += probabilities[i];
				
				if (u < sumProbabilities){				
					return i;
				} 
			}
		}
		
		return this.lastNode;
	}

	private int [] removeLoops(int []path) {
		
		for (int i = 0; i < this.pathSize; i++){			
			for (int j = this.pathSize - 1; j > i; j--){
				
				if (path[j] == path[i]){
					
					for (int k = i, l = j; k < this.pathSize - 1; k++, l++){
						
						if (l < path.length){
							path[k] = path[l];
							path[l] = 0;							
						}
						
					}			
					this.pathSize -= (j - i);
					this.pathPosition = this.pathSize - 1;
				}
			}
		}
		return path;
	}
}

