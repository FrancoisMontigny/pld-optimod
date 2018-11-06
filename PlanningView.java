package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.model.Delivery;
import main.model.ModelInterface;
import main.model.Step;
import main.model.Tour;

public class PlanningView extends JPanel {

	/* Components */
	private JTable planning;
	private JButton addDeliveryPoint;
	
	/* Listener */
	private ButtonListener buttonListener ;
	
	/* Components texts */
	private final String ADD_DELIVERY_POINT_BUTTON 		= "Ajouter un point de livraison";
	
	/* Actions */
	protected final static String ACTION_ADDING_DELIVERY_POINT 	= "ADD_DELIVERY_POINT";

	/* Board attributes */
	private final int columnsNumber 					= 4;
	private final String[] boardTitle 					= { "Livreur", "Adresse", "Heure de passage", "Trajet" };

	/* Graphic components */
	// private Graphics graphics;

	/**
	 * Create the graphical view for drawing the loaded plan with the scale s in
	 * the specified window w.
	 * 
	 * @param s
	 *            the scale
	 * @param w
	 *            the window
	 * @param p
	 *            the plan to print
	 */
	// FIXME : doc is not matching constructor
	public PlanningView(Window w) {
		super();
		this.buttonListener = w.buttonListener;
		/* Initialize */
		setSize(600, 900);
		/* Display */
		setBackground(Color.WHITE);
		createBoardPanel();
	}
	// TODO : mettre l'observer

	/**
	 * Function called to create the planning panel.
	 */
	public void createBoardPanel() {
		/* Building board dimensions */
		Collection<Delivery> deliveries = ModelInterface.getDeliveries();
		Object[][] boardDatas = new Object[deliveries.size()][columnsNumber];
		/* Building board datas */
		Collection<Tour> tours = ModelInterface.getTourPlanning();
		Iterator<Tour> itTours = tours.iterator();
		int currentLastEmptyLine = 0;
		while (itTours.hasNext()) {
			Tour currentTour = itTours.next();
			int deliveryMan = currentTour.getDeliveryManId();
			Collection<Delivery> deliveryPoints = currentTour.getDeliveryPoints();
			Collection<Step> steps = currentTour.getSteps();
			Iterator<Delivery> itDeliveries = deliveryPoints.iterator();
			while (itDeliveries.hasNext()) {
				Delivery currentDelivery = itDeliveries.next();
				boardDatas[currentLastEmptyLine][0] = deliveryMan;
				boardDatas[currentLastEmptyLine][1] = "(" + currentDelivery.getAddress().getLat() + "; " + currentDelivery.getAddress().getLon() + ")";
				/*SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
				Calendar hour = currentDelivery.getHour();
				dateFormat.setTimeZone(hour.getTimeZone());*/
				boardDatas[currentLastEmptyLine][2] = "H"; //dateFormat.format(hour.getTime()); 
				// TODO : calcul heure passage (Model ou IHM ?);
				boardDatas[currentLastEmptyLine][3] = "P";
				// TODO : liste des noms de rues dans l'ordre (Model)
				currentLastEmptyLine++;
			}
		}
		/* Building board */
		planning = new JTable(boardDatas, boardTitle);
		PlanningListener planningListener = new PlanningListener(planning);
		/* Displaying */
		addDeliveryPoint = new JButton (ADD_DELIVERY_POINT_BUTTON);
		addDeliveryPoint.setActionCommand(ACTION_ADDING_DELIVERY_POINT);
		addDeliveryPoint.addActionListener(buttonListener);
		this.add(addDeliveryPoint, BorderLayout.NORTH);
		this.add(new JScrollPane(planning), BorderLayout.CENTER);
	}
}