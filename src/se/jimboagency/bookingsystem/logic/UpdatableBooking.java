package se.jimboagency.bookingsystem.logic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class UpdatableBooking extends Booking {

    public UpdatableBooking(String bookingID, String flightNr, String passengerID, String name, int year, int week) {
        super(bookingID, flightNr, passengerID, name, year, week);

    }

    public void setNewName(String newName){
        this.name = newName;
    }
    public void setNewPassengerID(String newPassengerID){
        this.passengerID = newPassengerID;
    }

}
