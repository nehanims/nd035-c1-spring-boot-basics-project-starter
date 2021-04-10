package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM Credentials WHERE userid=#{loggedInUserId} ORDER BY credentialId")
    List<Credentials> getCredentials(Integer loggedInUserId);

    @Insert("INSERT INTO Credentials (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void addCredentials(Credentials credentials);

    @Update("UPDATE Credentials SET username=#{username}, url=#{url}, key=#{key}, password=#{password} WHERE credentialId=#{credentialId}")
    void updateCredentials(Credentials credentials);

    @Delete("DELETE FROM Credentials WHERE credentialId=#{credentialId}")
    void deleteCredential(Integer credentialId);

    @Select("SELECT * FROM Credentials WHERE credentialid=#{id}")
    Credentials getCredentialByCredentialId(Integer id);
}
