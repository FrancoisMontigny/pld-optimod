package main.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.controler.Controler;

/**
 * Listener catching the button events of the application.
 * 
 * @author H4204 - DURAFFOURG Maud, MONTIGNY François, SILVESTRI Lisa, STERNER
 *         Léo, THOLOT Cassandre
 */
public class ButtonListener implements ActionListener {

    private Controler controler;

    /**
     * Create a button listener for the specified application controller.
     * 
     * @param c is the application's controller.
     */
    public ButtonListener(Controler c) {
	this.controler = c;
    }

    /**
     * Function called by listener every time a button is clicked. Send the
     * corresponding message to the controler.
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     * 
     * @param e is the event that occurred.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
	switch (e.getActionCommand()) {
	case Window.ACTION_SELECTION_PLAN:
	    this.controler.openPlan();
	    break;
	case Window.ACTION_SELECTION_DELIVERY:
	    this.controler.openDeliveries();
	    break;
	case WindowHeader.ACTION_CHANGE_DELIVERY_MEN_COUNT:
	    this.controler.openParameters();
	    break;
	case Window.ACTION_CALCULATE_TOUR:
	    this.controler.calculatePlanning();
	    break;
	case WindowHeader.ACTION_RETURN:
	    this.controler.returnToState();
	    break;
	case PlanningView.ACTION_ADDING_DELIVERY_POINT:
	    this.controler.addDelivery();
	    break;
	case PlanningView.ACTION_SUPRESSING_DELIVERY_POINT:
	    this.controler.removeDelivery();
	    break;
	case PlanningView.ACTION_CANCELLING_MODIFICATIONS:
	    // TODO : controleur.cancelModification() => Gave up implementation
	    break;
	case AddingDeliveryView.ACTION_VALIDATION_ADDING_DELIVERY:
	    this.controler.confirmNewDelivery();
	    break;
	case AddingDeliveryView.ACTION_CANCELATION_ADDING_DELIVERY:
	    this.controler.cancelNewDelivery();
	    break;
	default:
	    throw new RuntimeException("Unhandled action : " + e.getActionCommand());
	}

    }

}