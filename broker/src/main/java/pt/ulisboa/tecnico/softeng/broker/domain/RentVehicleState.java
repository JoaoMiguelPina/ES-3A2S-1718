package pt.ulisboa.tecnico.softeng.broker.domain;


import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentVehicleState extends AdventureState {
	
	private static final int MAX_REMOTE_ERRORS = 5;
	
	@Override
	public State getState() {
		return State.RENT_VEHICLE;
	}

	
	@Override
	public void process(Adventure adventure) {
		try {
			Client client = adventure.getClient();
			String reference = CarInterface.reserveVehicle(adventure.getBegin(), adventure.getEnd(), client.getDrivingLicense(), client.getNif(), client.getIban());
			 
			
			adventure.setVehicleConfirmation(reference);
			
		} 
		catch (CarException ce) {
			adventure.setState(State.UNDO);
			return;
		}
		catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.UNDO);
			}
			return;
		}

		adventure.setState(State.CONFIRMED);

	}
}
