package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceConstructorTest {
	private static final String REFERENCE = "fatura";
	private static final float VALUE = (float) 3.6;
	private static final float IVA = (float) 0.23;
	private static final LocalDate date = new LocalDate(2018, 3, 6); 
	
	@Test
	public void success() {
		Invoice invoice = new Invoice(REFERENCE, VALUE, IVA, date);
		
		Assert.assertEquals(REFERENCE, invoice.getReference());
		Assert.assertEquals(VALUE, invoice.getValue());
		Assert.assertEquals(IVA, invoice.getIva());
		Assert.assertEquals(date, invoice.getDate());
	}
	
	@Test(expected = TaxException.class)
	public void nullReference() {
		new Invoice(null, VALUE, IVA, date);
	}
	
	@Test(expected = TaxException.class)
	public void emptyReference() {
		new Invoice("", VALUE, IVA, date);
	}
	
	@Test(expected = TaxException.class)
	public void blankReference() {
		new Invoice(" ", VALUE, IVA, date);
	}
	
	@Test(expected = TaxException.class)
	public void nullValue() {
		new Invoice(REFERENCE, null, IVA, date);
	}
	
	@Test(expected = TaxException.class)
	public void nullIva() {
		new Invoice(REFERENCE, VALUE, null, date);
	}
	
	@Test(expected = TaxException.class)
	public void nullDate() {
		new Invoice(REFERENCE, VALUE, IVA, null);
	}
	
	@Test(expected = TaxException.class)
	public void wrongDate() {
		new Invoice(REFERENCE, VALUE, IVA, new LocalDate(1969, 12 , 31));
	}
	

	@After
	public void tearDown() {
		
	}

}
