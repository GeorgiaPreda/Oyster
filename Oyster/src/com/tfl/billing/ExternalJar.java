package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ExternalJar {

    List<Customer> getCustomers();

    boolean isRegisteredId(UUID cardId);

    OysterCardReader getCardReader(Station station);

    void charge(Customer customer, List<Journey> journeys, BigDecimal cost);

    OysterCard getOysterCard(String id);
}
