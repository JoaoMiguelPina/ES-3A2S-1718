package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {
	private int tax;
	private IRS irs;
	private Set<Invoice> invoices;
	
	public ItemType(int new_tax) {
		if (new_tax < 0 || new_tax > 100) {
			throw new TaxException(); 
		}
		this.tax = new_tax;
		this.irs = IRS.getInstance();
		this.invoices = new HashSet<>();
		irs.addItemType(this);
	}

	public int getTax() {
		return this.tax;
	}
	
	public int getNumberOfInvoices(){
		return this.invoices.size();
	}
	
	public Invoice getInvoiceByReference(String reference){
		for(Invoice i: this.invoices) {
			if (i.getReference() == reference) {
				return i;
			}
		}
		return null;
	}
	
	public void addInvoice(Invoice invoice){
		this.invoices.add(invoice);
	}
	
}
