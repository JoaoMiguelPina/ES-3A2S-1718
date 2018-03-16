package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.*;

public abstract class TaxPayer {
	private String nif;
	private String name;
	private String address;
	private IRS irs;
	private Map<String, Invoice> invoices;
	
	public TaxPayer(String nif, String name, String address){
		this.irs = IRS.getInstance();
		checkArguments(nif, name, address);
		
		this.nif = nif;
		this.name = name;
		this.address = address;
		
		this.invoices = new HashMap<>();
		
		irs.addTaxPayer(this);
	}
	
	private void checkArguments(String nif, String name, String address){
		checkNif(nif);
		checkName(name);
		checkAddress(address);
	}
	
	private void checkNif(String nif) {
		if(nif == null || nif.equals("") || (nif.trim()).equals("")){
			throw new TaxException();
		}
		if(nif.length() != 9){
			throw new TaxException();
		}
		if(!nif.matches("[0-9]+")){
			throw new TaxException();
		}
		if(IRS.getTaxPayerByNIF(nif) != null){
			throw new TaxException();
		}
	}

	private void checkName(String name) {
		if(name == null || name.equals("") || (name.trim()).equals("")){
			throw new TaxException();
		}		
	}

	private void checkAddress(String address) {
		if(address == null || address.equals("") || (address.trim()).equals("")){
			throw new TaxException();
		}		
	}

	public String getNif() {
		return nif;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public int getNumberOfInvoices(){
		return this.invoices.size();
	}
	
	public Invoice getInvoiceByReference(String reference){
		return invoices.get(reference);
	}
	
	protected float getIvaValueByYear(int year) {
		float totalIVA = 0;
		
		if (year < 1970) {
			throw new TaxException();
		}
		
		for (Invoice i: this.invoices.values()) {
			LocalDate date = i.getDate();
			int comparingYear = date.getYear();
			if (comparingYear == year) {
				totalIVA += i.getIva();
			}
		}
		return totalIVA;
	}
	
	public void addInvoice(Invoice invoice){
		invoices.put(invoice.getReference(), invoice);
	}
	
	public void clear(){
		invoices.clear();
	}
}
