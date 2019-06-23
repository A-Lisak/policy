package com.lisak.services.retrieve;

import com.lisak.swagger.model.PolicyCover;

/**
 * Interface for calling an external service
 */
public interface RetrieveServiceClient {

    PolicyCover retrieve(Long policyNumber, String policyVersion, String showAgreementLines);
}