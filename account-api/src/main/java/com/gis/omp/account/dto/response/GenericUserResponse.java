package com.gis.omp.account.dto.response;

import com.gis.omp.account.dto.modelDto.SysUserDto;
import com.gis.omp.common.api.BaseResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GenericUserResponse extends BaseResponse {
    private SysUserDto data;
}
