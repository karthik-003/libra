package com.amk.libra.resource;

import com.amk.libra.helper.ExcelHelper;
import com.amk.libra.model.InterviewDetails;
import com.amk.libra.service.IncentiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/upload/")
@Slf4j
public class FileUploadResource {

    @Autowired
    IncentiveService incentiveService;

    @PostMapping("/incentiveData")
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
}
