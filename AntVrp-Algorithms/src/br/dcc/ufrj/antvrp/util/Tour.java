package br.dcc.ufrj.antvrp.util;

import java.util.ArrayList;
import java.util.Arrays;

import br.dcc.ufrj.antvrp.world.Customer;

public class Tour implements Cloneable {

	private Customer[] customers = null;
	private Customer firstCustomer = null;
	private double distance = 0;
	private int dimension = 0;
	private int size = 0;
	private boolean[] visited;
	private Step[] steps;
	private Step[] sortedSteps;

	public Tour(Customer firstCustomer, int dimension) {
		this.customers = new Customer[dimension * 2];
		this.steps = new Step[dimension * 2];
		this.visited = new boolean[dimension];
		this.customers[0] = firstCustomer;
		this.visited[0] = true;
		this.firstCustomer = firstCustomer;
		this.dimension = dimension;
		this.size = 1;
		
	}

	public Tour(int dimension) {
		this.customers = new Customer[dimension * 2];
		this.steps = new Step[dimension * 2];
		this.visited = new boolean[dimension];
		this.dimension = dimension;
	}
	
	public void sortSteps(){
		sortedSteps = steps.clone();
		Arrays.sort(sortedSteps);
	}
	
	public Step[] getSteps(){
		return this.steps;
	}
	
	public Step[] getSortedSteps(){
		return this.sortedSteps;
	}
	
	public boolean areNeighbor(Customer a, Customer b){
		
		for(Step hop: this.steps){
			
			if(hop != null){
				if ((a.getId() == hop.getSource().getId() && b.getId() == hop.getDestination().getId()) ||
					(a.getId() == hop.getDestination().getId() && b.getId() == hop.getSource().getId())){
					return true;
				}
			} else {
				return false;
			}
		}
		
		return false;
	}

	public Customer[] getCustomers() {
		return this.customers;
	}

	public void reset() {
		this.visited = new boolean[dimension];
		this.customers = new Customer[dimension * 2];
		this.steps = new Step[dimension * 2];
		this.customers[firstCustomer.getId() - 1] = this.firstCustomer;
		this.visited[firstCustomer.getId() - 1] = true;
		this.distance = 0;
		this.size = 1;
	}

	public void add(Customer customer) {
		this.customers[this.size] = customer;
		this.steps[this.size] = new Step(this.customers[this.size - 1], this.customers[this.size]);
		this.visited[customer.getId() - 1] = true;
		this.distance += this.customers[this.size - 1].getDistance(customer);
		this.size++;
	}

	public Customer getCurrentCustomer() {
		return this.customers[size - 1];
	}

	public Customer getCustomer(int customerIndex) {
		return this.customers[customerIndex];
	}

	public ArrayList<Tour> getRoutes() {
		ArrayList<Tour> result = new ArrayList<Tour>();
		Tour tour = null;
		Customer a = null;

		for (int i = 0; i < this.size; i++) {
			Customer b = this.customers[i];

			if (a != null) {

				if (a.getId() == this.firstCustomer.getId()) {
					tour = new Tour(a, this.dimension);
					result.add(tour);
				}

				tour.add(b);

			}
			a = b;
		}

		return result;
	}

	public double lengthSubTour(int beginIndex, int endIndex) {
		double result = 0;
		Customer a = this.customers[beginIndex];
		Customer b = null;

		for (int i = beginIndex + 1; i <= endIndex; i++) {
			b = this.customers[i];
			result += a.getDistance(b);
			a = b;
		}

		return result;
	}

	@Override
	public String toString() {

		return Arrays.toString(this.customers).replaceAll(", null", "");
	}

	public double getDistance() {
		return distance;
	}

	public Customer getFirstCustomer() {
		return firstCustomer;
	}

	public void setFirstCustomer(Customer firstCustomer) {
		this.firstCustomer = firstCustomer;
	}

	public double recalcDistance() {

		Customer a = null;
		double distance = 0;

		for (int i = 0; i < this.size; i++) {
			Customer b = this.customers[i];
			if (a != null) {
				distance += a.getDistance(b);
			}
			a = b;
		}

		this.distance = distance;
		return distance;
	}

	public Tour opt2() {
		ArrayList<Tour> routes = this.getRoutes();
		Customer[] customers = null;
		Customer t = null;
		int aIndex = 0;
		int bIndex = 0;
		int cIndex = 0;
		int dIndex = 0;

		for (int r = 0; r < routes.size(); r++) {
			Tour route = routes.get(r);
			customers = route.getCustomers();

			for (int i = 0; i < route.getSize() - 3; i++) {
				Customer a = customers[i];
				aIndex = i;
				Customer b = customers[i + 1];
				bIndex = i + 1;
				double distanceAB = a.getDistance(b);

				for (int j = 0; j < route.getSize() - 1;j++) {
					if (Math.abs(j - i) < 2){
						continue;
					}
					Customer c = customers[j];
					cIndex = j;
					Customer d = customers[j + 1];
					dIndex = j + 1;
					double distanceCD = c.getDistance(d);
					double distanceAC = a.getDistance(c);
					double distanceBD = b.getDistance(d);

					if (distanceAB + distanceCD > distanceAC + distanceBD && d.getId() != firstCustomer.getId()) {
						if (i < j) {
							route.changeOpt2(bIndex, cIndex);
							t = b;
							b = c;
							c = t;
							t = null;
						} else {
							route.changeOpt2(dIndex, aIndex);
							t = a;
							a = d;
							d = t;
							t = null;
						}
						route = routes.get(r);
						customers = route.getCustomers();
						distanceAB = a.getNeighbor(b.getId()).getDistance();
						distanceCD = c.getDistance(d);
						distanceAC = a.getDistance(c);
						distanceBD = b.getDistance(d);
					}
				}
			}
		}

		int i = 0;
		Customer a = null;
		for (Tour route : routes) {

			for (int j = 0; j < route.getSize(); j++) {
				Customer b = route.getCustomers()[j];
				if (a != null) {
					if (a.getId() != b.getId()) {
						this.customers[i] = b;
						i++;
					}

				} else {
					this.customers[i] = b;
					i++;

				}
				a = b;
			}
		}

		this.recalcDistance();

		return this;
	}
	
