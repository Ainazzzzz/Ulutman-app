package com.ulutman.mapper;

import com.ulutman.model.dto.ConditionsRequest;
import com.ulutman.model.dto.ConditionsResponse;
import com.ulutman.model.entities.Conditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConditionsMapper {

    public Conditions mapToEntity(ConditionsRequest request) {
        Conditions conditions = new Conditions();
        conditions.setPricePerMonth(request.getPricePerMonth());
        conditions.setUtilitiesIncluded(request.getUtilitiesIncluded());
        conditions.setDeposit(request.getDeposit());
        conditions.setCommission(request.getCommission());
        conditions.setPrepayment(request.getPrepayment());
        conditions.setLeaseTerm(request.getLeaseTerm());
        conditions.setShowPhoneNumber(request.isShowPhoneNumber());
        conditions.setRealtor(request.getRealtor());
        conditions.setRealtorRating(request.getRealtorRating());
        return conditions;
    }

    public ConditionsResponse mapToResponse(Conditions conditions) {
        return ConditionsResponse.builder()
                .id(conditions.getId())
                .pricePerMonth(conditions.getPricePerMonth())
                .utilitiesIncluded(conditions.getUtilitiesIncluded())
                .deposit(conditions.getDeposit())
                .commission(conditions.getCommission())
                .prepayment(conditions.getPrepayment())
                .leaseTerm(conditions.getLeaseTerm())
                .showPhoneNumber(conditions.isShowPhoneNumber())
                .realtor(conditions.getRealtor())
                .realtorRating(conditions.getRealtorRating())
                .build();
    }
}
