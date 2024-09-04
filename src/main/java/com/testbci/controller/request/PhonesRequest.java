package com.testbci.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhonesRequest {

    private Long number;
    private Integer cityCode;
    private String countryCode;
}
