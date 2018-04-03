package pt.ulisboa.tecnico.softeng.broker.interfaces;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;

import org.joda.time.LocalDate;

public class CarInterface {
	
	public static String reserveVehicle(LocalDate begin_date, LocalDate end_date, String driving_license, String NIF, String IBAN) {
		return RentACar.reserveVehicle(begin_date, end_date, driving_license , NIF, IBAN);
	}

	public static String cancelReservation(String reference) {
		return RentACar.cancelReservation(reference);
	}

	public static RentingData getRentingData(String reference) {
		return RentACar.getRentingData(reference);
	}
}
