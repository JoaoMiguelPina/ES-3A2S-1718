package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceConstructorTest {
	private static final float VALUE = (float) 100.0;
	private static final LocalDate DATE = new LocalDate(2018, 3, 6); 
	private static final int IVA = 23;
	private static final String TYPE_NAME = "Mercearia";
	private static final ItemType ITEM_TYPE = new ItemType(TYPE_NAME, IVA);
	private static final Seller SELLER = new Seller("500192612", "Alberto, Lda", "Rua José Pacheco");
	private static final Buyer BUYER = new Buyer("225031690", "António", "Rua Nova");
	
	
	@Test
	public void success() {
		Invoice invoice = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, BUYER);
		
		Assert.assertEquals(VALUE, invoice.getValue(), 0);
		Assert.assertEquals(DATE, invoice.getDate());
		Assert.assertEquals((float) 23.0, invoice.getIva(), 0);
		Assert.assertEquals(SELLER.getNif(), invoice.getSeller().getNif());
		Assert.assertEquals(BUYER.getNif(), invoice.getBuyer().getNif());
		Assert.assertEquals(1, BUYER.getNumberOfInvoices());
		Assert.assertEquals(1, SELLER.getNumberOfInvoices());
		Assert.assertEquals(1, ITEM_TYPE.getNumberOfInvoices());
	}
	
	
	
	@Test
	public void checkMatchingItemType() {
		Invoice newInvoice = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, BUYER);
		
		Invoice obtained = ITEM_TYPE.getInvoiceByReference(newInvoice.getReference());
		
		assertEquals(newInvoice, obtained);
	}
	
	/* NULL */
	
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
	
	
	@Test
	public void IVAequalsTAX() {
		
		ItemType testItemType = new ItemType(TYPE_NAME, 10);
		Invoice invoice = new Invoice(VALUE, DATE, testItemType, SELLER, BUYER);
		assertEquals(invoice.getIva(), testItemType.getTax(), 0);
	}
	
	@Test(expected = TaxException.class)
	public void overIVA() {
		int testIVA = 120;
		ItemType testItemType = new ItemType(TYPE_NAME, testIVA);
		new Invoice(VALUE, DATE, testItemType, SELLER, BUYER);
	}
	
	/* NEGATIVE VALUES */
	
	@Test(expected = TaxException.class)
	public void negativeValue1() {
		new Invoice((float)-20.6, DATE, ITEM_TYPE, SELLER, BUYER);
	}
	
	@Test(expected = TaxException.class)
	public void negativeValue2() {
		new Invoice((float)-0.2, DATE, ITEM_TYPE, SELLER, BUYER);
	}
	
	@Test
	public void positiveValue() {
		float f = (float) 0.4;
		Invoice testIn = new Invoice(f, DATE, ITEM_TYPE, SELLER, BUYER);
		assertEquals(f, testIn.getValue(), 0);
	}
	
	
	/* NEGATIVE VALUES */
	
	
	@Test(expected = TaxException.class)
	public void notBefore1904() {
		LocalDate newDATE = new LocalDate(1904, 3, 6); 
		new Invoice(VALUE, newDATE, ITEM_TYPE, SELLER, BUYER);
	}
	
	@Test(expected = TaxException.class)
	public void notBefore1969() {
		LocalDate newDATE = new LocalDate(1969, 12, 31); 
		new Invoice(VALUE, newDATE, ITEM_TYPE, SELLER, BUYER);
	}
	
	@Test
	public void notBefore1970() {
		LocalDate newDate = new LocalDate(1970, 1, 1); 
		Invoice testIn = new Invoice(VALUE, newDate, ITEM_TYPE, SELLER, BUYER);
		assertEquals(newDate, testIn.getDate());
	}
	
	
	@Test
	public void uniqueReference(){
		Invoice invoice1 = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, BUYER);
		Invoice invoice2 = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, BUYER);
		Assert.assertNotEquals(invoice1.getReference(), invoice2.getReference());
	}
	
	
	/* RETURN CORRECT NUMBER OF INVOICES */
	

	@Test
	public void zeroInvoices() {
		ItemType iTYPE = new ItemType("teste", IVA);
		int numberInvoices = iTYPE.getNumberOfInvoices();
		assertEquals(0, numberInvoices);
	}
	
	
	@Test
	public void oneInvoices() {
		ItemType iTYPE = new ItemType("teste", IVA);
		new Invoice(VALUE, DATE, iTYPE, SELLER, BUYER);
		int numberInvoices = iTYPE.getNumberOfInvoices();
		assertEquals(1, numberInvoices);
	}
	
	
	@Test
	public void twoInvoices() {
		ItemType iTYPE = new ItemType("teste", IVA);
		new Invoice(VALUE, DATE, iTYPE, SELLER, BUYER);
		new Invoice(VALUE, DATE, iTYPE, SELLER, BUYER);
		int numberInvoices = iTYPE.getNumberOfInvoices();
		assertEquals(2, numberInvoices);
	}
	
	
	@After
	public void tearDown() {
		IRS.clear();
	}	
}
