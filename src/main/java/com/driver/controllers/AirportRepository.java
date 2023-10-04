package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {


    HashMap<String,Airport> airportHashMap = new HashMap<>();

    HashMap<Integer, Flight> flightHashMap = new HashMap<>();

    HashMap<Integer,List<Integer>> flightPassangerMap = new HashMap<>();

    HashMap<Integer, Passenger> passengerHashMap = new HashMap<>();

    HashMap<Integer,Integer> passangerBookingmap = new HashMap<>();
    public void addAirport(Airport airport) {

        airportHashMap.put(airport.getAirportName(),airport);
    }

    public String getLargestAirportName() {

        String pans = "";
        int max = 0;
        for(Airport ar : airportHashMap.values())
        {
            if(ar.getNoOfTerminals() > max)
            {
                max = ar.getNoOfTerminals();

            }
        }

        List<String> list = new ArrayList<>();
        for(Airport ar : airportHashMap.values())
        {
            if(ar.getNoOfTerminals() == max)
            {
                list.add(ar.getAirportName());
            }
        }
        Collections.sort(list);
        pans = list.get(0);
        return pans;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {

        double dur = Double.MAX_VALUE;
        for(Flight f : flightHashMap.values())
        {
            if(f.getFromCity() == fromCity && f.getToCity() == toCity && f.getDuration() < dur)
            {
                dur = f.getDuration();
            }
        }

        if(dur == Double.MAX_VALUE) return -1;
        return dur;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {

        int num = 0;
        Airport ar = airportHashMap.get(airportName);
        City curr = ar.getCity();

        for(Flight f : flightHashMap.values())
        {
            if(f.getFlightDate() == date && (f.getFromCity()==curr || f.getToCity()==curr))
            {
                List<Integer> list = flightPassangerMap.get(f.getFlightId());
                num += list.size();
            }
        }
        return num;
    }

    public String addFlight(Flight flight) {
        
        if(!flightHashMap.containsKey(flight.getFlightId()))
        {
            flightHashMap.put(flight.getFlightId(), flight);
            List<Integer> list= new ArrayList<>();
            flightPassangerMap.put(flight.getFlightId(),list);
            return "SUCCESS";
        }
        else return "Flight Already Exist";
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        
        if(!flightHashMap.containsKey(flightId)) return null;
        Flight f = flightHashMap.get(flightId);
        City curr = f.getFromCity();
        
        for(Airport ar : airportHashMap.values())
        {
            if(ar.getCity() == curr)
            {
                return ar.getAirportName();
            }
        }
         return null;
    }

    public int calculateFlightFare(Integer flightId) {

        List<Integer> list = flightPassangerMap.get(flightId);
        int numOfExistingPassanger = list.size();
        int currFair = 3000 + numOfExistingPassanger*50;
        return currFair;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {

        if(!flightPassangerMap.containsKey(flightId) || !passengerHashMap.containsKey(passengerId)) return "FAILURE";
        List<Integer> list = flightPassangerMap.get(flightId);

        if(list.contains(passengerId)) return "FAILURE";
        Flight f = flightHashMap.get(flightId);
        if(list.size() == f.getMaxCapacity())return "FAILURE";

        list.add(passengerId);
        flightPassangerMap.put(flightId,list);
        passangerBookingmap.put(passengerId,passangerBookingmap.getOrDefault(passengerId,0)+1);
        return "SUCCESS";
    }

    public String cancelAticket(Integer flightId, Integer passengerId) {

        if(!flightPassangerMap.containsKey(flightId) || !passengerHashMap.containsKey(passengerId))return "FAILURE";
        List<Integer> list = flightPassangerMap.get(flightId);
        if(!list.contains(passengerId))return "FAILURE";
        list.remove(passengerId);
        flightPassangerMap.put(flightId,list);
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {

        return passangerBookingmap.getOrDefault(passangerBookingmap,0);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {

        List<Integer> list = flightPassangerMap.get(flightId);
        int totalNumOfPassanger = list.size();
        int totalrevenue = totalNumOfPassanger*3000 + totalNumOfPassanger*50;
        return totalrevenue;
    }

    public String addPassenger(Passenger passenger) {

        if(passengerHashMap.containsKey(passenger.getPassengerId())) return "Passenger already exist";
        else passengerHashMap.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }
}
