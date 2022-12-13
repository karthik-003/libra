package com.amk.libra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incentive {

    String interviewDate;
    String round;
    String candidateName;
    String candidateEmailId;
    String candidatePhone;
    String skill;
    String account;
    String result;
    String taSpoc;



}
