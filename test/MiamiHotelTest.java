import org.junit.Test;

public class MiamiHotelTest extends BaseHotelTest {
    private static final String CITY = "Miami";
    private static final String REGION_ID = "178286"; // Miami's region ID

    @Test
    public void searchMiamiHotelPrices() throws InterruptedException {
        searchHotelPrices(CITY, REGION_ID);
    }
}