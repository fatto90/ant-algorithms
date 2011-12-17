package br.ufrj.dcc.world;

import java.util.ArrayList;

public class Path {
	
	private ArrayList<Integer> path = new ArrayList<Integer>();
	private int size = 0;
	private long length = 0;
	
	
	public long getLength() {
		return length;
	}

	public void addNode(int node, long length){
		this.path.add(node);
		this.size += node;
		this.length += length;
	}
	
	public void addNode(int node){
		this.path.add(node);
		this.size += node;
	}

	public void getNode(int index){
		this.path.get(index);
	}

	public int getLastNode(){
		return this.path.get(path.size());
	}
	
	public int removeLastNode(){
		return this.path.remove(path.size());
	}
	
	public boolean containsNode(int node){
		return path.contains(node);
	}
	
	public void reset(){
		this.path = new ArrayList<Integer>();
		this.size = 0;
		this.length = 0;
	}
}
