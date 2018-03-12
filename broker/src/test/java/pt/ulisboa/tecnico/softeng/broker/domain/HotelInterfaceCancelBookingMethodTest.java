package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

public class HotelInterfaceCancelBookingMethodTest {	
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;
	private Booking booking;
	
	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
		this.booking = this.room.reserve(Type.DOUBLE, this.arrival, this.departure);
	}
	
	@Test
	public void success() {
		Assert.assertNotNull(HotelInterface.cancelBooking(this.booking.getReference()));
	}
	
	@Test(expected = HotelException.class)
	public void exceptionTest() {
		HotelInterface hotelinterface = new HotelInterface();
		hotelinterface.cancelBooking("XPTO");
		
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	

}
