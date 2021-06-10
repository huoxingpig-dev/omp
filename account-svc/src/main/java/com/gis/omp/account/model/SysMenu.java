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
 * only for web menu control
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sys_menu")
@EntityListeners(AuditingEntityListener.class)
public class SysMenu extends BaseModel{
    private Long parentId;
    private String name;
    private String url;  // menu url: 1: normal page /sys/user; 2:start with https; 3: iframe embeded page
    private String perms; // authorities
    private Integer type; // 0: directory; 1: menu; 2: button
    private String icon; // menu icon
    private Integer orderNum;
    private String external; // external web page tag
}
