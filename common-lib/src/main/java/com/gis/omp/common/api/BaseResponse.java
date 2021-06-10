package com.gis.omp.common.api;

/*
 * deprecated
 */
/*@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse {
    protected String message;
    @Builder.Default
    protected ResultCode code = ResultCode.SUCCESS;

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }
}*/