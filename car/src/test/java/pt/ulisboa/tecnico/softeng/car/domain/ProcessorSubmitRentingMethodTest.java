package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.car.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class ProcessorSubmitRentingMethodTest {
	private static final String CANCEL_PAYMENT_REFERENCE = "CancelPaymentReference";
	private static final String INVOICE_REFERENCE = "InvoiceReference";
	private static final String PAYMENT_REFERENCE = "PaymentReference";
	private static final String IBAN = "12345678901234";
	private static final String NIF = "123456789";
	public String DRIVERS_LICENSE= "abc1234123";
	public LocalDate begin;
	public LocalDate end;
	private Renting renting;
	private RentACar rentACar;
	private Car car;

	@Before
	public void setUp() {
		this.rentACar = new RentACar("Rent$", NIF, IBAN);
		this.car = new Car("22-33-HZ", 0, 123, this.rentACar);
		this.begin = new LocalDate(2016, 12, 19);
		this.end = new LocalDate(2017, 12, 21);
		this.renting = new Renting(DRIVERS_LICENSE, begin, end, this.car);
	}

	@Test
	public void success(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);

		new FullVerifications() {
			{
			}
		};
	}

	@Test
	public void oneTaxFailureOnSubmitInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = new TaxException();
				this.result = INVOICE_REFERENCE;
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVERS_LICENSE, begin, end, this.car));

		new FullVerifications(taxInterface) {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.times = 3;
			}
		};
	}

	@Test
	public void oneRemoteFailureOnSubmitInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = new RemoteAccessException();
				this.result = INVOICE_REFERENCE;
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVERS_LICENSE, begin, end, this.car));
		new FullVerifications(taxInterface) {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.times = 3;
			}
		};
	}

	@Test
	public void oneBankFailureOnProcessPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = new BankException();
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_REFERENCE;
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVERS_LICENSE, begin, end, this.car));

		new FullVerifications(bankInterface) {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.times = 3;
			}
		};
	}

	@Test
	public void oneRemoteFailureOnProcessPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = new RemoteAccessException();
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_REFERENCE;
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVERS_LICENSE, begin, end, this.car));

		new FullVerifications(bankInterface) {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.times = 3;
			}
		};
	}

	@Test
	public void successCancel(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.cancelInvoice(this.anyString);
				BankInterface.cancelPayment(this.anyString);
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.renting.cancel();

		new FullVerifications() {
			{
			}
		};
	}

	@Test
	public void oneBankExceptionOnCancelPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				BankInterface.cancelPayment(this.anyString);
				this.result = new BankException();
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.renting.cancel();
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVERS_LICENSE, begin, end, this.car));

		new FullVerifications(bankInterface) {
			{
				BankInterface.cancelPayment(this.anyString);
				this.times = 2;
			}
		};
	}

	@Test
	public void oneRemoteExceptionOnCancelPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				BankInterface.cancelPayment(this.anyString);
				this.result = new RemoteAccessException();
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.renting.cancel();
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVERS_LICENSE, begin, end, this.car));

		new FullVerifications(bankInterface) {
			{
				BankInterface.cancelPayment(this.anyString);
				this.times = 2;
			}
		};
	}

	@Test
	public void oneTaxExceptionOnCancelInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.cancelPayment(this.anyString);
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					public void delegate() {
						if (this.i < 1) {
							this.i++;
							throw new TaxException();
						}
					}
				};
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.renting.cancel();
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVERS_LICENSE, begin, end, this.car));

		new FullVerifications(taxInterface) {
			{
				TaxInterface.cancelInvoice(this.anyString);
				this.times = 2;
			}
		};
	}

	@Test
	public void oneRemoteExceptionOnCancelInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);

				BankInterface.cancelPayment(this.anyString);
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					public void delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						}
					}
				};
			}
		};

		this.rentACar.getProcessor().submitRenting(this.renting);
		this.renting.cancel();
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVERS_LICENSE, begin, end, this.car));

		new FullVerifications(taxInterface) {
			{
				TaxInterface.cancelInvoice(this.anyString);
				this.times = 2;
			}
		};
	}

	@After
	public void tearDown() {
		car.rentings.clear();
		Vehicle.plates.clear();
		RentACar.rentACars.clear();
		
	}

}
