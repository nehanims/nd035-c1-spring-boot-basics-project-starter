package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class CredentialsTab {

    public static final String URL1 = "http://example.com";
    public static final String URL1_EDITED = "http://www.example.com";
    public static final String URL2 = "http://www.another_example.com";
    public static final String USERNAME1 = "example_user1";
    public static final String USERNAME2 = "example_user2";
    public static final String USERNAME2_EDITED = "example_user2_edited";

    public static final String PLAIN_TEXT_PASSWORD1 = "password1";
    public static final String PLAIN_TEXT_PASSWORD1_EDITED = "password123";
    public static final String PLAIN_TEXT_PASSWORD2 = "password2";


    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id="addCredentialButton")//TODO rename this in the template - remember to manually check all usage
    private WebElement addCredentialButton;

    @FindBy(id="saveCredentialButton")
    private WebElement saveCredentialButton;

    @FindBy(id="credential-url")
    private WebElement urlField;

    @FindBy(id="credential-username")
    private WebElement usernameField;

    @FindBy(id="credential-password")
    private WebElement passwordfield;

    public CredentialsTab(WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = wait;
    }

    public void addCredentials(String url, String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
        addCredentialButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        saveCredentialForm(url, username, password);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("credentialModal")));
    }

    public List<Credential> getCredentials() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        return driver.findElements(By.cssSelector("#credentialTable > tbody > tr"))
                .stream()
                .map(Credential::new)
                .collect(Collectors.toList());
    }


    private void setTextToStringField(WebElement field, String text){
        field.click();
        field.clear();
        field.sendKeys(text);
    }

    public void deleteCredentials(Credential displayedCredentials) {
        displayedCredentials.getDeleteButton().click();
    }

    public void openEditor(Credential credentialRow){
        credentialRow.getEditButton().click();
    }

    public CredentialForm getCredentialsDisplayedInModalForm() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        urlField.click();
        return CredentialForm.builder()
                .url(urlField.getAttribute("value"))
                .username(usernameField.getAttribute("value"))
                .plainTextPassword(passwordfield.getAttribute("value"))
                .build();
    }

    public void saveCredentialForm(String url, String username, String password) {
        setTextToStringField(urlField, url);
        setTextToStringField(usernameField, username);
        setTextToStringField(passwordfield, password);
        saveCredentialButton.click();
    }
}
