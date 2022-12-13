package com.amk.libra.service;

import com.amk.libra.helper.ExcelHelper;
import com.amk.libra.model.Candidate;
import com.amk.libra.model.Incentive;
import com.amk.libra.model.InterviewDetails;
import com.amk.libra.model.InterviewRound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class IncentiveService {

    public Map<String,Set<Incentive>> getIncentiveData(List<InterviewDetails> interviewDetailsList){
        Map<String,Set<Incentive>> incentiveDetailForPanel = new HashMap<>();
        try {


            for(InterviewDetails interviewDetail: interviewDetailsList){

                Candidate candidate = interviewDetail.getCandidateDetails();
                for(InterviewRound round: interviewDetail.getInterviewRounds()){
                    if(!round.getRound().equalsIgnoreCase("Client Round")){
                        Incentive incentive = new Incentive();
                        incentive.setCandidateEmailId(candidate.getEmailId());
                        incentive.setCandidateName(candidate.getName());
                        incentive.setCandidatePhone(candidate.getPhone());
                        incentive.setSkill(candidate.getSkill());

                        incentive.setInterviewDate(round.getDate());
                        incentive.setResult(round.getResult());
                        incentive.setRound(round.getRound());
                        incentive.setAccount(round.getAccount());
                        Set<Incentive> panelInterviews;

                        if(incentiveDetailForPanel.containsKey(round.getPanelEmpId())){
                            panelInterviews = incentiveDetailForPanel.get(round.getPanelEmpId());
                        }else{
                            panelInterviews = new HashSet<>();
                        }
                        panelInterviews.add(incentive);
                        incentiveDetailForPanel.put(round.getPanelEmpId(),panelInterviews);
                    }
                }

            }

        }catch (Exception e){
            log.error("Exception occurred {}",e.getMessage());
        }
        ExcelHelper.createIncentiveTracker(incentiveDetailForPanel);
        return incentiveDetailForPanel;
    }


}
