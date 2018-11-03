package main.controler;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import main.model.ModelInterface;
import main.xml.XMLException;
import main.ui.Window;

public class Controler {
	private State currentState;
	private Window window;
	private ModelInterface model;
	protected final InitState initState = new InitState();
	protected final LoadedPlanState loadedPlanState = new LoadedPlanState();
	protected final LoadedDeliveriesState loadedDeliveriesState = new LoadedDeliveriesState();
	protected final PlanningState planningState = new PlanningState();
	protected final ParametersState parametersState = new ParametersState();
	protected final AddDeliveryState addState = new AddDeliveryState();
	
	/**
	 * Create application s controler
	 * @param model, model package s entry point
	 * @param window
	 */
	public Controler(){ //TODO : check if model is still a parameter (abstract class) + how to use abstract class
		this.currentState = initState;
		this.window = new Window(this);	
	}
	
	/**
	 * Load the xml formatted plan. Called when the "accueil" screen s button "Valider" is pushed. 
	 */
	public void openPlan(){
		try{
			currentState.openPlan(this, window);
		}
		catch(XMLException xml){ 
			System.out.println(xml);
		}
		catch(ParserConfigurationException parserConfig){
			System.out.println(parserConfig);
		}
		catch(SAXException sax){
			System.out.println(sax);
		}
		catch(IOException io){
			System.out.println(io);
		}
	}
	
	/**
	 * Load the xml formatted delivery request. Called when the "plan" screen s button "Valider" is pushed. 
	 */
	public void openDeliveries(){
		try{
			currentState.openDeliveries(this, window);
		}
		catch(XMLException xml){ 
			System.out.println(xml);
		}
		catch(ParserConfigurationException parserConfig){
			System.out.println(parserConfig);
		}
		catch(SAXException sax){
			System.out.println(sax);
		}
		catch(IOException io){
			System.out.println(io);
		}
	}
	
	/**
	 * Open parameters window
	 */
	public void openParameters() {
		try {
			currentState.openParameters(this, window);
		}
		catch( Exception e) {
			//TODO check exeption's handling + remove string when test done
			System.out.println(e +"controler open parameter");
		}
	}
	
	/**
	 * Calculate planning for the asked tour number
	 */
	public void calculatePlanning() {
		try {
			currentState.calculatePlanning(this, window);
		}
		catch( Exception e) {
			//TODO check exeption's handling + remove string when test done
			System.out.println(e +" controler calculateTour");
		}
	}
	
	/**
	 * Add a new delivery to a tour
	 */
	public void addDelivery() {
		try {
			currentState.addDelivery(this, window);
		}
		catch(Exception e) {
			//TODO : check exception handling + remove string when test done
			System.out.println(e +"controler adddelivery");
		}
	}
	
	/**
	 * Cancel addition of a new delivery
	 */
	public void cancelNewDelivery() {
		try {
			currentState.cancelNewDelivery(this, window);
		}
		catch(Exception e) {
			//TODO check handling + remove message
			System.out.println(e +"controler cancelnewdelivery");
		}
	}
	/**
	 * Confirms new number of tour
	 */
	public void confirmParameters() {
		try {
			currentState.confirmParameters(this, window);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	/**
	 * Confirm new delivery addition
	 */
	public void confirmNewDelivery() {
		try {
			currentState.confirmNewDelivery(this,window);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * model s getter.
	 */
	public ModelInterface getModel() {
		return model;
	}
	
	/**
	 * State s setter.
	 */
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	/**
	 * get the controler current state
	 * @return currentState
	 */
public State getCurrentState() {
	return currentState;
}
}
