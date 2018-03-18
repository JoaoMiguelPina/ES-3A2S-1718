package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class BuyerTaxReturnMethodTest {
	private static final float VALUE1 = (float) 101.0;
	private static final LocalDate DATE1 = new LocalDate(2018, 7, 4); 
	private static final String ITEM_TYPE_NAME1 = "Cereais";
	private int IVA1 = 23;
	private ItemType itemType1;
	private Seller seller1;
	private Buyer buyer1;
	
	private static final float VALUE2 = (float) 100.0;
	private static final LocalDate DATE2 = new LocalDate(2018, 10, 9); 
	private static final String ITEM_TYPE_NAME2 = "Mercearia";
	private static final int IVA2 = 15;
	private ItemType itemType2;
	
	Invoice invoice1;
	Invoice invoice2;
	
	@Before
	public void setUp() {
		this.itemType1 = new ItemType(ITEM_TYPE_NAME1, IVA1);
		this.seller1 = new Seller("500192612", "Alberto, Lda", "Rua José Pacheco");
		this.buyer1 = new Buyer("225031690", "António", "Rua Nova");

		this.itemType2 = new ItemType(ITEM_TYPE_NAME2, IVA2);
		
		this.invoice1 = new Invoice(VALUE1, DATE1, this.itemType1, this.seller1, this.buyer1);
		this.invoice2 = new Invoice(VALUE2, DATE2, this.itemType2, this.seller1, this.buyer1);
	}
	
	@Test
	public void noInvoices(){
		this.buyer1.clear();
		assertEquals(0, this.buyer1.taxReturn(2018), 0);
	}
	
	@Test
	public void oneInvoice(){
		this.buyer1.clear();
		this.invoice1 = new Invoice(VALUE1, DATE1, this.itemType1, this.seller1, this.buyer1);
		float expect = (float) (this.invoice1.getIva() * 0.05);
		assertEquals(expect, this.buyer1.taxReturn(2018), 0);
	}
	
	@Test
	public void twoInvoicesInSameYear() {	
		float test = this.buyer1.taxReturn(2018);
		float expect = (float) ((invoice1.getIva() + invoice2.getIva())*0.05);
		Assert.assertEquals(expect, test, 0);
	}
	
	@Test
	public void twoInvoicesInSameYearAndOtherInvoiceInOther() {	
		new Invoice(VALUE2, new LocalDate(2017,6,7), this.itemType2, this.seller1, this.buyer1);
		float test = this.buyer1.taxReturn(2018);
		float expect = (float) ((invoice1.getIva() + invoice2.getIva())*0.05);
		Assert.assertEquals(expect, test, 0);
	}
	
	@Test
	public void noYearTax() {
		float test = this.buyer1.taxReturn(2017);
		Assert.assertEquals(0, test, 0);
	}
	
	@Test(expected = TaxException.class)
	public void yearBefore1970Tax() {
		this.buyer1.taxReturn(1969);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}
}
