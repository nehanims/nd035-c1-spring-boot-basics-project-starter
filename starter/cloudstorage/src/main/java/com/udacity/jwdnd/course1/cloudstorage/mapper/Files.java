package com.udacity.jwdnd.course1.cloudstorage.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Files {
    private Integer fileId;
    private String filename;
    private String contentType;
    private String fileSize;
    private Integer userId;
    private byte[] fileData;
    //TODO not sure if this was the right approach, but I used a byte array, based on the following responses:
    // https://knowledge.udacity.com/questions/282016
    // https://knowledge.udacity.com/questions/395587
    // https://knowledge.udacity.com/questions/479892
    // https://knowledge.udacity.com/questions/292288
    // https://knowledge.udacity.com/questions/341427


}
