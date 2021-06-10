package com.gis.omp.account.dto.modelDto;

import com.gis.omp.account.constants.StatusEnum;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysRoleDto extends BaseModelDto {

    private long parentId; // later use

    private String name;	// 角色名称

    private String flag;	// 角色标识（未使用）

    private String remark;	// 备注信息

    private Byte delFlag = StatusEnum.OK.getCode();	// 是否删除(3：已删除, 1：正常)
}
