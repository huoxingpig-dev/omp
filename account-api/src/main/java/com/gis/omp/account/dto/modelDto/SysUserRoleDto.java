package com.gis.omp.account.dto.modelDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserRoleDto extends BaseModelDto {
    private Long userId;
    private Long roleId;
}
