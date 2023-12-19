package se.jimboagency.bookingsystem.ui;

// Class-import
import se.jimboagency.bookingsystem.logic.LogicManager;

// Library-import
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class UIManager {
    private  String[] args;
    private Scanner input;
    private LogicManager logic;

    public UIManager(LogicManager logic, String[] args) {
        input = new Scanner(System.in);
        this.logic = logic;
        this.args = args;
    }

    public void show_menu() {
        if(this.logic.authCheck(args)){
            String[] options = {"Sök resa", "Boka resa", "Avboka resa", "Uppdatera resa", "Skapa flight", "Ta bort flight", "Statistik", "Avsluta"};

            System.out.println("Hej och välkommen " + this.logic.getUsername());

            System.out.println("=================");
            for(int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print("========│ val: ");
            String choice = input.next();

            switch(choice) {
                // Search for Bookings
                case "1": {
                    searchBookings();
                    break;
                }

                // Create Booking
                case "2": {
                    createBooking();
                    break;
                }

                // Remove Booking
                case "3": {
                    rmBooking();
                    break;
                }

                //Update Booking
                case "4": {
                    updateBooking();
                    break;
                }

                // Create Flight
                case "5": {
                    createFlight();
                    break;
                }

                // Remove Flight
                case "6": {
                    rmFlight();
                    break;
                }

                // Statistics
                case "7": {
                    stats();
                    break;
                }

                // Exit Jimbo
                case "8": {
                    exitJimbo();
                    break;
                }
            }
        }else{
            System.out.println("Felaktig inmatning.");
            exitJimbo();
        }

    }

    public void searchBookings(){
        //System.out.println(name + " - " + matchQuantity + " found.")
        System.out.print("Skriv in personnr på passageraren eller q för att gå tillbaka: ");
        String searchPassenger = input.next();
        ArrayList<String> result = this.logic.searchPrinter(this.logic.searchBooking(searchPassenger),this.logic.searchFlight(searchPassenger));
        String name = this.logic.getPassengerName(searchPassenger);
        if(!name.equals("")){
            System.out.println(name + " - "+result.size()+" träff/-ar.");
            System.out.println("===============================");
            if(!searchPassenger.equals("q")){
                for(String row: result){
                    System.out.println(row + "\n");
                }

            }
        }else{
            System.out.println("Passageraren finns ej.");
        }


        show_menu();

    }

    public void createBooking(){
        // Updatable booking?
        System.out.print("Uppdateringsbar resa (y/n) eller q för att avsluta: ");
        String updatableInput = input.next();  // Handles several different synonyms of the same input (for example y, yes, j, ja)
        boolean updatable;
        while(true) {

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
            updatableInput = input.next();
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
                System.out.print("Försök igen: ");
                input.nextLine(); // Consume the invalid input
            }
        }
        input.nextLine(); // Consume the invalid input

        // Name (Firstname, Lastname)
        System.out.print("Namn (för och efternamn): ");
        String name = input.nextLine();

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
                    System.out.println("Fel, ogiltig vecka. Ange giltig vecka.");
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

        show_menu();
    }

    public void rmBooking(){
        System.out.print("Boknings-ID eller q för att gå tillbaka: ");
        String bookingID = input.next();

        while (!bookingID.equals("q")) {
            if (this.logic.rmBooking(bookingID)) { // Bookings can also be deleted if the relating flight is deleted
                break;
            } else {
                System.out.println("Angivet  Boknings-ID: " + bookingID + " finns ej.");
                System.out.print("Försök igen: ");
            }
            bookingID = input.next();
        }

        show_menu();
    }

    public void updateBooking(){
        System.out.print("Boknings-ID eller q för att gå tillbaka: ");
        String bookingID = input.next();
        input.nextLine(); // Consume the invalid input
        String newName;
        String oldPassengerID;
        String newPassengerID;
        
       if(this.logic.updatebleCheck(bookingID))
            while (!bookingID.equals("q")) {

                System.out.print("Skriv in det nya namnet: ");
                newName = input.nextLine();


                // Old passenger-ID
                System.out.print("Gammalt passagerar-ID: ");
                while (true) {
                    oldPassengerID = input.next(); // REGEX "12345678" (Numbers 0-9)
                    if (this.logic.passengerIDCheck(oldPassengerID)) {
                        break;
                    } else {
                        System.out.println("Fel format. Måste vara åtta siffror.");
                        System.out.print("Försök igen: ");
                        input.nextLine(); // Consume the invalid input
                    }
                }
                // Nytt passenger-ID
                System.out.print("Nytt passagerar-ID: ");
                while (true) {
                    newPassengerID = input.next(); // REGEX "12345678" (Numbers 0-9)
                    if (this.logic.passengerIDCheck(newPassengerID)) {
                        break;
                    } else {
                        System.out.println("Fel format. Måste vara åtta siffror.");
                        System.out.print("Försök igen: ");
                        input.nextLine(); // Consume the invalid input
                    }
                }
                this.logic.updateBooking(bookingID, oldPassengerID, newPassengerID, newName);
                break;
            }
       else{
            System.out.println("Hittade inte uppdateringsbar bokning.");
       }
       show_menu();
    }

    public void createFlight(){
        // Flight Number
        System.out.print("Flight-nummer: ");
        String flightNr;
        while (true) {
                flightNr = input.next();

                if (this.logic.flightnrCheck(flightNr)) {
                    if (this.logic.flightnrPatternCheck(flightNr)) {
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
        String airline = input.nextLine(); // Pre-programmed from list of airlines (minimum of 10 airlines)
        while(true){
            if(this.logic.airlineCheck(airline)){
                break;
            }   else{
                System.out.println("Ogiltigt flygbolag.");
                System.out.print("Försök igen: ");
            }
            airline = input.nextLine();
        }

        // Seat specification
        System.out.print("Platser: ");
        int seats;
        while (true) {
            try {
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

        show_menu();
    }

    public void rmFlight(){
        System.out.print("Flight-nummer eller q för att avsluta: ");
        String flightNr = input.next();
        while (!flightNr.equals("q")) {

            if (this.logic.rmFlight(flightNr)) { // All related bookings are also removed
                break;
            } else {
                System.out.println("Angivet Flight-nummer: " + flightNr + " finns ej.");
                System.out.print("Försök igen: ");
            }
            flightNr = input.next();
        }

        show_menu();
    }

    public void stats(){
        GUI gui = new GUI(this.logic);
        show_menu();
    }

    public void exitJimbo(){
        // Add save functions (flights,bookings), write to .json file
        System.out.print("hejdÅ");
        System.exit(0);
    }
}
