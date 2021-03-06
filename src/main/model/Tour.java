package main.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Tour represents a number of deliveries done by one delivery man. The delivery
 * man will leave the depot, travel to all deliveries using the shortest
 * possible path, and come back to the depot after all of them are done.
 *
 * @author H4204 - DURAFFOURG Maud, MONTIGNY François, SILVESTRI Lisa, STERNER
 *         Léo, THOLOT Cassandre
 */
public class Tour {

    private Delivery depot;
    private List<Step> steps;
    private List<Delivery> deliveryPoints;
    private int deliveryManId;

    /**
     * Build a tour.
     * 
     * @param depot          is the depot from where to leave.
     * @param steps          are the steps between each delivery.
     * @param deliveryPoints are the addresses of all deliveries.
     * @param deliveryManId  is the id of the delivery man.
     */
    public Tour(Delivery depot, List<Step> steps, List<Delivery> deliveryPoints, int deliveryManId) {
	this.depot = depot;
	this.steps = new ArrayList<>(steps);
	this.deliveryPoints = new ArrayList<>(deliveryPoints);

	this.deliveryManId = deliveryManId;
    }

    /**
     * Add a delivery at a given index of the list.
     * 
     * @param delivery is the delivery to add.
     * @param index    is the index where to add the delivery in the list.
     */
    public void addDeliveryAtIndex(Delivery delivery, int index) {
	if (delivery == null) {
	    throw new IllegalArgumentException();
	}
	this.deliveryPoints.add(index, delivery);
    }

    /**
     * Remove a delivery from the tour.
     * 
     * @param toRemove is the delivery to remove.
     */
    public void removeDelivery(Delivery toRemove) {
	this.deliveryPoints.remove(toRemove);
    }

    /**
     * Calculate and set all delivery hours.
     */
    public void calculateDeliveryHours() {
	Calendar departureTime = (Calendar) this.depot.getHour().clone();
	for (Step s : this.steps) {
	    List<Section> sections = s.getSections();
	    long lastIntersectionId = sections.get(sections.size() - 1).getEnd().getId();
	    boolean foundDelivery = false;
	    Iterator<Delivery> it = this.deliveryPoints.iterator();
	    Delivery delivery = this.depot;
	    // If we don't find the delivery, it's the depot
	    while (!foundDelivery && it.hasNext()) {
		Delivery d = it.next();

		if (lastIntersectionId == d.getAddress().getId()) {
		    delivery = d;
		    foundDelivery = true;
		}
	    }
	    if (delivery.equals(this.depot)) {
		continue;
	    }
	    long travelTimeInSeconds = Math.round(s.calculateLength() * 3600 / 15000);
	    departureTime.add(Calendar.SECOND, (int) travelTimeInSeconds);
	    Calendar deliveryTime = (Calendar) departureTime.clone();
	    delivery.setHour(deliveryTime);
	    departureTime.add(Calendar.SECOND, delivery.getDuration());
	}
    }

    /**
     * Getter for the delivery points.
     * 
     * @return List, a list of all deliveries in the tour, including the depot.
     */
    public List<Delivery> getDeliveryPoints() {
	List<Delivery> deliveriesAndDepot = new ArrayList<>(this.deliveryPoints);
	deliveriesAndDepot.add(this.depot);
	return deliveriesAndDepot;
    }

    /**
     * Getter for the steps.
     * 
     * @return List, a list of all the steps constituting the tour.
     */
    public List<Step> getSteps() {
	return this.steps;
    }

    /**
     * Getter for the delivery man's id.
     * 
     * @return Integer, the delivery man's id.
     */
    public int getDeliveryManId() {
	return this.deliveryManId;
    }

    @Override
    public String toString() {
	return "Tour [depot=" + this.depot + /* ", steps=" + steps + */ ", deliveryPoints=" + this.deliveryPoints
		+ ", deliveryManId=" + this.deliveryManId + "]";
    }
}
