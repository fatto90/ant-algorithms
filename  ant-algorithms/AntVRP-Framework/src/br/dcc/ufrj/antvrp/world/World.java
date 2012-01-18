package br.dcc.ufrj.antvrp.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.exception.IllegalArgumentWorldException;
import br.dcc.ufrj.antvrp.pheromone.Pheromone;
import br.dcc.ufrj.antvrp.util.Util;

public abstract class World {
	
	private int dimension;
	private int capacity;
	private int[] demands;
	protected ArrayList<City> cities;
	private String name;
	private String comment;
	private String type;
	private String edgeWeightType;
	
	private Random random;

	protected ArrayList<Ant> ants;
	protected Pheromone pheromone; 
	
	private static final String TAG_NAME = "NAME";
	private static final String TAG_COMMENT = "COMMENT";
	private static final String TAG_TYPE = "TYPE";
	private static final String TAG_DIMENSION = "DIMENSION";
	private static final String TAG_EDGE_WEIGHT_TYPE = "EDGE_WEIGHT_TYPE";
	private static final String TAG_CAPACITY = "CAPACITY";
	private static final String TAG_NODE_COORD_SECTION = "NODE_COORD_SECTION";
	private static final String TAG_DEMAND_SECTION = "DEMAND_SECTION";
	private static final String TAG_DEPOT_SECTION = "DEPOT_SECTION";
	private static final String TAG_EOF = "EOF";
	
	public World(){
		this.random = new Random();
	}
	
	public World(long seed){
		this.random = new Random(seed);
	}
	
