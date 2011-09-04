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
		int caminhoMedio = 0;
		long time = 0;

		try {
			time = System.currentTimeMillis();
			
			for (int i = 0; i < 100; i++) {
				world = new World();		
				world.initialize(Constants.ANTS_TOTAL);
				
				for (int j = 0; j < Constants.INTERATIONS_TOTAL; j++) {
					world.run();
				}
				
				int caminho = world.getBestPathSize();
				caminhoMedio += caminho;
				
				if (caminho == 5){
					caminho5++;

				} else if(caminho == 6){
					caminho6++;
					
				} else if(caminho == 7){
					caminho7++;
					
				} else if(caminho == 8){
					caminho8++;
				}
				
				//world.worldPrint();
			}
			
			System.out.println("Tempo total: " + (System.currentTimeMillis() - time) + " milisegundos.");

			System.out.println("Caminho5: " + (double) caminho5 / 100);
			System.out.println("Caminho6: " + (double) caminho6 / 100);
			System.out.println("Caminho7: " + (double) caminho7 / 100);
			System.out.println("Caminho8: " + (double) caminho8 / 100);
			
			System.out.println("Total: " + (caminho5 + caminho6 + caminho7 + caminho8));
			System.out.println("Caminho médio: " + (double) caminhoMedio / 100);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
