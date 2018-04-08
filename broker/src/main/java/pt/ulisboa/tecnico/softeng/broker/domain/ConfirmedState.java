package pt.ulisboa.tecnico.softeng.broker.domain;

import java.rmi.RemoteException;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ConfirmedState extends AdventureState {
	public static int MAX_BANK_EXCEPTIONS = 5;

	private int numberOfBankExceptions = 0;

	@Override
	public State getState() {
		return State.CONFIRMED;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			BankInterface.getOperationData(adventure.getPaymentConfirmation());
		} catch (BankException be) {
			this.numberOfBankExceptions++;
			if (this.numberOfBankExceptions == MAX_BANK_EXCEPTIONS) {
				adventure.setState(State.UNDO);
			}
			return;
		} catch (RemoteAccessException rae) {
			adventure.setState(State.CONFIRMED);
			return;
		}
		
		this.numberOfBankExceptions = 0;

		try {
			ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation());
		} catch (ActivityException ae) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			adventure.setState(State.CONFIRMED);
			return;
		}

		if (adventure.getRoomConfirmation() != null) {
			try {
				HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
			} catch (HotelException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				adventure.setState(State.CONFIRMED);
				return;
			}
		}
		
		if (adventure.getVehicleConfirmation() != null) {
			try {
				CarInterface.getRentingData(adventure.getVehicleConfirmation());
			} catch (CarException ce) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				adventure.setState(State.CONFIRMED);
				return;
			}
		}
		
		if (adventure.getInvoiceConfirmation() != null) {
			try {
				TaxInterface.getInvoiceData(adventure.getInvoiceConfirmation());
			} catch (TaxException e) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				adventure.setState(State.CONFIRMED);
				return;
			}
		}

		// TODO: prints the complete Adventure file, the info in operation,
		// reservation and booking

	}

}
