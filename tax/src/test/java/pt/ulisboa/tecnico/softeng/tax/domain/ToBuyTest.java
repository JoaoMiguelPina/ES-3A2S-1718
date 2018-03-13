package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ToBuyTest {
	private static final float VALUE1 = (float) 100.0;
	private static final LocalDate DATE1 = new LocalDate(2018, 3, 6); 
	private static final int IVA1 = 23;
	private static final ItemType ITEM_TYPE1 = new ItemType(IVA1);
	private static final Seller SELLER1 = new Seller("500192612", "Alberto, Lda", "Rua José Pacheco");
	private static final Buyer BUYER1 = new Buyer("225031690", "António", "Rua Nova");
	
	Invoice invoice1 = new Invoice(VALUE1, DATE1, ITEM_TYPE1, SELLER1, BUYER1);
	
	private static final float VALUE2 = (float) 90.0;
	private static final LocalDate DATE2 = new LocalDate(2018, 10, 9); 
	private static final int IVA2 = 23;
	private static final ItemType ITEM_TYPE2 = new ItemType(IVA2);
	private static final Buyer BUYER2 = new Buyer("225031390", "José", "Rua Nova Areeiro");
	

	Invoice invoice2 = new Invoice(VALUE2, DATE2, ITEM_TYPE2, SELLER1, BUYER2);
	
	@SuppressWarnings("deprecation")
	@Test
	public void success1() {
		float test = SELLER1.toPay(2018);
		float expect = invoice1.getIva();
		Assert.assertEquals(expect, test, 0);
	}
	
	@Test
	public void success2() {
		float test = SELLER1.toPay(2018);
		float expect = invoice1.getIva() + invoice2.getIva();
		Assert.assertEquals(expect, test, 0);
	}
	
	@Test(expected = TaxException.class)
	public void yearBefore1970() {
		SELLER1.toPay(1969);
	}
	
	

}
