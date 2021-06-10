package com.gis.omp.account.dto.modelDto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysDeptDto extends BaseModelDto {
    private String name;
    private Long parentId;  // top level: 0
    private Integer orderNum;
    private String address;
    private String website;
    private String contact;
    private String phoneNumber;
    private String email;

    //additional information
    private List<SysDeptDto> children;	// 子机构列表

    private String parentName;			// 父级机构名称

    private Integer level;				// 级别
}
