package br.ufrj.nce.saco.main;

import br.ufrj.nce.saco.core.WorldHandler;

public class Simulator {
	
	public static void main(String[] args) {
		WorldHandler handler = new WorldHandler(10);
		
		handler.initialize(10);
		
		try {
			
			for (int i = 0; i < 2000; i++) {
				if(i == 10){
					System.out.println("");
				}
				handler.run(i);
				
				System.out.println("Iteração " + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
