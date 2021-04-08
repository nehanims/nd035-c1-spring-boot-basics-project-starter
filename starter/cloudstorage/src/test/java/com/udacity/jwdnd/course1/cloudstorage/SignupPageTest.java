package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignupPageTest {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private static WebDriverWait webDriverWait;

    private SignupPage signupPage;
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.operadriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new OperaDriver();
        webDriverWait = new WebDriverWait(driver, 5);
        driver.get("http://localhost:" + this.port + "/signup");
        signupPage = new SignupPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    /*@Test
    public void getSignupPage() {
        assertEquals("Sign Up", driver.getTitle());
    }*/

    @Test
    @Order(1)
    public void testSignupValid(){
        signupPage.signup(SignupPage.FIRSTNAME, SignupPage.LAST_NAME, SignupPage.USERNAME, SignupPage.PASSWORD);
        webDriverWait.until(webDriver -> webDriver.findElement(By.id("successMessage")));
        assertTrue(signupPage.getSuccessMessage().isDisplayed());

        signupPage.signup(SignupPage.FIRSTNAME, SignupPage.LAST_NAME, SignupPage.USERNAME2, SignupPage.PASSWORD);
        webDriverWait.until(webDriver -> webDriver.findElement(By.id("successMessage")));
        assertTrue(signupPage.getSuccessMessage().isDisplayed());
    }

    @Test
    @Order(2)
    public void testSignupUserExists(){
        signupPage.signup();
        webDriverWait.until(webDriver -> webDriver.findElement(By.id("errorMessage")));
        assertTrue(signupPage.getErrorMessage().isDisplayed());
    }

    @Test
    @Order(3)//TODO move to LoginPageTest
    public void testHomePageAccessAfterLogin(){
        signupPage.navigateToLogin();
        webDriverWait.until(webDriver -> driver.findElement(By.id("loginButton")));
        assertEquals("Login", driver.getTitle());
        loginPage.login();
        webDriverWait.until(webDriver -> driver.findElement(By.id("credential-id")));
        assertEquals("Home", driver.getTitle());
    }

    @Test
    @Order(3)
    public void testLogout(){
        signupPage.navigateToLogin();
        webDriverWait.until(webDriver -> driver.findElement(By.id("loginButton")));
        assertEquals("Login", driver.getTitle());
        loginPage.login();
        webDriverWait.until(webDriver -> driver.findElement(By.id("credential-id")));
        assertEquals("Home", driver.getTitle());
        homePage.logout();
        assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(4)//TODO move test to homepage tests // TODO also add tests for get on other resources directly
    public void testHomePageAccessBeforeLogin() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/home");
        Thread.sleep(1000);
        assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(4)
    public void testLoginPageAccessAfterLogin() throws InterruptedException {
        signupPage.navigateToLogin();
        webDriverWait.until(webDriver -> driver.findElement(By.id("loginButton")));
        assertEquals("Login", driver.getTitle());
        loginPage.login();
        webDriverWait.until(webDriver -> driver.findElement(By.id("logoutButton")));
        assertEquals("Home", driver.getTitle());
    }

}
