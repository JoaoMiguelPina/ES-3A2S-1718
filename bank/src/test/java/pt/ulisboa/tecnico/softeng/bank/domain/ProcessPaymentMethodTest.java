package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class ProcessPaymentMethodTest {
	private Bank bank;
	private Account account;
	
	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
		this.account.deposit(100);
		this.account.getIBAN();
	}
	
	@Test(expected = BankException.class)
	public void negativeAmount() {
		this.account.withdraw(-20);
	}
	
	@Test
	public void successIBAN() {
		Bank.processPayment(this.account.getIBAN(), 30);
		Assert.assertEquals(70, this.account.getBalance());
	}


	@Test(expected = BankException.class)
	public void noBank() {
		Bank.banks.clear();
		Bank.processPayment("LOL", 30);
	}
	

	@Test(expected = BankException.class)
	public void zeroAmount() {
		Bank.processPayment(this.account.getIBAN(), 0);
	}

	@Test
	public void oneAmount() {
		Bank.processPayment(this.account.getIBAN(), 1);
		Assert.assertEquals(99, this.account.getBalance());
	}

	@Test
	public void equalToBalance() {
		Bank.processPayment(this.account.getIBAN(), 100);
		Assert.assertEquals(0, this.account.getBalance());
	}

	@Test(expected = BankException.class)
	public void equalToBalancePlusOne() {
		Bank.processPayment(this.account.getIBAN(), 101);
	}

	@Test(expected = BankException.class)
	public void moreThanBalance() {
		Bank.processPayment(this.account.getIBAN(), 150);
	}
	
	
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
