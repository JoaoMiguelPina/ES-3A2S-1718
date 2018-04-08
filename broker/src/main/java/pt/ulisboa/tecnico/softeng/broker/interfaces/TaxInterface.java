package pt.ulisboa.tecnico.softeng.broker.interfaces;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;

public class TaxInterface {
	public static String submitInvoice(InvoiceData invoiceData) {
		return IRS.submitInvoice(invoiceData);
	}

	public static String cancelInvoice(String invoiceReference) {
		return IRS.cancelInvoice(invoiceReference);
	}
	
	public static InvoiceData getInvoiceData(String reference){
		return IRS.getInvoiceData(reference);
	}

}
