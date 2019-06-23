package com.lisak.services.retrieve;

import com.lisak.configuration.OracleGatewayConfig;
import com.lisak.oraclegateway.OracleGatewayAPI;
import com.lisak.swagger.model.CoverLine;
import com.lisak.swagger.model.PolicyCover;
//import com.ku.api.oracle.gateway.swagger.model.OracleGatewayResponse;
//import com.ku.api.oracle.gateway.swagger.model.Request;
//import com.ku.api.oracle.gateway.swagger.model.Signature;
import com.lisak.util.JsonMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class RetrieveService  implements RetrieveServiceClient {

    @NonNull
    private final OracleGatewayAPI oracleGatewayAPI;

    @NonNull
    private final OracleGatewayConfig oracleGatewayConfig;

    static final String ACTIVE_ONLY = "active_only";

    @Override
    public PolicyCover retrieve(final Long policyNumber, final String version, final String showAgreementLines) {
//        final PolicyCover policyCover = getPolicyCover(policyNumber, version);

//        activeAgreementLines(showAgreementLines, policyCover);
//        getAnnualPremiumOfAllCoverLines(policyCover);

//        return policyCover.currentPolicyYearStart(policyCover.getYearStartDate())
//                .currentPolicyYearEnd(policyCover.getRenewalDate().minusDays(1));

//        return JsonMapper.getResource("policy-cover-normal.json", PolicyCover.class);
        return new PolicyCover();


//    private void getAnnualPremiumOfAllCoverLines(final PolicyCover policyCover) {
//        final BigDecimal annualPremium = policyCover.getCoverLines().stream()
//                .filter(coverLine -> coverLine.getCancelCode() == 0)
//                .map(CoverLine::getTariffPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        policyCover.setAnnualPremium(annualPremium);
//    }

//    private void activeAgreementLines(final String showAgreementLines, final PolicyCover policyCover) {
//        if (showAgreementLines.equalsIgnoreCase(ACTIVE_ONLY)) {
//            policyCover.getCoverLines().removeIf((CoverLine coverLine) -> coverLine.getCancelCode() != 0);
//        }
//    }

//    private PolicyCover getPolicyCover(final Long policyNumber, final String version) {
//        final Request request = new Request()
//                .signature(new Signature().functionName(oracleGatewayConfig.getRetrieveFunctionName()))
//                .payloadIn(JsonMapper.toJson(new PolicyNumberRequest().policyNo(policyNumber).version(version)));
//
//        final OracleGatewayResponse response = oracleGatewayAPI.sendRequest(request);
//
//        final PolicyCover policyCover = fromJson(extractPayload(response), PolicyCover.class);
//
//        if (isEmpty(policyCover.getCoverLines())) {
//            throw new OracleGatewayException(ORACLE_GATEWAY_EXCEPTION.getCode(), ORACLE_GATEWAY_EXCEPTION.getMessage());
//        }

//        return new PolicyCover();
    }
}