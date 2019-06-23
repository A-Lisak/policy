package com.lisak.services.accept;

import com.lisak.swagger.model.AcceptInitiateMtaRequest;

/**
 * Interface for calling an external service.
 */
public interface AcceptServiceClient {
    AcceptResponse acceptInitiate(AcceptInitiateMtaRequest acceptInitiateMtaRequest);
}
