package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;;

public class BulkRoomBookingConstructorMethodTest {
	private final int number = 5;	
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);	
	private BulkRoomBooking bulkRoomBooking;

	@Before
	public void setUp() {
		this.bulkRoomBooking = new BulkRoomBooking(number, arrival, departure);
	}
	
	@Test
	public void success() {
		
		Assert.assertEquals(arrival, bulkRoomBooking.getArrival());
		Assert.assertEquals(departure, bulkRoomBooking.getDeparture());
		Assert.assertEquals(number, bulkRoomBooking.getNumber());
	}
	
	@Test
	public void nullReferences() {
		
		Assert.assertEquals(0, bulkRoomBooking.getReferences().size());
	}

}
