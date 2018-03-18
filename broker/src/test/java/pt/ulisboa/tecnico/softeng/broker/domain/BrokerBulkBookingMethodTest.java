package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BrokerBulkBookingMethodTest {
	private final int number = 1;	
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);	
	private BulkRoomBooking bulkRoomBooking;
	private Hotel hotel;
	private Broker broker;
	
	@Before
	public void setUp() {
		this.bulkRoomBooking = new BulkRoomBooking(number, arrival, departure);
		this.hotel = new Hotel("XPTO123", "Lisboa");
		this.broker = new Broker("BR01", "WeExplore");
		new Room(this.hotel, "01", Type.DOUBLE);
	}
	
	@Test
	public void sucess() {
		this.broker.bulkBooking(this.number, this.arrival, this.departure);
		assertEquals(1, this.broker.getBulkBookings().size());
	}
	
	@Test (expected = HotelException.class)
	public void failException() {
		Hotel hotel1 = new Hotel("XPTO123", "Lisboa");
		Broker broker1 = new Broker("BR01", "WeExplore");
		broker1.bulkBooking(0, this.arrival, this.departure);
	}
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
		Broker.brokers.clear();
	}
	

}
