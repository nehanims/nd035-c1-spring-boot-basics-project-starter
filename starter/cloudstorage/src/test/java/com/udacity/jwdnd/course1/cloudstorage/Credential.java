package com.udacity.jwdnd.course1.cloudstorage;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Data
public class Credential {
    private Integer credentialId;
    private String url;
    private String username;
    private String encryptedPassword;

    private WebElement deleteButton;
    private WebElement editButton;

    public Credential(WebElement credentialElement) {
        editButton = credentialElement.findElement(By.cssSelector("td > button"));
        deleteButton = credentialElement.findElement(By.cssSelector("td > a"));
        credentialId = getCredentialId(deleteButton);
        url = credentialElement.findElement(By.tagName("th")).getText();
        username = credentialElement.findElements(By.tagName("td")).get(1).getText();
        encryptedPassword = credentialElement.findElements(By.tagName("td")).get(2).getText();//TODO also need encryption/decryption for the password, or should i just manually decrypt it and hard code it here?
    }

    private Integer getCredentialId(WebElement deleteButton) {
        String href = deleteButton.getAttribute("href");
        return Integer.parseInt(href.substring(href.lastIndexOf('/')+1));
    }
}
