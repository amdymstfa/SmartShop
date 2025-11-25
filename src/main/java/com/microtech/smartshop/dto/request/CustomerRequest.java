package com.microtech.smartshop.dto.request;

import com.microtech.smartshop.enums.CustomerTier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private CustomerTier tier;
}
