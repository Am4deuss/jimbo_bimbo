package se.jimboagency.bookingsystem.logic;

public class UnUpdatableBooking extends Booking {
    public UnUpdatableBooking(String bookingID, String flightNr, Passenger passenger, int year, int week) {
        super(bookingID, flightNr, passenger, year, week);
    }
}
