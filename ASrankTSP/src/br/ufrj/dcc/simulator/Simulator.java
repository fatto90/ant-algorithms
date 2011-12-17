package br.ufrj.dcc.simulator;

import java.io.IOException;

import br.ufrj.dcc.util.AntUtils;
import br.ufrj.dcc.world.World;
import br.ufrj.dcc.world.WorldMap;

public class Simulator {
	
	public static void main(String[] args) throws IOException {
		WorldMap map = AntUtils.loadDistances("C:\\Users\\Fabio\\Desktop\\brazil58.tsp"); 
		World world = new World(map);
		long mapSize = world.greedyPathSize();
		world.createPheromoneMatrix(mapSize);
		world.run();
	}
}
