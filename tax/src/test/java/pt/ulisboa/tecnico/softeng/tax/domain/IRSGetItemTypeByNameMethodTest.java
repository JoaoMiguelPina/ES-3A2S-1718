package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRSGetItemTypeByNameMethodTest {
	private static final String ITEM_TYPE_NAME1 = "Cereais";
	private static final int IVA1 = 23;

	private static final String ITEM_TYPE_NAME2 = "Mercearia";
	private static final int IVA2 = 10;
		
	
	@Test
	public void nullParameter() {		
		ItemType item = new ItemType(ITEM_TYPE_NAME1, IVA1);
		
		assertNull(IRS.getItemByName(null));
	}
	
	@Test
	public void noItemType() {		
		assertNull(IRS.getItemByName(ITEM_TYPE_NAME1));
	}
	
	@Test
	public void oneItemTypeSucess() {
		ItemType expected = new ItemType(ITEM_TYPE_NAME1, IVA1);
		ItemType obtained = IRS.getItemByName(ITEM_TYPE_NAME1);
		
		assertEquals(ITEM_TYPE_NAME1, obtained.getName());
		assertEquals(IVA1, obtained.getTax());
		assertEquals(expected, obtained);
	}
	
	@Test
	public void oneItemTypeFail() {
		ItemType item = new ItemType(ITEM_TYPE_NAME1, IVA1);
		ItemType obtained = IRS.getItemByName(ITEM_TYPE_NAME2);
		
		assertNull(obtained);
	}
	
	@Test
	public void twoItemTypeSucess() {
		ItemType expected = new ItemType(ITEM_TYPE_NAME1, IVA1);
		new ItemType(ITEM_TYPE_NAME2, IVA2);
		
		ItemType obtained = IRS.getItemByName(ITEM_TYPE_NAME1);
		
		assertEquals(ITEM_TYPE_NAME1, obtained.getName());
		assertEquals(IVA1, obtained.getTax());
		assertEquals(expected, obtained);
	}
	
	@Test
	public void twoItemTypeFail() {
		ItemType item1 = new ItemType(ITEM_TYPE_NAME1, IVA1);
		ItemType item2 = new ItemType(ITEM_TYPE_NAME2, IVA2);
		
		ItemType obtained = IRS.getItemByName("Teste");
		
		assertNull(obtained);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}

}