	protected abstract void createAnts(int total);
	protected abstract void tourConstruction();
	protected abstract void pheromoneUpdate();
	protected abstract void setup();
	protected abstract double getInitialPathSize();
	protected abstract void computeHeuristics();
	
	
	public void createWorld(String path) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		
		this.name = this.getName(reader);
		this.comment = this.getComment(reader);
		this.type = this.getType(reader);
		this.dimension = this.getDimension(reader);
		this.edgeWeightType = this.getEdgeWeightType(reader);
		this.capacity = this.getCapacity(reader);
		this.passNodeCoordSection(reader);
		this.cities = this.getCities(reader, this.dimension);
		this.passDemandSection(reader);
		this.getDemands(reader, this.cities);
		this.passDepotSection(reader);
		getDepots(reader);
		this.eof(reader);		
		reader.close();
		this.computeDistances();
		this.computeHeuristics();
		this.computeBetaHeuristics();
	}

	private void computeBetaHeuristics() {
		
		for(City city: this.cities){
			
			for(City neighbor: city.getNeighbors()){
				neighbor.setBetaHeuristic(Math.pow(neighbor.getHeuristic(), neighbor.getBeta()));
			}
			
		}
		
	}

	public void run(){
		this.tourConstruction();
		this.pheromoneUpdate();
		this.setup();
	}	
	
	public void createPheromones(double initialValue){
		double alphaPheromone = 0;
		for (City city : this.cities){
			for (City neighbor : city.getNeighbors()){
				neighbor.setPheromone(initialValue);				
				alphaPheromone = Math.pow(initialValue, neighbor.getAlpha());				
				neighbor.setAlphaPheromone(alphaPheromone);
			}
		}
	}
	
	private void computeDistances() {
		City city = null;
		City cityTemp = null;
		
		for (int i = 0; i < this.cities.size(); i++) {
			city = cities.get(i);
			
			for (int j = 0; j < this.dimension; j++) {
				cityTemp = cities.get(j);
				city.addNeigbour(cityTemp);
				
			}
		}
	}

	private String getName(BufferedReader reader) throws Exception{
		String[] values = reader.readLine().split(":");
		
		if (values != null && TAG_NAME.equals(Util.trim(values[0]))){
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_NAME);
		}
	}
	
	private String getComment(BufferedReader reader) throws Exception{
		String[] values = reader.readLine().split(":");
		
		if (values != null && TAG_COMMENT.equals(Util.trim(values[0]))){
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_COMMENT);
		}
	}

	private String getType(BufferedReader reader) throws Exception{

		String[] values = reader.readLine().split(":");
		
		if (values != null && TAG_TYPE.equals(Util.trim(values[0]))){
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_TYPE);
		}
	}
	
	private int getDimension(BufferedReader reader) throws Exception{
		String[] values = reader.readLine().split(":");
		String dimension = null;
		
		if (values != null && TAG_DIMENSION.equals(Util.trim(values[0]))){
			dimension = Util.trim(values[1]);
			
			if (dimension == null || "".equals(dimension)){
				throw new IllegalArgumentWorldException(TAG_DIMENSION);
			}
			
			return Integer.parseInt(dimension);
			
		} else {
			throw new IllegalArgumentWorldException(TAG_DIMENSION);
		}
	}

	private String getEdgeWeightType(BufferedReader reader) throws Exception{
		String[] values = reader.readLine().split(":");
		
		if (values != null && TAG_EDGE_WEIGHT_TYPE.equals(Util.trim(values[0]))){
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_EDGE_WEIGHT_TYPE);
		}
	}
	
	private int getCapacity(BufferedReader reader) throws Exception{
		String[] values = reader.readLine().split(":");
		String capacity = null;
		
		if (values != null && TAG_CAPACITY.equals(Util.trim(values[0]))){
			capacity = Util.trim(values[1]);
			
			if (capacity == null || "".equals(capacity)){
				throw new IllegalArgumentWorldException(TAG_CAPACITY);
			}
			
			return Integer.parseInt(capacity);
			
		} else {
			throw new IllegalArgumentWorldException(TAG_CAPACITY);
		}
	}
	
	private void passNodeCoordSection(BufferedReader reader) throws Exception {
		String value = reader.readLine();
		
		if (!TAG_NODE_COORD_SECTION.equals(Util.trim(value))){
			throw new IllegalArgumentWorldException(TAG_NODE_COORD_SECTION);
		}
	}
	
	private ArrayList<City> getCities(BufferedReader reader, int dimension) throws Exception{
		ArrayList<City> cities = new ArrayList<City>();
		int lat = 0;
		int lon = 0;
		int id = 0;
		City city = null;
		
		for (int i = 0; i < dimension; i++) {
			
			String[] values = reader.readLine().split(" ");
			
			if (values != null && values.length == 3){
				
				id = Integer.parseInt(Util.trim(values[0]));
				lat = Integer.parseInt(Util.trim(values[1]));
				lon = Integer.parseInt(Util.trim(values[2]));
				city = new City(id, lat, lon);
				
				cities.add(city);
				
			} else {
				throw new IllegalArgumentWorldException(TAG_NODE_COORD_SECTION);
			}
			
		}
		
		return cities;
	}

	private void passDemandSection(BufferedReader reader) throws Exception {
		String value = reader.readLine();
		
		if (!TAG_DEMAND_SECTION.equals(Util.trim(value))){
			throw new IllegalArgumentWorldException(TAG_DEMAND_SECTION);
		}
	}

	private void getDemands(BufferedReader reader, ArrayList<City> cities) throws Exception {
		String[] values = null;
		int id = 0;
		int demand = 0;
		
		for (City city : cities) {			
			values = reader.readLine().split(" ");
			
			if (values != null && values.length == 2){				
				id = Integer.parseInt(Util.trim(values[0]));
				demand = Integer.parseInt(Util.trim(values[1]));
				
				if (id == city.getId()){
					city.setDemand(demand);		
					
				} else {
					throw new IllegalArgumentWorldException(TAG_DEMAND_SECTION);
				}
				
			} else {
				throw new IllegalArgumentWorldException(TAG_DEMAND_SECTION);
			}
		}
	}
	
	private void passDepotSection(BufferedReader reader) throws Exception {
		String value = reader.readLine();
		
		if (!TAG_DEPOT_SECTION.equals(Util.trim(value))){
			throw new IllegalArgumentWorldException(TAG_DEPOT_SECTION);
		}
	}

	private void getDepots(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(" ");
		int id = 0;
		
		for (int i = 0; !"-1".equals(Util.trim(values[1])) ; i++) {
			
			id = Integer.parseInt(Util.trim(values[1]));
			
			if (values != null && values.length == 2){
				for (City city : this.cities) {
					if (city.getId() == id){
						city.setDepot(true);
						break;
					}
				}
				
			} else {
				throw new IllegalArgumentWorldException(TAG_DEPOT_SECTION);
			}
			values = reader.readLine().split(" ");
		}
	}

	private void eof(BufferedReader reader) throws Exception {
		String value = reader.readLine();
		
		if (!TAG_EOF.equals(Util.trim(value))){
			throw new IllegalArgumentWorldException(TAG_EOF);
		}
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public String getType() {
		return type;
	}

	public int getDimension() {
		return dimension;
	}

	public String getEdgeWeightType() {
		return edgeWeightType;
	}

	public int getCapacity() {
		return capacity;
	}

	public ArrayList<City> getCities() {
		return cities;
	}

	public int[] getDemands() {
		return demands;
	}

	public City getFirstDepot() {
		
		for(City city : this.cities){
			if(city.isDepot()){
				return city;
			}
		}
		
		return null;
	}
	
	public void setSeed(long seed){
		this.random = new Random(seed);
	}
	
	public double getSampleDouble(){
		return random.nextDouble();
	}

	public long getSampleLong(){
		return random.nextLong();
	}
	
	public int getSampleInt(){
		return random.nextInt();
	}
}
