package br.dcc.ufrj.antvrp.util;

import br.dcc.ufrj.antvrp.world.Customer;

public class Step implements Comparable<Step> {
	
	private Customer source;
	private Customer destination;
	private double distance;
	
	public Step(Customer source, Customer destination) {
		this.source = source;
		this.destination = destination;
		this.distance = source.getDistance(destination);
	}

	public Customer getSource() {
		return source;
	}

	public Customer getDestination() {
		return destination;
	}

	public double getDistance() {
		return distance;
	}

	@Override
	public int compareTo(Step o) {
		return this.distance - o.getDistance() < 0? -1: 1;
	}
	
}
