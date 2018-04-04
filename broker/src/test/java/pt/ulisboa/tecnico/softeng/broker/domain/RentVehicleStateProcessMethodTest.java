package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;

public class RentVehicleStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 50;
	private static final int AGE = 20;
	private static final LocalDate begin = new LocalDate(2016, 12, 19);
	private static final LocalDate end = new LocalDate(2016, 12, 21);
	private static final String NIF = "123456789";
	private static final String driving_license = "aAZ12";
	
	private static final String code = "911";
	private static final String name = "broker";
	private static final String brokerIBAN = "AK01987654321";
	private static final String brokerNIFSeller = "923456789";
	private static final String brokerNIFBuyer = "823456789";
	

	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	
	private Broker broker = new Broker(code, name, brokerNIFSeller, brokerNIFBuyer, brokerIBAN);

	private Client client = new Client(this.broker, IBAN, NIF, driving_license, AGE); 
	
	private Adventure adventure = new Adventure(this.broker, begin, begin, client, AMOUNT, true);

	
	@Test
	public void successRentVehicle(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setState(State.RENT_VEHICLE);
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, begin, AGE, NIF, IBAN);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}
	

	@Test
	public void activityException(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setState(State.RENT_VEHICLE);
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, begin, AGE,  NIF, IBAN);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test 
	public void singleRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setState(State.RENT_VEHICLE);
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, begin, AGE,  NIF, IBAN);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setState(State.RENT_VEHICLE);
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, begin, AGE,  NIF, IBAN);
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
	public void maxMinusOneRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setState(State.RENT_VEHICLE);
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, begin, AGE,  NIF, IBAN);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		
		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final ActivityInterface activityInterface) {
		
		this.adventure.setState(State.RESERVE_ACTIVITY);
		
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, begin, AGE,  NIF, IBAN);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return ACTIVITY_CONFIRMATION;
						}
					}
				};
				this.times = 3;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionOneActivityException(@Mocked final ActivityInterface activityInterface) {
		
		this.adventure = new Adventure(this.broker, begin, end, client, AMOUNT, true);
		this.adventure.setState(State.RESERVE_ACTIVITY);
		
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE,  NIF, IBAN);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new ActivityException();
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
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}
