package com.app.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.csv.parser.CSVToExcel;
import com.app.model.MasterExcelData;
import com.app.service.MasterExcelDataService;

@RestController
@CrossOrigin("*")
@RequestMapping("/masterdata")
public class MasterDataZipUploadController {
	private static int BUFFER_SIZE = 2048;
	private static int ROWS_TO_SKIP = 0;
	private static final Logger LOG = LoggerFactory.getLogger("MasterDataZipUploadController");
	@Autowired
	private MasterExcelDataService masterExcelDataService;

	@GetMapping("/uploadZip")
	public String uploadZip(Model model) {
		model.addAttribute("message", "upload master data zip");
		return "uploadZip";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data;application/zip", produces = "application/json")
	public ResponseEntity<String> uploadZip(@RequestParam(value = "year", required = true) final int year,
			@RequestParam(value = "quarterId", required = true) int quarterId, @RequestParam("title") String title,
			@RequestParam("file") MultipartFile zipContent, Model model, HttpServletRequest request) throws Exception {
		// hard code the signature for the moment
		String signature = "RETAILER_GROUP:*|CHANNEL:*|LOCALE:de-AT|INDUSTRY:5499";

		XSSFWorkbook workbook = processFileZipEntry(zipContent, signature);

		// FileOutputStream fileOut = new FileOutputStream("c:/tmp/FINAL.xlsx");
		// write this workbook to an FileOutputstream.
		// workbook.write(fileOut);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		workbook.write(baos);

		MasterExcelData masterData = new MasterExcelData();
		masterData.setTitle(title);
		masterData.setQuarter(quarterId);
		masterData.setYear(year);
		masterData.setXlsxFile(new Binary(BsonBinarySubType.BINARY, baos.toByteArray()));
		masterData.setSize(baos.size());
		masterData.setContentType("application/xlsx");
		String itemId= masterExcelDataService.addIssue(masterData);

		baos.flush();
		baos.close();
		return new ResponseEntity<String>(HttpStatus.CREATED).ok("MasterExcel file created successfully." + itemId);
	}

	@GetMapping(value = "/download/{id}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public byte[] getIssueByYearQtr(@PathVariable(value = "id") String id,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "quarterId", required = false) String quarterId) {
		byte[] data = null;
		MasterExcelData file=null;
		if (id != null) {
			file = masterExcelDataService.getIssue(id);
		} 
		if(file==null){
			file = masterExcelDataService.getIssue(Integer.parseInt(year), Integer.parseInt(quarterId));
		}

		if (file != null) {
			data = file.getXlsxFile().getData();
		}
		return data;
		
	}

	protected XSSFWorkbook processFileZipEntry(MultipartFile file, String signature) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(file.getBytes()))) { 
			ZipEntry zipEntry;
			while ((zipEntry = zipInputStream.getNextEntry()) != null) { 
				if (!zipEntry.isDirectory() && zipEntry.getName().toLowerCase().endsWith("csv")) {
					XSSFSheet sheet = workbook.createSheet(zipEntry.getName().toLowerCase().replaceFirst(".csv", ""));
					extractEntry(zipEntry, zipInputStream, sheet, ROWS_TO_SKIP);
				}
				zipInputStream.closeEntry();
			}

		}

		return workbook;
	}

	/*
	 * Utility method to read data from InputStream
	 */

	private static void extractEntry(final ZipEntry entry, ZipInputStream is, XSSFSheet sheet, int rowsToSkip)
			throws Exception {
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		try {
			fos = new ByteArrayOutputStream();
			final byte[] buf = new byte[BUFFER_SIZE];
			int read = 0;
			int length;
			while ((length = is.read(buf, 0, buf.length)) >= 0) {
				fos.write(buf, 0, length);
			}

			Reader reader = null;
			List<String[]> rows = null;
			try {
				reader = new InputStreamReader(new ByteArrayInputStream(fos.toByteArray()));
				rows = CSVToExcel.readAll(reader);
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} finally {
				if (reader != null) {

					reader.close();
					reader = null;
				}
			}

			int rowNum = 0;
			for (String[] rowData : rows) {
				if (rowsToSkip > rowNum)
					continue;
				// System.out.println();
				XSSFRow row = sheet.createRow(rowNum);
				for (int colIndex = 0; colIndex < rowData.length; colIndex++) {
					// System.out.print("\t" + rowData[colIndex]);
					XSSFCell cell = row.createCell(colIndex);
					cell.setCellValue(rowData[colIndex]);
				}
				rowNum++;
			}
			fos.flush();
			fos.close();
			if (reader != null)
				reader.close();
		} catch (Exception ioex) {
			if (fos != null)
				fos.close();

		}

	}

}
