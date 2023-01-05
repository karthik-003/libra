package com.amk.libra.service;

import com.amk.libra.helper.ExcelHelper;
import com.amk.libra.model.Candidate;
import com.amk.libra.model.Incentive;
import com.amk.libra.model.InterviewDetails;
import com.amk.libra.model.InterviewRound;
import com.amk.libra.request.ExportIncentiveDataRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class IncentiveService {

    public Map<String, Set<Incentive>> getIncentiveData(List<InterviewDetails> interviewDetailsList) {
        Map<String, Set<Incentive>> incentiveDetailForPanel = new HashMap<>();
        try {


            for (InterviewDetails interviewDetail : interviewDetailsList) {

                Candidate candidate = interviewDetail.getCandidateDetails();
                for (InterviewRound round : interviewDetail.getInterviewRounds()) {
                    if (!round.getRound().equalsIgnoreCase("Client Round")) {
                        Incentive incentive = new Incentive();
                        incentive.setCandidateEmailId(candidate.getEmailId());
                        incentive.setCandidateName(candidate.getName());
                        incentive.setCandidatePhone(candidate.getPhone());
                        incentive.setSkill(candidate.getSkill());

                        incentive.setInterviewDate(round.getDate());
                        incentive.setResult(round.getResult());
                        incentive.setRound(round.getRound());
                        incentive.setAccount(round.getAccount());
                        incentive.setPanelEmpId(round.getPanelEmpId());
                        incentive.setPanelEmpName(round.getPanelName());
                        Set<Incentive> panelInterviews;

                        if (incentiveDetailForPanel.containsKey(round.getPanelEmpId())) {
                            panelInterviews = incentiveDetailForPanel.get(round.getPanelEmpId());
                        } else {
                            panelInterviews = new HashSet<>();
                        }
                        panelInterviews.add(incentive);
                        incentiveDetailForPanel.put(round.getPanelEmpId(), panelInterviews);
                    }
                }

            }

        } catch (Exception e) {
            log.error("Exception occurred {}", e.getMessage());
        }

        return incentiveDetailForPanel;
    }

    public Resource exportIncentiveData(ExportIncentiveDataRequest request) {
        try {
            AtomicReference<Path> excelFile = new AtomicReference<>();
            Map<String, Set<Incentive>> incentiveMap = new HashMap<>();
            for (Incentive i : request.getIncentiveData()) {
                Set<Incentive> incentiveList = incentiveMap.containsKey(i.getPanelEmpId()) ?
                        incentiveMap.get(i.getPanelEmpId()) : new HashSet<>();
                incentiveList.add(i);
                incentiveMap.put(i.getPanelEmpId(), incentiveList);
            }
            log.info("Panel to export incentive data: " + incentiveMap.keySet());
            ExcelHelper.createIncentiveTracker(incentiveMap);
            Path path = Paths.get(".");
            Files.list(path).forEach(file -> {
                log.info(file.getFileName().toString());
                if (file.getFileName().toString().equalsIgnoreCase(ExcelHelper.INCENTIVE_TRACKER_FILE_NAME)) {
                    log.info("Match !!!!!!!");
                    excelFile.set(file);
                    return;
                }
            });
            if (excelFile.get() != null) {
                return new UrlResource(excelFile.get().toUri());
            }
        } catch (Exception e) {
            log.error("" + e);
        }
        return null;
    }
}
