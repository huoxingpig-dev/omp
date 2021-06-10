package com.gis.omp.account.dto.modelDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenuDto extends BaseModelDto {
    private Long roleId;
    private Long menuId;
}
