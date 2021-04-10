package com.udacity.jwdnd.course1.cloudstorage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CredentialForm {
    //private Integer credentialId;
    //private String key;
    private String url;
    private String username;
    private String plainTextPassword;

}
