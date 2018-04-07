package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

@RunWith(JMockit.class)
public class ProcessPaymentStateProcessMethodTest {
	
//	broker
	private static final String BROKER_CODE = "BR02";
	private static final String BROKER_NAME = "eXtremeADVENTURE";
	private static final String BROKER_NIF_SELLER = "123456789";
	private static final String BROKER_NIF_BUYER = "987654321";
	private static final String IBAN_BROKER = "BK01987654321";
	private static Broker broker;
	
//	client	
	private static final String IBAN_CLIENT = "CLNT01987654321";
	private static final String NIF_CLIENT = "225031999";
	private static final String DRIVING_LICENSE = "IMT123";
	private static final int AGE = 20;
	private static Client client;
	
//	activity
	private static final LocalDate begin = new LocalDate(2016, 12, 19);
	private static final LocalDate end = new LocalDate(2016, 12, 21);
	private static final double MARGIN_OF_PROFIT = 0.5;
	private static final boolean needsCar = true;
	private static final int AMOUNT = 300;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private Adventure adventure;
	
	@Before
	public void setUp(){
		broker = new Broker(BROKER_CODE, BROKER_NAME, BROKER_NIF_SELLER, BROKER_NIF_BUYER, IBAN_BROKER);
		client = new Client(broker, IBAN_CLIENT, NIF_CLIENT, DRIVING_LICENSE, AGE);
		
		this.adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, needsCar);
		this.adventure.setState(State.PROCESS_PAYMENT);
	}

	@Test
	public void success(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.TAX_PAYMENT, this.adventure.getState());
	}

	@Test
	public void oneBankException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);
				this.result = new BankException();
			}
		};

		this.adventure.process();
		
		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	
	@Test
	public void oneBankExceptionOneSuccess(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new BankException();
						} else {
							return PAYMENT_CONFIRMATION;
						}
					}
				};
				this.times = 2;
			}
		};

		this.adventure.process();
		this.adventure.process();
		
		Assert.assertEquals(State.TAX_PAYMENT, this.adventure.getState());
	}
	
	@Test
	public void twoBankExceptionOneSuccess(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new BankException();
						} else {
							return PAYMENT_CONFIRMATION;
						}
					}
				};
				this.times = 3;
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		
		Assert.assertEquals(State.TAX_PAYMENT, this.adventure.getState());
	}
	
	@Test
	public void singleRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return PAYMENT_CONFIRMATION;
						}
					}
				};
				this.times = 3;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionOneBankException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new BankException();
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

	@Test
	public void twoRemoteAccessExceptionOneBankException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new BankException();
						}
					}
				};
				this.times = 2;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		
		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void oneBankExceptionOneRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new BankException();
						} else {
							throw new RemoteAccessException();
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

	@Test
	public void twoBankExceptionOneRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN_CLIENT, AMOUNT);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new BankException();
						} else {
							throw new RemoteAccessException();
						}
					}
				};
				this.times = 2;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		
		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
}