import org.junit.Test;

public class AtlantaHotelTest extends BaseHotelTest {
    private static final String CITY = "Atlanta";
    private static final String REGION_ID = "178232";

    @Test
    public void searchAtlantaHotelPrices() throws InterruptedException {
        searchHotelPrices(CITY, REGION_ID);
    }
}