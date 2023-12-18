package se.jimboagency.bookingsystem.logic;

public abstract class Booking {

    protected String bookingID;
    protected String flightNr;
    protected Passenger passenger;
    protected int year;
    protected int week;


    public Booking(String bookingID, String flightNr, Passenger passenger, int year, int week){
        this.bookingID = bookingID;
        this.flightNr = flightNr;
        this.year = year;
        this.week = week;
        this.passenger = passenger;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getFlightNr() {
        return flightNr;
    }

    public String getPassengerID() {
        return this.passenger.getPassengerID();
    }

    public String getName() {
        return this.passenger.getName();
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

}
