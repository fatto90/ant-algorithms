package br.dcc.ufrj.antvrp.util;

import java.util.ArrayList;
import java.util.Arrays;

import br.dcc.ufrj.antvrp.world.Customer;

public class Tour implements Cloneable {
	
	private ArrayList<Customer> customers = null;
	private Customer firstCustomer = null;
	private double distance = 0;
	
	public Tour(Customer firstCustomer) {
		this.customers = new ArrayList<Customer>();
		this.customers.add(firstCustomer);		
		this.firstCustomer = firstCustomer;
	}
	
	public Tour(){
		this.customers = new ArrayList<Customer>();
	}
	
	public ArrayList<Customer> getCustomers(){
		return this.customers;
	}
	
	public void reset(){
		this.customers = new ArrayList<Customer>();
		this.customers.add(this.firstCustomer);
		this.distance = 0;
	}
	
	public void add(Customer customer){		
		this.customers.add(customer);
	}
	
	public Customer getCurrentCustomer(){
		return this.customers.get(this.customers.size() - 1);
	}
	
	public Customer getCustomer(int customerIndex){
		return this.customers.get(customerIndex);
	}
	
	public ArrayList<Tour> getRoutes(){
		ArrayList<Tour> result = new ArrayList<Tour>();		
		Tour tour = null;
		Customer a = null;
		
		for (Customer b : this.customers){
			if (a != null){
				
				if (a.getId() == this.firstCustomer.getId()){
					tour = new Tour(a);
					result.add(tour);
				}

				tour.add(b);
				tour.addDistance(a.getDistance(b));
				
			}
			a = b;
		}
		
		return result;
	}
	
	public double lengthSubTour(int beginIndex, int endIndex){
		double result = 0;
		Customer a = this.customers.get(beginIndex);
		Customer b = null;
		
		for (int i = beginIndex + 1; i <= endIndex; i++){
			b = this.customers.get(i);
			result += a.getDistance(b);
			a = b;
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.customers.toArray());
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

	public void addDistance(double distance) {
		this.distance += distance;		
	}
	
	public double recalcDistance(){
		
		Customer a = null;
		double distance = 0;
		
		for (Customer b: this.customers){
			if (a != null){
				distance += a.getDistance(b);
			}
			a = b;
		}
		
		this.distance = distance;
		return distance;
	}
	
	public Tour opt2IntraRoutes() {
		ArrayList<Tour> routes = this.getRoutes();
		ArrayList<Customer> customers = null;
		Customer t = null;
		int aIndex = 0;
		int bIndex = 0;
		int cIndex = 0;
		int dIndex = 0;
		
		for(int r = 0; r < routes.size(); r++){
			Tour route = routes.get(r);
			customers = route.getCustomers();
			
			for (int i = 0; i < route.getCustomers().size() - 3; ){
				Customer a = customers.get(i);
				aIndex = i;
				Customer b = customers.get(++i);
				bIndex = i;
				double distanceAB = a.getDistance(b);
				
				for(int j = 1; j < route.getCustomers().size() - 1;){
					if (j >= i - 2 && j <= i + 2){
						break;
					}
					Customer c = customers.get(j);
					cIndex = j;
					Customer d = customers.get(++j);				
					dIndex = j;
					double distanceCD = c.getDistance(d);
					double distanceAC = a.getDistance(c);
					double distanceBD = b.getDistance(d);
					
					if (distanceAB + distanceCD > distanceAC + distanceBD && d.getId() != firstCustomer.getId()){
						if (i < j){
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
		for(Tour route : routes){

			for (Customer b : route.getCustomers()){
				if (a != null){
					if (a.getId() != b.getId()){
						this.customers.set(i, b);
						i++;
					}
					
				} else {
					this.customers.set(i, b);
					i++;
					
				}
				a = b;
			}
		}
		
		this.recalcDistance();
		
		return this;
	}

	public Tour opt2InterRoutes() {
		ArrayList<Tour> routes = this.getRoutes();
		ArrayList<Customer> customers = null;
		Customer t = null;
		int aIndex = 0;
		int bIndex = 0;
		int cIndex = 0;
		int dIndex = 0;
		
		for(int r = 0; r < routes.size(); r++){
			Tour route = routes.get(r);
			customers = route.getCustomers();
			
			for (int i = 0; i < route.getCustomers().size() - 3; ){
				Customer a = customers.get(i);
				aIndex = i;
				Customer b = customers.get(++i);
				bIndex = i;
				double distanceAB = a.getDistance(b);
				
				for(int j = 1; j < route.getCustomers().size() - 1;){
					if (j >= i - 2 && j <= i + 2){
						break;
					}
					Customer c = customers.get(j);
					cIndex = j;
					Customer d = customers.get(++j);				
					dIndex = j;
					double distanceCD = c.getDistance(d);
					double distanceAC = a.getDistance(c);
					double distanceBD = b.getDistance(d);
					
					if (distanceAB + distanceCD > distanceAC + distanceBD && d.getId() != firstCustomer.getId()){
						if (i < j){
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
		for(Tour route : routes){

			for (Customer b : route.getCustomers()){
				if (a != null){
					if (a.getId() != b.getId()){
						this.customers.set(i, b);
						i++;
					}
					
				} else {
					this.customers.set(i, b);
					i++;
					
				}
				a = b;
			}
		}
		
		this.recalcDistance();
		
		return this;
	}

	
	private void changeOpt2(int start, int end) {
		ArrayList<Customer> customers = new ArrayList<Customer>(this.customers);
		for(int i = start, j = end; i <= end; i++, j--){
			this.customers.set(i, customers.get(j));
		}
	}
	
	@Override
	public Tour clone() {
		Tour clone = new Tour();
		clone.customers = this.customers;
		clone.distance = this.distance;
		clone.firstCustomer = this.firstCustomer;
		return clone;
	}
}
