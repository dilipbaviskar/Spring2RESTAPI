package com.app.mongodb.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.app.model.REISIssue;


public interface REISIssueDAL {
	REISIssue saveIssue(REISIssue reisIssue);

	List<REISIssue> getAllIssues();

	List<REISIssue> getAllIssuesPaginated(int pageNumber, int pageSize);

	REISIssue findOneById(String id);

	REISIssue findByYearQuarter(int year, int quarterId);

	List<REISIssue> findByYearRange(int lowerBoundYear, int upperBoundYear);

	REISIssue updateOneIssue(REISIssue reisIssue);

	void deleteOneIssue(REISIssue reisIssue);
}
