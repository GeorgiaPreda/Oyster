package com.tfl.billing;

import com.oyster.*;
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

        createJourneyEventsListForCustomer(customer, customerJourneyEvents);

        List<Journey> journeys = new ArrayList<>();
        createJourneyList(customerJourneyEvents, journeys);

        BigDecimal customerTotal = customerTotalCost(journeys);

        externalJarAdapter.charge(customer, journeys, totalDaySpent.roundToNearestPenny(customerTotal));
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
    private void createJourneyEventsListForCustomer(Customer customer, List<JourneyEvent> customerJourneyEvents) {
        List<JourneyEvent> eventLog = cardInteraction.getEventLog();
        for (JourneyEvent journeyEvent : eventLog) {
            if (journeyEvent.cardId().equals(customer.cardId())) {
                customerJourneyEvents.add(journeyEvent);
            }
        }
    }
    // does calculation per each journey and checks if the daily cap is reached
    private BigDecimal customerTotalCost(List<Journey> journeys) {
        BigDecimal customerTotal = new BigDecimal(0);
        for (Journey journey : journeys) {

            BigDecimal journeyPrice = totalDaySpent.costOfOneJourney(journey);
            customerTotal = customerTotal.add(journeyPrice);
        }
        customerTotal = testForDailyCap(customerTotal);
        return customerTotal;
    }
    // checks if the dailycap must be applied or not
    private BigDecimal testForDailyCap(BigDecimal customerTotal) {
        BigDecimal dailyCapForPeak=new BigDecimal(9);
        BigDecimal dailyCapForNonPeak=new BigDecimal(7);

        if(totalDaySpent.found_peak()==true) {
            if(customerTotal.compareTo(dailyCapForPeak)>0)
                customerTotal=dailyCapForPeak;}
        else
        if(customerTotal.compareTo(dailyCapForNonPeak)>0)
            customerTotal=dailyCapForNonPeak;

        return customerTotal;
    }



}
