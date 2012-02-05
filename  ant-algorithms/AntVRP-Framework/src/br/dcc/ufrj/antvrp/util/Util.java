package br.dcc.ufrj.antvrp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import br.dcc.ufrj.antvrp.world.Customer;

public class Util {

	public static String trim(String string) {
		return string == null ? "" : string.trim();
	}
	
	public static boolean empty(String string) {
		return string == null || "".equals(string);
	}

	public static double hypot(Customer city, Customer neighbor) {
		double lon = city.getLon() - neighbor.getLon();
		double lat = city.getLat() - neighbor.getLat();
		return Math.hypot(lon, lat);
	}

	private static boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public static int getIntegerStdin(String label, double inferiorLimit, double superiorLimit) throws Exception {
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
	
	public static int getIntegerStdin(String label) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		int temp = 0;

		while (empty(input)) {
			System.out.print(label);
			input = trim(in.readLine());

			if (isInteger(input)) {
				temp = Integer.parseInt(input);
				input = null;
				return temp;

			} else {
				input = null;
			}
		}
		return -1;
	}

	public static double getDoubleStdin(String label, double inferiorLimit, double superiorLimit) throws Exception {
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

	public static boolean getBooleanStdin(String label) throws Exception {
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
	
	public static String getStringStdin(String label) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		
		while (empty(input)) {
			System.out.print(label);
			input = trim(in.readLine());
		}
		return input;
	}

}
