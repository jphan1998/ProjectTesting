import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotelsDotComTest {

    private static WebDriver driver;

    @BeforeClass
    public static void setUp() throws Exception {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--user-data-dir=/Users/tim/Library/Application Support/Microsoft Edge/Default");
        driver = new EdgeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testHotelPrice() throws Exception {
        //Hotel-Search?adults=2&d1=2024-11-08&d2=2024-11-11&destination=Hawaii&endDate=2024-11-11&startDate=2024-11-08&hotelName=hyatt%20regency
        String adults = "adults=2&";
        String d1 = "d1=2024-11-08&";
        String d2 = "d2=2024-11-09&";
        String destination = "destination=Hawaii&";
        String startDate = "startDate=2024-11-08&";
        String endDate = "endDate=2024-11-09&";
        String hotelName = "hotelName=Hyatt%20Regency";

        String urlParams = "Hotel-Search?" + adults + d1 + d2 + destination + startDate + endDate + hotelName;
        driver.get("https://www.hotels.com/" + urlParams);

        List<WebElement> cardLinks = driver.findElements(By.className("uitk-card-link"));
        //System.out.println(cardLinks.size());
        cardLinks.get(0).click();

        String text = driver.getPageSource();

        String pattern = "(\\d+) total";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        m.find(0);
        String price = m.group(0);
        System.out.println(price);

    }
}