	public Tour opt3() {
		ArrayList<Tour> routes = this.getRoutes();
		Customer t = null;
		Customer[] customers = null;

		for (int r = 0; r < routes.size(); r++) {
			Tour route = routes.get(r);
			customers = route.getCustomers();
			
			for (int i = 0; i < route.getSize() - 1; i++){
				for (int j = 0; j < route.getSize() - 1; j++){
					if (Math.abs(j - i) < 2){
						continue;
					}
					
					for (int k = 0; k < route.getSize() - 1; k++){
						if (Math.abs(k - j) < 2 || Math.abs(k - i) < 2){
							continue;
						}
						
						int[] v = {i, j, k};
						Arrays.sort(v);
						
						int x = v[0];
						int y = v[1];
						int z = v[2];

						Customer a = customers[x];
						Customer b = customers[x + 1];
						Customer c = customers[y];
						Customer d = customers[y + 1];
						Customer e = customers[z];
						Customer f = customers[z + 1];
						
						double distanciaAntes = a.getDistance(b) + c.getDistance(d) + e.getDistance(f);
						double distanciaDepois = a.getDistance(d) + e.getDistance(b) + c.getDistance(f);
						
						if (distanciaAntes > distanciaDepois){
							System.out.println(Arrays.toString(customers));
							System.out.println(Tour.getDistance(customers));

							customers[x + 1] = d;
							customers[y + 1] = b;
							t = b;
							b = d;
							d = t;
							
							customers[y] = e;
							customers[z] = c;
							t = c;
							c = e;
							e = t;
							System.out.println(Arrays.toString(customers));
							System.out.println(Tour.getDistance(customers));
						}
					}
				}
			}
		}	
		
		int i = 0;
		Customer a = null;
		for (Tour route : routes) {

			for (int j = 0; j < route.getSize(); j++) {
				Customer b = route.getCustomers()[j];
				if (a != null) {
					if (a.getId() != b.getId()) {
						this.customers[i] = b;
						i++;
					}

				} else {
					this.customers[i] = b;
					i++;

				}
				a = b;
			}
		}

		this.recalcDistance();

		return this;
	}

	public Tour interchange() {
		ArrayList<Tour> routes = this.getRoutes();
		double distanceAfter = 0;
		double distanceBefore = 0;

		for (Tour route1 : routes) {
			for (int i = 3; i < route1.getSize(); i++) {
				Customer aLess = route1.getCustomers()[i - 3];
				Customer a = route1.getCustomers()[i - 2];
				Customer b = route1.getCustomers()[i - 1];
				Customer bGreater = route1.getCustomers()[i];

				if (a == null || a.isDepot()) {
					break;
				}

				for (Tour route2 : routes) {
					if (!route1.equals(route2)){
						
						for (int j = 3; j < route2.getSize(); j++){
							Customer cLess = route2.getCustomers()[j - 3];
							Customer c = route2.getCustomers()[j - 2];
							Customer d = route2.getCustomers()[j - 1];
							Customer dGreater = route2.getCustomers()[j];

							if (c == null || c.isDepot()) {
								break;
							}
							distanceBefore = aLess.getDistance(a) + b.getDistance(bGreater) + cLess.getDistance(c) + d.getDistance(dGreater);
							distanceAfter =  aLess.getDistance(c) + d.getDistance(bGreater) + cLess.getDistance(a) + b.getDistance(dGreater);
							
							if (distanceBefore > distanceAfter){
								route2.getCustomers()[j - 2] = a;
								route2.getCustomers()[j - 1] = b;
								route1.getCustomers()[i - 2] = c;
								route1.getCustomers()[i - 1] = d;
								a = c;
								b = d;
							}
						}
					}
				}
			}
		}

		int i = 0;
		Customer a = null;
		for (Tour route : routes) {

			for (int j = 0; j < route.getSize(); j++) {
				Customer b = route.getCustomers()[j];
				if (a != null) {
					if (a.getId() != b.getId()) {
						this.customers[i] = b;
						i++;
					}

				} else {
					this.customers[i] = b;
					i++;

				}
				a = b;
			}
		}

		this.recalcDistance();

		return this;
	}

	private void changeOpt2(int start, int end) {
		Customer[] customers = this.customers.clone();
		for (int i = start, j = end; i <= end; i++, j--) {
			this.customers[i] = customers[j];
		}
	}

	@Override
	public Tour clone() {
		Tour clone = new Tour(this.dimension);
		clone.customers = this.customers;
		clone.distance = this.distance;
		clone.firstCustomer = this.firstCustomer;
		clone.size = size;
		clone.dimension = dimension;
		return clone;
	}

	public int getSize() {
		return size;
	}

	public boolean contains(Customer customer) {
		return this.visited[customer.getId() - 1];
	}
	
	public static double getDistance(Customer[] customers){
		Customer a = null;
		double distance = 0;

		for (int i = 0; i < customers.length; i++) {
			Customer b = customers[i];
			if (a != null) {
				if (b != null){
					distance += a.getDistance(b);
				} else {
					break;
				}
			}
			a = b;
		}

		return distance;
		
	}
}
