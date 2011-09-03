package br.ufrj.nce.saco.main;

import br.ufrj.nce.saco.core.WorldHandler;

public class Simulator {

	public static void main(String[] args) {
		WorldHandler handler = new WorldHandler(2003453123);

		handler.initialize(1);

		try {
			for (int i = 0; i < 20; i++) {
				System.out.println("Iteração " + i);
				handler.run(i);
				// handler.worldPrint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
