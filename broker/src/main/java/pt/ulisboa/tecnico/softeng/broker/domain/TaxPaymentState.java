package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class TaxPaymentState extends AdventureState {

	private static final int MAX_REMOTE_ERRORS = 5;

	@Override
	public State getState() {
		return State.TAX_PAYMENT;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			InvoiceData invoiceData = new InvoiceData(adventure.getBroker().getNifAsSeller(), 
													   adventure.getClient().getNif(), 
													   "ADVENTURE", 
													   adventure.getAmount() + adventure.getAmount()*adventure.getMarginOfProfit(), 
													   adventure.getBegin());
			adventure.setInvoiceConfirmation(TaxInterface.submitInvoice(invoiceData));
			
		} 
		catch (TaxException te) {
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
