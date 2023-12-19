package se.jimboagency.bookingsystem.logic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.regex.Pattern;

public class LogicManager {

    private Map<String, User> employees;
    private LocalDate currentDate;
    private Map<String, Flight> flights;
    private Map<String, Booking> bookings;
    private Map<String, Passenger> passengers;
    private Airline airline;
    private String username;
    private String filepathFlights;
    private String filepathBookings;
    private String filepathPassengers;

    // Constructor
    public LogicManager() {
        filepathFlights = "flights.json";
        filepathBookings = "bookings.json";
        filepathPassengers = "passengers.json";

        employees = new HashMap<>();
        employees.put("shipa001", new User("shipa001", "Shima Parsson", "3eff43"));
        employees.put("Ennpe001", new User("Ennpe001", "Ennia Pettersson", "wedr5t"));
        employees.put("julno001", new User("julno001", "Julia Noor", "wwr4r3"));
        employees.put("johni001", new User("johni001", "Johan Karlsson", "lk9h6d"));
        employees.put("adabe001", new User("adab001", "Adam Bergquvist", "0o3u33"));

        currentDate = LocalDate.now();

        airline = new Airline();

        flights = new HashMap<>();
        //flights.put("AA-123", new Flight("AA-123","Test1","00:00","måndag","Test2","SAS","80","4"));

        bookings = new HashMap<>();
        //bookings.put("UP-0", new UpdatableBooking("UP-0", "AA-123", new Passenger("12345678", "Test Testsson"),  2024, 1));
        //bookings.put("UN-0", new UnUpdatableBooking("UN-0", "AA-123", new Passenger("12345678", "Test Testsson"), 2024, 1));

        passengers = new HashMap<>();
        //passengers.put("12345678", new Passenger("12345678", "Test Testsson"));

        loadFromJson();
        saveToJson();
    }

    // Error-management + Save to .json

