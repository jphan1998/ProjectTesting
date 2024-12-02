import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotelsDotComTest {

    private static WebDriver driver;

    @BeforeClass
    public static void setUp() throws Exception {
        /*EdgeOptions options = new EdgeOptions();
        options.addArguments("--user-data-dir=/Users/tim/Library/Application Support/Microsoft Edge/Default");
        driver = new EdgeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));*/
        driver = new ChromeDriver();
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

    @Test
    public void testHotelPrice2() throws InterruptedException {
        driver.get("https://www.hotels.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(2000);
        driver.manage().window().maximize();

        // Enter the city
        WebElement cityInput = driver.findElement(By.xpath("//*[@aria-label='Where to?']"));
        wait.until(ExpectedConditions.visibilityOf(cityInput));
        cityInput.click();
        wait.until(ExpectedConditions.visibilityOf(cityInput));
        WebElement destinationInput = driver.findElement(By.xpath("//*[@id='destination_form_field']"));
        destinationInput.sendKeys("Los Angeles");
        wait.until(ExpectedConditions.visibilityOf(cityInput));
        destinationInput.sendKeys(Keys.RETURN);
        //cityInput.submit();

        WebElement calender = driver.findElement(By.xpath("//button[@data-testid='uitk-date-selector-input1-default']"));
        calender.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='uitk-cal-controls-button uitk-cal-controls-button-inset-multi uitk-cal-controls-button-next']"))).click();

        WebElement flexible = driver.findElement(By.xpath("//a[@href='#EGDSBasicTabSearchForm_date_form_nested_flexible_tab_dates']"));
        flexible.click();

        List<WebElement> months = driver.findElements(By.xpath("//label[@class='button-toggle-custom-content-container']"));

        for (WebElement month : months) {
            month.click();
        }
        driver.findElement(By.xpath("//button[@id='search_button']")).click();

        // Wait for results to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@aria-label='Search by property name']")));

        // Search Hotels
        WebElement searchHotels = driver.findElement(By.xpath("//*[@aria-label='Search by property name']"));
        searchHotels.click();
        WebElement hotelInput = driver.findElement(By.xpath("//input[@class='uitk-field-input uitk-typeahead-input uitk-typeahead-input-v2']"));
        hotelInput.sendKeys("Four Seasons");
        hotelInput.sendKeys(Keys.RETURN);

        //Filtering the results
        Select sort = new Select(driver.findElement(By.xpath("//select[@id='sort-filter-dropdown-sort']")));
        sort.selectByVisibleText("Price: low to high");

        String price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='uitk-text uitk-type-end uitk-type-300 uitk-text-default-theme']"))).getText();
        System.out.println(price);

        String date = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='uitk-text uitk-type-end uitk-type-300 uitk-type-bold uitk-text-default-theme']"))).getText();
        System.out.println(date);
    }
}
