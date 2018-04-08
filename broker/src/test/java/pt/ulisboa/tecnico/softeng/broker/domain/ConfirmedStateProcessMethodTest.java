package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest {	
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 30;
	private static final int AGE = 20;
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String RENT_CONFIRMATION = "RentConfirmation";
	private static final String INVOICE_CONFIRMATION = "InvoiceConfirmation";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private static final String NIF = "123456789";
	private static final String driving_license = "aAZ12";
	
	private static final String code = "911";
	private static final String name = "broker";
	private static final String brokerIBAN = "AK01987654321";
	private static final String brokerNIFSeller = "923456789";
	private static final String brokerNIFBuyer = "823456789";

	
	private Broker broker = new Broker(code, name, brokerNIFSeller, brokerNIFBuyer, brokerIBAN);
	private Client client = new Client(this.broker, IBAN, NIF, driving_license, AGE);
	private Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, true);
	
	
	@Before
	public void setUp() {
		this.adventure.setState(State.CONFIRMED);
	}

	@Test
	public void successAll(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void successPaymentAndActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionStartingInActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionStartingInRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionStartingInCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionStartingInRoomWithCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInRoomWithCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInRoomWithCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionStartingInTax(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInTax(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInTax(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);

				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionStartingInTaxWithoutCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInTaxWithoutCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInTaxWithoutCar(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionStartingInTaxWithoutCarAndRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInTaxWithoutCarAndRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInTaxWithoutCarAndRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					void delegate() {
						this.i++;
						if (this.i == 1) {
							// return value is irrelevant
						} else {
							throw new RemoteAccessException();
						}
					}
				};

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		
		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void activityException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void hotelException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void carException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				this.result = new CarException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void taxException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				
				TaxInterface.getInvoiceData(INVOICE_CONFIRMATION);
				this.result = new TaxException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}