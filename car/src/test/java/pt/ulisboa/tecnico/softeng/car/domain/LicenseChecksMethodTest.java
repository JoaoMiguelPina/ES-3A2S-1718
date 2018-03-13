package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.Car;
import pt.ulisboa.tecnico.softeng.car.RentACar;

public class LicenseChecksMethodTest {
	private Car car;
	private RentACar rentACar;

	@Before
	public void setUp() {
		
		RentingData rd = new rentingdata("1234", "12-13-GF", "123457EFR", "9090", 10-12-12, 14-2-12);
	}
	
	@Test
	public void checks(){
		//License needs to be created with letters followed by numbers AAAAA111111
		String iterated = rd.getLicense();
		int tamanhoTotal = iterated.length();
		int tamanhoLetras = 0;
		//checking that the letters come first
		for(int i= 0; i >= 65 && i < 122; i++) {
			tamanhoLetras ++;
			continue;
		}
		Assert.assertTrue(tamanhoLetras>=1);
		//checking for the numbers
		for(int i= tamanhoLetras; i < tamanhoTotal; i++) {
			Assert.assertFalse(iterated.charAt(i) >= 65 && iterated.charAt(i) < 122);		
		}
	}
	
	@After
	public void tearDown() {
		rd.destroyRentingData();
	}

}
