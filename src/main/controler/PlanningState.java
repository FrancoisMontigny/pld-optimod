package main.controler;

import main.model.Delivery;
import main.model.Intersection;
import main.model.ModelInterface;
import main.model.Section;
import main.model.Step;
import main.model.TourCalculator;
import main.ui.InputDialogSelector;
import main.ui.InputDialogSelector.SelectionCancelledException;
import main.ui.Window;

/**
 * PlanningState is the state in which a tour planning has been calculated.
 *
 */
class PlanningState extends DefaultState {

    // TODO : check if planning can be recalculated
    /**
     * Calculate the planning for the given deliveries request and plan.
     * 
     * @param controler is the application's controler.
     * @param window is the application's graphical window.
     */
    public void calculatePlanning(Controler controler, Window window) {
	ModelInterface.getTourCalculator().calculateTours();
	window.displayTourPlanningPanel();
    }

    /**
     * Open parameters modal.
     * 
     * @param controler is the application's controler.
     * @param window is the application's graphical window.
     */
    public void openParameters(Controler controler, Window window) {
	try {
	    ModelInterface.setDeliveryMenCount(InputDialogSelector
		    .getIntegerFromInput("Veuillez choisir le nombre de livreurs", "Nombre de livreurs"));
	} catch (SelectionCancelledException e) {
	    System.out.println("Selection was cancelled, ignoring...");
	}
    }

    /**
     * Move a delivery from one tour to another.
     * 
     * @param controler is the application's controler.
     * @param window is the application's graphical window.
     */
    public void moveDelivery(Controler controler, Window window) {
	// int newTourId = window.getNewTourId();
	// Delivery movedDelivery = window.getMovedDelivery();
	// ModelInterface.moveDelivery(newTourId, movedDelivery);
    }

    /**
     * Remove a delivery from a tour.
     * 
     * @param controler is the application's controler.
     * @param window is the application's graphical window.
     */
    public void removeDelivery(Controler controler, Window window) {
	Intersection i = controler.getSelectedIntersection();
	Delivery toRemove = ModelInterface.findCorrespondingDelivery(i);
	if (toRemove != null) {
	    ModelInterface.removeDelivery(toRemove);
	    window.redraw();
	    window.redrawTable();
	}
    }

    /**
     * Add a delivery to a tour.
     * 
     * @param controler is the application's controler.
     * @param window is the application's graphical window.
     */
    public void addDelivery(Controler controler, Window window) {
	//  XXX
//<<<<<<< HEAD
//	// TODO : window.openNewDelivery()
//	// FIXME : get duration
//	// XXX do we need to change state ?
//	// XXX use with GUI
//	Delivery newDelivery = new Delivery(0, controler.getRightClickedIntersection());
//	Delivery precedingDelivery = ModelInterface.findCorrespondingDelivery(controler.getSelectedIntersection());
//	if (precedingDelivery != null) {
//	    // FIXME : de-hardcode null and get corresonding tour
//	    TourCalculator.getInstance().addDeliveryAfterDelivery(newDelivery, precedingDelivery, null);
//	    controler.getWindow().redraw();
//	    controler.getWindow().redrawTable();
//	}
//
////	controler.setCurrentState(controler.addState);
//=======
	controler.getWindow().displayAddingDeliveryPanel();
	controler.setCurrentState(controler.addState);
//>>>>>>> ui
    }

    @Override
    public void clickedNearSection(Controler controler, Window window, Section section) {
	window.highlightSelectedSection(section);
    }

    @Override
    public void clickedNearIntersection(Controler controler, Window window, Intersection closestIntersection) {
	Delivery selectedDelivery = ModelInterface.findCorrespondingDelivery(closestIntersection);
	if (selectedDelivery != null) {
	    Step step = ModelInterface.findStepBeforeDelivery(selectedDelivery);
	    if (!selectedDelivery.equals(ModelInterface.getDepot())) {
		window.listSectionsOfStep(step);
	    } else {
		window.hideSectionsList();
	    }
	}
	controler.setSelectedIntersection(closestIntersection);
    }

    @Override
    public void rightClickedNearIntersection(Controler controler, Window window, Intersection intersection) {
	window.highlightRightClickedIntersection(intersection);

	controler.setRightClickedIntersection(intersection);
    }

    public String stateToString() {
	return "planningState";
    }
}
