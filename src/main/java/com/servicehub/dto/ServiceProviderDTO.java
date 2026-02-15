package com.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String category;
    private Integer experience;
    private String address;
}