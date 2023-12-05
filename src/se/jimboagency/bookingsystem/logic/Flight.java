package se.jimboagency.bookingsystem.logic;

public class Flight {

    private String flightNr;
    private String departureCity;
    private String time;
    private String date;
    private String arrivalCity;
    private String airline;
    private String seats;
    private String flightTime;

    // Constructor
    public Flight(String flightNr, String departureCity, String time, String date, String arrivalCity, String airline, String seats, String flightTime){
        this.flightNr = flightNr;
        this.departureCity = departureCity;
        this.time = time;
        this.date = date;
        this.arrivalCity = arrivalCity;
        this.airline = airline;
        this.seats = seats;
        this.flightTime = flightTime;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getAirline() {
        return airline;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }
}
