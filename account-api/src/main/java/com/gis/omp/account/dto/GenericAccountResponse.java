package com.gis.omp.account.dto;

import com.gis.omp.common.api.CommonResult;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GenericAccountResponse extends CommonResult {
    private AccountDto account;
}
