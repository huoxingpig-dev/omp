package com.gis.omp.account.dto.modelDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysAuthorityDto extends BaseModelDto {
    private long parentId;
    private String name; // name
    private String desc; // description
}
