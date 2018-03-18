package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {
	private int tax;
	private String name;
	private IRS irs;
	private Map<String, Invoice> invoices;
	
	/*Tax is in percentage! Between 0% and 100% */
	public ItemType(String name, int tax) {
		checkArguments(tax, name);
		
		this.name = name;
		this.tax = tax;
		this.irs = IRS.getInstance();
		this.invoices = new HashMap<>();
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
		return this.invoices.get(reference);
	}
	
	public void addInvoice(Invoice invoice){
		this.invoices.put(invoice.getReference(), invoice);
	}
	
	public void clear(){
		invoices.clear();
	}
}
