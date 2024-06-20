package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

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

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password) {
		// Create a dummy account for logging in later.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10); // Increased wait time
		driver.get("http://localhost:8000/signup");
		System.out.println("Navigated to signup page");

		// Wait for the signup page to load
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		System.out.println("Signup page title verified");

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);
		System.out.println("First name entered");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);
		System.out.println("Last name entered");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);
		System.out.println("Username entered");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);
		System.out.println("Password entered");

		// Attempt to sign up
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();
		System.out.println("Sign up button clicked");

		// Wait for the login page and verify the success message
		webDriverWait.until(ExpectedConditions.urlContains("/login"));
		System.out.println("Navigated to login page");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		System.out.println("Success message is visible");

		String successMessage = driver.findElement(By.id("success-msg")).getText();
		System.out.println("Success message text: " + successMessage);
		Assertions.assertTrue(successMessage.contains("Sign up was successful!"));
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:8000/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","Red","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:8000/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:8000/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void testSignupAndLogin() {
		// Create a test account and verify doMockSignUp() is accessible
		doMockSignUp("Redirection","Test","RT","123");
		// Verify doLogin() is accessible
		doLogIn("RT", "123");

	}

	@Test
	public void testUnauthorizedUserAccess() {
		// Try to access the home page without logging in
		driver.get("http://localhost:8000/home");
		Assertions.assertEquals("Login", driver.getTitle());

		// Try to access a random made-up URL
		driver.get("http://localhost:8000/some-random-page");
		Assertions.assertEquals("Login", driver.getTitle());

		// Verify that the login page is accessible
		driver.get("http://localhost:8000/login");
		Assertions.assertEquals("Login", driver.getTitle());

		// Verify that the signup page is accessible
		driver.get("http://localhost:8000/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testSignUpLoginAccessHomeLogout() {
		// Sign up a new user
		doMockSignUp("FirstName", "LastName", "username", "password");

		// Log in with the newly created user
		doLogIn("username", "password");

		// Verify that the home page is accessible
		Assertions.assertEquals("Home", driver.getTitle());

		// Log out
		driver.findElement(By.id("logout-button")).click();

		// Verify that the user is redirected to the login page
		Assertions.assertEquals("Login", driver.getTitle());

		// Try to access the home page again
		driver.get("http://localhost:8000/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testCreateNote() {
		// Sign up and log in
		doMockSignUp("Note", "Create", "NC", "123");
		doLogIn("NC", "123");

		// Create a new note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-btn"))).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys("Test Note");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description"))).sendKeys("This is a test note.");

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteSubmit1"))).click();

		// Wait for the success message and click the link to go back to the home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Click on the notes tab to verify the note is displayed
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

		// Verify the note is displayed
		Boolean found = wait.until(ExpectedConditions.textToBe(
				By.xpath("//*[@id='noteTable']/tbody/tr/th"),
				"Test Note")
		);

		Assertions.assertTrue(found);
	}

	@Test
	public void testEditNote() {
		// Sign up and log in
		doMockSignUp("Note", "Edit", "NE", "123");
		doLogIn("NE", "123");

		// Create a new note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-btn"))).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys("Test Note");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description"))).sendKeys("This is a test note.");

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteSubmit1"))).click();

		// Wait for the success message and click the link to go back to the home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Click on the notes tab to verify the note is displayed
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

		// Edit the note
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Edit']"))).click();

		WebElement noteTitle = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		noteTitle.clear();
		noteTitle.sendKeys("Edited Note");

		WebElement noteDescription = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		noteDescription.clear();
		noteDescription.sendKeys("This is an edited test note.");

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteSubmit1"))).click();

		// Wait for the success message and click the link to go back to the home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Click on the notes tab to verify the changes are displayed
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

		// Verify the note is displayed
		Boolean foundTitle = webDriverWait.until(ExpectedConditions.textToBe(
				By.xpath("//*[@id='noteTable']/tbody/tr/th"),
				"Edited Note")
		);
		Boolean foundDescription = webDriverWait.until(ExpectedConditions.textToBe(
				By.xpath("//*[@id='noteTable']/tbody/tr/td[2]"),
				"This is an edited test note.")
		);

		Assertions.assertTrue(foundTitle);
		Assertions.assertTrue(foundDescription);
	}

	@Test
	public void testDeleteNote() {
		// Sign up and log in
		doMockSignUp("Note", "Delete", "ND", "123");
		doLogIn("ND", "123");

		// Create a new note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-btn"))).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys("Test Note");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description"))).sendKeys("This is a test note.");

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteSubmit1"))).click();

		// Wait for the success message and click the link to go back to the home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Click on the notes tab to verify the note is displayed
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

		// Delete the note
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn btn-danger' and contains(@href, 'note/delete')]"))).click();

		// Wait for the success message and click the link to go back to the home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Click on the notes tab to verify the note is no longer displayed
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		Assertions.assertFalse(driver.getPageSource().contains("Test Note"));
		Assertions.assertFalse(driver.getPageSource().contains("This is a test note."));
	}

	@Test
	public void testCreateCredentials() {
		// Sign up and log in
		doMockSignUp("Credentials", "Create", "CC", "123");
		doLogIn("CC", "123");

		// Create a new credential
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-btn"))).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).sendKeys("http://example.com");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username"))).sendKeys("testuser");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password"))).sendKeys("password");

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialSubmit1"))).click();

		// Wait for the success message and click the link to go back to the home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Click on the credentials tab to verify the credential is displayed
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

		// Verify the credential is displayed and the password is encrypted
		Boolean foundUrl = webDriverWait.until(ExpectedConditions.textToBe(
				By.xpath("//table[@id='credentialTable']/tbody/tr/th"),
				"http://example.com")
		);
		Boolean foundUsername = webDriverWait.until(ExpectedConditions.textToBe(
				By.xpath("//table[@id='credentialTable']/tbody/tr/td[2]"),
				"testuser")
		);
		// Check if password is encrypted
		WebElement passwordElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//table[@id='credentialTable']/tbody/tr/td[3]")));
		String displayedPassword = passwordElement.getText();
		Assertions.assertNotEquals("password", displayedPassword);
		Assertions.assertNotEquals("", displayedPassword);

		Assertions.assertTrue(foundUrl);
		Assertions.assertTrue(foundUsername);
	}

	@Test
	public void testViewEditCredentials() {
		// Sign up and log in
		doMockSignUp("Credentials", "Edit", "CE", "123");
		doLogIn("CE", "123");

		// Create a new credential
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-btn"))).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).sendKeys("http://example.com");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username"))).sendKeys("testuser");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password"))).sendKeys("password");
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialSubmit1"))).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// View existing credentials
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@id='credentialTable']/tbody/tr/td/button[text()='Edit']"))).click();

		// Verify the viewable password is unencrypted
		WebElement passwordElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		String viewablePassword = passwordElement.getAttribute("value");
		Assertions.assertEquals("password", viewablePassword);

		// Edit the credential
		WebElement urlElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		urlElement.clear();
		urlElement.sendKeys("http://newexample.com");

		WebElement usernameElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		usernameElement.clear();
		usernameElement.sendKeys("newuser");

		passwordElement.clear();
		passwordElement.sendKeys("newpassword");

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialSubmit1"))).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Verify the changes
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

		Boolean foundUrl = webDriverWait.until(ExpectedConditions.textToBe(
				By.xpath("//table[@id='credentialTable']/tbody/tr/th"),
				"http://newexample.com")
		);
		Boolean foundUsername = webDriverWait.until(ExpectedConditions.textToBe(
				By.xpath("//table[@id='credentialTable']/tbody/tr/td[2]"),
				"newuser")
		);
		passwordElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//table[@id='credentialTable']/tbody/tr/td[3]")));
		String displayedPassword = passwordElement.getText();
		Assertions.assertNotEquals("newpassword", displayedPassword);
		Assertions.assertNotEquals("", displayedPassword);

		Assertions.assertTrue(foundUrl);
		Assertions.assertTrue(foundUsername);
	}

	@Test
	public void testDeleteCredentials() {
		// Sign up and log in
		doMockSignUp("Credentials", "Delete", "CD", "123");
		doLogIn("CD", "123");

		// Create a new credential
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-btn"))).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).sendKeys("http://example.com");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username"))).sendKeys("testuser");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password"))).sendKeys("password");
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialSubmit1"))).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Verify the credential is created
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		Boolean foundUrl = webDriverWait.until(ExpectedConditions.textToBe(
				By.xpath("//table[@id='credentialTable']/tbody/tr/th"),
				"http://example.com")
		);
		Boolean foundUsername = webDriverWait.until(ExpectedConditions.textToBe(
				By.xpath("//table[@id='credentialTable']/tbody/tr/td[2]"),
				"testuser")
		);
		Assertions.assertTrue(foundUrl);
		Assertions.assertTrue(foundUsername);

		// Delete the credential
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn btn-danger' and contains(@href, 'credential/delete')]"))).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		driver.findElement(By.id("success-msg")).findElement(By.tagName("a")).click();

		// Verify the credential is deleted
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		List<WebElement> credentialRows = driver.findElements(By.xpath("//table[@id='credentialTable']/tbody/tr"));
		Assertions.assertTrue(credentialRows.isEmpty());
	}


}
