package com.gis.omp.account.utils;


import com.gis.omp.account.constants.StatusEnum;
import com.gis.omp.common.api.ResultCode;

/**
 * 数据状态工具类
 * @author Hongyu Jiang
 * @since  Jun. 22 2020
 */
public class StatusUtil {

	/**
	 * 获取状态StatusEnum对象
	 * @param param 状态字符参数，如DELETE/OK/FREEZED
	 * @return StatusEnum对象
	 */
	public static StatusEnum getStatusEnum(String param) {
		try {
			return StatusEnum.valueOf(param.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(ResultCode.STATUS_ERROR.getMessage());
		}
	}

	/**
	 * 获取审核状态AuditTypeEnum对象
	 * @param param 状态字符参数，如UNAUDITED/AUDITED_PASS/AUDITED_REFUSE
	 * @return AuditTypeEnum对象
	 */
//	public static AuditTypeEnum getAuditTypeEnum(String param) {
//		try {
//			return AuditTypeEnum.valueOf(param.toUpperCase());
//		} catch (IllegalArgumentException e) {
//			throw new IllegalArgumentException(ResultCode.STATUS_ERROR.getMessage());
//		}
//	}

	/**
	 * 获取状态ReleaseTypeEnum对象
	 * @param param 状态字符参数，如DELETE/OK/FREEZED
	 * @return ReleaseTypeEnum对象
	 */
//	public static ReleaseTypeEnum getReleaseTypeEnum(String param) {
//		try {
//			return ReleaseTypeEnum.valueOf(param.toUpperCase());
//		} catch (IllegalArgumentException e) {
//			throw new IllegalArgumentException(ResultCode.STATUS_ERROR.getMessage());
//		}
//	}

}
