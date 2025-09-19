package com.linkedin;

import java.time.Duration;
import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Alert;
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

public class invlidacceptcancle {
    public static void main(String[] args) throws IOException {
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
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first_name"))).sendKeys("raween");
            driver.findElement(By.id("last_name")).sendKeys("kanishka");
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

            // Step 7: Wait for Order List
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ids[]")));

            // Intentionally do NOT select any booking checkbox to trigger alert on cancel

            // Click the "Cancel Selected" button without selecting any booking
            driver.findElement(By.name("cancelall")).click();

            // Handle alert on no selection
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert message when no booking selected: " + alert.getText());

            alert.accept();  // Accept alert first

            // Now take the screenshot (alert is gone)
            TakesScreenshot ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            File destFile = new File("CancelWithoutSelection_Alert.png");
            FileUtils.copyFile(srcFile, destFile);
            System.out.println("Screenshot saved at: " + destFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close browser
            driver.quit();
        }
    }
}
