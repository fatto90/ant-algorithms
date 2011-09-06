package br.ufrj.nce.saco.core.ant;

import br.ufrj.nce.saco.core.path.Path;

/**
 * @author Fabio Barbosa
 * This class represents a single ant used in Simple-ACO algorithm
 *
 */
public class SingleAnt {

	private int lastPathSize;
	private int alpha = 2;
	private Mode mode;
	private Path path = new Path();
	

	public enum Mode {
		FORWARD, BACKWARD
	};

	public SingleAnt(int startNode) {
		this.mode = Mode.FORWARD;
		this.path.addNode(startNode);
		//this.id = id;
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
		return this.mode == Mode.BACKWARD? 1.0 / (double) lastPathSize: 0.0; 
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

		if (this.mode == Mode.BACKWARD) {
			if (path.size() > 1) {
				path.discard();
			}
		} else {
			int temp = findNextNode(pheromoneTrail, u);
			this.path.addNode(temp);
		}
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
				probabilities[i] = Math.pow(pheromoneTrail[i], this.alpha) / totalPheromone;
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
