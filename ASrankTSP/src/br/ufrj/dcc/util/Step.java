package br.ufrj.dcc.util;

public class Step {
	
	private int source;
	private int destination;
	private long distance;
	
	
	public Step(int source, int destination, long distance) {
		this.source = source;
		this.destination = destination;
		this.distance = distance;
	}

	public int getSource() {
		return source;
	}

	public int getDestination() {
		return destination;
	}
	
	public long distance(){
		return distance;
	}
	
	@Override
	public String toString() {		
		return "|" + source + ", " + destination + "| = " + distance;
	}
	
}
