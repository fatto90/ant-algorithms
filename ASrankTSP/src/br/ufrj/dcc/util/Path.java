package br.ufrj.dcc.util;

import java.util.ArrayList;
import java.util.Arrays;

public class Path {
	
	private ArrayList<Integer> path = new ArrayList<Integer>();
	private ArrayList<Step> steps = new ArrayList<Step>();
	
	private int size = 0;
	private long length = 0;
	private int startNode = 0;
	
	public Path(int startNode) {
		this.startNode = startNode;
		path.add(startNode);
	}
	
	public int getStartNode(){
		return this.startNode;
	}
	
	public long getLength() {
		return length;
	}

	public void addNode(int node, long length){
		int source = this.getLastNode();
		steps.add(new Step(source, node, length));
		this.path.add(node);
		this.size += node;
		this.length += length;
	}
	
	public void getNode(int index){
		this.path.get(index);
	}

	public int getLastNode(){
		return this.path.get(path.size() - 1);
	}
	
	public int removeLastNode(){
		return this.path.remove(path.size());
	}
	
	public boolean containsNode(int node){
		return path.contains(node);
	}
	
	public void reset(){
		this.path = new ArrayList<Integer>();
		this.steps = new ArrayList<Step>();
		this.path.add(this.startNode);
		this.size = 0;
		this.length = 0;
	}
	
	public String toString(){		
		return Arrays.toString(this.path.toArray());
	}
	
	public int size(){
		return path.size();
	}
	
	public ArrayList<Step> getSteps(){
		return this.steps;
	}
}
