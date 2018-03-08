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
		if(date.getYear() < 1970){
			throw new TaxException();
		}
		
		this.sellerNIF = sellerNIF;
		this.buyerNIF = buyerNIF;
		this.itemType = itemType;
		this.value = value;
		this.date = date;
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
