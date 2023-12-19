package se.jimboagency.bookingsystem.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class Airline {
    private String[] airlines =  {"sas","lufthansa","india air","bra","pakistan air"};
    public Airline(){
        this.airlines = airlines;
    }

    public boolean checkAirline(String airline){
        return Arrays.asList(airlines).contains(airline.toLowerCase());
    }
}
