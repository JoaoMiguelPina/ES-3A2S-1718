package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class TaxPaymentStateProcessMethodTest {
	private static final String INVOICE_SUBMITTED = "InvoiceSubmitted";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	private Client client;
	private double marginOfProfit = 20;
	private boolean needsCar = true;
	private InvoiceData invoice;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		
		this.broker = new Broker("BR01", "eXtremeADVENTURE", "123456789", "987654321", "123");
		this.client = new Client(this.broker, "BK011234567", "225031999", "IMT123", 20);
		this.adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
		this.adventure.setState(State.TAX_PAYMENT);
	}

	@Test
	public void success(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice(invoice);
				this.result = INVOICE_SUBMITTED;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void taxException(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice(invoice);
				this.result = new TaxException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void singleRemoteAccessException(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice(invoice);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.TAX_PAYMENT, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessException(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice(invoice);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.TAX_PAYMENT, this.adventure.getState());
	}
	
	@Test
	public void maxRemoteAccessException(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice(invoice);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}


	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice(invoice);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return INVOICE_SUBMITTED;
						}
					}
				};
				this.times = 5;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionOneBankException(@Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice(invoice);
				
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new TaxException();
						}
					}
				};
				this.times = 2;

			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
}
