package br.ufrj.nce.saco.core.ant;

import java.util.Arrays;

import br.ufrj.nce.saco.core.path.Path;

/**
 * @author Fabio Barbosa
 * This class represents a single ant used in Simple-ACO algorithm
 *
 */
public class SingleAnt {

	public enum Mode {
		FORWARD, BACKWARD
	};
	
	private int lastPathSize;
	private int alpha = 2;
	private int targetNode;
	private int sourceNode;
	private int destinationNode;
	private boolean pheromoneUpdateConstantEnabled = false;
	private boolean logsOn = false;
	private Mode mode;
	private Path path = new Path();

	public SingleAnt(int sourceNode, int destinationNode) {
		this.mode = Mode.FORWARD;
		this.path.addNode(sourceNode);
		this.targetNode = destinationNode;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		
	}
	
	public boolean isLogsOn() {
		return logsOn;
	}

	public void setLogsOn(boolean logsOn) {
		this.logsOn = logsOn;
	}

	public int getSourceNode() {
		return sourceNode;
	}
	public void setSourceNode(int sourceNode) {
		this.sourceNode = sourceNode;
	}
	public int getDestinationNode() {
		return destinationNode;
	}
	public void setDestinationNode(int destinationNode) {
		this.destinationNode = destinationNode;
	}

	public boolean isPheromoneUpdateConstantEnabled() {
		return pheromoneUpdateConstantEnabled;
	}

	public void setPheromoneUpdateConstantEnabled(boolean pheromoneUpdateConstantEnabled) {
		this.pheromoneUpdateConstantEnabled = pheromoneUpdateConstantEnabled;
	}

	public int getTargetNode() {
		return targetNode;
	}
	
	public void setTargetNode(int targetNode) {
		this.targetNode = targetNode;
	}
	
	public void setPath(Path path) {
		this.path = path;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int getCurrentNode() {
		return this.path.getCurrentNode();
	}

	public int getPreviousNode() {
		return this.path.getPreviousNode();
	}

	public Mode getMode() {
		return this.mode;
	}

	public int getPathSize() {
		return this.path.size();
	}

	public boolean isPheromoneAvaible() {
		return this.mode == Mode.BACKWARD;
	}

	public String getPath() {
		return this.path.toString();
	}

	public void removeLoops() {
		this.path.removeLoops();
	}

	public double getPheromoneAmount() {
		if (this.logsOn){
			System.out.println("Tamanho do ultimo caminho: " + (lastPathSize - 1));
		}
		
		if (this.pheromoneUpdateConstantEnabled){
			return this.mode == Mode.BACKWARD?  1.0: 0.0; 
			
		} else {
			return this.mode == Mode.BACKWARD?  1.0/ (double) (lastPathSize - 1): 0.0; 
		}
		
	}

	public void switchMode() {
		
		if (this.mode == Mode.FORWARD) {
			mode = Mode.BACKWARD;
			this.lastPathSize = this.path.size();

		} else {
			this.lastPathSize = 0;
			mode = Mode.FORWARD;
		}
	}

	public void move(double[] pheromoneTrail, double u) throws Exception {

		if (this.mode == Mode.BACKWARD){

			if (!isNextNodeValid(pheromoneTrail, this.path.getLastNode())){
				
				if (this.logsOn){
					System.out.println("Encontrou caminho inválido");
				}
				
				int temp = findNextNode(pheromoneTrail, u);
				this.path.addNode(temp);
				this.setModeForward();
				return;
			}
			
			if (path.size() > 1) {
				path.comeBack();
			}
			
		} else {
			int temp = findNextNode(pheromoneTrail, u);
			this.path.addNode(temp);
		}
	}
	
	private boolean isNextNodeValid(double[] pheromoneTrail, int nextNode){
		return pheromoneTrail[nextNode] > 0;
	}

	private int findNextNode(double[] pheromoneTrail, double u) {
		double totalPheromone = 0;
		double probabilities[] = new double[pheromoneTrail.length];
		double sumProbabilities = 0;

		for (int i = 0; i < pheromoneTrail.length; i++) {
			if (i != this.path.getPreviousNode()) {
				totalPheromone += Math.pow(pheromoneTrail[i], this.alpha);
			}
		}
		
		// if there isn't neighbors with pheromone amount greater than zero, than the ant come back to the last node
		if (totalPheromone == 0.0){
			return this.path.getPreviousNode();
		}

		for (int i = 0; i < pheromoneTrail.length; i++) {
			if (i != this.path.getPreviousNode()) {
				/*				
				double probability = Math.pow(pheromoneTrail[i], this.alpha) / totalPheromone;

				if (probability == 1.0){
					return i;
				}
	*/			
				probabilities[i] = Math.pow(pheromoneTrail[i], this.alpha) / totalPheromone;
			}
		}
		
		if (this.logsOn){
			System.out.println("Probabilidade de movimento: " + Arrays.toString(probabilities));
			System.out.println("Variável aleatória: " + u);			
		}


		for (int i = 0; i < pheromoneTrail.length; i++) {
			if (i != this.path.getPreviousNode()) {
				sumProbabilities += probabilities[i];

				if (u < sumProbabilities) {
					return i;
				}
			}
		}

		return this.path.getCurrentNode();
	}

	public void resetPath() {
		this.path = new Path();
		path.addNode(this.sourceNode);
	}

	public void setModeBackward() {
		this.mode = Mode.BACKWARD;
		this.lastPathSize = this.path.size();
	}

	public void setModeForward() {
		this.mode = Mode.FORWARD;
		this.lastPathSize = 0;
		mode = Mode.FORWARD;
		
	}
}
