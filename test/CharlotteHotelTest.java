import org.junit.Test;


public class CharlotteHotelTest extends BaseHotelTest {
    private static final String CITY = "Charlotte";
    private static final String REGION_ID = "178247"; // Charlotte's region ID

    @Test
    public void searchCharlotteHotelPrices() throws InterruptedException {
        searchHotelPrices(CITY, REGION_ID);
    }
    // ... same methods as Atlanta
}