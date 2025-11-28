package com.microtech.smartshop.mapper;

import com.microtech.smartshop.dto.response.TierHistoryResponse;
import com.microtech.smartshop.entity.TierHistory;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TierHistoryMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(target = "promotion", expression = "java(tierHistory.isPromotion())")
    TierHistoryResponse toResponse(TierHistory tierHistory);

    List<TierHistoryResponse> toResponseList(List<TierHistory> tierHistories);
}