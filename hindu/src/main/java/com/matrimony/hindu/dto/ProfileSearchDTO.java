package com.matrimony.hindu.dto;

import lombok.Data;

@Data
public class ProfileSearchDTO  {
    private Integer minAge;
    private Integer maxAge;
    private String city;
}