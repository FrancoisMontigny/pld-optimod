package main.ui;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.controler.Controler;
import main.model.Intersection;
import main.model.ModelInterface;

public class PlanningListener implements ListSelectionListener {

	private JTable planning;
	private Controler controler;

	public PlanningListener(JTable t, Controler controler) {
		this.planning = t;
		this.controler = controler;
		
	}
//
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		if (e.getClickCount() == 2) {
//			Point p = e.getPoint();
//			int row = planning.rowAtPoint(p);
//			System.out.println("N� ligne cliqu�e : " + row);
//			// TODO : controleur.modifLigne(row) qui ouvre une range selector (1
//			// � nbr livreurs)
//			// et change le delivery man de la ligne avec le retour de la range
//			// selector
//		}
//	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		String address = (String) planning.getValueAt(planning.getSelectedRow(), 1);
		
		String[] fragments = address.substring(1, address.length() - 1).split("; ");
		double lat = Double.parseDouble(fragments[0]);
		double lon = Double.parseDouble(fragments[1]);
		
		Intersection closest = ModelInterface.findClosestIntersection(lat, lon);
		
//		controler.clickedPlanning FIXME : ???
		
	}

}
