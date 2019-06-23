package com.lisak.services.accept;

import com.lisak.swagger.model.AcceptInitiateMtaResponseItem;
import lombok.Data;


@Data
public class AcceptResult {

    private AcceptInitiateMtaResponseItem acceptInitiateMtaResponseItem;
    private Error error;

    public AcceptResult acceptInitiateMtaResponseItem(AcceptInitiateMtaResponseItem acceptInitiateMtaResponseItem) {
        this.acceptInitiateMtaResponseItem = acceptInitiateMtaResponseItem;
        return this;
    }

    public AcceptResult error(Error error) {
        this.error = error;
        return this;
    }
}