package br.ufrj.nce.saco.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import br.ufrj.nce.saco.core.world.World;
import br.ufrj.nce.saco.utils.Constants;

public class Simulator {

	private World world;
	private int simulationTotal = 0;
	private int stepsTotal = 0;
	private int pathsSize = 0;

	public static void main(String[] args) throws Exception {
		Simulator simulator = new Simulator();
		BufferedWriter file = null;
		simulator.setup();

		int[] pathsSize = new int[simulator.getPathsSize()];

		int caminhoMedio = 0;
		long time = 0;

		try {
			time = System.currentTimeMillis();
			file = new BufferedWriter(new FileWriter("resultado.txt"));

			for (int i = 0; i < simulator.getSimulationTotal(); i++) {
				simulator.execute(simulator.stepsTotal);
				int pathSize = simulator.world.getBestPathSize();
				caminhoMedio += pathSize;
				pathsSize[pathSize]++;

				// file.write("Simulação " + i + " - Melhor caminho: " +
				// simulator.world.getBestPath());
				// file.newLine();
				simulator.world.reset();
			}

			file.write("Tempo total: " + (System.currentTimeMillis() - time) + " milisegundos.");
			file.newLine();
			for (int i = 0; i < pathsSize.length; i++) {
				if (i > 0) {
					file.write("Tamanho: " + i + " = " + (double) pathsSize[i] * 100 / simulator.getSimulationTotal() + "%");
					file.newLine();
				}
			}

			file.write("Total de simulacoes: " + simulator.getSimulationTotal());
			file.newLine();
			file.write("Melhor caminho medio: " + (double) caminhoMedio / simulator.getSimulationTotal());
			file.newLine();
			file.write("---------------------------------------------------------------------------------------");
			file.newLine();
			file.newLine();

			System.out.println("\n\nSimulacao concluida! Veja o resultado no arquivo resultado.txt");

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			file.close();
		}
	}

	private int getPathsSize() {
		return this.pathsSize;
	}

	private void execute(int steps) throws Exception {
		for (int j = 0; j < steps; j++) {
			this.world.run();
		}
	}

	private void setup() throws Exception {
		this.world = this.getWorldStdin("Informe o nome do arquivo: ");
		this.world.setAlpha(getDoubleStdin("Digite um valor numerico entre 1 e 2 para alfa: ", 1, 2));
		this.world.setEvaporationRate(getDoubleStdin("Digite um valor numerico entre 0 e 1 para a taxa de evaporacao de feromonio: ", 0, 1));
		this.world.setPheromoneUpdateIsConstant(getBooleanStdin("O quantidade de feromonio depositada e constante? Digite S ou N: "));
		this.world.createAnts(getIntegerStdin("Informe a quantidade de formigas. (Deve ser maior que zero.): ", 0, 1000000));
		this.setStepsTotal(getIntegerStdin("Informe a quantidade de passos de cada formiga: (Deve ser maior que zero): ", 0, 1000000));
		this.setSimulationTotal(getIntegerStdin("Informe a quantidade de simulacoes: (Deve ser maior que zero): ", 0, 1000000));

		this.world.setAllLogsOn(Constants.PRINT_LOGS_ON);
		this.world.setPrintAntTrack(Constants.PRINT_TRACK);
		this.world.setPrintBestPath(Constants.PRINT_BEST_PATH);

	}

	private World getWorldStdin(String label) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		World world = null;

		while (world == null) {
			System.out.print(label);
			input = in.readLine();
			world = createWorld(input);
		}
		return world;
	}

	private World createWorld(String fileName) throws Exception {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			World world = null;
			String line = null;
			String[] fields = null;

			fields = reader.readLine().split(";");

			if (fields.length != 3) {
				return null;

			} else {
				this.pathsSize  = Integer.parseInt(fields[2].trim());
				world = new World(Integer.parseInt(fields[0].trim()), Integer.parseInt(fields[1].trim()), Integer.parseInt(fields[2].trim()));
			}

			while ((line = reader.readLine()) != null) {
				fields = line.split(";");

				if (fields.length != 2) {
					return null;

				} else {
					world.putPheromone(Integer.parseInt(fields[0].trim()), Integer.parseInt(fields[1].trim()));
				}
			}

			return world;

		} catch (Exception e) {
			return null;
		}
	}

	private boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private int getIntegerStdin(String label, double inferiorLimit, double superiorLimit) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		int temp = 0;

		while (input != null) {
			System.out.print(label);
			input = in.readLine().trim();

			if (isInteger(input)) {
				temp = Integer.parseInt(input.trim());

				if (temp >= inferiorLimit && temp <= superiorLimit) {
					input = null;
					return temp;

				} else {
					input = " ";
				}
			} else {
				input = " ";
			}
		}
		return 0;
	}

	private double getDoubleStdin(String label, double inferiorLimit, double superiorLimit) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		double temp = 0;

		while (input != null) {
			System.out.print(label);
			input = in.readLine().trim();

			if (isDouble(input)) {
				temp = Double.parseDouble(input);

				if (temp >= inferiorLimit && temp <= superiorLimit) {
					input = null;
					return temp;

				} else {
					input = " ";
				}
			} else {
				input = " ";
			}
		}
		return 0;
	}

	private boolean getBooleanStdin(String label) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = "";

		while (input != null) {
			System.out.print(label);
			input = in.readLine().trim();

			if (input.toUpperCase().equals("S") || input.toUpperCase().equals("N")) {
				return input.toUpperCase().equals("S");
			} else {
				input = " ";
			}
		}
		return false;
	}

	public int getStepsTotal() {
		return stepsTotal;
	}

	public void setStepsTotal(int stepsTotal) {
		this.stepsTotal = stepsTotal;
	}

	public int getSimulationTotal() {
		return simulationTotal;
	}

	public void setSimulationTotal(int simulationTotal) {
		this.simulationTotal = simulationTotal;
	}
}
