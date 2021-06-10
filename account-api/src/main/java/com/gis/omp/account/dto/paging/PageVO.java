package com.gis.omp.account.dto.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据封装类
 * 	通用，适合未集成MyBatis的场情况。
 *
 * Created by JHy on 2020/4/3.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageVO<T> implements Serializable {

    private List<T> list;				// list result of this page(分页数据)
    private Integer pageNum;			// page number(当前页码)
    private Integer pageSize;			// result amount of this page(每页数量)
    private Integer totalPage;			// total page(页码总数)
    private Long totalRow;				// total row(记录总数)
}

