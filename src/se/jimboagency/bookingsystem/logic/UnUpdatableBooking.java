package se.jimboagency.bookingsystem.logic;

public class UnUpdatableBooking extends Booking {
    public UnUpdatableBooking(String bookingID, String flightNr, String passengerID, String name, int year, int week) {
        super(bookingID, flightNr, passengerID, name, year, week);
    }
}
