package com.gis.omp.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

/*
 * role dmo, role related to menu, dept, privilege
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sys_role")
@EntityListeners(AuditingEntityListener.class)
public class SysRole extends BaseModel {
    private long parentId; // later use
    private String name;
    private String flag; // later use
    private String remark;
}
