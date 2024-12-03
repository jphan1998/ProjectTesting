import org.junit.Test;

public class AustinHotelTest extends BaseHotelTest {
    private static final String CITY = "Austin";
    private static final String REGION_ID = "178234"; // Austin's region ID

    @Test
    public void searchAustinHotelPrices() throws InterruptedException {
        searchHotelPrices(CITY, REGION_ID);
    }
}