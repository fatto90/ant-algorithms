package br.ufrj.nce.saco.main;

import br.ufrj.nce.saco.core.world.World;
import br.ufrj.nce.saco.utils.Constants;

public class Simulator {

	public static void main(String[] args) throws Exception {
		World world;
		int caminho5 = 0;
		int caminho6 = 0;
		int caminho7 = 0;
		int caminho8 = 0;
		int caminho9 = 0;
		int caminho10 = 0;
		int caminho11 = 0;
		int caminho12 = 0;
		
		int caminhoMedio = 0;
		long time = 0;

		try {
			time = System.currentTimeMillis();
			
			for (int i = 0; i < Constants.SIMULATIONS; i++) {
				world = new World();		
				world.initialize(Constants.ANTS);
				
				for (int j = 0; j < Constants.STEPS_1; j++) {
					world.run();
				}
				
				for (int j = 0; j < Constants.STEPS_2; j++) {
					world.removePheromone();
					world.run();
				}
				for (int j = 0; j < Constants.STEPS_2; j++) {
					world.addPheromone();
					world.run();
				}
				
				int caminho = world.countBestPath();
				caminhoMedio += caminho;
				
				if (caminho == 5){
					caminho5++;

				} else if(caminho == 6){
					caminho6++;
					
				} else if(caminho == 7){
					caminho7++;
					
				} else if(caminho == 8){
					caminho8++;
					
				} else if(caminho == 9){
					caminho9++;
					
				} else if(caminho == 10){
					caminho10++;
					
				} else if(caminho == 11){
					caminho11++;
					
				} else if(caminho == 12){
					caminho12++;
				}
				
				//world.worldPrint();
				if (Constants.DISPLAY_BEST_PATH){
					System.out.println("Melhor caminho: " + world.chooseBestPath());
				}
			}
			
			System.out.println("Tempo total: " + (System.currentTimeMillis() - time) + " milisegundos.");

			System.out.println("Caminho5: " + (double) caminho5 / Constants.SIMULATIONS);
			System.out.println("Caminho6: " + (double) caminho6 / Constants.SIMULATIONS);
			System.out.println("Caminho7: " + (double) caminho7 / Constants.SIMULATIONS);
			System.out.println("Caminho8: " + (double) caminho8 / Constants.SIMULATIONS);
			System.out.println("Caminho9: " + (double) caminho9 / Constants.SIMULATIONS);
			System.out.println("Caminho10: " + (double) caminho10 / Constants.SIMULATIONS);
			System.out.println("Caminho11: " + (double) caminho11 / Constants.SIMULATIONS);
			System.out.println("Caminho12: " + (double) caminho12 / Constants.SIMULATIONS);
			
			System.out.println("Total: " + (caminho5 + caminho6 + caminho7 + caminho8 + caminho9 + caminho10 + caminho11 + caminho12));
			System.out.println("Melhor caminho médio: " + (double) caminhoMedio / Constants.SIMULATIONS);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
