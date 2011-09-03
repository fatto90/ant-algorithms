package br.ufrj.nce.saco.main;

import br.ufrj.nce.saco.core.WorldHandler;
import br.ufrj.nce.saco.utils.Constants;

public class Simulator {

	public static void main(String[] args) {
		WorldHandler handler = new WorldHandler();

		handler.initialize(Constants.ANTS_TOTAL);

		try {
			for (int i = 0; i < Constants.INTERATIONS_TOTAL; i++) {
				handler.run(i);
			}
			
			System.out.println("Melhor caminho: " + handler.getBestPath());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
