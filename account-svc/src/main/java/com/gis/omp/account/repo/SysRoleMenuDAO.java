package com.gis.omp.account.repo;

import com.gis.omp.account.model.SysRoleMenuRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色和菜单管理DAO接口及其实现
 * 	使用JPA的DAO层用命名法，免去SQL编写。
 *
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysRoleMenuDAO extends JpaRepository<SysRoleMenuRelation, Long> {

	/**
	 * 通过角色编号查找角色菜单集合(使用SQL形式)
	 * @param roleId 角色ID
	 * @return 角色菜单信息列表
	 */
	@Query("select rm from SysRoleMenuRelation rm where rm.roleId=:roleId")
	List<SysRoleMenuRelation> findRoleMenus(Long roleId);

	/**
	 * 根据角色编号查找角色菜单信息(使用JPA DAO命名法)
	 * @param roleId 角色ID
	 * @return 角色菜单信息列表
	 */
	List<SysRoleMenuRelation> findByRoleId(Long roleId);

	/**
	 * 根据角色编号删除角色菜单
	 * @param roleId 角色ID
	 * @return 影响结果
	 */
	@Transactional
	@Query(value = "DELETE FROM sys_role_menu rm WHERE rm.role_id=?1", nativeQuery = true)
	@Modifying
	int deleteByRoleId(Long roleId);


	/**
	 * 根据菜单编号查找角色菜单信息(使用JPA DAO命名法)
	 * @param menuId 菜单ID
	 * @return 角色菜单信息列表
	 */
	List<SysRoleMenuRelation> findByMenuId(Long menuId);

}
