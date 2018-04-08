package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.hotel.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Processor {
	// important to use a set to avoid double submission of the same booking when it
	// is cancelled while trying to pay or send invoice
	private final Set<Booking> bookingToProcess = new HashSet<>();

	public void submitBooking(Booking booking) {
		this.bookingToProcess.add(booking);
		processInvoices();
	}

	private void processInvoices() {
		Set<Booking> failedToProcess = new HashSet<>();
		for (Booking booking : this.bookingToProcess) {
			if (!booking.isCancelled()) {
				if (booking.getPaymentReference() == null) {
					try {
						String ref = BankInterface.processPayment(booking.getIban(), booking.getAmount());
						booking.setPaymentReference(ref);
						BankInterface.getOperationData(ref);
					} catch (BankException | RemoteAccessException ex) {
						failedToProcess.add(booking);
						continue;
					}
				}
				InvoiceData invoiceData = new InvoiceData(booking.getHotelNif(), booking.getNif(), booking.getType(),
						booking.getAmount(), booking.getArrival());
				try {
					String invoice = TaxInterface.submitInvoice(invoiceData);
					booking.setInvoiceReference(invoice);
					TaxInterface.getInvoiceData(invoice);
				} catch (TaxException | RemoteAccessException ex) {
					failedToProcess.add(booking);
				}
				
				
			} else {
				try {
					if (booking.getCancelledPaymentReference() == null) {
						booking.setCancelledPaymentReference(
								BankInterface.cancelPayment(booking.getPaymentReference()));
								BankInterface.getOperationData(booking.getReference());
					}
					String ref = TaxInterface.cancelInvoice(booking.getInvoiceReference());
					booking.setCancelledInvoice(true);
					TaxInterface.getInvoiceData(ref);
					
				} catch (BankException | TaxException | RemoteAccessException ex) {
					failedToProcess.add(booking);
				}

			}
		}

		this.bookingToProcess.clear();
		this.bookingToProcess.addAll(failedToProcess);

	}

	public void clean() {
		this.bookingToProcess.clear();
	}

}
