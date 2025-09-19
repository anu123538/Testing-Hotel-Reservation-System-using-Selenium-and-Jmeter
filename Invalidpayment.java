package com.linkedin;

import java.time.Duration;
import java.io.File;
import java.io.IOException;

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

public class Invalidpayment {
    public static void main(String[] args) throws IOException, InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Step 1: Login
            driver.get("https://adactinhotelapp.com/");
            driver.manage().window().maximize();

            driver.findElement(By.name("username")).sendKeys("anuththaraima");
            driver.findElement(By.name("password")).sendKeys("IMVDY6");
            driver.findElement(By.id("login")).click();

            // Step 2: Wait for Search Hotel page and fill search form with valid data
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("location")));

            new Select(driver.findElement(By.id("location"))).selectByVisibleText("Sydney");
            new Select(driver.findElement(By.id("hotels"))).selectByVisibleText("Hotel Creek");
            new Select(driver.findElement(By.id("room_type"))).selectByVisibleText("Standard");
            new Select(driver.findElement(By.id("room_nos"))).selectByVisibleText("2 - Two");

            driver.findElement(By.id("datepick_in")).clear();
            driver.findElement(By.id("datepick_in")).sendKeys("20/09/2025");

            driver.findElement(By.id("datepick_out")).clear();
            driver.findElement(By.id("datepick_out")).sendKeys("25/09/2025");

            new Select(driver.findElement(By.id("adult_room"))).selectByVisibleText("1 - One");
            new Select(driver.findElement(By.id("child_room"))).selectByVisibleText("0 - None");

            driver.findElement(By.id("Submit")).click();

            // Step 3: Select hotel and continue
            wait.until(ExpectedConditions.elementToBeClickable(By.name("radiobutton_0"))).click();
            driver.findElement(By.id("continue")).click();

            // Step 4: Fill booking details with EMPTY CVV
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first_name"))).sendKeys("Raween");
            driver.findElement(By.id("last_name")).sendKeys("Kanishka");
            driver.findElement(By.id("address")).sendKeys("123 Main Street, Sydney");
            driver.findElement(By.id("cc_num")).sendKeys("4111111111111111");

            new Select(driver.findElement(By.id("cc_type"))).selectByVisibleText("VISA");
            new Select(driver.findElement(By.id("cc_exp_month"))).selectByVisibleText("December");
            new Select(driver.findElement(By.id("cc_exp_year"))).selectByVisibleText("2025");

            driver.findElement(By.id("cc_cvv")).clear();  // **Empty CVV field**

            // Click Book Now
            driver.findElement(By.id("book_now")).click();

            // Step 5: Wait and capture the CVV error message
            // The error message usually appears inside span with id "cc_cvv_span"
            WebElement cvvErrorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cc_cvv_span")));

            System.out.println("CVV Error Message: " + cvvErrorMsg.getText());

            // Step 6: Take screenshot for evidence
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File("empty_cvv_error.png"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
