package se.jimboagency.bookingsystem.ui;

// Class-import
import se.jimboagency.bookingsystem.logic.LogicManager;

// Library-import
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
        int choice = input.nextInt();

        switch(choice) {
            case 5: {
                // Flight Number
                String flightNr;
                while (true) {
                    System.out.print("Flight-nummer: ");
                    flightNr = input.next();

                    if (this.logic.flightnrCheck(flightNr)) {
                        break;
                    } else {
                        System.out.println("Flight-nummer existerar redan.");
                    }
                }

                // Departure City
                System.out.print("Avgångsstad: ");
                String departureCity = input.next();

                // Departure Time
                System.out.print("Avgångstid: ");
                String time = input.next();

                // Departure Date
                System.out.print("Avgångsdag: ");
                String date = input.next();

                // Arrival City
                System.out.print("Ankomstdag: ");
                String arrivalCity = input.next();

                // Airline
                System.out.print("Flygbolag: ");
                String airline = input.next();

                // Seat specification
                String seats;
                while (true) {
                    System.out.print("Platser: ");
                    seats = input.next();

                    if (this.logic.seatCheck(seats)) {
                        break;
                    } else {
                        System.out.println("Numret måste vara mellan 80 och 380.");
                    }
                }

                // Flight time/duration
                System.out.print("Flygtid: ");
                String flightTime = input.next();

                if(logic.createFlightCheck(departureCity, airline, arrivalCity)){
                    logic.createFlight(flightNr, departureCity, time, date, arrivalCity, airline, seats, flightTime);
                } else {
                    System.out.println("Error: Ett annat flyg har samma flygbolag, avgångsstad och ankomststad.");
                }

                logic.showFlights();

                show_menu();
            }
        }

        //}
    }
}
