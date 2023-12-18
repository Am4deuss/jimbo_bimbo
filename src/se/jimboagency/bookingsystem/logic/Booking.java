package se.jimboagency.bookingsystem.logic;

public abstract class Booking {

    protected String bookingID;
    protected String flightNr;
    protected String passengerID;
    protected String name;
    protected int year;
    protected int week;

    public Booking(String bookingID, String flightNr, String passengerID, String name, int year, int week){
        this.bookingID = bookingID;
        this.flightNr = flightNr;
        this.passengerID = passengerID;
        this.name = name;
        this.year = year;
        this.week = week;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getFlightNr() {
        return flightNr;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

}
