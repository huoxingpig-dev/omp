package com.gis.omp.account.service.iface;

import com.gis.omp.account.dto.request.CreateRoleRequest;
import com.gis.omp.account.dto.modelDto.SysMenuDto;
import com.gis.omp.account.dto.modelDto.SysRoleDto;
import com.gis.omp.account.dto.modelDto.SysRoleMenuDto;
import com.gis.omp.account.service.base.CurdService;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 角色管理Service
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysRoleService extends CurdService<SysRoleDto> {

	/**
	 *  创建角色
	 * @param record 创建角色信息
	 * @return 0:suc, -1:failed
	 */
	int createRole(CreateRoleRequest record);

	/**
	 *  更新角色信息
	 * @param record 更新角色信息
	 * @return 0:suc, -1:failed
	 */
	int updateRole(SysRoleDto record);
	/**
	 * 查询所有角色信息
	 * @return 角色信息列表
	 */
	List<SysRoleDto> listAll();

	/**
	 * 根据名称查询角色信息
	 * @param name 角色名称
	 * @return 角色信息列表
	 */
	List<SysRoleDto> findByName(String name);

	/**
	 * 通过角色编号查询角色对应的菜单集合
	 * @param roleId 角色编号
	 * @return 菜单集合
	 */
	List<SysMenuDto> findRoleMenus(Long roleId);

	/**
	 * 保存角色菜单
	 * @param records 角色菜单信息列表
	 * @return 是否保存成功
	 */
	int saveRoleMenus(List<SysRoleMenuDto> records);

	/**
	 * 判断指定的用户是否存在角色
	 * @param id 用户ID
	 * @return 是否存在角色
	 */
	Boolean existsUserOk(Long id);

	/**
	 * 获取角色列表数据
	 * @param sort 排序对象
	 * @return 角色列表
	 */
	List<SysRoleDto> getListBySortOk(Sort sort);

	/**
	 * 角色标识是否重复
	 * @param role 角色实体类
	 * @return 标识是否重复
	 */
	boolean duplicateByName(SysRoleDto role);




}
