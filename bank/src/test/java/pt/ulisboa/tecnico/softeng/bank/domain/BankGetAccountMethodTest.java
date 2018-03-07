package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankGetAccountMethodTest {
	Bank bank;
	Client client;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
	}

	@Test
	public void success() {
		Account account = new Account(this.bank, this.client);

		Account result = this.bank.getAccount(account.getIBAN());

		Assert.assertEquals(account, result);
	}

	@Test
	public void nullIBAN() {
		Assert.assertNull(this.bank.getAccount(null));
	}

	@Test
	public void emptyIBAN() {
		Assert.assertNull(this.bank.getAccount(""));
	}

	@Test
	public void blankIBAN() {
		Assert.assertNull(this.bank.getAccount("    "));
	}

	@Test
	public void emptySetOfAccounts() {
		Assert.assertNull(this.bank.getAccount("XPTO"));
	}

	@Test
	public void severalAccountsDoNoMatch() {
		new Account(this.bank, this.client);
		new Account(this.bank, this.client);

		Assert.assertNull(this.bank.getAccount("XPTO"));
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
