package com.linkedin;

import java.time.Duration;
import java.io.File;


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

public class Invaliddate {
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

            // Step 2: Wait for Search Hotel page
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("location")));

            // Step 3: Fill Search Form with invalid dates (check-in after check-out)
            new Select(driver.findElement(By.id("location"))).selectByVisibleText("Sydney");
            new Select(driver.findElement(By.id("hotels"))).selectByVisibleText("Hotel Creek");
            new Select(driver.findElement(By.id("room_type"))).selectByVisibleText("Standard");
            new Select(driver.findElement(By.id("room_nos"))).selectByVisibleText("2 - Two");

            driver.findElement(By.id("datepick_in")).clear();
            driver.findElement(By.id("datepick_in")).sendKeys("20/09/2025");  // Invalid: after check-out date

            driver.findElement(By.id("datepick_out")).clear();
            driver.findElement(By.id("datepick_out")).sendKeys("15/09/2025"); // Check-out before check-in

            new Select(driver.findElement(By.id("adult_room"))).selectByVisibleText("1 - One");
            new Select(driver.findElement(By.id("child_room"))).selectByVisibleText("0 - None");

            driver.findElement(By.id("Submit")).click();

            // Step 4: Capture error message
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@id='checkin_span' or @id='checkout_span']")));

            System.out.println("Error Message Displayed: " + errorMsg.getText());

            // Optional: Take screenshot on failure
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File("invalid_date_error.png"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