    public void saveToJson(){

        // Lite speciellt för booking eftersom vi behöver särskilja updatable/unupdatablebookings
        Map<String, UpdatableBooking> upBookings  = new HashMap<>();
        Map<String, UnUpdatableBooking> unBookings  = new HashMap<>();

        for(Map.Entry<String, Booking> entry : bookings.entrySet()){
            Booking currentBooking = entry.getValue();
            if((currentBooking.getClass()) == (UpdatableBooking.class)){
                UpdatableBooking upBooking = (UpdatableBooking) currentBooking ;
                upBookings.put(entry.getKey(), upBooking);
            } else{
                UnUpdatableBooking unBooking = (UnUpdatableBooking) currentBooking;
                unBookings.put(entry.getKey(), unBooking);
            }
        }

        Gson gson = new Gson();

        String jsonFlights = gson.toJson(flights);
        String jsonUpBookings = gson.toJson(upBookings);
        String jsonUnBookings = gson.toJson(unBookings);
        String jsonPassengers = gson.toJson(passengers);

        try {
            BufferedWriter writerFlights = new BufferedWriter(new FileWriter(filepathFlights));
            writerFlights.write(jsonFlights);
            writerFlights.close();

            BufferedWriter writerBookings = new BufferedWriter(new FileWriter(filepathBookings));
            writerBookings.write(jsonUpBookings);
            writerBookings.newLine();
            writerBookings.write(jsonUnBookings);
            writerBookings.close();

            BufferedWriter writerPassengers = new BufferedWriter(new FileWriter(filepathPassengers));
            writerPassengers.write(jsonPassengers);
            writerPassengers.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadFromJson() {

        String dataFlightsJson = null;
        String dataUpBookingsJson = null;
        String dataUnBookingsJson = null;
        String dataPassengersJson = null;

        try {
            BufferedReader flightsReader = new BufferedReader(new FileReader("flights.json"));
            dataFlightsJson = flightsReader.readLine();
            flightsReader.close();

            BufferedReader bookingsReader = new BufferedReader(new FileReader("bookings.json"));
            dataUpBookingsJson = bookingsReader.readLine();
            dataUnBookingsJson = bookingsReader.readLine();
            bookingsReader.close();

            BufferedReader passengersReader = new BufferedReader(new FileReader("passengers.json"));
            dataPassengersJson = passengersReader.readLine();
            passengersReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        Type flightType = new TypeToken<Map<String, Flight>>() {}.getType();
        Type upBookingType = new TypeToken<Map<String, UpdatableBooking>>() {}.getType();
        Type unBookingType = new TypeToken<Map<String, UnUpdatableBooking>>() {}.getType();
        Type passengerType = new TypeToken<Map<String, Passenger>>() {}.getType();

        Map<String, UpdatableBooking> upBookings;
        Map<String, UnUpdatableBooking> unBookings;

        flights = gson.fromJson(dataFlightsJson, flightType);
        upBookings = gson.fromJson(dataUpBookingsJson, upBookingType);
        unBookings = gson.fromJson(dataUnBookingsJson, unBookingType);
        passengers = gson.fromJson(dataPassengersJson, passengerType);

        try {
            for (Map.Entry<String, UpdatableBooking> entry : upBookings.entrySet()) {
                UpdatableBooking currentBooking = entry.getValue();
                bookings.put(entry.getKey(), currentBooking);
            }

            for (Map.Entry<String, UnUpdatableBooking> entry : unBookings.entrySet()) {
                UnUpdatableBooking currentBooking = entry.getValue();
                bookings.put(entry.getKey(), currentBooking);
            }
        }
        catch(Exception e){

        }

    }

    // (*) Functions used in several places
    public boolean authCheck(String[] args){
        username = "";
        String password = "";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-u") && i + 1 < args.length) {
                username = args[i + 1];
                i++; // Move to the next argument
            } else if (args[i].equals("-p") && i + 1 < args.length) {
                password = args[i + 1];
                i++; // Move to the next argument
            }
        }
        try{
            if(employees.get(username).getPassword(password)){

                return true;
            }
            return false;
        }
        catch(Exception e){
            return false;
        }
    }

    public String getUsername(){
        return employees.get(username).getEmployeeName();
    }

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

    public String getPassengerName(String passengerID){
        String name;
        try{
           name = passengers.get(passengerID).getName();
        }catch(Exception e){
            name = "";
        }
        return name;
    }

    //Takes personID and returns all bookings
    public ArrayList<Booking> searchBooking(String inputID){
        ArrayList<Booking> searchedBookings = new ArrayList<Booking>();

        for(Map.Entry<String, Booking> entry : bookings.entrySet()){
            Booking currentBooking = entry.getValue();

            if(currentBooking.passenger.getPassengerID().equals(inputID)){
                searchedBookings.add(entry.getValue());
            }
        }
        return searchedBookings;
    }

    //Takes a personID and returns all flights
    public ArrayList<Flight> searchFlight(String inputID){
        ArrayList<Flight> searchedFlights = new ArrayList<Flight>();
        for(Map.Entry<String, Booking> entry : bookings.entrySet()){
            Booking currentBooking = entry.getValue();
            if(currentBooking.passenger.getPassengerID().equals(inputID)){;
                searchedFlights.add(flights.get(currentBooking.getFlightNr()));
            }
        }
        return searchedFlights;
    }

    // Returns array for UIManager-functions: searchBookings() and stats()
    public ArrayList<String> searchPrinter(ArrayList<Booking> bookingList, ArrayList<Flight> flightList){
        ArrayList<String> print = new ArrayList<>();
        for(Booking currentBooking : bookingList) {
            Flight currentFlight = flights.get(currentBooking.getFlightNr());
            print.add(currentBooking.getBookingID() + " - " + currentBooking.getFlightNr() + " - " + currentFlight.getDepartureCity() + "(" + currentFlight.getDepDay() + ")" + " - " + currentFlight.getArrivalCity() + "(" + currentFlight.getArvDay() + ")" + " - " + currentBooking.getWeek() + " - " + currentBooking.getYear() + " - " + currentFlight.getDepTime() + " - " + currentFlight.getArvTime());

        }
        return print;
    }

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
            return (year == currentYear && week >= currentWeek) || year > currentYear; // Returns true if input-year is equal (and input-week has not passed) or greater than the devices' current-year
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
        Booking booking;

        if(updatable){
            booking = new UpdatableBooking(bookingID, flightNr, new Passenger(passengerID, name), year, week); // Fix
        } else {
            booking = new UnUpdatableBooking(bookingID, flightNr, new Passenger(passengerID, name), year, week); // Fix
        }

        bookings.put(bookingID, booking);
        if(!passengers.containsKey(passengerID)){
          passengers.put(passengerID, new Passenger(passengerID, name));
        }

        saveToJson();

        return bookingID;
    }

    // (3) Remove Booking (Cancel Booking) related functions
    public boolean rmBooking(String bookingID){
        if(bookings.containsKey(bookingID)){
            String oldPassengerID = bookings.get(bookingID).getPassengerID();

            bookings.remove(bookingID);

            int check = 0;
            for(Map.Entry<String, Booking> entry : bookings.entrySet()){
                String currentPassengerID = entry.getValue().getPassengerID();
                if(Objects.equals(currentPassengerID, oldPassengerID)){
                    check += 1;
                }
            }
            if(check == 0){
                passengers.remove(oldPassengerID);
            }

            saveToJson();

            return true;
        } else{
            return false;
        }
    }

