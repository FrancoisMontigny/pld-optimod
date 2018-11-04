package main.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;
import model.tsp.TSP1;
import model.tsp.TemplateTSP;

public class TourCalculator {

	private TourFactory tourFactory = TourFactory.getInstance();
	private Plan map = ModelInterface.getPlan();
	private List<Delivery> deliveries;
	
	private Delivery depot; // XXX
	
	/* Unique instance */
	private static TourCalculator instance = null;

	
	
	/* TSP related fields */
	private double[][] costTSP;
	private int nodesCount;
	private int[] delay;
	
	private int calculationTimeLimitMs = 10000;
	
	
	
	private HashMap<Pair<Long, Long>, Step> steps;
	
	private TourCalculator() {
		tourFactory = TourFactory.getInstance();
		deliveries = new ArrayList<>();
		steps = new HashMap<>();
	}

	@Deprecated
	public static TourCalculator init(Plan map, List<Delivery> deliveries, Delivery depot) {
		if (instance == null) getInstance();
		
		instance.deliveries = deliveries;
		instance.map = map;
		instance.depot = depot;
		return instance;
	}

	public static TourCalculator getInstance() {
		if (instance == null) {
			instance = new TourCalculator();
		}
		return instance;
	}

	public void addDelivery(Delivery d) {
		this.deliveries.add(d);
	}

	public void print() {
		for (Delivery d : deliveries) {
			System.out.println("got delivery with intersection id :" + d.getAddress().getId());
		}
	}
	
	public void setDepot(Delivery depot) {
		this.depot = depot;
	}
	
	public void setMap(Plan map) {
		this.map = map;
	}
	
	
	
	public void calculateTours(int deliveryMenCount) {
		// FIXME : for now, assumes a deleveryMenCount of 1

		/* Creates the sub-graph */
		createGraph();

		/* Solves TSP within the sub-graph, and create the tours */
		resolveTSP();
		
	}

	


	/**
	 * Creates the graph to be used with TSP algorithm
	 * 
	 * It uses the plan and all the deliveries, as well as the depot
	 * XXX : how do we know which delivery is the depot ? (-> added the depot as 
	 * an attribute)
	 * 
	 * It creates a sub-graph, containing only the useful nodes (deliveries and depot), 
	 * and the arrows corresopnd to the shortest path found between the 2 nodes in the
	 * main graph
	 * 
	 * It assumes the ordering of deliveries remain the same (i.e. the list 
	 * deliveries will not be modified)
	 * And it expect that the depot will always be the first in both list (this 
	 * is actually a pre-condution in the TSP algotithm)
	 */
	private void createGraph() {
		/* Initialization */
		nodesCount = 1 + deliveries.size(); // depot as first + deliveries
		
		delay = new int[nodesCount];
		delay[0] = 0; // no delay in the depot
		for (int i = 0; i < deliveries.size(); i++) {
			delay[i+1] = deliveries.get(i).getDuration();
		}
		
		costTSP = new double[nodesCount][nodesCount];

		
		/* Calculation of costs with Dijkstra */
		// First line of matrix is depot; other lines are the delivery points
		costTSP[0] = dijkstraHelper(depot.getAddress());
		for (int i = 0; i < deliveries.size(); i++) {
			costTSP[i+1] = dijkstraHelper(deliveries.get(i).getAddress());
		}
	}
	

	/**
	 * Disjkstra helper function, create the useful row for TSP cost matrix based
	 * on the result of the algorithm
	 */
	private double[] dijkstraHelper(Intersection source) {
		Pair<HashMap<Long, Double>, HashMap<Long, Long>> result = map.Dijkstra(source);
		
		HashMap<Long, Double> cost = result.getKey();
		HashMap<Long, Long> predecessors = result.getValue();
		
		/* "Header" of the list : the ids of the nodes to use in the correct order */
		long[] idsList = new long[nodesCount];		
		idsList[0] = depot.getAddress().getId();
		for (int i = 0; i < deliveries.size(); i++) {
			idsList[i+1] = deliveries.get(i).getAddress().getId();
		}
		
		/* We use the header to construct the cost line */
		double[] costResult = new double[nodesCount];
		for (int i = 0; i < nodesCount; i++) {
			costResult[i] = cost.get(idsList[i]);
		}
		
		/* We also create the steps */
		createSteps(predecessors, source);
		
		/* And we return the cost */
		return costResult;
	}
	
	
	
	
	/**
	 * Wrapper function around TSP resolution, delegate responsability to TSP.
	 * Execute the TSP algorithm, extract the needed information to create the tours
	 */
	private void resolveTSP() {
		TemplateTSP tsp = new TSP1();
		tsp.chercherEtAfficherMeilleureSolution(calculationTimeLimitMs, nodesCount, costTSP, delay);
		
		List<Step> solutionSteps = findStepsFromResult(tsp.getMeilleureSolution());
		
		TourFactory.createTour(1, solutionSteps, deliveries); // FIXME : hardcoded 1 and deliveries		
	}

	
	
	
	
	private void createSteps(HashMap<Long, Long> predecessors, Intersection source) {
		long sourceId = source.getId();
		for (Long id : predecessors.keySet()) {
			if (id == sourceId) {
				continue;
			}
			
			ArrayList<Long> intersectionsIds = new ArrayList<>();
			Long currentId = id;
			do {
				intersectionsIds.add(currentId);
				currentId = predecessors.get(currentId);
			} while (currentId != sourceId);
			
			intersectionsIds.add(sourceId);
			
			Collections.reverse(intersectionsIds);
			
			System.out.println(intersectionsIds);
			
			ArrayList<Section> sections = new ArrayList<>();
			for (int i = 0; i < intersectionsIds.size() - 1; i++) {
				sections.add(findSectionBetween(intersectionsIds.get(i), intersectionsIds.get(i + 1)));
			}
			
			Step step = new Step(sections);
			Pair<Long, Long> pair = new Pair<Long, Long>(sourceId, id);
			
			steps.put(pair, step);
		}
	}

	private Section findSectionBetween(Long idStart, Long idEnd) {
		for (Section section : map.getIntersectionById(idStart).getOutcomingSections()) {
			if (section.getIdEndIntersection() == idEnd) {
				return section;
			}
		}
		return null;
	}
	
	private List<Step> findStepsFromResult(Integer[] solution) {
		List<Step> list = new ArrayList<Step>();
		for (int i = 0; i < solution.length - 1; i++) {
			/* Convert ids used with TSP to ids used in Model*/
			long idStart = solution[i] == 0 ? depot.getAddress().getId() : deliveries.get(solution[i]-1).getAddress().getId();
			long idEnd = solution[i+1] == 0 ? depot.getAddress().getId() : deliveries.get(solution[i+1]-1).getAddress().getId();

			list.add(steps.get(new Pair<Long, Long>(idStart, idEnd)));
		}
		
		/* Add latest step : last delivery -> depot */
		long idStart = deliveries.get(solution[solution.length-1]-1).getAddress().getId();
		long idEnd = depot.getAddress().getId();

		list.add(steps.get(new Pair<Long, Long>(idStart, idEnd)));

		
		return list;
	}
	
	public List<Delivery> getDeliveries() {
		return deliveries;
	}
	
	public Delivery getDepot() {
		return depot;
	}
}
