package com.app.mongodb.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.app.model.REISIssue;
import com.app.mongodb.repository.REISIssueDAL;

@Repository
public class REISIssueRepository implements REISIssueDAL {
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public REISIssue saveIssue(REISIssue reisIssue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<REISIssue> getAllIssues() {
		// TODO Auto-generated method stub
		return mongoTemplate.findAll(REISIssue.class);
	}

	@Override
	public List<REISIssue> getAllIssuesPaginated(int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.skip(pageNumber * pageSize);
		query.limit(pageSize);
		return mongoTemplate.find(query, REISIssue.class);

	}

	@Override
	public REISIssue findOneById(String id) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, REISIssue.class);
	}

	@Override
	public REISIssue findByYearQuarter(int year, int quarterId) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("year").is(year).andOperator(Criteria.where("quarterId").is(quarterId)));
		return mongoTemplate.findOne(query, REISIssue.class);

	}

	@Override
	public List<REISIssue> findByYearRange(int lowerBoundYear, int upperBoundYear) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(
				Criteria.where("year").gt(lowerBoundYear).andOperator(Criteria.where("year").lt(upperBoundYear)));
		return mongoTemplate.find(query, REISIssue.class);

	}

	@Override
	public REISIssue updateOneIssue(REISIssue reisIssue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteOneIssue(REISIssue reisIssue) {
		// TODO Auto-generated method stub
		 mongoTemplate.remove(reisIssue);
	}

}