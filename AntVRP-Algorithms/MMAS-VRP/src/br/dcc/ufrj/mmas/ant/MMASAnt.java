package br.dcc.ufrj.mmas.ant;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.world.Customer;

public class MMASAnt extends Ant {

	public MMASAnt(int id, Customer firstCustomer, int capacity, int dimension) {
		super(id, firstCustomer, capacity, dimension);
	}

	@Override
	public Customer chooseNextMove(Customer customer, double sample) {
		Customer candidate = null;
		double acumulator = 0;
		double sum = 0;
		int size = customer.getListCandidateSize() / 4;
		int iterations = 0;

		for (int i = 0, j = 0; j < size && i < customer.getListCandidateSize(); i++) {
			candidate = customer.getCandidate(i);
			if (!this.getTour().contains(candidate)) {
				sum += candidate.getAtractivity();
				j++;
			}
			iterations++;
		}

		for (int i = 0; i < iterations; i++) {
			candidate = customer.getCandidate(i);

			if (!this.getTour().contains(candidate)) {
				acumulator += candidate.getAtractivity() / sum ;

				if (acumulator > sample) {
					if (this.currentCapacity - candidate.getDemand() >= 0) {
						return candidate;
					} else {
						currentCapacity = this.totalCapacity;
						return this.getFirstCustomer();
					}
				}
			}
		}
		return null;
	}

	@Override
	public double dropPheromone() {
		return 1 / this.getTour().getDistance();
	}

}
