package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CredentialsTest {
    @LocalServerPort
    private int port;

    @Autowired
    private CredentialsService credentialsService;
    @Autowired
    private EncryptionService encryptionService;

    private static WebDriver driver;
    private static WebDriverWait webDriverWait;

    private static HomePage homePage;
    private CredentialsTab credentialsTab;


    @BeforeAll
    static void beforeAll() {
        WebDriverManager.operadriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new OperaDriver();
        webDriverWait = new WebDriverWait(driver, 5);

        homePage = new HomePage(driver);
        credentialsTab = new CredentialsTab(driver, webDriverWait);

        driver.get("http://localhost:" + port + "/signup");
        homePage.signupAndLogin();//TODO signup everytime seems like bad practice but BeforeAll method is static for some reason
        homePage.navigateToCredentialsTab();

    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void testAddCredentials(){

        // add 2 sets of credentials and verify that both are displayed
        credentialsTab.addCredentials(CredentialsTab.URL1, CredentialsTab.USERNAME1, CredentialsTab.PLAIN_TEXT_PASSWORD1);
        List<Credential> credentials = credentialsTab.getCredentials();
        assertEquals(1 , credentials.size());

        credentialsTab.addCredentials(CredentialsTab.URL2, CredentialsTab.USERNAME2, CredentialsTab.PLAIN_TEXT_PASSWORD2);
        credentials = credentialsTab.getCredentials();
        assertEquals(2 , credentials.size());

        //Verify that the first saved record is displayed
        // (ensure that the password is shown encrypted, not the original plain text)
        Credential displayedCredentials1 = credentials.get(0);
        Credentials savedCredentials1 = credentialsService.getCredentialByCredentialId
                (displayedCredentials1.getCredentialId());

        assertEquals(CredentialsTab.URL1, displayedCredentials1.getUrl());
        assertEquals(CredentialsTab.USERNAME1, displayedCredentials1.getUsername());
        assertEquals(encryptionService.encryptValue(CredentialsTab.PLAIN_TEXT_PASSWORD1, savedCredentials1.getKey()),
                displayedCredentials1.getEncryptedPassword());

        //Verify that the second saved record is displayed
        // (ensure that the password is shown encrypted, not the original plain text)
        Credential displayedCredentials2 = credentials.get(1);
        Credentials savedCredentials2 = credentialsService.getCredentialByCredentialId
                (displayedCredentials2.getCredentialId());

        assertEquals(CredentialsTab.URL2, displayedCredentials2.getUrl());
        assertEquals(CredentialsTab.USERNAME2, displayedCredentials2.getUsername());
        assertEquals(encryptionService.encryptValue(CredentialsTab.PLAIN_TEXT_PASSWORD2, savedCredentials2.getKey()),
                displayedCredentials2.getEncryptedPassword());

    }

    @Test
    @Order(2)
    void testDeleteCredentials(){


        //verify that 2 records are available and verify the content of the records
        List<Credential> credentials = credentialsTab.getCredentials();
        assertEquals(2 , credentials.size());

        Credential displayedCredentials1 = credentials.get(0);
        assertEquals(CredentialsTab.URL1, displayedCredentials1.getUrl());
        assertEquals(CredentialsTab.USERNAME1, displayedCredentials1.getUsername());

        Credential displayedCredentials2 = credentials.get(1);
        assertEquals(CredentialsTab.URL2, displayedCredentials2.getUrl());
        assertEquals(CredentialsTab.USERNAME2, displayedCredentials2.getUsername());

        //delete 1st record and verify that 1 record is available and the correct record was deleted
        // (by checking that the remaining record is the one that was not deleted)
        credentialsTab.deleteCredentials(displayedCredentials1);

        credentials = credentialsTab.getCredentials();
        assertEquals(1 , credentials.size());

        displayedCredentials2 = credentials.get(0);
        assertEquals(CredentialsTab.URL2, displayedCredentials2.getUrl());
        assertEquals(CredentialsTab.USERNAME2, displayedCredentials2.getUsername());

        //delete the remaining record and verify that no record is available
        credentialsTab.deleteCredentials(displayedCredentials2);

        credentials = credentialsTab.getCredentials();
        assertEquals(0 , credentials.size());

    }

    @Test
    @Order(3)
    void testEditCredentials(){
        //add 2 credential records
        credentialsTab.addCredentials(CredentialsTab.URL1, CredentialsTab.USERNAME1, CredentialsTab.PLAIN_TEXT_PASSWORD1);
        credentialsTab.addCredentials(CredentialsTab.URL2, CredentialsTab.USERNAME2, CredentialsTab.PLAIN_TEXT_PASSWORD2);
        List<Credential> credentials = credentialsTab.getCredentials();
        assertEquals(2, credentials.size());

        //edit the first record
        // (verify that the password is shown unencrypted in the modal window, but is encrypted in the table before and after saving changes)
        Credential displayedCredentialRowData1 = credentials.get(0);
        Credentials savedCredentials1 = credentialsService.getCredentialByCredentialId
                (displayedCredentialRowData1.getCredentialId());

        assertEquals(CredentialsTab.URL1, displayedCredentialRowData1.getUrl());
        assertEquals(CredentialsTab.USERNAME1, displayedCredentialRowData1.getUsername());
        assertEquals(encryptionService.encryptValue(CredentialsTab.PLAIN_TEXT_PASSWORD1, savedCredentials1.getKey()),
                displayedCredentialRowData1.getEncryptedPassword());

        //open the editor and verify that the form data matches the data that was entered in
        //(verify that the password displayed is in plain text)
        credentialsTab.openEditor(displayedCredentialRowData1);
        CredentialForm displayedCredentialForm1 = credentialsTab.getCredentialsDisplayedInModalForm();

        assertEquals(CredentialsTab.URL1, displayedCredentialForm1.getUrl());
        assertEquals(CredentialsTab.USERNAME1, displayedCredentialForm1.getUsername());
        assertEquals(CredentialsTab.PLAIN_TEXT_PASSWORD1, displayedCredentialForm1.getPlainTextPassword());


        //make changes and save the credential form data
        credentialsTab.saveCredentialForm(CredentialsTab.URL1_EDITED, CredentialsTab.USERNAME1, CredentialsTab.PLAIN_TEXT_PASSWORD1_EDITED);


        //verify that the changes have been successfully made and the updated encrypted password is displayed in the table
        credentials = credentialsTab.getCredentials();

        displayedCredentialRowData1 = credentials.get(0);
        savedCredentials1 = credentialsService.getCredentialByCredentialId
                (displayedCredentialRowData1.getCredentialId());
        assertEquals(CredentialsTab.URL1_EDITED, displayedCredentialRowData1.getUrl());
        assertEquals(CredentialsTab.USERNAME1, displayedCredentialRowData1.getUsername());
        assertEquals(encryptionService.encryptValue(CredentialsTab.PLAIN_TEXT_PASSWORD1_EDITED, savedCredentials1.getKey()),
                displayedCredentialRowData1.getEncryptedPassword());

    }

}
