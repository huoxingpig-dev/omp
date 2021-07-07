package com.gis.omp.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * user permission mdo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sys_authority")
//@EntityListeners(AuditingEntityListener.class)
public class SysAuthority extends BaseModel {
    private long parentId;
    private String name;
    private String desc;
    private String remark;
}
