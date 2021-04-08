package com.udacity.jwdnd.course1.cloudstorage;

import org.h2.mvstore.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    @FindBy(id="logoutButton")
    private WebElement logoutButton;

    @FindBy(id= "nav-files")
    private WebElement filesTab;

    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="nav-credentials")
    private WebElement credentialsTab;

    private SignupPage signupPage;
    private LoginPage loginPage;

    public HomePage(WebDriver driver) {
        this.signupPage = new SignupPage(driver);
        this.loginPage = new LoginPage(driver);
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    public void signupAndLogin(){
        signupPage.signup();
        signupPage.navigateToLogin();
        loginPage.login();
    }

    public void navigateToFilesTab() {
        filesTab.click();
    }

    public void navigateToNotesTab() {
        notesTab.click();
    }

    public void navigateToCredentialsTab() {
        credentialsTab.click();
    }
}
