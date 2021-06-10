package com.gis.omp.account.service.iface;

import com.gis.omp.account.constants.StatusEnum;
import com.gis.omp.account.dto.request.CreateUserRequest;
import com.gis.omp.account.dto.modelDto.SysUserDto;
import com.gis.omp.account.dto.modelDto.SysUserRoleDto;
import com.gis.omp.account.service.base.CurdService;

import java.util.List;
import java.util.Set;

/**
 * 用户管理Service
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysUserService extends CurdService<SysUserDto> {
	/**
	 *  创建用户
	 * @param record 创建用户信息
	 * @return 0:suc, -1:failed
	 */
	int createUser(CreateUserRequest record);

	/**
	 *  更新用户信息
	 * @param record 更新用户信息
	 * @return 0:suc, -1:failed
	 */
	int updateUser(SysUserDto record);

	/**
	 * 保存用户列表, 创建/更新
	 * @param userList 用户实体列表
	 * @return 用户数据列表
	 */
	List<SysUserDto> save(List<SysUserDto> userList);

	/**
	 * 通过用户名查找用户
	 * @param userName 用户名
	 * @return 用户数据
	 */
	SysUserDto getByName(String userName);

	/**
	 * 通过用户名删除用户
	 * @param userName 用户名
	 */
	int deleteUserByName(String userName);

	/**
	 * 查找用户的菜单权限标识集合
	 * @param userName 用户名
	 * @return 菜单权限标识集合
	 */
	Set<String> getPermissions(String userName);

	/**
	 * 通过用户编号查找用户的角色集合
	 * @param userId 用户编号
	 * @return 用户数据列表
	 */
	List<SysUserRoleDto> findUserRoles(Long userId);

	/**
	 * 用户名是否重复
	 * @param userDO 用户对象
	 * @return 是否重复
	 */
	Boolean duplicateByUserName(SysUserDto userDO);

	/**
	 * 状态（启用，冻结，删除）/批量状态处理
	 * @param statusEnum 数据状态
	 * @param idList 数据ID列表
	 * @return 操作结果
	 */
	Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

	/**
	 * 查询所有用户信息
	 * @return 用户信息列表
	 */
	List<SysUserDto> listAll();

	/**
	 * 统计所有用户数量
	 * @return 用户数量
	 */
	Long countUserGross();
}
