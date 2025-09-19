package com.linkedin;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class register {
    public static void main(String[] args) throws InterruptedException {

        // Setup ChromeDriver automatically
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();

        try {
            // Open Adactin Hotel App
            driver.get("https://adactinhotelapp.com/");

            // Wait and click "New User Register Here"
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement registerLink = wait.until(ExpectedConditions
                    .elementToBeClickable(By.linkText("New User Register Here")));
            registerLink.click();

            // Fill Registration Form
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));

            driver.findElement(By.id("username")).sendKeys("anuththara01"); // ≥ 8 chars
            driver.findElement(By.id("password")).sendKeys("anu2221");     // ≥ 6 chars
            driver.findElement(By.id("re_password")).sendKeys("anu2221");
            driver.findElement(By.id("full_name")).sendKeys("Anuththara Imanshi");
            driver.findElement(By.id("email_add")).sendKeys("ruumalii420@gmail.com");

            // ⚠️ Manual captcha entry required
            System.out.println("⚠️ Please enter captcha manually in browser (waiting 30s)...");
            Thread.sleep(30000);

            // Accept Terms & Conditions
            driver.findElement(By.id("tnc_box")).click();

            // Wait and click Submit button
            WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
            submitBtn.click();

            // Wait for result (success/error message)
            Thread.sleep(5000);
            System.out.println("✅ Registration submitted. Check browser for result.");

            // Take screenshot of success page
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileHandler.copy(screenshot, new File("registration_success.png"));
                System.out.println("📸 Screenshot saved as registration_success.png");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } finally {
            // Keep browser open for 10s, then quit
            Thread.sleep(10000);
            driver.quit();
        }
    }
}
