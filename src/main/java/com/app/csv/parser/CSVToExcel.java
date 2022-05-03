package com.app.csv.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

 
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CSVToExcel {
	private CSVToExcel() {
		
	}
	public static List<String[]> readAll(Reader reader) throws Exception{
		CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser).build();
		List<String[]> list = new ArrayList<String[]>();
		try {
			list = csvReader.readAll();
			reader.close();
			csvReader.close();
		}  
		finally {}
		return list;
	}
}
