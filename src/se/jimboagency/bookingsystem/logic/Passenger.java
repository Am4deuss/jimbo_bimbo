package se.jimboagency.bookingsystem.logic;

public class Passenger {
    private String name;
    private String passengerID;

    public Passenger(String passengerID, String name) {
        this.name = name;
        this.passengerID = passengerID;
    }

    public String getName(){
        return name;
    }
    public String getPassengerID(){
        return this.passengerID;
    }
    public void setName(String newName){
        name = newName;
    }

    public void setPassengerID(String newPassengerID){
        passengerID = newPassengerID;
    }
}
