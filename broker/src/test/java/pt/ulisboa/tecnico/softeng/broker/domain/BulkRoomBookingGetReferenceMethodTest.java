package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBookingGetReferenceMethodTest {
	private final int number = 1;	
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);	
	private BulkRoomBooking bulkRoomBooking;
	private Hotel hotel;
	
	@Before
	public void setUp() {
		this.bulkRoomBooking = new BulkRoomBooking(number, arrival, departure);
		this.hotel = new Hotel("XPTO123", "Lisboa");
	}
	
	@Test
	public void sucess() {
		new Room(this.hotel, "01", Type.DOUBLE);
		this.bulkRoomBooking.processBooking();
		this.bulkRoomBooking.getReference(Type.DOUBLE.name());
	}
	
	@Test
	public void cancelleds() {
		this.bulkRoomBooking.processBooking();
		this.bulkRoomBooking.processBooking();
		this.bulkRoomBooking.processBooking();
		this.bulkRoomBooking.processBooking();
		this.bulkRoomBooking.getReference(Type.DOUBLE.name());
	}
	
	@Test
	public void finalReturn() {
		this.bulkRoomBooking.getReference(Type.DOUBLE.name());
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	

}
