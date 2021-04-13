package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotesTest {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private static WebDriverWait webDriverWait;

    private static HomePage homePage;
    private static NotesTab notesTab;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.operadriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new OperaDriver();
        webDriverWait = new WebDriverWait(driver, 5);

        homePage = new HomePage(driver);
        notesTab = new NotesTab(driver, webDriverWait);

        driver.get("http://localhost:" + port + "/signup");
        homePage.signupAndLogin();//TODO signup everytime seems like bad practice but BeforeAll method is static for some reason
        homePage.navigateToNotesTab();

    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testAddNote(){
        notesTab.addNote(NotesTab.TITLE1, NotesTab.DESCRIPTION1);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));
        List<Note> notes = notesTab.getNotes();
        assertTrue(notes.size()==1);

        notesTab.addNote(NotesTab.TITLE2, NotesTab.DESCRIPTION2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));
        notes = notesTab.getNotes();
        assertTrue(notes.size()==2);

        Note note1 = notes.get(0);
        assertEquals(NotesTab.TITLE1, note1.getTitle());
        assertEquals(NotesTab.DESCRIPTION1, note1.getDescription());

        Note note2 = notes.get(1);
        assertEquals(NotesTab.TITLE2, note2.getTitle());
        assertEquals(NotesTab.DESCRIPTION2, note2.getDescription());

    }

    @Test
    @Order(2)
    public void testDeleteNote(){

        //verify that 2 records are available and verify the content of the records
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));
        List<Note> notes = notesTab.getNotes();
        assertEquals(2, notes.size());
        Note note1 = notes.get(0);
        assertEquals(NotesTab.TITLE1, note1.getTitle());
        assertEquals(NotesTab.DESCRIPTION1, note1.getDescription());
        Note note2 = notes.get(1);
        assertEquals(NotesTab.TITLE2, note2.getTitle());
        assertEquals(NotesTab.DESCRIPTION2, note2.getDescription());

        //delete 1st record and verify that 1 record is available and the correct record was deleted
        // (by checking that the remaining record is the one that was not deleted)
        notesTab.deleteNote(note1);

        notes = notesTab.getNotes();
        assertTrue(notes.size()==1);
        note2 = notes.get(0);
        assertEquals(NotesTab.TITLE2, note2.getTitle());
        assertEquals(NotesTab.DESCRIPTION2, note2.getDescription());

        //delete the remaining record and verify that no record is available
        notesTab.deleteNote(note2);

        notes = notesTab.getNotes();
        assertTrue(notes.size()==0);

        //add the notes back for the next test!
        notesTab.addNote(NotesTab.TITLE1, NotesTab.DESCRIPTION1);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));

        notesTab.addNote(NotesTab.TITLE2, NotesTab.DESCRIPTION2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));
        notes = notesTab.getNotes();
        assertTrue(notes.size()==2);


    }

    @Test
    @Order(3)
    public void testEditNote() {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));
        List<Note> notes = notesTab.getNotes();
        assertTrue(notes.size()==2);

        Note note1 = notes.get(0);
        assertEquals(note1.getTitle(), NotesTab.TITLE1);
        assertEquals(note1.getDescription(), NotesTab.DESCRIPTION1);

        notesTab.editNote(note1, NotesTab.TITLE1_EDITED, NotesTab.DESCRIPTION1);

        notes = notesTab.getNotes();//TODO is there a better way to do this?

        Note note2 = notes.get(1);
        assertEquals(note2.getTitle(), NotesTab.TITLE2);
        assertEquals(note2.getDescription(), NotesTab.DESCRIPTION2);

        notesTab.editNote(note2, NotesTab.TITLE2, NotesTab.DESCRIPTION2_EDITED);

        notes = notesTab.getNotes();

        note1 = notes.get(0);
        assertEquals(NotesTab.TITLE1_EDITED, note1.getTitle());
        assertEquals(NotesTab.DESCRIPTION1, note1.getDescription());

        note2 = notes.get(1);
        assertEquals(note2.getTitle(), NotesTab.TITLE2);
        assertEquals(note2.getDescription(), NotesTab.DESCRIPTION2_EDITED);

    }

}
