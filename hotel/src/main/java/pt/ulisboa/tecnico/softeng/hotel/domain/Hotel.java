package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.domain.Processor;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final String NIF;
	private final String IBAN;
	private final double priceSingle;
	private final double priceDouble;
	private final Set<Room> rooms = new HashSet<>();
	
	private final Processor processor = new Processor();
	

	public Hotel(String code, String name, String nif, String iban, double priceS, double priceD) {
		checkArguments(code, name, priceS, priceD);

		this.code = code;
		this.name = name;
		this.NIF = nif;
		this.IBAN = iban;
		this.priceSingle = priceS;
		this.priceDouble = priceD;
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name, Double priceS, Double priceD) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0|| priceS < 0 || priceD < 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}
	
	public String getIBAN() {
		return IBAN;
	}

	public String getNIF() {
		return NIF;
	}
	
	public double getPriceDouble() {
		return priceDouble;
	}

	public double getPriceSingle() {
		return priceSingle;
	}
	
	public Processor getProcessor() {
		return this.processor;
	}
	
	int getNumberOfRooms() {
		return this.rooms.size();
	}

	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	private Booking getBooking(String reference) {
		for (Room room : this.rooms) {
			Booking booking = room.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure, String nif, String iban) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				Booking booking = room.reserve(type, arrival, departure, nif, iban);
				hotel.getProcessor().submitBooking(booking);
				return booking.getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String reference) {
		for (Hotel hotel : hotels) {
			Booking booking = hotel.getBooking(reference);
			if (booking != null) {
				return booking.cancel();
			}
		}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				Booking booking = room.getBooking(reference);
				if (booking != null) {
					
					if (!booking.isCancelled()) {
						try {
							BankInterface.getOperationData(booking.getPaymentReference());
							TaxInterface.getInvoiceData(booking.getInvoiceReference());
						}
						catch(BankException | TaxException e) {throw new HotelException();}
					}
					else {
						try {
							BankInterface.getOperationData(booking.getCancelledPaymentReference());
							TaxInterface.getInvoiceData(booking.getCancellation());
						}
						catch(BankException | TaxException e) {throw new HotelException();}
					}
					
					return new RoomBookingData(room, booking);
				}
			}
		}
		throw new HotelException();
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure, String nif, String iban) {
		if (number < 1 || nif == null || iban == null || nif == "" || iban == "") {
			throw new HotelException();
		}

		Set<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();
		for (Room room : rooms) {
			references.add(room.reserve(room.getType(), arrival, departure, nif, iban).getReference());
		}

		return references;
	}

	static Set<Room> getAvailableRooms(int number, LocalDate arrival, LocalDate departure) {
		Set<Room> rooms = new HashSet<>();
		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				if (room.isFree(room.getType(), arrival, departure)) {
					rooms.add(room);
					if (rooms.size() == number) {
						return rooms;
					}
				}
			}
		}
		return rooms;
	}
	
	public static Hotel getHotel(String name) {
		for( Hotel hotel: hotels) {
			if(hotel.getName() == name) {
				return hotel;
			}
		}
		return null;
	}

    public void removeRooms() {
        rooms.clear();
    }
}
