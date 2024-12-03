import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HotelsDotComTest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    // Update target hotels to match the most common brands
    private static final String[] HOTELS = {
            "Hampton Inn",
            "Holiday Inn Express",
            "Marriott Hotels & Resorts",
            "Hyatt Place",
            "Doubletree"
    };

    private static final String[] CITIES = {
            "Atlanta",
            "Las Vegas",
            "New York City",
            "Miami",
            "Paris",
            "Los Angeles"
    };

    @BeforeClass
    public static void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void searchHotelPrices() throws InterruptedException {
        // Start with Atlanta
        searchHotelInCity("Atlanta", null);
    }

    private void searchHotelInCity(String city, String hotel) throws InterruptedException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2025, 5, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (startDate.isBefore(endDate)) {
            LocalDate checkoutDate = startDate.plusDays(1);

            // Build URL with parameters for dates and sort order
            String baseUrl = "https://www.hotels.com/Hotel-Search";
            String params = String.format("?destination=%s%%20(and%%20vicinity)" +
                    "&sort=REVIEW_RELEVANT" +
                    "&adults=2&rooms=1&regionId=178232" +
                    "&startDate=%s&endDate=%s",
                    city.replace(" ", "%20"),
                    startDate.format(formatter),
                    checkoutDate.format(formatter));

            driver.get(baseUrl + params);
            Thread.sleep(3000); // Wait for results to load

            try {
                // Get all hotel cards
                List<WebElement> hotelCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector("div[data-stid='lodging-card-responsive']")));

                System.out.println("\nDate: " + startDate.format(formatter));
                System.out.println("Found " + hotelCards.size() + " hotels in " + city);
                System.out.println("Scanning for target brands...\n");

                // Process each listing
                for (WebElement card : hotelCards) {
                    try {
                        String hotelName = card.findElement(By.cssSelector("h3.uitk-heading")).getText();
                        WebElement priceElement = card.findElement(
                                By.cssSelector("div[data-test-id='price-summary'] .uitk-type-500"));
                        String price = priceElement.getText();

                        // Check if hotel name contains any of our target brands
                        boolean isTargetHotel = false;
                        for (String brand : HOTELS) {
                            if (hotelName.contains(brand)) {
                                isTargetHotel = true;
                                break;
                            }
                        }

                        if (isTargetHotel) {
                            price = price.replaceAll("[^0-9]", "");
                            System.out.println(hotelName + ": $" + price);

                            // TODO: Store this data point
                            // savePrice(city, hotelName, price, startDate);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }

                // Move to next date
                startDate = startDate.plusDays(1);

                // Add a delay between searches to avoid rate limiting
                Thread.sleep(5000);

            } catch (Exception e) {
                System.out.println("Error searching for hotels in " + city + " on " + startDate);
                e.printStackTrace();

                // If we hit an error, still advance the date
                startDate = startDate.plusDays(1);
            }
        }
    }
}
