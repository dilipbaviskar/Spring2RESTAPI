package com.app.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "masterexceldata")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MasterExcelData {
    @Id
    private String id;
    
    private int year;
    private int quarter;
    private String title;
        
    private Binary xlsxFile;
    private long size;
    private String contentType;    
    
}