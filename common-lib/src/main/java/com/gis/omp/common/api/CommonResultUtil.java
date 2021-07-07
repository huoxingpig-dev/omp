package com.gis.omp.common.api;


/**
 * deprecated 暂时废弃
 * 通用返回对象(最外层对象)工具
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public class CommonResultUtil {

	/**
	 * 成功返回结果
	 *
	 * @param data 获取的数据
	 */
	public static <T> CommonResult<T> success(T data) {
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
	}

	/**
	 * 成功返回结果
	 *
	 * @param data 获取的数据
	 * @param  message 提示信息
	 */
	public static <T> CommonResult<T> success(T data, String message) {
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
	}

	/**
	 * 失败返回结果
	 * @param errorCode 错误码
	 */
	public static <T> CommonResult<T> failed(ResultCode errorCode) {
		return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
	}

	/**
	 * 失败返回结果
	 * @param errorCode 错误码
	 * @param message 错误信息
	 */
	public static <T> CommonResult<T> failed(ResultCode errorCode, String message) {
		return new CommonResult<T>(errorCode.getCode(), message, null);
	}

	/**
	 * 失败返回结果
	 * @param errorCode 错误码
	 * @param message 错误信息
	 */
	public static <T> CommonResult<T> failed(Integer errorCode, String message) {
		return new CommonResult<T>(errorCode, message, null);
	}

	/**
	 * 失败返回结果
	 * @param message 提示信息
	 */
	public static <T> CommonResult<T> failed(String message) {
		return new CommonResult<T>(ResultCode.FAILURE.getCode(), message, null);
	}

	/**
	 * 失败返回结果
	 */
	public static <T> CommonResult<T> failed() {
		return failed(ResultCode.FAILURE);
	}

	/**
	 * 参数验证失败返回结果
	 */
	public static <T> CommonResult<T> validateFailed() {
		return failed(ResultCode.PARAM_VALID_ERROR);
	}

	/**
	 * 参数验证失败返回结果
	 * @param message 提示信息
	 */
	public static <T> CommonResult<T> validateFailed(String message) {
		return new CommonResult<T>(ResultCode.PARAM_VALID_ERROR.getCode(), message, null);
	}

	/**
	 * 未登录返回结果
	 */
	public static <T> CommonResult<T> unauthorized(T data) {
		return new CommonResult<T>(ResultCode.UN_AUTHORIZED.getCode(), ResultCode.UN_AUTHORIZED.getMessage(), data);
	}

	/**
	 * 未授权返回结果
	 */
	public static <T> CommonResult<T> forbidden(T data) {
		return new CommonResult<T>(ResultCode.NO_PERMISSIONS.getCode(), ResultCode.NO_PERMISSIONS.getMessage(), data);
	}



}
