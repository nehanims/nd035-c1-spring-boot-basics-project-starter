package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver webDriver;

    @FindBy(id ="inputUsername")
    private WebElement usernameField;

    @FindBy(id ="inputPassword")
    private WebElement passwordField;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    public LoginPage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void login(){
        login(SignupPage.USERNAME, SignupPage.PASSWORD);
    }

    public void login(String username, String password) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        usernameField.clear();
        usernameField.click();
        usernameField.sendKeys(username);

        passwordField.clear();
        passwordField.click();
        passwordField.sendKeys(password);

        loginButton.click();
    }


}
