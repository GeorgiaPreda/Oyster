package com.tfl.billing;

import com.oyster.*;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;

import java.math.BigDecimal;
import java.util.*;

public class TravelTracker implements ScanListener {

    private final List<JourneyEvent> eventLog = new ArrayList();
    private final Set<UUID> currentlyTravelling = new HashSet();
    private final TotalDaySpent totalDaySpent = new TotalDaySpent();
    private ExternalJarAdapter externalJarAdapter;
    private CardInteraction cardInteraction;
    static final int dailyCapForPeak = 9;
    static final int dailyCapNonPeak = 7;

    public TravelTracker(CardInteraction cardInteraction) {
        this.cardInteraction = cardInteraction;
        this.externalJarAdapter = new ExternalJarAdapter();
    }

    public void chargeAccounts() {
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        List<Customer> customers = this.externalJarAdapter.getCustomers();
        Iterator var3 = customers.iterator();

        while(var3.hasNext()) {
            Customer customer = (Customer)var3.next();
            this.totalJourneysFor(customer);
        }

    }

    private void totalJourneysFor(Customer customer) {
        List<JourneyEvent> customerJourneyEvents = new ArrayList();
        List<JourneyEvent> eventLog = this.cardInteraction.getEventLog();
        Iterator var4 = eventLog.iterator();

        while(var4.hasNext()) {
            JourneyEvent journeyEvent = (JourneyEvent)var4.next();
            if (journeyEvent.cardId().equals(customer.cardId())) {
                customerJourneyEvents.add(journeyEvent);
            }
        }

        List<Journey> journeys = this.getJourneysForEachCustomer(customerJourneyEvents);
        BigDecimal customerTotal = this.customerTotalCost(journeys);
        this.externalJarAdapter.charge(customer, journeys, this.totalDaySpent.roundToNearestPenny(customerTotal));
    }

    private List<Journey> getJourneysForEachCustomer(List<JourneyEvent> customerJourneyEvents) {
        List<Journey> journeys = new ArrayList();
        JourneyEvent start = null;
        Iterator var4 = customerJourneyEvents.iterator();

        while(var4.hasNext()) {
            JourneyEvent event = (JourneyEvent)var4.next();
            if (event instanceof JourneyStart) {
                start = event;
            }

            if (event instanceof JourneyEnd && start != null) {
                journeys.add(new Journey(start, event));
                start = null;
            }
        }

        return journeys;
    }

    private BigDecimal customerTotalCost(List<Journey> journeys) {
        BigDecimal customerTotal = new BigDecimal(0);

        BigDecimal journeyPrice;
        for(Iterator var3 = journeys.iterator(); var3.hasNext(); customerTotal = customerTotal.add(journeyPrice)) {
            Journey journey = (Journey)var3.next();
            journeyPrice = this.totalDaySpent.costOfOneJourney(journey);
        }

        customerTotal = this.testForDailyCap(customerTotal);
        return customerTotal;
    }

    private BigDecimal testForDailyCap(BigDecimal customerTotal) {
        BigDecimal dailyCapForPeak = new BigDecimal(9);
        BigDecimal dailyCapForNonPeak = new BigDecimal(7);
        if (this.totalDaySpent.found_peak()) {
            if (customerTotal.compareTo(dailyCapForPeak) > 0) {
                customerTotal = dailyCapForPeak;
            } else if (customerTotal.compareTo(dailyCapForNonPeak) > 0) {
                customerTotal = dailyCapForNonPeak;
            }
        }

        return customerTotal;
    }

    public void connect(OysterCardReader... cardReaders) {
        OysterCardReader[] var2 = cardReaders;
        int var3 = cardReaders.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            OysterCardReader cardReader = var2[var4];
            cardReader.register(this);
        }

    }

    public void cardScanned(UUID cardId, UUID readerId) {
        if (this.currentlyTravelling.contains(cardId)) {
            this.eventLog.add(new JourneyEnd(cardId, readerId));
            this.currentlyTravelling.remove(cardId);
        } else {
            if (!CustomerDatabase.getInstance().isRegisteredId(cardId)) {
                System.out.println(cardId);
                throw new UnknownOysterCardException(cardId);
            }

            this.currentlyTravelling.add(cardId);
            this.eventLog.add(new JourneyStart(cardId, readerId));
        }

    }

    public void cardScanned(UUID cardId, UUID readerId, String time) {
        if (this.currentlyTravelling.contains(cardId)) {
            this.eventLog.add(new JourneyEnd(cardId, readerId, time));
            this.currentlyTravelling.remove(cardId);
        } else {
            if (!CustomerDatabase.getInstance().isRegisteredId(cardId)) {
                throw new UnknownOysterCardException(cardId);
            }

            this.currentlyTravelling.add(cardId);
            this.eventLog.add(new JourneyStart(cardId, readerId, time));
        }

    }



}
