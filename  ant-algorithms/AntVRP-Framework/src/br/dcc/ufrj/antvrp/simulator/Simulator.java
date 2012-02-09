package br.dcc.ufrj.antvrp.simulator;

import br.dcc.ufrj.antvrp.util.Util;

public abstract class Simulator {
	
	private String fileName = "C:\\Users\\Fabio\\Desktop\\dataset\\att48.vrp";
	private final int RODADAS_ESTAVEIS = 100;
	private int numeroSimulacoes = 100;
	private int antAmount = 1000;
	private boolean isDefaultMode = false;
	
	protected void setFileName() throws Exception{
		String fileName = Util.getStringStdin("Informe o dataset: ");
		if (!"0".equals(fileName)){
			this.fileName = fileName; 
		} else{
			this.isDefaultMode = true;
		}
	}
	
	protected void setNumeroSimulacoes() throws Exception{
		int numeroSimulacoes = Util.getIntegerStdin("Informe o número de resultados desejados: ");
		this.numeroSimulacoes = numeroSimulacoes; 
	}
	
	protected void setQuantidadeFormigas() throws Exception{
		int antAmount = Util.getIntegerStdin("Informe a quantidade de formigas: ");
		this.antAmount = antAmount ; 
	}
	
	public int getAntAmount() {
		return antAmount;
	}

	public int getRodadasEstaveis() {
		return RODADAS_ESTAVEIS;
	}

	public int getNumeroSimulacoes() {
		return numeroSimulacoes;
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isDefaultMode() {
		return isDefaultMode;
	}

}
