package com.gis.omp.account.dto.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestVO {

    /**
     * 当前页码
     */
    private int pageNum = 1;

    /**
     * 每页数量
     */
    private int pageSize = 10;

    /**
     * 查询参数
     */
    private List<PageParam> params = new ArrayList<>();

    /**
     * 查询参数对象
     * @param name 参数名称
     * @return 返回值
     */
    public PageParam getParam(String name) {
        for(PageParam param:this.params) {
            if(name != null && name.equals(param.getName())) {
                return param;
            }
        }
        return null;
    }
    /**
     * 查询参数值
     * @param name 参数名称
     * @return 返回值
     */
    public String getParamValue(String name) {
        PageParam param = getParam(name);
        if(param != null) {
            return param.getValue();
        }
        return null;
    }
}
