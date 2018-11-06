package main.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.controler.Controler;

public class ButtonListener implements ActionListener {

	private Controler controler;

	public ButtonListener(Controler c) {
		this.controler = c;
	}

	/*
	 * Function called by listener every time a button is clicked. Send the
	 * corresponding message to the controler.
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * @param e the event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case Window.ACTION_SELECTION_PLAN:
			controler.openPlan();
			break;
		case Window.ACTION_SELECTION_DELIVERY:
			controler.openDeliveries();
			break;
		case WindowHeader.ACTION_PARAMETERS:
			controler.openParameters();
			break;
		case Window.ACTION_CALCULATE_TOUR:
			controler.calculateTour();
			break;
		case PlanningView.ACTION_ADDING_DELIVERY_POINT :
			// TODO : controleur.addDeleveryPoint() qui ouvre une modale qui demande clic plan
			// puis avec choix livreur,heure et dur�e
			break;
		default:
			throw new RuntimeException("Unhandled action : " + e.getActionCommand());
		}
	}
}