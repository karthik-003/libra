package com.amk.libra.helper;

import com.amk.libra.model.Candidate;
import com.amk.libra.model.Incentive;
import com.amk.libra.model.InterviewDetails;
import com.amk.libra.model.InterviewRound;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * BEWARE : Very Clumsy.
 */
@Slf4j
public class ExcelHelper {

    public static final String INCENTIVE_TRACKER_FILE_NAME = "Incentive_Tracker.xlsx";
    public static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String SHEET = "Sheet1";

    private static final DataFormatter df = new DataFormatter();

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    private static final String[] INCENTIVE_COLUMNS_HEADER = {"Sl.No.", "Interview Date", "Candidate Name", "Candidate Mobile"
            , "Candidate Email", "Account", "Skill", "Stage of Interview", "Interviewer Full Name", "Interviewer Employee ID",
            "Interview Mode", "Talent Acquisition Team Member Name (Recruiter's Full Name)", "Interview Feedback", "Amount"
            , "Additional Comments"};

    public static List<InterviewDetails> getInterviewDetails(InputStream is) {
        List<InterviewDetails> interviewDetailsList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);


            Iterator<Row> rows = sheet.iterator();
            int rowNum = 0;

            while (rows.hasNext()) {
                InterviewDetails interviewDetails = new InterviewDetails();
                Row currentRow = rows.next();
                if (rowNum != 0 || checkIfRowIsEmpty(currentRow)) {

                    interviewDetails.setCandidateDetails(buildCandidateData(currentRow));
                    interviewDetails.setInterviewRounds(buildInterviewData(currentRow));

                    interviewDetailsList.add(interviewDetails);
                }

                rowNum++;
            }
        } catch (Exception e) {
            log.error("error occurred during getCandidateDetails():: {}", e.getMessage());
            e.printStackTrace();
        }
        return interviewDetailsList;
    }

    private static boolean checkIfRowIsEmpty(Row row) {
        if (row == null || row.getLastCellNum() <= 0) {
            return true;
        }
        Cell cell = row.getCell(row.getFirstCellNum());
        return cell == null || "".equals(cell.getRichStringCellValue().getString());
    }

    private static Candidate buildCandidateData(Row currentRow) {
        Iterator<Cell> cellsInRow = currentRow.iterator();
        Candidate.CandidateBuilder candidate = Candidate.builder();

        while (cellsInRow.hasNext()) {
            Cell currentCell = cellsInRow.next();
            String value = df.formatCellValue(currentCell);
            switch (currentCell.getColumnIndex()) {
                case 0:
                    candidate.modifiedDate(value);
                    break;
                case 1:
                    candidate.ageing(value);
                    break;
                case 2:
                    candidate.sourcingDate(value);
                    break;
                case 4:
                    candidate.name(value);
                    break;
                case 5:
                    candidate.phone(value);
                    break;
                case 6:
                    candidate.emailId(value);
                    break;
                case 7:
                    candidate.skill(value);
                    break;
                case 35:
                    candidate.lastWorkingDay(value);
                    break;
                case 36:
                    candidate.flag(value);
                    break;
                case 37:
                    candidate.experience(value);
                    break;
                case 38:
                    candidate.currentCompany(value);
                    break;
                case 39:
                    candidate.noticePeriod(value);
                    break;
                case 40:
                    candidate.currentCTC(value);
                    break;
                case 41:
                    candidate.expectedCTC(value);
                    break;
                case 45:
                    candidate.currentLocation(value);
                    break;
                case 46:
                    candidate.preferredLocation(value);
                    break;
                case 47:
                    candidate.source(value);
                    break;
                case 56:
                    candidate.gender(value);
                    break;
            }
        }

        return candidate.build();
    }


    private static List<InterviewRound> buildInterviewData(Row row) {
        //Populate L1 round.
        List<InterviewRound> interviewRounds = new ArrayList<>();
        if (hasL1Round(row)) {
            InterviewRound.InterviewRoundBuilder l1Round = InterviewRound.builder();
            l1Round.round("L1");
            for (Cell currentCell : row) {
                String value = df.formatCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 6:
                        l1Round.candidateEmailId(value);
                        break;
                    case 13:
                        l1Round.panelEmpId(value);
                        break;
                    case 14:
                        l1Round.date(value);
                        break;
                    case 15:
                        l1Round.time(value);
                        break;
                    case 16:
                        l1Round.selectDate(value);
                        break;
                    case 17:
                        l1Round.remarks(value);
                        break;
                }
            }
            interviewRounds.add(l1Round.build());
        }
        if (hasL2Round(row)) {
            InterviewRound.InterviewRoundBuilder l2Round = InterviewRound.builder();
            l2Round.round("L2");
            for (Cell currentCell : row) {
                String value = df.formatCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 6:
                        l2Round.candidateEmailId(value);
                        break;
                    case 18:
                        l2Round.panelEmpId(value);
                        break;
                    case 19:
                        l2Round.date(value);
                        break;
                    case 20:
                        l2Round.time(value);
                        break;
                    // TODO: Populate selectDate and remarks. Not present in Excel.
//                    case 16:
//                        l2Round.selectDate(value);
//                        break;
//                    case 17:
//                        l2Round.remarks(value);
//                        break;
                }
            }
            interviewRounds.add(l2Round.build());
        }
        if (hasL3Round(row)) {
            InterviewRound.InterviewRoundBuilder l3Round = InterviewRound.builder();
            l3Round.round("L3");
            for (Cell currentCell : row) {
                String value = df.formatCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 6:
                        l3Round.candidateEmailId(value);
                        break;
                    case 21:
                        l3Round.panelEmpId(value);
                        break;
                    case 22:
                        l3Round.date(value);
                        break;
                    case 23:
                        l3Round.time(value);
                        break;
                    // TODO: Populate selectDate and remarks. Not present in Excel.
//                    case 16:
//                        l3Round.selectDate(value);
//                        break;
//                    case 17:
//                        l3Round.remarks(value);
//                        break;
                }
            }
            interviewRounds.add(l3Round.build());
        }
        if (hasELRound(row)) {
            InterviewRound.InterviewRoundBuilder elRound = InterviewRound.builder();
            elRound.round("EL Round");
            for (Cell currentCell : row) {
                String value = df.formatCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 6:
                        elRound.candidateEmailId(value);
                        break;
                    case 24:
                        elRound.panelEmpId(value);
                        break;
                    case 25:
                        elRound.date(value);
                        break;
                    case 26:
                        elRound.time(value);
                        break;
                    case 27:
                        elRound.selectDate(value);
                        break;
                    // TODO: Populate remarks. Not present in Excel.
//                    case 17:
//                        elRound.remarks(value);
//                        break;
                }
            }
            interviewRounds.add(elRound.build());
        }
        if (hasClientRound(row)) {
            InterviewRound.InterviewRoundBuilder clientRound = InterviewRound.builder();
            clientRound.round("Client Round");
            for (Cell currentCell : row) {
                String value = df.formatCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 6:
                        clientRound.candidateEmailId(value);
                        break;
                    case 29:
                        clientRound.panelName(value);
                        break;
                    case 30:
                        clientRound.date(value);
                        break;
                    case 31:
                        clientRound.time(value);
                        break;
                    case 32:
                        clientRound.selectDate(value);
                        break;
                    // TODO: Populate remarks. Not present in Excel.
//                    case 17:
//                        clientRound.remarks(value);
//                        break;
                }
            }
            interviewRounds.add(clientRound.build());
        }
        return interviewRounds;

    }

    private static boolean hasL1Round(Row currentRow) {
        return !df.formatCellValue(currentRow.getCell(14)).isBlank();
    }

    private static boolean hasL2Round(Row currentRow) {
        return !df.formatCellValue(currentRow.getCell(19)).isBlank();
    }

    private static boolean hasL3Round(Row currentRow) {
        return !df.formatCellValue(currentRow.getCell(22)).isBlank();
    }

    private static boolean hasELRound(Row currentRow) {
        return !df.formatCellValue(currentRow.getCell(25)).isBlank();
    }

    private static boolean hasClientRound(Row currentRow) {
        return !df.formatCellValue(currentRow.getCell(30)).isBlank();
    }

    public static FileOutputStream createIncentiveTracker(Map<String, Set<Incentive>> incentiveData) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        for (Map.Entry<String, Set<Incentive>> entry : incentiveData.entrySet()) {
            String empId = entry.getKey();
            int serialNumber = 1;
            Set<Incentive> incentives = entry.getValue();
            //Create Sheet for every Panel.
            XSSFSheet sheet = xssfWorkbook.createSheet(empId);
            int rowNum = 0;
            int cellNum = 0; // Only incremented while creating header.
            //Create Header
            Row headerRow = sheet.createRow(rowNum++);
            for (String s : INCENTIVE_COLUMNS_HEADER) {
                Cell c = headerRow.createCell(cellNum++);
                c.setCellValue(s);
            }
            //Populate the data
            for (Incentive incentive : incentives) {
                Row row = sheet.createRow(rowNum++);
                populateRowData(incentive,row,cellNum,serialNumber++,empId);
            }
        }
        return saveExcelFile(xssfWorkbook);

    }

    private static void populateRowData(Incentive incentive,Row row,int totalCells,
                                        int serialNumber, String empId){
        try{
            for (int i = 0; i < totalCells; i++) {
                Cell cell = row.createCell(i);
                switch (i) {
                    case 0:
                        cell.setCellValue(serialNumber);
                        break;
                    case 1:
                        cell.setCellValue(incentive.getInterviewDate());
                        break;
                    case 2:
                        cell.setCellValue(incentive.getCandidateName());
                        break;
                    case 3:
                        cell.setCellValue(incentive.getCandidatePhone());
                        break;
                    case 4:
                        cell.setCellValue(incentive.getCandidateEmailId());
                        break;
                    case 5:
                        cell.setCellValue(incentive.getAccount());
                        break;
                    case 6:
                        cell.setCellValue(incentive.getSkill());
                        break;
                    case 7:
                        cell.setCellValue(incentive.getRound());
                        break;
                    case 8:
                        cell.setCellValue("PANEL_NAME");
                        break;
                    case 9:
                        cell.setCellValue(empId);
                        break;
                    case 10:
                        cell.setCellValue("TEAMS");
                        break;
                    case 11:
                        cell.setCellValue(incentive.getTaSpoc());
                        break;
                    case 12:
                        cell.setCellValue(incentive.getResult());
                        break;
                    case 13:
                        cell.setCellValue("500");
                        break;
                    case 14:
                        cell.setCellValue("ADDITIONAL_COMMENTS");
                        break;
                }
            }
        }catch (Exception e){
            log.error("Error while populating row data: {}",e.getMessage());
            e.printStackTrace();
        }

    }

    private static FileOutputStream saveExcelFile(XSSFWorkbook workbook){
        try {
            FileOutputStream os = new FileOutputStream(INCENTIVE_TRACKER_FILE_NAME);
            workbook.write(os);
            os.close();
            return os;
        } catch (Exception e) {
            log.error("Exception in saveExcelFile: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
