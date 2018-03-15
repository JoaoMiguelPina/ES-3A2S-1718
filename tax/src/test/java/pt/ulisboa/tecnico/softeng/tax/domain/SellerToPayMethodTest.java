package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerToPayMethodTest {
	private static float VALUE1 = (float) 100.0;
	private static final LocalDate DATE1 = new LocalDate(2018, 3, 6);
	private static final String ITEM_TYPE_NAME1 = "Cereais";
	private static final int IVA1 = 23;
	private ItemType itemType1;
	private Seller seller1;
	private Buyer buyer1;	
	
	private static final float VALUE2 = (float) 1000.0;
	private static final LocalDate DATE2 = new LocalDate(2018, 10, 9); 
	private static final String ITEM_TYPE_NAME2 = "Mercearia";
	private static final int IVA2 = 10;
	private ItemType itemType2;
	private Buyer buyer2;
	
	Invoice invoice1;
	Invoice invoice2;
	
	@Before
	public void setUp() {
		this.itemType1 = new ItemType(ITEM_TYPE_NAME1, IVA1);
		this.seller1 = new Seller("500192612", "Alberto, Lda", "Rua José Pacheco");
		this.buyer1 = new Buyer("225031690", "António", "Rua Nova");

		this.itemType2 = new ItemType(ITEM_TYPE_NAME2, IVA2);
		this.buyer2 = new Buyer("225031390", "José", "Rua Nova Areeiro");
		
		this.invoice1 = new Invoice(VALUE1, DATE1, this.itemType1, this.seller1, this.buyer1);
		this.invoice2 = new Invoice(VALUE2, DATE2, this.itemType2, this.seller1, this.buyer2);
	}
	
	@Test
	public void success() {
		float test = this.seller1.toPay(2018);
		float expect = this.invoice1.getIva() + this.invoice2.getIva();
		Assert.assertEquals(expect, test, 0);
	}
	
	@Test(expected = TaxException.class)
	public void noYear() {
		float test = this.seller1.toPay(2017);
		float expect = this.invoice1.getIva() + this.invoice2.getIva();
		Assert.assertEquals(expect, test, 0);
	}
	
	
	@Test(expected = TaxException.class)
	public void yearBefore1970() {
		this.seller1.toPay(1969);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}
	

}
