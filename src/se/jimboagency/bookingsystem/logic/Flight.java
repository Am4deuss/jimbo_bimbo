package se.jimboagency.bookingsystem.logic;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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

    public String getDepartureCity(){
        return departureCity;
    }

    public String getAirline(){
        return airline;
    }

    public String getArrivalCity(){
        return arrivalCity;
    }

    public String getDepTime(){
        return time;
    }

    public String getArvTime(){
        String[] splitTime = time.split(":");
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime updatedTime = localTime.plusHours(Integer.parseInt(flightTime));
        return updatedTime.toString();
    }

    public String getDepDay(){
        return date;
    }

    public String getArvDay(){
        boolean newDate = false;

        String[] splitTime = time.split(":");
        int newTime = Integer.parseInt(splitTime[0]) + Integer.parseInt(flightTime);

        if(newTime > 23){
            newDate = true;
        }

        if(newDate){
            String[] weekdays = {"måndag", "tisdag", "onsdag", "torsdag", "fredag", "lördag", "söndag"};

            int index = Arrays.binarySearch(weekdays, date.toLowerCase());

            if(index > 6) {
                index = 0;
            } else {
                index++;
            }

            return weekdays[0];
        }

        return date;
    }

}
