package com.gis.omp.common.api;

public class BaseResponseUtil {
    /**
     * 成功
     * @return
     */
    public static BaseResponse success() {
        return new BaseResponse(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 失败
     * @return
     */
    public static BaseResponse failed() {
        return new BaseResponse(ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMessage());
    }
}
