package com.amk.libra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    String name;
    String emailId;
    String gender;
    String phone;
    String skill;
    String sourcingDate;
    String modifiedDate;
    String ageing;
    String flag;
    String source;
    String lastWorkingDay;
    String currentLocation;
    String preferredLocation;
    String currentCompany;
    String currentCTC;
    String expectedCTC;
    String noticePeriod;
    String experience;


}
