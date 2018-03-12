package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

public class BankInterfaceCancelPaymentMethodTest {	
	private Bank bank;
	private Account account;
	private String reference;
	
	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
		this.reference = this.account.deposit(100);
	}
	
	@Test
	public void success() {
		assertNotNull(BankInterface.cancelPayment(this.reference));
	}
	
	@Test(expected = BankException.class)
	public void nullReference() {
		BankInterface.cancelPayment(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		BankInterface.cancelPayment("");
	}

	@Test(expected = BankException.class)
	public void notExistsReference() {
		BankInterface bi = new BankInterface();
		bi.cancelPayment("XPTO");
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
	

}
