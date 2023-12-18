package se.jimboagency.bookingsystem.logic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class UpdatableBooking extends Booking {

    public UpdatableBooking(String bookingID, String flightNr, Passenger passenger, int year, int week) {
        super(bookingID, flightNr, passenger, year, week);

    }

    public void setNewName(String newName){
        this.passenger.setName(newName);
    }
    public void setNewPassengerID(String newPassengerID){
        this.passenger.setPassengerID(newPassengerID);
    }

}
