package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM Files WHERE fileid=#{fileId}")
    Files getFile(Integer fileId);

    @Select("SELECT * FROM Files WHERE userid = #{loggedInUserId}")
    List<Files> getAllFiles(Integer loggedInUserId);

    @Insert("INSERT INTO Files (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void saveFile(Files file);

    @Delete("DELETE FROM Files WHERE fileid=#{fileId}")
    void deleteFile(Integer fileId);

}
