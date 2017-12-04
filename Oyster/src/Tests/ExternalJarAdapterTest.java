package Tests;

import com.oyster.OysterCard;
import com.tfl.billing.ExternalJarAdapter;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ExternalJarAdapterTest {

    ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();

    @Test
    public void assertThatGetCustomersMethodReturnsTheCustomersList(){
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        assertEquals(externalJarAdapter.getCustomers(),customerDatabase.getCustomers());
    }

    @Test
    public void assertThatGetOysterCardReturnsANewOysterWithTheGivenStringAsId(){
        String id = "38400000-8cf0-11bd-b23e-10b96e4ef00d";

        assertTrue(externalJarAdapter.getOysterCard(id) instanceof OysterCard);
        assertEquals(externalJarAdapter.getOysterCard(id).id().toString(), id);
    }

    @Test
    public void assertThatGetCardReaderReturnsTheCorrectReader(){

        assertEquals(externalJarAdapter.getCardReader(Station.PADDINGTON), OysterReaderLocator.atStation(Station.PADDINGTON));
    }


}
