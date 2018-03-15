package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {
	private int tax;
	private String name;
	private IRS irs;
	private Set<Invoice> invoices;
	
	/*Tax is in percentage! Between 0*/
	public ItemType(String name, int tax) {
		checkArguments(tax, name);
		
		this.tax = tax;
		this.irs = IRS.getInstance();
		this.invoices = new HashSet<>();
		irs.addItemType(this);
	}
	
	private void checkArguments(int tax, String name){
		checkTax(tax);
		checkName(name);
	}
	
	private void checkTax(int tax){
		if (tax < 0 || tax > 100) {
			throw new TaxException(); 
		}
	}
	
	private void checkName(String name){
		if(name == null || name.equals("") || (name.trim()).equals("")){
			throw new TaxException();
		}	
		if(IRS.getItemTypeByName(name) != null){
			throw new TaxException();
		}
	}
	
	public int getTax() {
		return this.tax;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNumberOfInvoices(){
		return this.invoices.size();
	}
	
	public Invoice getInvoiceByReference(String reference) {
		for(Invoice i: this.invoices) {
			if (i.getReference() == reference) {
				return i;
			}
		}
		throw new TaxException();
	}
	
	public void addInvoice(Invoice invoice){
		this.invoices.add(invoice);
	}
	
}
