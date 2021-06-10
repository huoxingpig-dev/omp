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
 * organization mdo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sys_dept")
@EntityListeners(AuditingEntityListener.class)
public class SysDept extends BaseModel{
    private String name;
    private Long parentId;  // top level: 0
    private Integer orderNum;
    private String address;
    private String website;
    private String contact;
    private String phoneNumber;
    private String email;
}
