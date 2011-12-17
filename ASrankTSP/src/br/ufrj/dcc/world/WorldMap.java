package br.ufrj.dcc.world;

import java.io.IOException;

import br.ufrj.dcc.util.AntUtils;

public class WorldMap {
	
	private long[][] distances;
	
	public WorldMap(long[][] distances) {
		this.distances = distances;
	}
	
	public int getSize(){
		return this.distances.length;
	}
	
	public long getDistance(int i, int j){
		int x = Math.min(i, j) - 1;
		int y = this.distances.length - Math.max(i, j);
		return this.distances[x][y];
	}

	public long[] getDistances(int i) {
		long[] distancesFromNodeI = new long[distances.length];
		
		for (int j = 0; j < distances.length; j++) {
			int x = Math.min(i, j);
			int y = this.distances.length - Math.max(i, j) - 1;
			distancesFromNodeI[j] = this.distances[x][y];
		}
		
		return distancesFromNodeI;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		WorldMap map = AntUtils.loadDistances("C:\\Users\\Fabio\\Desktop\\brazil58.tsp"); 
		map.getDistances(54);
		
		
		
	}
}
