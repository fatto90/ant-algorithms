package br.ufrj.dcc.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AntUtils {
	/**
	 * This method should be used to read the file which contains the heuristic
	 * information
	 * 
	 * @throws IOException
	 * */
	public static long[][] loadDistances(String folderPath, String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = null;
		int dimension = 0;
		long [][]distances = null;
		boolean readDistancesEnabled = false;
		int i = 0;

		while ((line = reader.readLine()) != null) {
			if (line.trim().startsWith("DIMENSION: ")) {
				dimension = Integer.parseInt(line.trim().split(": ")[1]);
				distances = new long[dimension][dimension];
			}
			
			if ("EDGE_WEIGHT_SECTION".equals(line.trim())){
				readDistancesEnabled = true;
			}
			
			if(readDistancesEnabled){
				String values[] = line.trim().split(" ");
				
				for (int j = 0; j < values.length; j++){
					distances[i][j] = Long.parseLong(values[j]);
				}
				i++;
			}
		}

		return distances;
	}
}
