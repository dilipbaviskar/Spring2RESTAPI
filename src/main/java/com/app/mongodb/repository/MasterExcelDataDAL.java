package com.app.mongodb.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.app.model.MasterExcelData;
import com.app.model.REISIssue;


public interface MasterExcelDataDAL {
	MasterExcelData saveIssue(MasterExcelData data);

	List<MasterExcelData> getAllIssues();

	List<MasterExcelData> getAllIssuesPaginated(int pageNumber, int pageSize);

	MasterExcelData findOneById(String id);

	MasterExcelData findByYearQuarter(int year, int quarterId);

	List<MasterExcelData> findByYearRange(int lowerBoundYear, int upperBoundYear);

	MasterExcelData updateOneIssue(MasterExcelData reisIssue);

	void deleteOneIssue(MasterExcelData reisIssue);
}
