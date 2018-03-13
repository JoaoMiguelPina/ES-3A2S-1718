package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class Invoice {
	
	private static int counter = 0;
	private final String reference;
	private final float value;
	private final float iva;
	private final LocalDate date;
	private final Seller seller;
	private final Buyer buyer;
	private final ItemType itemType; 
	
	public Invoice(float value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer){
		checkArguments(value, date, itemType, seller, buyer);
		
		this.reference = Integer.toString(++Invoice.counter);
		this.value = value;
		this.date = date;
		this.itemType = itemType;
		this.seller = seller;
		this.buyer = buyer;
		this.iva = this.itemType.getTax() * value;
	}
	
	private void checkArguments(float value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer)	{
		checkValue(value);
		checkDate(date);
		checkItemType(itemType);
		checkSeller(seller);
		checkBuyer(buyer);
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
	
	private void checkItemType(ItemType itemType){
		if(itemType == null){
			throw new TaxException();
		}
	}
	
	private void checkSeller(Seller seller){
		if(seller == null){
			throw new TaxException();
		}
	}
	
	private void checkBuyer(Buyer buyer){
		if(buyer == null){
			throw new TaxException();
		}
	}

	public static void setCounter(int counter) {
		Invoice.counter = counter;
	}

	public float getValue() {
		return this.value;
	}

	public float getIva() {
		return this.iva;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public Buyer getBuyer() {
		return this.buyer;
	}

	public String getReference() {
		return this.reference;
	}

	public Seller getSeller() {
		return seller;
	}
	
}
