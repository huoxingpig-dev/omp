package com.gis.omp.account.model;

import com.gis.omp.account.constants.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

/**
 *  user info dmo, extend from BaseModel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sys_user")
@EntityListeners(AuditingEntityListener.class)
public class SysUser extends BaseModel{
    private String username;
    private String nickName;
    private Byte sex;
    private String avatar;
    private String email;
    private String mobile;
    private Byte status = StatusEnum.OK.getCode();
    private Long deptId;		// dept ID relation
    private String password;  // password hash
}
