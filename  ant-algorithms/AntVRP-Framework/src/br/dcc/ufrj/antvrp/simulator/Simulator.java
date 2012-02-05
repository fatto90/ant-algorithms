package br.dcc.ufrj.antvrp.simulator;

import br.dcc.ufrj.antvrp.util.Util;

public abstract class Simulator {
	
	private String fileName;
	private final int RODADAS_ESTAVEIS = 100;
	private int numeroSimulacoes = 100;
	private int antAmount = 1000;
	private int rankSize = 20;
	private boolean isDefaultMode = false;
	
	protected void setFileName() throws Exception{
		String fileName = Util.getStringStdin("Informe o dataset: ");
		if (!"0".equals(fileName)){
			this.fileName = fileName; 
		} else{
			this.isDefaultMode = true;
			this.fileName = "C:\\Users\\Fabio\\Desktop\\dataset\\att48.vrp";
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
	
	protected void setTamanhoRank() throws Exception{
		int rankSize = Util.getIntegerStdin("Informe o tamanho do ranking de formigas: ");
		this.rankSize = rankSize; 
	}

	public int getAntAmount() {
		return antAmount;
	}

	public int getRankSize() {
		return rankSize;
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
