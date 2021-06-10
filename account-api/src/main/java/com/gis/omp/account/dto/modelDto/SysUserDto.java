package com.gis.omp.account.dto.modelDto;

import com.gis.omp.account.constants.StatusEnum;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserDto extends BaseModelDto {
    @NotNull
    private String username;
    private String nickName;
    private String password;  // password hash
    private Byte sex;
    private String avatar;
    //@Email(message = "Invalid email")
    private String email;
    //@PhoneNumber
    private String mobile;
    private Byte status = StatusEnum.OK.getCode();
    private Long deptId;		// dept ID relation

    // additional information
    private String deptName;	// 机构名称
    private String roleNames;	// 角色名称
    private List<SysUserRoleDto> userRoles = new ArrayList<>();
}
