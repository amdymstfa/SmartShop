package com.microtech.smartshop.mapper;

import com.microtech.smartshop.dto.request.CreatePromoCodeRequest;
import com.microtech.smartshop.dto.response.PromoCodeResponse;
import com.microtech.smartshop.entity.PromoCode;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromoCodeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "used", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    PromoCode toEntity(CreatePromoCodeRequest request);

    @Mapping(target = "valid", expression = "java(promoCode.isValid())")
    PromoCodeResponse toResponse(PromoCode promoCode);
}
