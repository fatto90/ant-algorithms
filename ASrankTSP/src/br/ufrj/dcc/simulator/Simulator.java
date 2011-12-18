package br.ufrj.dcc.simulator;

import java.io.IOException;

import br.ufrj.dcc.util.AntUtils;
import br.ufrj.dcc.util.WorldMap;
import br.ufrj.dcc.world.World;

public class Simulator {
	
	public static void main(String[] args) throws IOException {
		WorldMap map = AntUtils.loadDistances("C:\\Users\\Fabio\\Desktop\\brazil58.tsp"); 
		World world = new World(map, 5);
		long pathSize = world.greedyPathSize();
		System.out.println("Busca Gulosa " + pathSize );
		world.createPheromoneMatrix(pathSize);
		
		for(int i = 0; i < 100; i++){
			world.run();
			
		}
	}
}
