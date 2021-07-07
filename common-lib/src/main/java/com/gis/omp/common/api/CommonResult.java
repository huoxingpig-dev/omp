package com.gis.omp.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回对象(最外层对象), 适合对WEB提供
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResult<T> {
    private long code;			// 返回结果状态值
    private String message;		// 返回提示信息
    private T data;				// 返回结果体
}
