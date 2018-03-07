package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BuyerConstructorTest {
	private final Buyer buyer;
	
	private final String NIF = "225506740";
	private final String NAME = "Ronnie O'Sullivan";
	private final String ADDRESS = "Rua Agusta nº3";
	
	@Test
	public void sucess() {
		buyer = new Buyer(NIF, NAME, ADDRESS);
		
		assertEquals(NIF, buyer.getNIF());		
		assertEquals(NAME, buyer.getName());
		assertEquals(ADDRESS, buyer.getAddress());
		assertEquals(1, IRS.taxPayers.size());
		assertEquals(0, buyer.getNumberOfInvoice());
	}
	
	@After
	public void tearDown() {
		IRS.taxPayers.clear();
	}

}
