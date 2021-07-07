package com.gis.omp.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAuthorityRequest {
    private long parentId; // lateruse
    private String name; // name
    private String desc; // description
}
