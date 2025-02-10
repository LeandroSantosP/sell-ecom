package com.leandrosps.demo_sell_ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Address {
    private String state;
    private String city;
    private String code;
}
