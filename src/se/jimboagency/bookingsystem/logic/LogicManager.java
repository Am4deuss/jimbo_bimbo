package se.jimboagency.bookingsystem.logic;

import java.io.*;
import java.nio.Buffer;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.regex.Pattern;

public class LogicManager {

    LocalDate currentDate;
    Map<String, Flight> flights;
    Map<String, Booking> bookings;

    // Constructor
    public LogicManager() {
        currentDate = LocalDate.now();

        flights = new HashMap<String, Flight>();
        flights.put("AA-123", new Flight("AA-123","a","a","a","a","a","a","a"));

        bookings = new HashMap<String, Booking>();
        bookings.put("test1", new UpdatableBooking());
        bookings.put("test2", new UnUpdatableBooking());

        showBookings();
    }

    // Error-management (ONLY FOR DEBUG)
    public void showFlights(){
        for (Map.Entry<String, Flight> entry : flights.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void showBookings(){
        for (Map.Entry<String, Booking> entry : bookings.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // (*) Functions used in several places
    public boolean flightnrCheck(String flightNr) {
        if(flights.containsKey(flightNr)){
            return false;
        } else{
            return true;
        }
    }

    public boolean flightnrPatternCheck(String flightNr){
        String regex;
        regex = "^[A-Z]{2}-[0-9]{3}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(flightNr).matches(); // Returns true if input matches pattern
    }

    // (1) Search for Booking related functions

    // (2) Create Booking related functions
    public String updatableInputCheck(String updatable) {
        String[] trueInputs = {"y", "yes", "j", "ja"};
        String[] falseInputs = {"n", "no", "n", "nej"};
        if (Arrays.asList(trueInputs).contains(updatable.toLowerCase())){ ;
            return "t";
        } else if(Arrays.asList(falseInputs).contains(updatable.toLowerCase())){
            return "f";
        } else {
            return "x";
        }
    }

    public boolean passengerIDCheck(String passengerID){
        String regex;
        regex = "^[0-9]{8}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(passengerID).matches(); // Returns true if input matches pattern
    }

    public boolean yearCheck(int year){
        int currentYear = currentDate.getYear();
        return year >= currentYear; // Returns true if input-year is equal or greater than the devices' current-year
    }

    public boolean weekCheck(int week, int year){
        int currentYear = currentDate.getYear();
        int currentWeek = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        if(week > 0 && week <= 52 ){
            return year == currentYear && week >= currentWeek || year > currentYear; // Returns true if input-year is equal (and input-week has not passed) or greater than the devices' current-year
        } else {
            return false;
        }
    }

    public String generateBookingID(boolean updatable) {
        String data = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("bookingid.txt"));
            data = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] currentIDs = data.split(";", 2); // Removes ";" from "UP-XXXX;UN-XXXX
        String value;

        if(updatable){
            value = currentIDs[0];
        } else {
            value = currentIDs[1];
        }

        String[] valueSplit = value.split("-", 2); // Removes "-" from "UP/UN-XXXX"
        String valueNumber = valueSplit[1];
        int valueNumberInt = Integer.parseInt(valueNumber);
        int newValueInt = valueNumberInt + 1; // Adds +1 to currentID to increase counter
        String newValue = Integer.toString(newValueInt);
        String newID = valueSplit[0] + "-" + newValue; // Puts everything together again "UP/UN" + "-" + "XXXX+1)

        if(updatable){
            data = newID + ";" + currentIDs[1];
        } else {
            data = currentIDs[0] + ";" + newID;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("bookingid.txt"));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newID;
    }

    public String createBooking(boolean updatable, String flightNr, String passengerID, String name, int year, int week){
        String bookingID = generateBookingID(updatable);

        if(updatable){
            Booking booking = new UpdatableBooking(); // Fix
        } else {
            Booking booking = new UnUpdatableBooking(); // Fix
        }

        return bookingID;
    }

    // (3) Remove Booking (Cancel Booking) related functions

    // (4) Update Booking related functions

    // (5) Create Flight related functions
    public boolean timePatternCheck(String time){
        String regex;
        if(time.charAt(0) == '2'){ // Changes regex depending on if the first char is 0-1 or 2
            regex = "^[0-2][0-3]:[0-5][0-9]$";
        } else {
            regex = "^[0-1][0-9]:[0-5][0-9]$";
        }
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(time).matches(); // Returns true if input matches pattern
    }

    public boolean weekdayCheck(String weekday){
        String[] weekdays = {"monday", "måndag", "tuesday", "tisdag", "wednesday", "onsdag", "thursday", "torsdag", "friday", "fredag", "saturday", "lördag", "sunday", "söndag"};
        return Arrays.asList(weekdays).contains(weekday.toLowerCase());
    }

    public boolean seatCheck(int seats) {
        return (seats >= 80) && (seats <= 380);
    }

    public boolean flightTimeCheck(int flightTime){
        return flightTime >= 1 && flightTime <= 18;
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

    public void createFlight(String flightNr, String departureCity, String time, String weekday, String arrivalCity, String airline, int seats, int flightTime){
        String seatsString = Integer.toString(seats);
        String flightTimeString = Integer.toString(flightTime);
        Flight flight = new Flight(flightNr, departureCity, time, weekday, arrivalCity, airline, seatsString, flightTimeString);
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

    // (7) Statistics (GUI) related functions

}
