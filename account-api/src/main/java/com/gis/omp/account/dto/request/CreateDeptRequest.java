package com.gis.omp.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDeptRequest {
    @NotBlank
    private String name;
    private Long parentId;  // top level: 0
    private String address;
    private String website;
    private String contact;
    private String phoneNumber;
    private String email;
}
