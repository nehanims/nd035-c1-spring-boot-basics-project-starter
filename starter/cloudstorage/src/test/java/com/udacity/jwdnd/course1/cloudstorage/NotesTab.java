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

public class NotesTab {

    public static final String TITLE1 = "Test Note Title 1";
    public static final String TITLE1_EDITED = "Note1 Edited Title";
    public static final String TITLE2 = "Test Note Title 2";

    public static final String DESCRIPTION1 = "Test Note Description 1";
    public static final String DESCRIPTION2 = "Test Note Description 2";
    public static final String DESCRIPTION2_EDITED = "Note2 Edited Description";
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id="addNoteButton")
    private WebElement addNoteButton;

    @FindBy(id="saveNoteButton")
    private WebElement saveNoteButton;

    @FindBy(id="note-title")
    private WebElement noteTitleField;

    @FindBy(id="note-description")
    private WebElement noteDescriptionField;

    public NotesTab(WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = wait;
    }

    public void addNote(String title, String description) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        addNoteButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        saveNoteForm(title, description);
    }

    public void deleteNote(Note note) {
        note.getDeleteButton().click();
    }

    public void editNote(Note note, String newTitle, String newDescription) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));
        note.getEditButton().click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        saveNoteForm(newTitle, newDescription);
    }


    public List<Note> getNotes() {
        //TODO see best practice for this.. also refer http://elementalselenium.com/tips/25-tables*/

        return driver.findElements(By.cssSelector("#notesTable > tbody > tr"))
                .stream()
                .map(Note::new)
                .collect(Collectors.toList());
    }

    private void saveNoteForm(String title, String description) {
        noteTitleField.clear();
        noteTitleField.click();
        noteTitleField.sendKeys(title);

        noteDescriptionField.clear();
        noteDescriptionField.click();
        noteDescriptionField.sendKeys(description);

        saveNoteButton.click();
    }
}
