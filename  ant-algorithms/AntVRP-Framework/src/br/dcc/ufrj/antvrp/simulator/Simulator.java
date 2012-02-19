package br.dcc.ufrj.antvrp.simulator;

import br.dcc.ufrj.antvrp.util.Util;

public abstract class Simulator {
	
	protected String fileName = "/Users/fabioaab/Dropbox/fabio/datasets/CVRP/att-n48-k4.vrp";
	protected final int RODADAS_ESTAVEIS = 100;
	protected int numeroSimulacoes = 100;
	protected int antAmount = 1000;
	protected boolean isDefaultMode = false;
	
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
