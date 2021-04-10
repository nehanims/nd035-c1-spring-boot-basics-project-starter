package com.udacity.jwdnd.course1.cloudstorage;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Data
public class Note{
    // TODO Can you add @FindBy to these fields and see if you could pass the driver and use PageFactory on the fly to initialize this note object using tr note element
    private String title;
    private String description;
    private WebElement deleteButton;
    private WebElement editButton;

    public Note(WebElement noteElement) {
        editButton = noteElement.findElement(By.cssSelector("td > button"));
        deleteButton = noteElement.findElement(By.cssSelector("td > a"));
        title = noteElement.findElement(By.tagName("th")).getText();
        description = noteElement.findElements(By.cssSelector("td")).get(1).getText();
    }
}