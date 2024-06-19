package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CloudStorageApplicationTests {

	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeAll
	static void beforeAll() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\ibrah\\Downloads\\chromedriver-win64\\chromedriver.exe");
	}

	@BeforeEach
	public void beforeEach() {
		ChromeOptions options = new ChromeOptions();
		//options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1080");
		this.driver = new ChromeDriver(options);
		this.wait = new WebDriverWait(driver, 10);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:8000/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void doMockSignUp(String firstName, String lastName, String userName, String password) {
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5); // Increased wait time
		driver.get("http://localhost:8000/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		// Wait for the login page and verify the success message
		webDriverWait.until(ExpectedConditions.urlContains("/login"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up! Please log in."));
	}






	private void doLogIn(String userName, String password) {
		driver.get("http://localhost:8000/login");
		System.out.println("Navigated to login page");

		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);
		System.out.println("Username entered");

		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);
		System.out.println("Password entered");

		WebElement loginButton = driver.findElement(By.id("submit-button"));
		loginButton.click();
		System.out.println("Login button clicked");

		try {
			wait.until(ExpectedConditions.titleContains("Home"));
			System.out.println("Successfully logged in and redirected to home page");
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Failed to log in or redirect to home page");
			takeScreenshot("login-error.png");
			throw e;
		}
	}

	private void takeScreenshot(String fileName) {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSignUpAndLogin() {
		doMockSignUp("Test", "User", "testuser", "password");
		System.out.println("Sign-up completed");
		doLogIn("testuser", "password");
		System.out.println("Login completed");
	}




	@Test
	public void testBadUrl() {
		doMockSignUp("URL", "Test", "UT", "123");
		doLogIn("UT", "123");
		driver.get("http://localhost:8000/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}

	@Test
	public void testLargeUpload() {
		doMockSignUp("Large File", "Test", "LFT", "123");
		doLogIn("LFT", "123");

		String fileName = "upload5m.zip";
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();

		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}

		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}

	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:8000/login", driver.getCurrentUrl());
	}
}
