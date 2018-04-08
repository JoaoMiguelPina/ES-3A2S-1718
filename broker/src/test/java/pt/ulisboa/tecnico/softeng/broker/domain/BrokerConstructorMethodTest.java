package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class BrokerConstructorMethodTest {
	private final static String CODE = "BR01";
	private final static String NAME = "WeExplore";
	private final static String NIF_AS_SELLER = "123456789";
	private final static String NIF_AS_BUYER = "987654321";
	private final static String IBAN = "123";
	
	@Test
	public void success() {
		Broker broker = new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN);

		Assert.assertEquals(CODE, broker.getCode());
		Assert.assertEquals(NAME, broker.getName());
		Assert.assertEquals(NIF_AS_SELLER, broker.getNifAsSeller());
		Assert.assertEquals(NIF_AS_BUYER, broker.getNifAsBuyer());
		Assert.assertEquals(IBAN, broker.getIban());
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}

	@Test
	public void nullCode() {
		try {
			new Broker(null, NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Broker("", NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankCode() {
		try {
			new Broker("  ", NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueCode() {
		Broker broker = new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN);

		try {
			new Broker(CODE, NAME, "111111111", "222222222", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}

	@Test
	public void nullName() {
		try {
			new Broker(CODE, null, NIF_AS_SELLER, NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Broker(CODE, "", NIF_AS_SELLER, NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankName() {
		try {
			new Broker(CODE, "   ", NIF_AS_SELLER, NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void nullNifSeller() {
		try {
			new Broker(CODE, NAME, null, NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyNifSeller() {
		try {
			new Broker(CODE, NAME, "", NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankNifSeller() {
		try {
			new Broker(CODE, NAME, "   ", NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void uniqueNifSeller() {
		Broker broker = new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN);

		try {
			new Broker("BR02", NAME, NIF_AS_SELLER, "222222222", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}
	
	@Test
	public void nullNifBuyer() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, null, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyNifBuyer() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, "", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankNifBuyer() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, "    ", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void uniqueNifBuyer() {
		Broker broker = new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, IBAN);

		try {
			new Broker("BR02", NAME, "111111111", NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}
	
	@Test
	public void nullIban() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, null);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyIban() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, "");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankIban() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_BUYER, "    ");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void wrongNifSeller() {
		try {
			new Broker(CODE, NAME, "WrongNifs", NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void wrongNifBuyer() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, "WrongNifs", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void eightDigitsNifSeller() {
		try {
			new Broker(CODE, NAME, "12345678", NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void eightDigitsNifBuyer() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, "12345678", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void tenDigitsNifSeller() {
		try {
			new Broker(CODE, NAME, "1234567890", NIF_AS_BUYER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void tenDigitsNifBuyer() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, "1234567890", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void nifSellerEqualsNifBuyer() {
		try {
			new Broker(CODE, NAME, NIF_AS_SELLER, NIF_AS_SELLER, IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
