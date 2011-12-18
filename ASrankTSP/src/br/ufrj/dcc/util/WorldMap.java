package br.ufrj.dcc.util;

import java.io.IOException;
import java.util.Random;


public class WorldMap {

	private long[][] distances;

	public WorldMap(long[][] distances) {
		this.distances = distances;
	}

	public int getSize() {
		return this.distances.length;
	}

	public long getDistance(int i, int j) {
		int x = Math.max(Math.min(i, j) - 1, 0);
		int y = this.distances.length - Math.max(i, j);
		return this.distances[x][y];
	}

	public long[] getDistances(int x) {
		
		long[] distancesAux = new long[this.distances.length];
		int jaux = 0;
		for (int j = 0; j < this.distances.length; j++) {
			
			if (j < x){
				distancesAux[j] = this.distances[j][this.distances.length - x];
				jaux = j;
				
			}else {
				distancesAux[j] = this.distances[jaux][this.distances.length - j - 1];
			}
		}
		
		return distancesAux;
	}

	public static void main(String[] args) throws IOException {

		WorldMap map = AntUtils.loadDistances("C:\\Users\\Fabio\\Desktop\\brazil58.tsp");
		Random random = new Random(10);

		for (int i = 0; i < 10; i++) {
			int a = random.nextInt(56) + 1;
			int b = random.nextInt(56) + 1;
			long distance = map.getDistance(a, b);

			System.out.println("Distancia entre " + a + " e " + b + " = " + distance);
		}
	}
}
