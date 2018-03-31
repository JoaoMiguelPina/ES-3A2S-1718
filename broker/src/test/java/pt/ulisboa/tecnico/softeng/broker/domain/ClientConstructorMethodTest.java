package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class ClientConstructorMethodTest {
	private static Broker BROKER;
	private static final String IBAN = "11011";
	private static final String NIF = "225031999";
	private static final String DRIVING_LICENSE = "IMT123";
	private static final int AGE = 20;
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 100;

	@Before
	public void setUp(){
		this.BROKER = new Broker("BR01", "WeExplore", "123456789", "987654321", "123");
	}
	
	@Test
	public void sucess(){
		Client client = new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, AGE);
		
		assertEquals(BROKER, client.getBroker());
		assertEquals(IBAN, client.getIban());
		assertEquals(NIF, client.getNif());
		assertEquals(DRIVING_LICENSE, client.getDrivingLicense());
		assertEquals(AGE, client.getAge());
		assertEquals(client, BROKER.getClientByNif(NIF));
	}
	
	/* NULL ARGUMENTS
	 * @note driving license can be null, a client might not have one
	 */
	@Test(expected = BrokerException.class)
	public void nullBroker(){
		new Client(null, IBAN, NIF, DRIVING_LICENSE, AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void nullIBAN(){
		new Client(BROKER, null, NIF, DRIVING_LICENSE, AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void nullNIF(){
		new Client(BROKER, IBAN, null, DRIVING_LICENSE, AGE);
	}
	
	/*Blank and empty arguments*/
	@Test(expected = BrokerException.class)
	public void emptyIBAN(){
		new Client(BROKER, "", NIF, DRIVING_LICENSE, AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void blankIBAN(){
		new Client(BROKER, "    ", NIF, DRIVING_LICENSE, AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void emptyNIF(){
		new Client(BROKER, IBAN, "", DRIVING_LICENSE, AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void blankNIF(){
		new Client(BROKER, IBAN, "    ", DRIVING_LICENSE, AGE);
	}
	
	/*Boundary limits tests for age*/
	@Test(expected = BrokerException.class)
	public void lessThanMinAge(){
		new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, MIN_AGE/2);
	}
	
	@Test(expected = BrokerException.class)
	public void minAgeMinusOne(){
		new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, MIN_AGE - 1);
	}
	
	@Test
	public void ageEqualsMinAge(){
		Client client = new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, MIN_AGE);
		
		assertEquals(BROKER, client.getBroker());
		assertEquals(IBAN, client.getIban());
		assertEquals(NIF, client.getNif());
		assertEquals(DRIVING_LICENSE, client.getDrivingLicense());
		assertEquals(MIN_AGE, client.getAge());
	}
	
	@Test
	public void minAgePlusOne(){
		Client client = new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, MIN_AGE + 1);
		
		assertEquals(BROKER, client.getBroker());
		assertEquals(IBAN, client.getIban());
		assertEquals(NIF, client.getNif());
		assertEquals(DRIVING_LICENSE, client.getDrivingLicense());
		assertEquals(MIN_AGE + 1, client.getAge());
	}
	
	@Test
	public void maxAgeMinusOne(){
		Client client = new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, MAX_AGE - 1);
		
		assertEquals(BROKER, client.getBroker());
		assertEquals(IBAN, client.getIban());
		assertEquals(NIF, client.getNif());
		assertEquals(DRIVING_LICENSE, client.getDrivingLicense());
		assertEquals(MAX_AGE - 1, client.getAge());
	}
	
	@Test
	public void ageEqualsMaxAge(){
		Client client = new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, MAX_AGE);
		
		assertEquals(BROKER, client.getBroker());
		assertEquals(IBAN, client.getIban());
		assertEquals(NIF, client.getNif());
		assertEquals(DRIVING_LICENSE, client.getDrivingLicense());
		assertEquals(MAX_AGE, client.getAge());
	}
	
	@Test(expected = BrokerException.class)
	public void maxAgePlusOne(){
		new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, MAX_AGE + 1);
	}
	
	/*NIF errors tests*/
	@Test(expected = BrokerException.class)
	public void eightDigitsNIF(){
		new Client(BROKER, IBAN, "12345678", DRIVING_LICENSE, AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void tenDigitsNIF(){
		new Client(BROKER, IBAN, "1234567890", DRIVING_LICENSE, AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void noDigitsNIF(){
		new Client(BROKER, IBAN, "TESTESTES", DRIVING_LICENSE, AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void uniqueNIF(){
		new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, AGE);
		new Client(BROKER, IBAN, NIF, DRIVING_LICENSE, AGE);
	}
	
	/*Driving License errors tests*/
	@Test(expected = BrokerException.class)
	public void onlyLetters(){
		new Client(BROKER, IBAN, NIF, "AAAAAAAAAAA", AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void singleLetter(){
		new Client(BROKER, IBAN, NIF, "A", AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void onlyNumbers(){
		new Client(BROKER, IBAN, NIF, "11111111111", AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void singleNumber(){
		new Client(BROKER, IBAN, NIF, "1", AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void numbersBeforeLetters(){
		new Client(BROKER, IBAN, NIF, "111AAA", AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void singleNumberBeforeLetter(){
		new Client(BROKER, IBAN, NIF, "1A", AGE);
	}
	
	@Test(expected = BrokerException.class)
	public void invalidCombinations(){
		new Client(BROKER, IBAN, NIF, "A1A1", AGE);
	}
	
	@After
	public void tearDown(){
		Broker.brokers.clear();
	}
}
