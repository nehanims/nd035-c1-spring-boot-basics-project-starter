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
class SignupPageTest {

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

    @Test
    @Order(1)
    void testSignupValid(){
        signupPage.signup(SignupPage.FIRSTNAME, SignupPage.LAST_NAME, SignupPage.USERNAME, SignupPage.PASSWORD);
        webDriverWait.until(webDriver -> driver.findElement(By.id("loginButton")));
        assertTrue(loginPage.successMessageIsDsplayed());

        driver.get("http://localhost:" + this.port + "/signup");
        signupPage.signup(SignupPage.FIRSTNAME, SignupPage.LAST_NAME, SignupPage.USERNAME2, SignupPage.PASSWORD);
        webDriverWait.until(webDriver -> driver.findElement(By.id("loginButton")));
        assertTrue(loginPage.successMessageIsDsplayed());

    }

    @Test
    @Order(2)
    void testSignupUserExists(){
        signupPage.signup();
        webDriverWait.until(webDriver -> webDriver.findElement(By.id("errorMessage")));
        assertTrue(signupPage.getErrorMessage().isDisplayed());
    }

    @Test
    @Order(3)//TODO move to LoginPageTest
    void testHomePageAccessAfterLogin(){
        navigateToLogin();
        webDriverWait.until(webDriver -> driver.findElement(By.id("loginButton")));
        assertEquals("Login", driver.getTitle());
        loginPage.login();
        webDriverWait.until(webDriver -> driver.findElement(By.id("credential-id")));
        assertEquals("Home", driver.getTitle());
    }

    @Test
    @Order(3)
    void testLogout(){
        navigateToLogin();
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
    void testHomePageAccessBeforeLogin() {
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(webDriver -> driver.findElement(By.id("loginButton")));
        assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(4)
    void testLoginPageAccessAfterLogin() {
        navigateToLogin();
        webDriverWait.until(webDriver -> driver.findElement(By.id("loginButton")));
        assertEquals("Login", driver.getTitle());
        loginPage.login();
        webDriverWait.until(webDriver -> driver.findElement(By.id("logoutButton")));
        assertEquals("Home", driver.getTitle());
    }


    public void navigateToLogin() {
        driver.get("http://localhost:" + this.port + "/login");
    }


}
