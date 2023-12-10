package se.jimboagency.bookingsystem.logic;

public abstract class Booking {

    private String bookingID;
    private String flightNr;
    private String passengerID;
    private String name;
    private int year;
    private int week;

    public Booking(String bookingID, String flightNr, String passengerID, String name, int year, int week){
        this.bookingID = bookingID;
        this.flightNr = flightNr;
        this.passengerID = passengerID;
        this.name = name;
        this.year = year;
        this.week = week;
    }

    public String getFlightNr() {
        return flightNr;
    }

}
