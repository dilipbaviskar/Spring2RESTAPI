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

import com.app.model.MasterExcelData;
import com.app.model.REISIssue;
import com.app.mongodb.repository.impl.MasterExcelDataRepository;
import com.app.mongodb.repository.impl.REISIssueRepository;
@Service
public class MasterExcelDataService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired 
	MasterExcelDataRepository repo;
	
    public String addIssue(MasterExcelData masterData) throws IOException {
    	MasterExcelData issue= getIssue(masterData.getYear() , masterData.getQuarter());
        MasterExcelData savedIssue=  mongoTemplate.save(masterData);
        return savedIssue.getId(); 
    }
    
    public MasterExcelData getIssue(int year , int quarterid)
    {    	
    	return repo.findByYearQuarter(year, quarterid);    	 
    }

    public MasterExcelData getIssue(String id)
    {    	
    	return repo.findOneById(id);    	 
    }
 
}
