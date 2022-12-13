package com.amk.libra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDetails {

    String candidateEmailId;
    String offerDate;
    String offerStatus;
    String offerPipelineStatus;
    String updatedOn;

}
