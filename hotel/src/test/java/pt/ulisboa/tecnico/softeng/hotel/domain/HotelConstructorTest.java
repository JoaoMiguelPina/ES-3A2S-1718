package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelConstructorTest {
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private static final String NIF = "224194217";
	private static final String IBAN = "1234567890";
	private static final double PRICES = 123;
	private static final double PRICED = 124;

	@Test
	public void success() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF, IBAN, PRICES, PRICED);

		Assert.assertEquals(HOTEL_NAME, hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
	}

	@Test(expected = HotelException.class)
	public void nullCode() {
		new Hotel(null, HOTEL_NAME, NIF, IBAN, PRICES, PRICED);
	}
	
	@Test(expected = HotelException.class)
	public void negativeSinglePrice() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF, IBAN, -20, PRICED);
	}
	
	@Test(expected = HotelException.class)
	public void negativeDoublePrice() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF, IBAN, PRICES, -20);
	}

	@Test(expected = HotelException.class)
	public void blankCode() {
		new Hotel("      ", HOTEL_NAME, NIF, IBAN, PRICES, PRICED);
	}

	@Test(expected = HotelException.class)
	public void emptyCode() {
		new Hotel("", HOTEL_NAME, NIF, IBAN, PRICES, PRICED);
	}

	@Test(expected = HotelException.class)
	public void nullName() {
		new Hotel(HOTEL_CODE, null, NIF, IBAN, PRICES, PRICED);
	}

	@Test(expected = HotelException.class)
	public void blankName() {
		new Hotel(HOTEL_CODE, "  ", NIF, IBAN, PRICES, PRICED);
	}

	@Test(expected = HotelException.class)
	public void emptyName() {
		new Hotel(HOTEL_CODE, "", NIF, IBAN, PRICES, PRICED);
	}

	@Test(expected = HotelException.class)
	public void codeSizeLess() {
		new Hotel("123456", HOTEL_NAME, NIF, IBAN, PRICES, PRICED);
	}

	@Test(expected = HotelException.class)
	public void codeSizeMore() {
		new Hotel("12345678", HOTEL_NAME, NIF, IBAN, PRICES, PRICED);
	}

	@Test(expected = HotelException.class)
	public void codeNotUnique() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF, IBAN, PRICES, PRICED);
		new Hotel(HOTEL_CODE, HOTEL_NAME + " City", NIF, IBAN, PRICES, PRICED);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
