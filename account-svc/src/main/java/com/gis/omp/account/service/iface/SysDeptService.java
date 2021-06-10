package com.gis.omp.account.service.iface;

import com.gis.omp.account.dto.modelDto.SysDeptDto;
import com.gis.omp.account.service.base.CurdService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 机构管理Service
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysDeptService extends CurdService<SysDeptDto> {

	/**
	 * 查询机构树
	 * @return 返回机构列表
	 */
	List<SysDeptDto> findTree();

	/**
	 * 分页获取机构信息列表
	 * @param pageNum 页码
	 * @param pageSize 每页几条数据
	 * @return 机构信息列表
	 */
	Page<SysDeptDto> listSysDept(Integer pageNum, Integer pageSize);

	/**
	 * 查询机构树
	 * @return 返回机构列表
	 */
	List<SysDeptDto> findTreeByName(String name);

}
