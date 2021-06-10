package com.gis.omp.account.dto.modelDto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysMenuDto extends BaseModelDto {
    private Long parentId;
    private String name;
    private String url;  // menu url: 1: normal page /sys/user; 2:start with https; 3: iframe embeded page
    private String perms; // authorities
    private Integer type; // 0: directory; 1: menu; 2: button
    private String icon; // menu icon
    private Integer orderNum;
    private String external; // external web page tag

    // additional
    private String parentName;	// 父菜单名称

    private Integer level;		//

    private List<SysMenuDto> children;
}
