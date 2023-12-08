package se.jimboagency.bookingsystem.ui;

// Class-import
import se.jimboagency.bookingsystem.logic.LogicManager;

// Library-import
import java.util.InputMismatchException;
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
        String updatable = input.next(); // REGEX "y or n" ??? Up for debate

        // Flight number
        System.out.print("Flight-nummer: ");
        String flightNr;
        while (true) {
            flightNr = input.next(); // Check if flight is not fully booked (if full -> ERROR)

            if (this.logic.flightnrCheck(flightNr)) {
                break;
            } else {
                System.out.println("Angivet Flight-nummer: " + flightNr + " finns ej.");
                System.out.println("Försök igen: ");
            }
        }

        // Passenger-ID
        System.out.print("Passagerar-ID: ");
        String passengerID = input.next(); // REGEX HERE "yymmddxxxx" (Numbers 0-9) (Personal identification number)

        // Name (Firstname, Lastname)
        System.out.print("Namn (för och efternamn): ");
        String name = input.next(); // Input management here

        // Year
        System.out.print("År: ");
        String year = input.next(); // REGEX HERE "0000" (INT 1-9), Input management - year can not have passed

        // Week
        System.out.print("Vecka: ");
        // (ONLY INTS ALLOWED (MAYBE TRY/CATCH)) Input management - week can not have passed, Must be in range of 1-52
        while (true) {
            try {
                int week = input.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Fel format. Skriv ett tal.");
                System.out.println("Försök igen: ");
                input.nextLine(); // Consume the invalid input
            }
        }


        // If all of this is correct -> Booking successfully created!
        // Call createBooking() in logic
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
                    System.out.println("Försök igen: ");
                }
            } else {
                System.out.println("Flight-nummer existerar redan.");
            }
        }

        // Departure City
        System.out.print("Avgångsstad: ");
        String departureCity = input.next(); // Pre-programmed from list of cities

        // Departure Time
        System.out.print("Avgångstid (Skrivs enligt 00:00): ");
        String time; // REGEX "00:59"
        while (true) {
            time = input.next();
            if(!this.logic.timePatternCheck(time)){
                System.out.println("Fel format.");
                System.out.print("Försök igen: ");
            } else {
                break;
            }
        }

        // Departure Date
        System.out.print("Avgångsdag: ");
        String date = input.next(); // Used for calculations together with time, flightTime

        // Arrival City
        System.out.print("Ankomststad: ");
        String arrivalCity = input.next(); // Pre-programmed from list of cities

        // Airline
        System.out.print("Flygbolag: ");
        String airline = input.next(); // Pre-programmed from list of airlines

        // Seat specification
        int seats;
        while (true) {
            try {
                System.out.print("Platser: ");
                seats = input.nextInt();

                if (!this.logic.seatCheck(seats)) { // Checks if integer is between 80-380
                    System.out.println("Numret måste vara mellan 80 och 380.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) { // Input is not an integer -> ERROR
                System.out.println("Fel format. Skriv ett tal.");
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume the invalid input
            }
        }

        // Flight time/duration
        int flightTime;
        System.out.print("Flygtid: ");
        while (true) {
            try {
                flightTime = input.nextInt();
                if(!this.logic.flightTimeCheck(flightTime)){ // Checks if integer is greater than 0
                    System.out.println("Talet måste vara större än 0.");
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
            logic.createFlight(flightNr, departureCity, time, date, arrivalCity, airline, seats, flightTime);
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

            if (this.logic.removeFlight(flightNr)) {
                break;
            } else {
                System.out.println("Angivet Flight-nummer: " + flightNr + " finns ej.");
            }
        }

        logic.showFlights(); // Debug

        show_menu();
    }
}
