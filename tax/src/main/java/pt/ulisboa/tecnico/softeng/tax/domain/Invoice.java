package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;


public class Invoice {
	
	private static int counter = 0;
	private final String reference;
	private final float value;
	private final float iva;
	private final LocalDate date;
	private final Seller seller;
	private final Buyer buyer;
	private ItemType ITEM_TYPE; 
	
	public Invoice(float val, LocalDate data, ItemType iType, Seller s, Buyer b){
		this.reference = Integer.toString(++Invoice.counter);
		this.value = val;
		this.date =data;
		this.ITEM_TYPE = iType;
		this.seller = s;
		this.buyer = b;
		this.iva = this.ITEM_TYPE.getTax();
	}

	public int getCounter() {
		return this.counter;
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
