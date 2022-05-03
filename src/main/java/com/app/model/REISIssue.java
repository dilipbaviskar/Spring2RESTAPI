package com.app.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "reis_issue_library")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class REISIssue {
    @Id
    private String id;
    
    private int year;
    private int quarter;
    private String title;
        
    private Binary pdfFile;
    private long size;
    private String contentType;
    REISIssue(String title)
    {
    	this.title = title;
    }
}