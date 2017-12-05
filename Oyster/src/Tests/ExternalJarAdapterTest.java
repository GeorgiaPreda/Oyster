package Tests;

import com.oyster.OysterCard;
import com.tfl.billing.ExternalJarAdapter;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class ExternalJarAdapterTest {


    //tests if ExternalJarAdapter gets the right data from ExternalJar

    ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();

    @Test
    public void assertThatGetCustomersMethodReturnsTheCustomersList(){

        assertEquals(externalJarAdapter.getCustomers(),customerDatabase.getCustomers());
    }

    @Test
    public void assertThatGetOysterCardReturnsANewOysterWithTheGivenStringAsId(){
        String id = "38400000-8cf0-11bd-b23e-10b96e4ef00d";

        assertTrue(externalJarAdapter.getOysterCard(id) instanceof OysterCard);
        assertEquals(externalJarAdapter.getOysterCard(id).id().toString(), id);
    }

    @Test
    public void assertThatIsRegisteredMethodReturnsTheSameAsCustomerDataBasesIsRegistered() throws Exception{

        UUID cardID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        assertEquals(externalJarAdapter.isRegisteredId(cardID),customerDatabase.isRegisteredId(cardID));
    }

    @Test
    public void assertThatGetCardReaderReturnsTheCorrectReader(){

        assertEquals(externalJarAdapter.getCardReader(Station.PADDINGTON), OysterReaderLocator.atStation(Station.PADDINGTON));
    }


}
