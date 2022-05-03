package com.app.mongodb.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.app.model.MasterExcelData;
import com.app.model.REISIssue;
import com.app.mongodb.repository.MasterExcelDataDAL;
import com.app.mongodb.repository.REISIssueDAL;

@Repository
public class MasterExcelDataRepository implements MasterExcelDataDAL {
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public MasterExcelData saveIssue(MasterExcelData masterData) {
		// TODO Auto-generated method stub
		return mongoTemplate.save(masterData);
	}

	@Override
	public List<MasterExcelData> getAllIssues() {
		// TODO Auto-generated method stub
		return mongoTemplate.findAll(MasterExcelData.class);
	}

	@Override
	public List<MasterExcelData> getAllIssuesPaginated(int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.skip(pageNumber * pageSize);
		query.limit(pageSize);
		return mongoTemplate.find(query, MasterExcelData.class);

	}

	@Override
	public MasterExcelData findOneById(String id) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, MasterExcelData.class);
	}

	@Override
	public MasterExcelData findByYearQuarter(int year, int quarterId) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("year").is(year).andOperator(Criteria.where("quarterId").is(quarterId)));
		return mongoTemplate.findOne(query, MasterExcelData.class);

	}

	@Override
	public List<MasterExcelData> findByYearRange(int lowerBoundYear, int upperBoundYear) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(
				Criteria.where("year").gt(lowerBoundYear).andOperator(Criteria.where("year").lt(upperBoundYear)));
		return mongoTemplate.find(query, MasterExcelData.class);

	}

	@Override
	public MasterExcelData updateOneIssue(MasterExcelData reisIssue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteOneIssue(MasterExcelData reisIssue) {
		// TODO Auto-generated method stub
		 mongoTemplate.remove(reisIssue);
	}

}