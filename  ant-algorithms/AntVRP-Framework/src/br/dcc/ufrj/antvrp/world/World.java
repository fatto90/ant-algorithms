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
	private int antAmount;
	private long seed;
	private double initialPheromoneValue = 1;

	private String name;
	private String comment;
	private String type;
	private String edgeWeightType;

	protected Ant[] ants;
	protected Customer[] customers;
	protected Customer[] depots;
	private Tour bestTour;
	private Tour worstTour;
	

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

	protected abstract Tour getInitialTour();

	protected abstract void computeHeuristics();
	
	protected abstract double getIntialValue(int antsAmount);

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
		this.customers = this.getCustomers(reader, this.dimension);
		this.passDemandSection(reader);
		this.getDemands(reader, this.customers);
		this.passDepotSection(reader);
		this.getDepots(reader);
		this.eof(reader);
		reader.close();
		this.computeDistances();
		this.computeHeuristics();
	}
	
	public void reset() {
		this.bestTour = null;
		for (Customer city : this.customers) {
			for (Customer neighbor : city.getListCandidates()) {
				neighbor.setPheromone(this.initialPheromoneValue);
			}
		}
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

	public void createPheromones() {
		this.initialPheromoneValue = this.getIntialValue(this.antAmount);
		for (Customer city : this.customers) {
			for (Customer neighbor : city.getListCandidates()) {
				neighbor.setPheromone(this.initialPheromoneValue);
			}
		}
	}

	protected void computeDistances() {
		for (Customer city : this.customers) {
			for (Customer neighbor : this.customers) {
				city.addNeigbour(neighbor);				
			}
			city.createVectors();
		}
	}

	protected String getName(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(":");

		if (values != null && TAG_NAME.equals(Util.trim(values[0]))) {
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_NAME);
		}
	}

	protected String getComment(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(":");

		if (values != null && TAG_COMMENT.equals(Util.trim(values[0]))) {
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_COMMENT);
		}
	}

	protected String getType(BufferedReader reader) throws Exception {

		String[] values = reader.readLine().split(":");

		if (values != null && TAG_TYPE.equals(Util.trim(values[0]))) {
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_TYPE);
		}
	}

	protected int getDimension(BufferedReader reader) throws Exception {
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

	protected String getEdgeWeightType(BufferedReader reader) throws Exception {
		String[] values = reader.readLine().split(":");

		if (values != null && TAG_EDGE_WEIGHT_TYPE.equals(Util.trim(values[0]))) {
			return Util.trim(values[1]);
		} else {
			throw new IllegalArgumentWorldException(TAG_EDGE_WEIGHT_TYPE);
		}
	}

	protected int getCapacity(BufferedReader reader) throws Exception {
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

	protected void passNodeCoordSection(BufferedReader reader) throws Exception {
		String value = reader.readLine();

		if (!TAG_NODE_COORD_SECTION.equals(Util.trim(value))) {
			throw new IllegalArgumentWorldException(TAG_NODE_COORD_SECTION);
		}
	}

	protected Customer[] getCustomers(BufferedReader reader, int dimension) throws Exception {
		Customer[] customers = new Customer[dimension];
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

				customers[i] = city;

			} else {
				throw new IllegalArgumentWorldException(TAG_NODE_COORD_SECTION);
			}

		}

		return customers;
	}

	protected void passDemandSection(BufferedReader reader) throws Exception {
		String value = reader.readLine();

		if (!TAG_DEMAND_SECTION.equals(Util.trim(value))) {
			throw new IllegalArgumentWorldException(TAG_DEMAND_SECTION);
		}
	}

	protected void getDemands(BufferedReader reader, Customer[] cities) throws Exception {
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

	protected void passDepotSection(BufferedReader reader) throws Exception {
		String value = reader.readLine();

		if (!TAG_DEPOT_SECTION.equals(Util.trim(value))) {
			throw new IllegalArgumentWorldException(TAG_DEPOT_SECTION);
		}
	}

	protected void getDepots(BufferedReader reader) throws Exception {
		ArrayList<Customer> depots = new ArrayList<Customer>();
		String value = reader.readLine().replace(" ", "");
		int id = 0;

		for (;!"-1".equals(value);) {

			id = Integer.parseInt(value);

			if (value != null && value.length() > 0) {
				for (Customer customer : this.customers) {
					if (customer.getId() == id) {
						customer.setDepot(true);
						depots.add(customer);
						break;
					}
				}

			} else {
				throw new IllegalArgumentWorldException(TAG_DEPOT_SECTION);
			}
			value =  reader.readLine().replace(" ", "");
		}
		
		this.depots = new Customer[depots.size()];
		for (int i = 0; i < depots.size(); i++){
			this.depots[i] = depots.get(i);
		}
	}

	protected void eof(BufferedReader reader) throws Exception {
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

	public Customer[] getCities() {
		return customers;
	}

	public int[] getDemands() {
		return demands;
	}

	public Customer getFirstDepot() {

		for (Customer city : this.customers) {
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
		return this.customers[cityId - 1];
	}

	public Tour getBestTour() {
		return bestTour;
	}

	public void setBestTour(Tour bestTour) {
		this.bestTour = bestTour;
	}

	public void setAntAmount(int antAmount) {
		this.antAmount = antAmount;
	}

	public int getAntAmount() {
		return antAmount;
	}

	public Tour getWorstTour() {
		return worstTour;
	}

	public void setWorstTour(Tour worstTour) {
		this.worstTour = worstTour;
	}
	
	
}
