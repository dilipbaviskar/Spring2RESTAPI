package com.app.service;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.model.REISIssue;
import com.app.mongodb.repository.impl.REISIssueRepository;
@Service
public class REISIssueService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired 
	REISIssueRepository repo;
	
    public String addIssue(int year,int quarterId,String title, MultipartFile file) throws IOException { 
        if(file.isEmpty()) {
        	
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("HTTPResponseCode", 200);
            jsonResult.put("ErrorDesc", "Issue file is empty");
        }
   
        REISIssue reisIssue = new REISIssue();        
        reisIssue.setTitle(title);
        reisIssue.setQuarter(quarterId);
        reisIssue.setYear(year);
        reisIssue.setPdfFile(new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
        reisIssue.setContentType(file.getContentType());
        reisIssue.setSize( file.getSize());
        
        REISIssue savedIssue=  mongoTemplate.save(reisIssue);
        return savedIssue.getId(); 
    }
    
    public REISIssue getIssue(int year , int quarterid)
    {    	
    	return repo.findByYearQuarter(year, quarterid);    	 
    }

    public REISIssue getIssue(String id)
    {    	
    	return repo.findOneById(id);    	 
    }
 
}
