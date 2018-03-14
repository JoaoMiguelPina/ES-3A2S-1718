package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class TaxReturnMethodTest {
	private static final float VALUE1 = (float) 101.0;
	private static final LocalDate DATE1 = new LocalDate(2018, 7, 4); 
	private static final int IVA1 = 23;
	private static final ItemType ITEM_TYPE1 = new ItemType(IVA1);
	private static final Seller SELLER1 = new Seller("500142612", "Alberto, Lda", "Rua José Pacheco");
	private static final Buyer BUYER1 = new Buyer("225231690", "António", "Rua Nova");
	
	
	
	private static final float VALUE2 = (float) 100.0;
	private static final LocalDate DATE2 = new LocalDate(2018, 10, 9); 
	private static final int IVA2 = 23;
	private static final ItemType ITEM_TYPE2 = new ItemType(IVA2);
	private static final Buyer BUYER2 = new Buyer("225031390", "José", "Rua Nova Areeiro");
	
	Invoice invoice1 = new Invoice(VALUE1, DATE1, ITEM_TYPE1, SELLER1, BUYER1);
	Invoice invoice2 = new Invoice(VALUE2, DATE2, ITEM_TYPE2, SELLER1, BUYER2);
	
	
	@Test
	public void success() {
		
		float test = BUYER1.taxReturn(2018);
		float expect = (float) (invoice1.getIva()*0.05);
		Assert.assertEquals(expect, test, 0);
	}
	
	@Test(expected = TaxException.class)
	public void noYearTax() {
		float test = BUYER1.taxReturn(2017);
		float expect = invoice1.getIva() + invoice2.getIva();
		Assert.assertEquals(expect, test, 0);
	}
	
	
	@Test(expected = TaxException.class)
	public void yearBefore1970Tax() {
		BUYER1.taxReturn(1969);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}

}
