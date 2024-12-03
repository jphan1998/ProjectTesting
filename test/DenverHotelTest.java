import org.junit.Test;


public class DenverHotelTest extends BaseHotelTest {
    private static final String CITY = "Denver";
    private static final String REGION_ID = "178254"; // Denver's region ID

    @Test
    public void searchDenverHotelPrices() throws InterruptedException {
        searchHotelPrices(CITY, REGION_ID);
    }
    // ... same methods as Atlanta
}