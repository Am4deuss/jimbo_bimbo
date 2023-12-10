package se.jimboagency.bookingsystem.logic;

public class UpdatableBooking extends Booking {

    public UpdatableBooking(String bookingID, String flightNr, String passengerID, String name, int year, int week) {
        super(bookingID, flightNr, passengerID, name, year, week);
    }

}
