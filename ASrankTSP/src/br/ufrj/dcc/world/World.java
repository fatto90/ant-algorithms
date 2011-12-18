package br.ufrj.dcc.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import br.ufrj.dcc.ant.Ant;
import br.ufrj.dcc.util.Pheromone;
import br.ufrj.dcc.util.Step;
import br.ufrj.dcc.util.WorldMap;

public class World {

	private WorldMap worldMap;
	private Pheromone pheromone;
	private Ant[] ants;
	private Ant[] rank;
	private Random rand;
	private int rankSize = 10;
	private Ant bestSoFar;

	public World(WorldMap worldMap) {
		this.worldMap = worldMap;
		ants = new Ant[worldMap.getSize()];
		this.rand = new Random();

		for (int i = 0; i < ants.length; i++) {
			ants[i] = new Ant(i + 1);
		}
	}

	public World(WorldMap worldMap, long seed) {
		this.worldMap = worldMap;
		ants = new Ant[worldMap.getSize()];
		this.rand = new Random(seed);

		for (int i = 0; i < ants.length; i++) {
			ants[i] = new Ant(i + 1);
		}
	}

	public WorldMap getWorldMap() {
		return this.worldMap;
	}

	public void createPheromoneMatrix(double initialValue) {

		if (this.worldMap.getSize() == 0 || this.worldMap == null) {
			throw new IllegalArgumentException("WorldMap has not been initiated.");
		}
		this.pheromone = new Pheromone(initialValue, this.worldMap.getSize());
	}

	public long[] greedySearch(WorldMap worldMap) {
		int[] indexes = new int[worldMap.getSize()];
		long[] results = new long[worldMap.getSize()];
		int i = 1;
		indexes[0] = 1;

		for (int k = 0; k < worldMap.getSize() - 1; k++) {
			results[k] = Long.MAX_VALUE;

			for (int j = 1; j <= worldMap.getSize(); j++) {
				long value = worldMap.getDistance(i, j);

				if (value == 0) {
					continue;
				}

				if (results[k] > value && !containsValue(indexes, j)) {
					results[k] = value;
					indexes[k + 1] = j;
				}
			}
			i = indexes[k + 1];

			if (k == worldMap.getSize() - 2) {
				results[k + 1] = worldMap.getDistance(i, 1);
			}
		}
		return results;
	}

	public long greedyPathSize() {
		int[] indexes = new int[this.worldMap.getSize()];
		long[] results = new long[this.worldMap.getSize()];
		long result = 0;
		int i = 1;
		indexes[0] = 1;

		for (int k = 0; k < this.worldMap.getSize() - 1; k++) {
			results[k] = Long.MAX_VALUE;

			for (int j = 1; j <= this.worldMap.getSize(); j++) {
				long value = this.worldMap.getDistance(i, j);

				if (value == 0) {
					continue;
				}

				if (results[k] > value && !containsValue(indexes, j)) {
					results[k] = value;
					indexes[k + 1] = j;
				}
			}
			result += results[k];
			i = indexes[k + 1];

			if (k == this.worldMap.getSize() - 2) {
				results[k + 1] = this.worldMap.getDistance(i, 1);
				result += this.worldMap.getDistance(i, 1);
			}
		}
		return result;
	}

	public boolean containsValue(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return true;
			}
		}
		return false;
	}

	public void run() {
		tourConstruction();
		updatePheromone();
		System.out.println(bestSoFar.toString());
		resetAnts();
	}

	private void resetAnts() {
		for (int i = 0; i < ants.length; i++) {
			ants[i].getPath().reset();
		}
		System.out.println("Melhor formiga 2: " + bestSoFar.getHomeNode() + " Caminho: " + bestSoFar.getPathLength());
	}

	private void updatePheromone() {
		this.rank = this.ants;
		Arrays.sort(rank);

		pheromone.evaporate();

		for (int i = 0; i < rankSize - 1; i++) {
			ArrayList<Step> steps = ants[i].getPath().getSteps();

			for (Iterator<Step> iterator = steps.iterator(); iterator.hasNext();) {
				Step step = iterator.next();
				this.pheromone.addValue(step.getSource(), step.getDestination(), ants[i].dropPheromone() * (rankSize - i));
			}
		}

		ArrayList<Step> steps = bestSoFar.getPath().getSteps();

		for (Iterator<Step> iterator = steps.iterator(); iterator.hasNext();) {
			Step step = iterator.next();
			this.pheromone.addValue(step.getSource(), step.getDestination(), bestSoFar.dropPheromone() * rankSize);
		}
	}

	private void tourConstruction() {
		for (int i = 1; i < worldMap.getSize(); i++) {
			for (int j = 0; j < ants.length; j++) {
				long distances[] = worldMap.getDistances(ants[j].getCurrentNode());
				double pheromones[] = pheromone.getPheromones(j);
				double sample = rand.nextDouble();
				ants[j].move(distances, pheromones, sample);

				if (i == ants.length - 1) {
					int homeNode = ants[j].getHomeNode();
					int currentNode = ants[j].getCurrentNode();
					long distance = worldMap.getDistance(currentNode, homeNode);
					ants[j].moveHomeNode(distance);

					if (ants[j].getPath().size() != worldMap.getSize() + 1) {
						throw new RuntimeException("Erro no caminho da formiga " + (j) + " Caminho de tamanho diferente da quantidade de cidades no mundo " + ants[j].getPath().size());
					}
					if (bestSoFar == null || bestSoFar.getPathLength() > ants[j].getPathLength()) {
						bestSoFar = ants[j];
					}
				}
			}
		}
	}
}
