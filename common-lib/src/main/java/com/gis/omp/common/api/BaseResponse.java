package com.gis.omp.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 采用这种方式是为了便于微服务之间的强类型调用， 结构体保留data字段是与前端调用风格统一
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse {
    @Builder.Default
    protected long code = ResultCode.SUCCESS.getCode();;     // 返回结果状态值

    protected String message;     // 返回提示信息

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS.getCode();
    }
}



/*public class BaseResponse {
    protected String message;
    @Builder.Default
    protected ResultCode code = ResultCode.SUCCESS;

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }
}*/