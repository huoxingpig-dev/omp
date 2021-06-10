package com.gis.omp.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMenuRequest {
    private Long parentId;
    private String name;
    private String url;  // menu url: 1: normal page /sys/user; 2:start with https; 3: iframe embeded page
    private String perms; // authorities
    private Integer type; // 0: directory; 1: menu; 2: button
    private String icon; // menu icon
}
