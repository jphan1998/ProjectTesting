import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BaseHotelTest {
    protected static WebDriver driver;
    protected static WebDriverWait wait;

    protected static final String[] HOTELS = {
            "Hampton Inn",
            "Holiday Inn Express",
            "Marriott Hotels & Resorts",
            "Hyatt Place",
            "Doubletree"
    };

    @BeforeClass
    public static void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        HotelPriceDatabase.initializeDatabase();
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void searchHotelPrices(String city, String regionId) throws InterruptedException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2025, 5, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (startDate.isBefore(endDate)) {
            LocalDate checkoutDate = startDate.plusDays(1);

            String baseUrl = "https://www.hotels.com/Hotel-Search";
            String params = String.format("?destination=%s%%20(and%%20vicinity)" +
                    "&sort=REVIEW_RELEVANT" +
                    "&adults=2&rooms=1&regionId=%s" +
                    "&startDate=%s&endDate=%s",
                    city.replace(" ", "%20"),
                    regionId,
                    startDate.format(formatter),
                    checkoutDate.format(formatter));

            driver.get(baseUrl + params);
            Thread.sleep(3000);

            try {
                List<WebElement> hotelCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector("div[data-stid='lodging-card-responsive']")));

                System.out.println("\nDate: " + startDate.format(formatter));
                System.out.println("Found " + hotelCards.size() + " hotels in " + city);
                System.out.println("Scanning for target brands...\n");

                int savedCount = 0;
                for (WebElement card : hotelCards) {
                    try {
                        String hotelName = card.findElement(By.cssSelector("h3.uitk-heading")).getText();
                        WebElement priceElement = card.findElement(
                                By.cssSelector("div[data-test-id='price-summary'] .uitk-type-500"));
                        String price = priceElement.getText();

                        boolean isTargetHotel = false;
                        for (String brand : HOTELS) {
                            if (hotelName.contains(brand)) {
                                isTargetHotel = true;
                                break;
                            }
                        }

                        if (isTargetHotel) {
                            price = price.replaceAll("[^0-9]", "");
                            int priceValue = Integer.parseInt(price);
                            System.out.println(hotelName + ": $" + price);
                            HotelPriceDatabase.savePrice(city, hotelName, priceValue, startDate);
                            savedCount++;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }

                // Verify database entries for this date
                System.out.println("\nSaved " + savedCount + " hotels for " + startDate.format(formatter));
                HotelPriceDatabase.verifyPrices(city, startDate);

                startDate = startDate.plusDays(1);
                Thread.sleep(5000);

            } catch (Exception e) {
                System.out.println("Error searching for hotels in " + city + " on " + startDate);
                e.printStackTrace();
                startDate = startDate.plusDays(1);
            }
        }
    }
}