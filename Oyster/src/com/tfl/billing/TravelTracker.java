package com.tfl.billing;

import com.tfl.external.Customer;


import java.math.BigDecimal;
import java.util.*;

public class TravelTracker  {

    private final TotalDaySpent totalDaySpent = new TotalDaySpent();
    private ExternalJarAdapter externalJarAdapter;
    private CardInteraction cardInteraction;


    public TravelTracker(CardInteraction cardInteraction) {
        this.cardInteraction = cardInteraction;
        this.externalJarAdapter = new ExternalJarAdapter();
    }

    //charge each customer from the database
    public void chargeAccounts() {
        List<Customer> customers = externalJarAdapter.getCustomers();

        for (Customer customer : customers) {
            totalJourneysFor(customer);
        }

    }
    //gets the total payment a customer must do and charge him
    private void totalJourneysFor(Customer customer) {

        List<JourneyEvent> customerJourneyEvents = new ArrayList<>();
        createJourneyEventsList(customer, customerJourneyEvents);  //creates a list with journey events which corresponds to a given customer
        List<Journey> journeys = new ArrayList<>();
        createJourneyList(customerJourneyEvents, journeys);        // creates a list with the journeys

        BigDecimal customerTotal = totalDaySpent.customerTotalCost(journeys); //calculates how much a customer must be charged

        externalJarAdapter.charge(customer, journeys, totalDaySpent.roundToNearestPenny(customerTotal));  // charges customers
    }

    //creates a list of Journeys
    private void createJourneyList(List<JourneyEvent> customerJourneyEvents, List<Journey> journeys) {
        JourneyEvent start = null;
        for (JourneyEvent event : customerJourneyEvents) {
            if (event instanceof JourneyStart) {
                start = event;
            }
            if (event instanceof JourneyEnd && start != null) {
                journeys.add(new Journey(start, event));
                start = null;
            }
        }
    }
    // choose only the journey events that correspond to a given customer
    private void createJourneyEventsList(Customer customer, List<JourneyEvent> customerJourneyEvents) {
        List<JourneyEvent> eventLog = cardInteraction.getEventLog();
        for (JourneyEvent journeyEvent : eventLog) {
            if (journeyEvent.cardId().equals(customer.cardId())) {
                customerJourneyEvents.add(journeyEvent);
            }
        }
    }





}
