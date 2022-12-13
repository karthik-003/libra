package com.amk.libra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRound {

    String candidateEmailId;
    String round;
    String panelName;
    String panelEmpId;
    String account;
    String result;
    String date;
    String time;
    String remarks;
    String selectDate;
}
