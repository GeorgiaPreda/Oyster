package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ExternalJarAdapter implements ExternalJar  {


    //gets the customer list from the Database
    @Override
    public List<Customer> getCustomers() {

        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        List<Customer> customers = customerDatabase.getCustomers();
        return customers;
    }


    //gets response from the DataBase( which is in ExternalJar) if a card is registered
    @Override
    public boolean isRegisteredId(UUID cardId) {
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        return customerDatabase.isRegisteredId(cardId);
    }

    //get OysterCardReader data from ExternalJar
    @Override
    public OysterCardReader getCardReader(Station station) {
        OysterCardReader ourReader = OysterReaderLocator.atStation(station);
        return ourReader;
    }


    // charges a given customer with a given journeys and total amount of payment that needs to be done
    @Override
    public void charge(Customer customer, List<Journey> journeys, BigDecimal cost) {
        PaymentsSystem.getInstance().charge(customer,journeys,cost);
    }

    //gets OysterCard data from ExternalJar
    @Override
    public OysterCard getOysterCard(String id) {

        OysterCard myOysterCard = new OysterCard(id);
        return myOysterCard;
    }
}
