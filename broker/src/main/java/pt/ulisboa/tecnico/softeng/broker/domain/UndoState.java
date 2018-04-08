package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class UndoState extends AdventureState {

	@Override
	public State getState() {
		return State.UNDO;
	}

	@Override
	public void process(Adventure adventure) {
		if (requiresCancelPayment(adventure)) {
			try {
				adventure.setPaymentCancellation(BankInterface.cancelPayment(adventure.getPaymentConfirmation()));
			} catch (BankException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (requiresCancelActivity(adventure)) {
			try {
				adventure.setActivityCancellation(
						ActivityInterface.cancelReservation(adventure.getActivityConfirmation()));
			} catch (ActivityException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (requiresCancelRoom(adventure)) {
			try {
				adventure.setRoomCancellation(HotelInterface.cancelBooking(adventure.getRoomConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				// does not change state
			}
		}
		
		if (requiresCancelInvoice(adventure)) {
			try {
				adventure.setInvoiceCancellation(TaxInterface.cancelInvoice(adventure.getInvoiceConfirmation()));
			} catch (TaxException | RemoteAccessException ex) {
				// does not change state
			}
		}
		
		if (requiresCancelVehicle(adventure)) {
			try {
				adventure.setVehicleCancellation(CarInterface.cancelReservation(adventure.getVehicleConfirmation()));
			} catch (CarException | RemoteAccessException ex) {
				// does not change state
			}
		}
		
		if (!requiresCancelPayment(adventure) && !requiresCancelActivity(adventure) && !requiresCancelRoom(adventure) && !requiresCancelInvoice(adventure) && !requiresCancelVehicle(adventure)) {
			adventure.setState(State.CANCELLED);
		}
	}

	public boolean requiresCancelPayment(Adventure adventure) {
		return adventure.requiresCancelPayment();
	}
	
	public boolean requiresCancelActivity(Adventure adventure) {
		return adventure.requiresCancelActivity();
	}
	
	public boolean requiresCancelRoom(Adventure adventure) {
		return adventure.requiresCancelRoom();
	}

	public boolean requiresCancelInvoice(Adventure adventure) {
		return adventure.requiresCancelInvoice();
	}
	
	public boolean requiresCancelVehicle(Adventure adventure) {
		return adventure.requiresCancelVehicle();
	}
}
/*
Create method requiresCancelVehicle, should return the result of adventure.cancelVehicle() (check #207).
Create method requiresCancelInvoice, should return the result of adventure.cancelInvoice() (check #207).
Add the new 2 options: possibility to cancel a vehicle and to cancel the invoice.
Change the last ifto take in consideration last point.*/