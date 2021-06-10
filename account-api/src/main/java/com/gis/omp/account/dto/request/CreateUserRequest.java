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
public class CreateUserRequest {
    @NotBlank
    private String username;	// 用户名

    private String nickName;	// 昵称

    private Byte sex;			// 性别(1：男, 2：女)

    private String password;	// 密码

    private Long deptId;		// dept ID relation

    private String email;		// 邮箱

    private String mobile;		// 手机号
}
