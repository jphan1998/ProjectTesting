import org.junit.Test;

public class HotelsDotComTest extends BaseHotelTest {

    @Test
    public void searchHotelPrices() throws InterruptedException {
        // Run all cities
        searchHotelPrices("Atlanta", "178232");
        searchHotelPrices("Denver", "178254");
        searchHotelPrices("Charlotte", "178247");
        searchHotelPrices("Austin", "178234");
        searchHotelPrices("Miami", "178286");
    }
} 