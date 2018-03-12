package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceConstructorTest {
	private static final float VALUE = (float) 3.6;
	private static final LocalDate DATE = new LocalDate(2018, 3, 6); 
	private static final int IVA = 23;
	private static final ItemType ITEM_TYPE = new ItemType(IVA);
	private static final Seller SELLER = new Seller("500192612", "Alberto, Lda", "Rua José Pacheco");
	private static final Buyer BUYER = new Buyer("225031690", "António", "Rua Nova");
	
	
	@Test
	public void success() {
		Invoice invoice = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, BUYER);
		
		Assert.assertEquals(VALUE, invoice.getValue());
		Assert.assertEquals(DATE, invoice.getDate());
		Assert.assertEquals(IVA, invoice.getIva());
		Assert.assertEquals(SELLER.getNif(), invoice.getSeller().getNif());
		Assert.assertEquals(BUYER.getNif(), invoice.getBuyer().getNif());
		Assert.assertEquals(1, BUYER.getNumberOfInvoices());
		Assert.assertEquals(1, SELLER.getNumberOfInvoices());
	}
	
	/* NULL */
	
	@Test(expected = TaxException.class)
	public void nullValue() {
		new Invoice(null, DATE, ITEM_TYPE, SELLER, BUYER);
	}
	
	@Test(expected = TaxException.class)
	public void nullDate() {
		new Invoice(VALUE, null, ITEM_TYPE, SELLER, BUYER);
	}
	
	@Test(expected = TaxException.class)
	public void nullItemType() {
		new Invoice(VALUE, DATE, null, SELLER, BUYER);
	}
	
	@Test(expected = TaxException.class)
	public void nullBuyer() {
		new Invoice(VALUE, DATE, ITEM_TYPE, null, BUYER);
	}
	
	@Test(expected = TaxException.class)
	public void nullSeller() {
		new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, null);
	}
	
	
	
	@Test(expected = TaxException.class)
	public void negativeValue() {
		new Invoice(-20.6, DATE, ITEM_TYPE, SELLER, BUYER);
	}
	
	@Test(expected = TaxException.class)
	public void notBefore1970Date() {
		newDATE = new LocalDate(1965, 3, 6); 
		new Invoice(VALUE, newDATE, ITEM_TYPE, SELLER, BUYER);
	}
	
}
