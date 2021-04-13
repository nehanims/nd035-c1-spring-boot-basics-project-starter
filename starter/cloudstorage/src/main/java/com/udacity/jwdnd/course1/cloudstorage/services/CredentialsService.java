package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialsService {
    private final CredentialsMapper credentialsMapper;
    private final EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    public List<CredentialsForm> getCredentials(Integer loggedInUserId) {

        return credentialsMapper.getCredentials(loggedInUserId)
                .stream()
                .map(this::getCredentialsForm)
                .collect(Collectors.toList());
    }

    CredentialsForm getCredentialsForm(Credentials credentials){

        return CredentialsForm.builder()
                .credentialId(credentials.getCredentialId())
                .url(credentials.getUrl())
                .username(credentials.getUsername())
                .password(credentials.getPassword())
                .key(credentials.getKey())
                .build();
    }

    public void saveCredentials(CredentialsForm credentialsForm, Integer loggedInUserId) {
        if(credentialsForm.getCredentialId()==null) {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);
            String encryptedPassword = encryptionService.encryptValue(credentialsForm.getPassword(), encodedKey);
            Credentials credentials = Credentials.builder()
                    .credentialId(credentialsForm.getCredentialId())
                    .url(credentialsForm.getUrl())
                    .username(credentialsForm.getUsername())
                    .key(encodedKey)
                    .password(encryptedPassword)
                    .userId(loggedInUserId)
                    .build();
            credentialsMapper.addCredentials(credentials);
        }
        else {
            String encryptedPassword = encryptionService.encryptValue(credentialsForm.getPassword(), credentialsForm.getKey());
            Credentials credentials = Credentials.builder()
                    .credentialId(credentialsForm.getCredentialId())
                    .url(credentialsForm.getUrl())
                    .username(credentialsForm.getUsername())
                    .key(credentialsForm.getKey())
                    .password(encryptedPassword)
                    .userId(loggedInUserId)
                    .build();
            credentialsMapper.updateCredentials(credentials);
        }
    }

    public void deleteCredentials(Integer credentialId) {
        credentialsMapper.deleteCredential(credentialId);
    }

    public Credentials getCredentialByCredentialId(Integer id){
        return credentialsMapper.getCredentialByCredentialId(id);
    }

}
