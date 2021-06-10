package com.gis.omp.account.repo;

import com.gis.omp.account.model.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色管理DAO接口及其实现
 * 	使用JPA的DAO层用命名法，免去SQL编写。
 *
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysRoleDAO extends JpaRepository<SysRole, Long> {

	// 根据角色名称查询角色信息
	//@Query("select role from SysRoleDO role where role.delFlag=1 and (role.name like %?1%)")
	@Query("select role from SysRole role where (role.name like %?1%)")
    Page<SysRole> listByName(String name, PageRequest pageRequest);

	/**
	 * 根据角色名称查询角色数据
	 * @param name 角色名称
	 * @return 角色信息
	 */
	public List<SysRole> findByName(String name);

	/**
	 * 根据角色标识查询角色数据
	 * @param flag 角色标识
	 * @return 角色信息
	 */
	public List<SysRole> findByFlag(String flag);

	/**
	 * 根据角色名称查询角色数据，且排查指定ID的角色
	 * @param name 角色名称
	 * @param id 角色ID
	 * @return 角色信息
	 */
	public List<SysRole> findByNameAndIdNot(String name, Long id);

	/**
	 * 根据角色标识查询角色数据，且排查指定ID的角色
	 * @param flag 角色标识
	 * @param id 角色ID
	 * @return 角色信息
	 */
	public List<SysRole> findByFlagAndIdNot(String flag, Long id);

	/**
	 * 取消角色与用户之间的关系
	 * @param ids 角色ID集合
	 * @return 影响结果
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM sys_user_role WHERE role_id in ?1", nativeQuery = true)
	public Integer cancelUserJoin(List<Long> ids);

	/**
	 * 取消角色与菜单之间的关系
	 * @param ids 角色ID集合
	 * @return 影响结果
	 */
//	@Modifying
//	@Transactional
//	@Query(value = "DELETE FROM sys_role_menu WHERE role_id in ?1", nativeQuery = true)
//	public Integer cancelMenuJoin(List<Long> ids);


	/**
	 * 查找相应状态的角色
	 * @param sort 排序对象
	 * @param delFlag 状态
	 * @return 角色信息列表
	 */
	////public List<SysRole> findAllByDelFlag(Sort sort, Byte delFlag);


	/**
	 * 获取指定的用户是否存在角色，以判断指定的用户是否存在角色
	 * @param id 用户ID
	 * @param status 状态
	 * @return 角色信息列表
	 */
//	@Query(
//		value = "select r from sys_role r, sys_user_role ur where ur.user_id=:id and r.id=ur.role_id and r.del_flag=:status",
//		nativeQuery = true)
//	@Query(
//			value = "select r from sys_role r, sys_user_role ur where ur.user_id=:id and r.id=ur.role_id",
//			nativeQuery = true)
//	public List<SysRole> existsByUserIdAndStatus(Long id, Byte status);

	/**
	 * 查找多个角色
	 * @param ids 角色ID列表
	 * @return 角色列表
	 */
	public List<SysRole> findByIdIn(List<Long> ids);

}
