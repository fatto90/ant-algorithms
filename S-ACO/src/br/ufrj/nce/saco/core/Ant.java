package br.ufrj.nce.saco.core;

import br.ufrj.nce.saco.utils.Constants;

public class Ant {

	private int sourceNode;
	private int destinationNode;
	private int lastPathSize;

	private Mode mode;

	public enum Mode {
		FORWARD, BACKWARD
	};

	private Path path = new Path();

	public Ant(int sourceNode, int destinationNode) {
		this.mode = Mode.FORWARD;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.path.addNode(sourceNode);
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

	public void switchMode() {
		this.mode = (this.mode == Mode.FORWARD ? Mode.BACKWARD : Mode.FORWARD);
	}

	public String getPath() {
		return this.path.toString();
	}

	public void move(double[] pheromoneTrail, double u) throws Exception {

		if (this.mode == Mode.BACKWARD) {
			if (path.size() > 1) {
				path.discard();
			}
		} else {
			int temp = findNextNode(pheromoneTrail, u);
			this.path.addNode(temp);
		}

		if (path.getPreviousNode() == this.sourceNode) {
			this.lastPathSize = 0;
		}
		
		if (path.getCurrentNode() == this.sourceNode && this.path.size() == 1) {
			this.switchMode();
			this.path.reset();
			this.path.addNode(this.sourceNode);
		}

		if (path.getCurrentNode() == this.destinationNode) {
			this.switchMode();
			this.path.removeLoops();
			this.lastPathSize = this.path.size();
		}
	}

	public double getPheromoneAmount() {
		
	double amount = 0; 
		
		if (lastPathSize > 0){
			amount = 1.0 / (double) lastPathSize;
			System.out.println("L: " + lastPathSize + " - Pheromone: " + amount);
		}
		return amount;
	}

	private int findNextNode(double[] pheromoneTrail, double u) {
		double totalPheromone = 0;
		double probabilities[] = new double[pheromoneTrail.length];
		double sumProbabilities = 0;

		for (int i = 0; i < pheromoneTrail.length; i++) {
			if (i != this.path.getPreviousNode()) {
				totalPheromone += Math.pow(pheromoneTrail[i], Constants.ALPHA);
			}
		}

		for (int i = 0; i < pheromoneTrail.length; i++) {
			if (i != this.path.getPreviousNode()) {
				probabilities[i] = Math.pow(pheromoneTrail[i], Constants.ALPHA) / totalPheromone;
			}
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
}
