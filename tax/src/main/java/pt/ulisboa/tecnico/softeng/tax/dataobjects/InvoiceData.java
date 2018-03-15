package pt.ulisboa.tecnico.softeng.tax.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceData {
	private String sellerNIF;
	private String buyerNIF;
	private String itemType;
	private float value;
	private LocalDate date;
	
	public InvoiceData(String sellerNIF, String buyerNIF, String itemType, float value, LocalDate date) {
		checkArguments(sellerNIF, buyerNIF, itemType, value, date);
		
		this.sellerNIF = sellerNIF;
		this.buyerNIF = buyerNIF;
		this.itemType = itemType;
		this.value = value;
		this.date = date;
	}
	
	private void checkArguments(String sellerNIF, String buyerNIF, String itemType, float value, LocalDate date) {
		checkNIF(sellerNIF);
		checkNIF(buyerNIF);
		checkItemType(itemType);
		checkValue(value);
		checkDate(date);
	}
	
	private void checkNIF(String nif){
		if(nif == null || nif.equals("") || (nif.trim()).equals("")){
			throw new TaxException();
		}
		if(nif.length() != 9){
			throw new TaxException();
		}
		if(!nif.matches("[0-9]+")){
			throw new TaxException();
		}
	}

	private void checkItemType(String itemType){
		if(itemType == null || itemType.equals("") || (itemType.trim()).equals("")){
			throw new TaxException();
		}
	}
	
	private void checkValue(float value){
		if(value < 0){
			throw new TaxException();
		}
	}
	
	private void checkDate(LocalDate date){
		if(date == null){
			throw new TaxException();
		}
		if(date.getYear() < 1970){
			throw new TaxException();
		}
	}
	
	public String getSellerNIF() {
		return sellerNIF;
	}

	public String getBuyerNIF() {
		return buyerNIF;
	}

	public String getItemType() {
		return itemType;
	}

	public float getValue() {
		return value;
	}

	public LocalDate getDate() {
		return date;
	}
}
