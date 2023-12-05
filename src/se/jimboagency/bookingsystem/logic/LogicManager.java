package se.jimboagency.bookingsystem.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LogicManager {

    Map<String, Flight> flights;

    // Constructor
    public LogicManager() {
        flights = new HashMap<String, Flight>();
        flights.put("AA-123", new Flight("AA-123","a","a","a","a","a","a","a"));
    }

    // Error-management (ONLY FOR DEBUG)
    public void showFlights(){
        for (Map.Entry<String, Flight> entry : flights.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // (5) Create Flight related functions
    public boolean flightnrCheck(String flightNr) {
        if(flights.containsKey(flightNr)){
            return false;
        } else{
            return true;
        }
    }

    public boolean seatCheck(String seats) {
        int intSeats = Integer.parseInt(seats);
        return (intSeats >= 80) && (intSeats <= 380);
    }

    // Checks if another flight has the same flight airline, departure city and arrival city
    public boolean createFlightCheck(String departureCity, String airline, String arrivalCity) {
        for(Map.Entry<String, Flight> entry : flights.entrySet()){
            Flight currentFlight = entry.getValue();
            if((Objects.equals(departureCity, currentFlight.getDepartureCity())) && (Objects.equals(airline, currentFlight.getAirline())) && (Objects.equals(arrivalCity, currentFlight.getArrivalCity()))){
                return false;
            } else {
                return true;
            }

        }
        return true;
    }

    public void createFlight(String flightNr, String departureCity, String time, String date, String arrivalCity, String airline, String seats, String flightTime){
        Flight flight = new Flight(flightNr, departureCity, time, date, arrivalCity, airline, seats, flightTime);
        flights.put(flightNr, flight);
    }

    // (6) Remove Flight
    public boolean removeFlight(String flightNr){
        if(flights.containsKey(flightNr)){
            flights.remove(flightNr);
            return true;
        } else{
            return false;
        }
    }

}