    // (4) Update Booking related functions
    public void updateBooking(String bookingID, String oldPassengerID, String newPassengerID, String newName) {
        bookings.get(bookingID).passenger.setPassengerID(newPassengerID);
        bookings.get(bookingID).passenger.setName(newName);

        int check = 0;
        for (Map.Entry<String, Booking> entry : bookings.entrySet()) {
            String currentPassengerID = entry.getValue().getPassengerID();
            if (Objects.equals(currentPassengerID, oldPassengerID)) {
                check += 1;
            }
        }
        if (check == 0) {
            passengers.remove(oldPassengerID);
        }

        //If the updated booking contains a new passenger, this passenger is added.
        if (!passengers.containsKey(newPassengerID)) {
            passengers.put(newPassengerID, new Passenger(newPassengerID, newName));
        }

        saveToJson();
    }

    public boolean updatebleCheck(String bookingID){
       if(bookings.containsKey(bookingID)){
           if(bookingID.charAt(1) == 'P'){
                return true;
           }
       }
       return false;
    }

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
        String[] weekdays = {"måndag", "tisdag", "onsdag", "torsdag", "fredag", "lördag", "söndag"};
        return Arrays.asList(weekdays).contains(weekday.toLowerCase());
    }

    public boolean airlineCheck(String airline){
        return this.airline.checkAirline(airline);
    }

    public boolean seatCheck(int seats) {
        return (seats >= 80) && (seats <= 380);
    }

    public boolean flightTimeCheck(int flightTime){
        return flightTime >= 1 && flightTime <= 18;
    }

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
    } // Checks if another flight has the same flight airline, departure city and arrival city

    public void createFlight(String flightNr, String departureCity, String time, String weekday, String arrivalCity, String airline, int seats, int flightTime){
        String seatsString = Integer.toString(seats);
        String flightTimeString = Integer.toString(flightTime);
        Flight flight = new Flight(flightNr, departureCity, time, weekday, arrivalCity, airline, seatsString, flightTimeString);
        flights.put(flightNr, flight);

        saveToJson();
    }

    // (6) Remove Flight
    public boolean rmFlight(String flightNr){
        List<String> bookingsToRemove = new ArrayList<>();
        if(flights.containsKey(flightNr)){
            flights.remove(flightNr);
            for(Map.Entry<String, Booking> entry : bookings.entrySet()) { // Loops through every booking to check for bookings with the selected flightNr
                Booking currentBooking =  entry.getValue();
                if(Objects.equals(flightNr, currentBooking.getFlightNr())){
                    System.out.println(currentBooking.getFlightNr());
                    bookingsToRemove.add(entry.getKey()); // Adds the booking to a list if it contains the selected flightNr
                }
            }
            for(String element : bookingsToRemove){
                bookings.remove(element);
            }

            saveToJson();

            return true;
        } else{
            return false;
        }
    }

    // (7) Statistics (GUI) related functions
    public ArrayList<String> findStats(String year, String week, boolean human){
        ArrayList<String> results = new ArrayList<>();
        int yearInt = Integer.parseInt(year);
        int weekInt = Integer.parseInt(week);

        if(human){
            results = searchPrinter(searchBookingStats(yearInt, weekInt), searchFlightStats(yearInt, weekInt));
        }

        return results;
    }

    public ArrayList<Booking> searchBookingStats(int year, int week){
        ArrayList<Booking> searchedBookings = new ArrayList<>();
        for(Map.Entry<String, Booking> entry : bookings.entrySet()){
            Booking currentBooking = entry.getValue();
            int currentYear = currentBooking.getYear();
            int currentWeek = currentBooking.getWeek();

            if((currentYear == year) && (currentWeek == week)){
                searchedBookings.add(entry.getValue());
            }
        }
        return searchedBookings;
    }

    public ArrayList<Flight> searchFlightStats(int year, int week){
        ArrayList<Flight> searchedFlights = new ArrayList<>();
        for(Map.Entry<String, Booking> entry : bookings.entrySet()){
            Booking currentBooking = entry.getValue();
            if((currentBooking.getWeek() == week) && (currentBooking.getYear() == year)){;
                searchedFlights.add(flights.get(currentBooking.getFlightNr()));
            }
        }
        return searchedFlights;
    }

}
