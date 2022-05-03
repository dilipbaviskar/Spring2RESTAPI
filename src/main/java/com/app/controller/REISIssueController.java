package com.app.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.model.REISIssue;
import com.app.service.REISIssueService;
import org.springframework.http.MediaType;

@RestController
@CrossOrigin("*")
@RequestMapping("/issue")
public class REISIssueController {
	@Autowired
	private REISIssueService reisIssueService;

//	    @GetMapping("/download")
//	    public String getIssue(@RequestParam(required=true)final int year, @RequestParam int quarterId, @RequestParam("title") String title, Model model) {
//	    	validateInputParam(  year,  quarterId);
//	        REISIssue reisIssue = reisIssueService.getIssue(year, quarterId);
//	        model.addAttribute("title", reisIssue.getTitle());
//	        model.addAttribute("issue", Base64.getEncoder().encodeToString(reisIssue.getPdfFile().getData()));
//	        return "photos";
//	    }

	@GetMapping("/upload")
	public String uploadIssue(Model model) {
		model.addAttribute("message", "hello");
		return "uploadIssue";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String uploadIssue(@RequestParam(value = "year", required = true) final int year,
			@RequestParam(value = "quarterId", required = true) int quarterId, @RequestParam("title") String title,
			@RequestParam("file") MultipartFile binaryContent, Model model, HttpServletRequest request)
			throws IOException {

		validateInputParam(year, quarterId);
		String id = reisIssueService.addIssue(year, quarterId, title, binaryContent);
		return "redirect:/issue/" + id;
	}

	@GetMapping(value = "/download/{id}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public byte[] getIssueByYearQtr(@PathVariable(value="id") String id,@RequestParam(value="year",required=false)   String year, @RequestParam(value="quarterId",required=false) String  quarterId) {
		byte[] data = null;
		REISIssue file;
		if (id != null) {
			file = reisIssueService.getIssue(id);
		} else {
			file = reisIssueService.getIssue(Integer.parseInt(year), Integer.parseInt(quarterId));
		}
		if (file != null) {
			data = file.getPdfFile().getData();
		}
		return data;
	}

	private void validateInputParam(int year, int quarterId) throws IllegalArgumentException {
		if (year > Calendar.getInstance().get(Calendar.YEAR) || year < 2000)
			throw new IllegalArgumentException("Invalid Year for Issue Upload");
		if (quarterId > 4 || quarterId < 1)
			throw new IllegalArgumentException("Invalid Quarter Id for Issue Upload. Valid Values are {1,2,3,4}");

	}
}
