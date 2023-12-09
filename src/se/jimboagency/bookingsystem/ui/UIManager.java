package se.jimboagency.bookingsystem.ui;

// Class-import
import se.jimboagency.bookingsystem.logic.LogicManager;

// Library-import
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class UIManager {
    private Scanner input;
    private LogicManager logic;

    public UIManager(LogicManager logic) {
        input = new Scanner(System.in);
        this.logic = logic;
    }

    public void show_menu() {
        //if(auth()) {
        String[] options = {"Sök resa", "Boka resa", "Avboka resa", "Uppdatera resa", "Skapa flight", "Ta bort flight", "Statistik", "Avsluta"};

        System.out.println("=================");
        for(int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print("========│ val: ");
        String choice = input.next();

        switch(choice) {
            case "2": {
                createBooking();
                break;
            }

            case "5": {
                createFlight();
                break;
            }

            // Remove Flight
            case "6": {
                rmFlight();
                break;
            }
        }
    }

    public void createBooking(){
        // Updatable booking?
        System.out.print("Uppdateringsbar resa (y/n): ");
        String updatableInput;
        boolean updatable;
        while(true) {
            updatableInput = input.next(); // Handles several different synonyms of the same input (for example y, yes, j, ja)

            if(Objects.equals(this.logic.updatableInputCheck(updatableInput), "t")) {
                updatable = true;
                break;
            } else if(Objects.equals(this.logic.updatableInputCheck(updatableInput), "f")){
                updatable = false;
                break;
            } else {
                System.out.println("Fel format.");
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume input
            }
        }

        // Flight number
        System.out.print("Flight-nummer: ");
        String flightNr;
        while(true) {
            flightNr = input.next(); // Check if flight is not fully booked (if full -> ERROR)

            if (this.logic.flightnrPatternCheck(flightNr)) { // REGEX "AA-000" -> "ZZ-999"
                if(!this.logic.flightnrCheck(flightNr)) {
                    break;
                } else {
                    System.out.println("Angivet Flight-nummer: " + flightNr + " finns ej.");
                    System.out.print("Försök igen: ");
                    input.nextLine(); // Consume input
                }
            } else {
                System.out.println("Fel format.");
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume input
            }
        }

        // Passenger-ID
        System.out.print("Passagerar-ID: ");
        String passengerID;
        while(true) {
            passengerID = input.next(); // REGEX "12345678" (Numbers 0-9)
            if(this.logic.passengerIDCheck(passengerID)){
                break;
            } else {
                System.out.println("Fel format. Måste vara åtta siffror.");
                System.out.println("Försök igen: ");
                input.nextLine(); // Consume the invalid input
            }
        }

        // Name (Firstname, Lastname)
        System.out.print("Namn (för och efternamn): ");
        String name = input.next();
        input.nextLine(); // Consume input

        // Year
        System.out.print("År: ");
        int year;
        while(true) {
            try {
                year = input.nextInt(); // Input management - must be an integer, year can not have passed
                if(this.logic.yearCheck(year)){
                    break;
                } else {
                    System.out.println("Fel, Året har passerat. Ange giltigt år.");
                    System.out.print("Försök igen: ");
                    input.nextLine(); // Consume the invalid input
                }
            } catch (InputMismatchException e) {
                System.out.println("Fel format. Ange ett tal.");
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume the invalid input
            }
        }

        // Week
        System.out.print("Vecka: ");
        int week;
        while(true) {
            try {
                week = input.nextInt(); // Input management - week can not have passed, Must be in range of 1-52
                if(this.logic.weekCheck(week, year)){
                    break;
                } else {
                    System.out.println("Fel, veckan har passerat. Ange giltig vecka.");
                    System.out.print("Försök igen: ");
                    input.nextLine(); // Consume the invalid input
                }
            } catch (InputMismatchException e) {
                System.out.println("Fel format. Ange ett tal.");
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume the invalid input
            }
        }

        String bookingID = logic.createBooking(updatable, flightNr, passengerID, name, year, week); // Returns unique generated bookingID
        System.out.println("Bokning skapad!");
        System.out.println("Ditt resenummer är: " + bookingID);

        logic.showBookings();

        show_menu();
    }

    public void createFlight(){
        // Flight Number
        System.out.print("Flight-nummer: ");
        String flightNr;
        while (true) {
            flightNr = input.next();

            if (this.logic.flightnrCheck(flightNr)) {
                if (this.logic.flightnrPatternCheck(flightNr)){
                    break;
                } else {
                    System.out.println("Fel format.");
                    System.out.print("Försök igen: ");
                }
            } else {
                System.out.println("Flight-nummer existerar redan.");
                System.out.print("Försök igen: ");
            }
        }

        // Departure City
        System.out.print("Avgångsstad: ");
        String departureCity = input.next();
        input.nextLine(); // Consume input

        // Departure Time
        System.out.print("Avgångstid (Skrivs enligt 00:00): ");
        String time;
        while(true) {
            time = input.next();
            if(this.logic.timePatternCheck(time)){ // REGEX "00:00" -> "23:59"
                break;
            } else {
                System.out.println("Fel format.");
                System.out.print("Försök igen: ");
            }
        }

        // Departure Date
        System.out.print("Avgångsdag: ");
        String weekday;
        while(true) {
            weekday = input.next(); // Used for calculations together with time, flightTime

            if(this.logic.weekdayCheck(weekday)){
                break;
            } else {
                System.out.println("Måste vara en veckodag.");
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume input
            }
        }

        // Arrival City
        System.out.print("Ankomststad: ");
        String arrivalCity = input.next();
        input.nextLine(); // Consume input

        // Airline
        System.out.print("Flygbolag: ");
        String airline = input.next(); // Pre-programmed from list of airlines (min 10)
        input.nextLine(); // Consume input

        // Seat specification
        int seats;
        while (true) {
            try {
                System.out.print("Platser: ");
                seats = input.nextInt();

                if (this.logic.seatCheck(seats)) { // Checks if integer is between 80-380
                    break;
                } else {
                    System.out.println("Numret måste vara mellan 80 och 380.");
                }
            } catch (InputMismatchException e) { // Input is not an integer -> ERROR
                System.out.println("Fel format. Skriv ett tal.");
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume the invalid input
            }
        }

        // Flight time/duration
        int flightTime;
        System.out.print("Flygtid (timmar): ");
        while (true) {
            try {
                flightTime = input.nextInt();
                if(!this.logic.flightTimeCheck(flightTime)){ // Checks if integer is between 1 and 18
                    System.out.println("Heltalet måste vara mellan 1 och 18 (timmar).");
                    System.out.print("Försök igen: ");
                } else {
                    break;
                }
            } catch (InputMismatchException e) { // Input is not an integer -> ERROR
                System.out.println("Fel format. Skriv ett tal.");
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume the invalid input
            }
        }


        if(logic.createFlightCheck(departureCity, airline, arrivalCity)){
            logic.createFlight(flightNr, departureCity, time, weekday, arrivalCity, airline, seats, flightTime);
        } else {
            System.out.println("Error: Ett annat flyg har samma flygbolag, avgångsstad och ankomststad.");
        }

        logic.showFlights(); // Debug

        show_menu();
    }

    public void rmFlight(){
        System.out.print("Flight-nummer: ");
        String flightNr;
        while (true) {
            flightNr = input.next();

            if (this.logic.removeFlight(flightNr)) { // Fix so that all related bookings are removed aswell
                break;
            } else {
                System.out.println("Angivet Flight-nummer: " + flightNr + " finns ej.");
            }
        }

        logic.showFlights(); // Debug

        show_menu();
    }
}
