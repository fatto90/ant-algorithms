package br.dcc.ufrj.antvrp.util;

import br.dcc.ufrj.antvrp.world.City;

public class Util {

	public static String trim(String string) {
		return string == null ? "" : string.trim();
	}
	
	public static double hypot(City city, City neighbor){
		double lon = city.getLon() - neighbor.getLon();
		double lat = city.getLat() - neighbor.getLat();
		return Math.hypot(lon, lat);
	}
	
	
}
