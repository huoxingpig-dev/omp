package com.gis.omp.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sys_role_authority")
@EntityListeners(AuditingEntityListener.class)
public class SysRoleAuthorityRelation extends BaseModel {
    private Long roleId;
    private Long authorityId;
}
