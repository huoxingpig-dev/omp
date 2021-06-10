package com.gis.omp.account.dto.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  paging param
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageParam {
    private String name;
    private String value;
}
