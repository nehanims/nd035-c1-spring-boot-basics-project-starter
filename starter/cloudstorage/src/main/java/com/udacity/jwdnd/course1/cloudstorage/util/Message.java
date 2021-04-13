package com.udacity.jwdnd.course1.cloudstorage.util;

public class Message {
    public static final String OPERATION_NOT_ALLOWED = "ERROR: Operation not allowed";

    public static final String NOTE_DOES_NOT_EXIST = "ERROR: Operation failed: Note does not exist";
    public static final String NOTE_DELETED_SUCCESSFULLY = "SUCCESS: Note deleted successfully";
    public static final String NOTE_SAVED_SUCCESSFULLY = "SUCCESS: Note saved successfully";
    public static final String DUPLICATE_NOTE = "ERROR: Note with the same title and description already exist";
    public static final String NOTE_DESCRIPTION_EXCEEDS_LIMIT = "ERROR: Note description exceeds 1000 character limit";

    public static final String FILE_SAVED_SUCCESSFULLY = "SUCCESS: File saved successfully";
    public static final String FILE_DELETED_SUCCESSFULLY = "SUCCESS: File deleted successfully";
    public static final String FILE_DOES_NOT_EXIST = "ERROR: Operation failed: File does not exist";

    public static final String CREDENTIAL_SAVED_SUCCESSFULLY = "SUCCESS: Credential saved successfully";
    public static final String CREDENTIAL_DELETED_SUCCESSFULLY = "SUCCESS: Credential deleted successfully";
    public static final String CREDENTIAL_DOES_NOT_EXIST = "ERROR: Operation failed: Credential does not exist";
    public static final String DUPLICATE_CREDENTIAL = "ERROR: Credential with entered username for input URL already exists";

}
