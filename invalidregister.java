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
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;

public class invalidregister {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 1: Open the login page
            driver.get("https://adactinhotelapp.com/");
            driver.manage().window().maximize();

            // Step 2: Enter invalid login credentials
            driver.findElement(By.name("username")).sendKeys("vindya");
            driver.findElement(By.name("password")).sendKeys("1235");
            driver.findElement(By.id("login")).click();

            // Step 3: Wait for error message
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//b[contains(text(),'Invalid Login details or Your Password might have expired.')]")));

            // Step 4: Check if error message is displayed
            if (errorMessage.isDisplayed()) {
                System.out.println("❌ Test Passed: Invalid login error message displayed.");
            } else {
                System.out.println("⚠️ Test Failed: No error message displayed.");
            }

            // Step 5: Take a screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File("invalid_login.png");
            FileUtils.copyFile(srcFile, destFile);
            System.out.println("📸 Screenshot taken: invalid_login.png");

        } catch (Exception e) {
            System.out.println("❌ Test Failed with exception: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
