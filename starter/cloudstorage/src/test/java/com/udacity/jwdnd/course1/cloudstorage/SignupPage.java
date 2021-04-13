package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    public static final String FIRSTNAME = "Jane";
    public static final String LAST_NAME = "Doe";
    public static final String USERNAME = "janedoe";
    public static final String PASSWORD = "password";
    public static final String USERNAME2 = "johndoe";
    private final WebDriver driver;

    @FindBy(id ="loginLink")
    private WebElement loginLink;

    @FindBy(id ="errorMessage")
    private WebElement errorMessage;

    @FindBy(id ="inputFirstName")
    private WebElement firstNameField;

    @FindBy(id ="inputLastName")
    private WebElement lastNameField;

    @FindBy(id ="inputUsername")
    private WebElement usernameField;

    @FindBy(id ="inputPassword")
    private WebElement passwordField;

    @FindBy(id = "signupButton")
    private WebElement signupButton;

    public SignupPage(WebDriver webDriver){
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void signup(){
        signup(FIRSTNAME, LAST_NAME, USERNAME, PASSWORD);
    }

    public void signup(String firstname, String lastName, String username, String password){

        firstNameField.clear();
        firstNameField.sendKeys(firstname);

        lastNameField.clear();
        lastNameField.sendKeys(lastName);

        usernameField.clear();
        usernameField.sendKeys(username);

        passwordField.clear();
        passwordField.sendKeys(password);

        signupButton.click();
    }

    public WebElement getErrorMessage(){
        return errorMessage;
    }


}
