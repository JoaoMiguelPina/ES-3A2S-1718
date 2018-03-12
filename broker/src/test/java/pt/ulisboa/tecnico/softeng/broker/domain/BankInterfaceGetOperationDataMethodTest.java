package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

public class BankInterfaceGetOperationDataMethodTest {	
	private static int AMOUNT = 100;
	private Bank bank;
	private Account account;
	private String reference;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
		this.reference = this.account.deposit(AMOUNT);
	}
	
	@Test
	public void success() {
		BankOperationData data = BankInterface.getOperationData(this.reference);

		Assert.assertEquals(this.reference, data.getReference());
		Assert.assertEquals(this.account.getIBAN(), data.getIban());
		Assert.assertEquals(Type.DEPOSIT.name(), data.getType());
		Assert.assertEquals(AMOUNT, data.getValue());
		Assert.assertNotNull(data.getTime());
	}

	@Test(expected = BankException.class)
	public void nullReference() {
		BankInterface.getOperationData(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		BankInterface.getOperationData("");
	}

	@Test(expected = BankException.class)
	public void referenceNotExists() {
		BankInterface.getOperationData("XPTO");
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
	

}
