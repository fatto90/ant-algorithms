package br.dcc.ufrj.antvrp.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import br.dcc.ufrj.antvrp.ant.Ant;
import br.dcc.ufrj.antvrp.exception.IllegalArgumentWorldException;
import br.dcc.ufrj.antvrp.pheromone.Pheromone;
import br.dcc.ufrj.antvrp.util.Tour;
import br.dcc.ufrj.antvrp.util.Util;

public abstract class World {

	private int dimension;
	private int capacity;
	private int[] demands;
	private long seed;

	private String name;
	private String comment;
	private String type;
	private String edgeWeightType;

	protected ArrayList<Ant> ants;
	protected ArrayList<Customer> cities;
	private Tour bestTour;

	protected Pheromone pheromone;
	private Random random;

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

	protected abstract void createAnts(int total);

	protected abstract void tourConstruction() throws Exception;

	protected abstract void pheromoneUpdate();

	protected abstract double getInitialTourSize();

	protected abstract void computeHeuristics();

	public World() {
		this.seed = new Random().nextLong();
		this.random = new Random(this.seed);
	}

	public World(long seed) {
		this.seed = seed;
		this.random = new Random(this.seed);
	}

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
	}

	protected void addPheromone(Customer a, Customer b, double pheromone) {
		Customer na = a.getNeighbor(b.getId());
		na.setPheromone(pheromone + na.getPheromone());

		Customer nb = b.getNeighbor(a.getId());
		nb.setPheromone(pheromone + nb.getPheromone());
	}

	public void run() throws Exception {
		this.tourConstruction();
		this.pheromoneUpdate();
	}

	public void createPheromones(double initialValue) {
		for (Customer city : this.cities) {
			for (Customer neighbor : city.getListCandidates()) {
				neighbor.setPheromone(initialValue);
			}
		}
	}

	private void computeDistances() {
		for (Customer city : this.cities) {
			for (Customer neighbor : this.cities) {
				city.addNeigbour(neighbor);				
			}
			city.createVectors();
		}
	}

	private String getName(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(":");

		if (values != null && TAG_NAME.equals(Util.trim(values[0]))) {
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_NAME);
		}
	}

	private String getComment(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(":");

		if (values != null && TAG_COMMENT.equals(Util.trim(values[0]))) {
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_COMMENT);
		}
	}

	private String getType(BufferedReader reader) throws Exception {

		String[] values = reader.readLine().split(":");

		if (values != null && TAG_TYPE.equals(Util.trim(values[0]))) {
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_TYPE);
		}
	}

	private int getDimension(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(":");
		String dimension = null;

		if (values != null && TAG_DIMENSION.equals(Util.trim(values[0]))) {
			dimension = Util.trim(values[1]);

			if (dimension == null || "".equals(dimension)) {
				throw new IllegalArgumentWorldException(TAG_DIMENSION);
			}

			return Integer.parseInt(dimension);

		} else {
			throw new IllegalArgumentWorldException(TAG_DIMENSION);
		}
	}

	private String getEdgeWeightType(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(":");

		if (values != null && TAG_EDGE_WEIGHT_TYPE.equals(Util.trim(values[0]))) {
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_EDGE_WEIGHT_TYPE);
		}
	}

	private int getCapacity(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(":");
		String capacity = null;

		if (values != null && TAG_CAPACITY.equals(Util.trim(values[0]))) {
			capacity = Util.trim(values[1]);

			if (capacity == null || "".equals(capacity)) {
				throw new IllegalArgumentWorldException(TAG_CAPACITY);
			}

			return Integer.parseInt(capacity);

		} else {
			throw new IllegalArgumentWorldException(TAG_CAPACITY);
		}
	}

	private void passNodeCoordSection(BufferedReader reader) throws Exception {
		String value = reader.readLine();

		if (!TAG_NODE_COORD_SECTION.equals(Util.trim(value))) {
			throw new IllegalArgumentWorldException(TAG_NODE_COORD_SECTION);
		}
	}

	private ArrayList<Customer> getCities(BufferedReader reader, int dimension) throws Exception {
		ArrayList<Customer> cities = new ArrayList<Customer>();
		int lat = 0;
		int lon = 0;
		int id = 0;
		Customer city = null;
		String[] values = null;
		
		for (int i = 0; i < dimension; i++) {

			values = reader.readLine().split(" ");

			if (values != null && values.length == 3) {

				id = Integer.parseInt(Util.trim(values[0]));
				lat = Integer.parseInt(Util.trim(values[1]));
				lon = Integer.parseInt(Util.trim(values[2]));
				city = new Customer(id, lat, lon);

				cities.add(city);

			} else {
				throw new IllegalArgumentWorldException(TAG_NODE_COORD_SECTION);
			}

		}

		return cities;
	}

	private void passDemandSection(BufferedReader reader) throws Exception {
		String value = reader.readLine();

		if (!TAG_DEMAND_SECTION.equals(Util.trim(value))) {
			throw new IllegalArgumentWorldException(TAG_DEMAND_SECTION);
		}
	}

	private void getDemands(BufferedReader reader, ArrayList<Customer> cities) throws Exception {
		String[] values = null;
		int id = 0;
		int demand = 0;

		for (Customer city : cities) {
			values = reader.readLine().split(" ");

			if (values != null && values.length == 2) {
				id = Integer.parseInt(Util.trim(values[0]));
				demand = Integer.parseInt(Util.trim(values[1]));

				if (id == city.getId()) {
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

		if (!TAG_DEPOT_SECTION.equals(Util.trim(value))) {
			throw new IllegalArgumentWorldException(TAG_DEPOT_SECTION);
		}
	}

	private void getDepots(BufferedReader reader) throws Exception {
		String value = reader.readLine().replace(" ", "");
		int id = 0;

		for (int i = 0; !"-1".equals(value); i++) {

			id = Integer.parseInt(value);

			if (value != null && value.length() > 0) {
				for (Customer city : this.cities) {
					if (city.getId() == id) {
						city.setDepot(true);
						break;
					}
				}

			} else {
				throw new IllegalArgumentWorldException(TAG_DEPOT_SECTION);
			}
			value =  reader.readLine().replace(" ", "");
		}
	}

	private void eof(BufferedReader reader) throws Exception {
		String value = reader.readLine();

		if (!TAG_EOF.equals(Util.trim(value))) {
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

	public ArrayList<Customer> getCities() {
		return cities;
	}

	public int[] getDemands() {
		return demands;
	}

	public Customer getFirstDepot() {

		for (Customer city : this.cities) {
			if (city.isDepot()) {
				return city;
			}
		}

		return null;
	}

	public void setSeed(long seed) {
		this.random = new Random(seed);
	}

	public double getSampleDouble() {
		return random.nextDouble();
	}

	public long getSampleLong() {
		return random.nextLong();
	}

	public int getSampleInt() {
		return random.nextInt();
	}

	public long getSeed() {
		return seed;
	}

	public Customer getCustomer(int cityId) {
		return this.cities.get(cityId - 1);
	}

	public double tourLength(String route) {
		double length = 0;
		String[] teste = route.replace(" ", "").split(",");
		Customer c1 = null;
		Customer c2 = null;
		
		c1 = this.getCustomer(Integer.parseInt(teste[0]));
		for (int i = 1; i < teste.length; i++) {
			c2 = this.getCustomer(Integer.parseInt(teste[i]));
			length += Util.hypot(c1, c2);
			c1 = c2;
		}
		
		return length;
	}

	public Tour getBestTour() {
		return bestTour;
	}

	public void setBestTour(Tour bestTour) {
		this.bestTour = bestTour;
	}
	
	
}
