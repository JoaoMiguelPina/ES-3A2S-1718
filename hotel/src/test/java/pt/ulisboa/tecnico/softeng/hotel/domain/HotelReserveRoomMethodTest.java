package pt.ulisboa.tecnico.softeng.hotel.domain;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelReserveRoomMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void success() {
		String string = this.hotel.reserveRoom(Type.DOUBLE, this.arrival, this.departure);
		RoomBookingData booking = this.hotel.getRoomBookingData(string);
		
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
		Assert.assertEquals(this.hotel.getName(), booking.getHotelName());
		Assert.assertEquals(this.hotel.getCode(), booking.getHotelCode());
		Assert.assertEquals(this.room.getNumber(), booking.getRoomNumber());
		Assert.assertEquals(this.room.getType().name(), booking.getRoomType());
		Assert.assertNull(booking.getCancellationDate());
		
		
	}
	
	@Test (expected = HotelException.class)
	public void nullRoomType() {
		this.hotel.reserveRoom(null, this.arrival, this.departure);
	}
	
	@Test (expected = HotelException.class)
	public void invalidRoomType() {
		this.hotel.reserveRoom(Type.SINGLE, this.arrival, this.departure);
	}
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}