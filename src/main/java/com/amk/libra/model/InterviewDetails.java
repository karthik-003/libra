package com.amk.libra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDetails {

    Candidate candidateDetails;
    List<InterviewRound>interviewRounds;
    OfferDetails offerDetails;
}
