package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;;

public class BulkRoomBookingConstructorMethodTest {
	private final int number = 5;	
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);	

	@Test
	public void success() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(number, arrival, departure);
		
		Assert.assertEquals(arrival, bulkRoomBooking.getArrival());
		Assert.assertEquals(departure, bulkRoomBooking.getDeparture());
		Assert.assertEquals(number, bulkRoomBooking.getNumber());

	}

}
