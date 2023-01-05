package com.amk.libra.resource;

import com.amk.libra.helper.ExcelHelper;
import com.amk.libra.model.InterviewDetails;
import com.amk.libra.request.ExportIncentiveDataRequest;
import com.amk.libra.service.IncentiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/incentive")
@Slf4j
public class IncentiveResource {

    @Autowired
    IncentiveService incentiveService;

    @PostMapping("/prepareData")
    @CrossOrigin("*")
    public ResponseEntity<Object> getIncentiveData(@RequestParam("file") MultipartFile file){
        String message = "";
        try {
            InputStream is = file.getInputStream();
            message = ExcelHelper.hasExcelFormat(file) ? "Excel file uploaded" : "Excel file not uploaded. ";
            if(ExcelHelper.hasExcelFormat(file)){
                List<InterviewDetails> interviewDetailsList = ExcelHelper.getInterviewDetails(is);
                return ResponseEntity.ok(incentiveService.getIncentiveData(interviewDetailsList));
                //return ResponseEntity.ok(interviewDetailsList);
            }
            return ResponseEntity.badRequest().body(message);
        }catch (Exception e){
            log.error("Exception while uploading file: {}",e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/interviewData")
    public ResponseEntity<Object> getInterviewDetails(@RequestParam("file") MultipartFile file){
        String message = "";
        try{
            InputStream is = file.getInputStream();
            message = ExcelHelper.hasExcelFormat(file) ? "Excel file uploaded" : "Excel file not uploaded. ";
            if(ExcelHelper.hasExcelFormat(file)){
                List<InterviewDetails> interviewDetailsList = ExcelHelper.getInterviewDetails(is);
                return ResponseEntity.ok(interviewDetailsList);
            }
            return ResponseEntity.badRequest().body(message);

        }catch (Exception e){
            log.error("Exception while uploading file: {}",e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/export")
    @CrossOrigin("*")
    public ResponseEntity<Object> exportIncentiveData(@RequestBody ExportIncentiveDataRequest request){
        log.info("Export Request: "+request.getIncentiveData().size());
        Resource fileResource = null;
        try{
            fileResource = incentiveService.exportIncentiveData(request);
        }catch (Exception e){
            ResponseEntity.internalServerError().build();
        }
        if(fileResource == null){
            log.error("file not found...");
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + fileResource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(fileResource);


    }
}
