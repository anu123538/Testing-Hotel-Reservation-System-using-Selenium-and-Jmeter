package com.linkedin;

import java.time.Duration;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;

public class login {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 1: Login
            driver.get("https://adactinhotelapp.com/");
            driver.manage().window().maximize();

            driver.findElement(By.name("username")).sendKeys("anuththaraima");
            driver.findElement(By.name("password")).sendKeys("IMVDY6");
            driver.findElement(By.id("login")).click();

            // Step 2: Fill Search Form
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("location")));
            new Select(driver.findElement(By.id("location"))).selectByVisibleText("Sydney");
            new Select(driver.findElement(By.id("hotels"))).selectByVisibleText("Hotel Creek");
            new Select(driver.findElement(By.id("room_type"))).selectByVisibleText("Standard");
            new Select(driver.findElement(By.id("room_nos"))).selectByVisibleText("2 - Two");

            driver.findElement(By.id("datepick_in")).clear();
            driver.findElement(By.id("datepick_in")).sendKeys("15/09/2025");

            driver.findElement(By.id("datepick_out")).clear();
            driver.findElement(By.id("datepick_out")).sendKeys("19/09/2025");

            new Select(driver.findElement(By.id("adult_room"))).selectByVisibleText("1 - One");
            new Select(driver.findElement(By.id("child_room"))).selectByVisibleText("0 - None");

            driver.findElement(By.id("Submit")).click();

            // Step 3: Select Hotel and Continue
            wait.until(ExpectedConditions.elementToBeClickable(By.name("radiobutton_0"))).click();
            driver.findElement(By.id("continue")).click();

            // Step 4: Fill Booking Details
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first_name"))).sendKeys("vindya");
            driver.findElement(By.id("last_name")).sendKeys("nmadumali");
            driver.findElement(By.id("address")).sendKeys("123 Main Street, Sydney");
            driver.findElement(By.id("cc_num")).sendKeys("1111111111111111");

            new Select(driver.findElement(By.id("cc_type"))).selectByVisibleText("VISA");
            new Select(driver.findElement(By.id("cc_exp_month"))).selectByVisibleText("December");
            new Select(driver.findElement(By.id("cc_exp_year"))).selectByVisibleText("2025");
            driver.findElement(By.id("cc_cvv")).sendKeys("123");

            driver.findElement(By.id("book_now")).click();

            // Step 5: Wait for Order Number (booking confirmation)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order_no")));

            // Step 6: Go to Booked Itinerary (Inventory)
            driver.findElement(By.linkText("Booked Itinerary")).click();

            // Step 7: Check if any bookings exist
            List<WebElement> bookings = driver.findElements(By.name("ids[]"));
            if (bookings.isEmpty()) {
                System.out.println("❌ No bookings found to cancel.");
                return;
            }

            // Wait until the checkbox is visible
            wait.until(ExpectedConditions.visibilityOf(bookings.get(0)));

            // Grab Order ID for the first booking
            String orderId = driver.findElement(By.xpath("//input[@name='ids[]']/parent::td/following-sibling::td[1]")).getText();
            System.out.println("⚙ Cancelling booking with Order ID: " + orderId);

            // Select checkbox and cancel booking
            bookings.get(0).click();
            driver.findElement(By.name("cancelall")).click();

            // Accept confirmation alert
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();

            // Wait until booking is removed from list
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[text()='" + orderId + "']")));

            System.out.println("✅ Booking with Order ID " + orderId + " cancelled successfully.");

            // Pause before taking screenshot
            Thread.sleep(1000);

            // Take screenshot after cancellation
            try {
                File cancelShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String filePath = System.getProperty("user.dir") + "/booking_cancelled.png";
                FileUtils.copyFile(cancelShot, new File(filePath));
                System.out.println("📸 Screenshot saved at: " + filePath);
            } catch (IOException e) {
                System.out.println("⚠ Failed to save screenshot: " + e.getMessage());
            }

            // Pause to see result before browser closes
            Thread.sleep(5000);

        } catch (Exception e) {
            System.out.println("❗ Exception occurred: " + e.getMessage());
            e.printStackTrace();

            // Optional: Take screenshot on failure
            try {
                File errorShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String filePath = System.getProperty("user.dir") + "/error_screenshot.png";
                FileUtils.copyFile(errorShot, new File(filePath));
                System.out.println("⚠ Error screenshot saved at: " + filePath);
            } catch (IOException io) {
                System.out.println("⚠ Failed to save error screenshot: " + io.getMessage());
            }
        } finally {
            driver.quit();
        }
    }
}