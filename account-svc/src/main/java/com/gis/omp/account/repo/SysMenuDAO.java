package com.gis.omp.account.repo;

import com.gis.omp.account.model.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单管理DAO接口及其实现
 * 	使用JPA的DAO层用命名法，免去SQL编写。
 *
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysMenuDAO extends JpaRepository<SysMenu, Long> {

	/**
	 * 查找多个菜单
	 * @param ids ID列表
	 * @return 菜单列表
	 */
	public List<SysMenu> findByIdIn(List<Long> ids);

	/**
	 * 查找响应状态的菜单
	 * @param sort 排序对象
	 * @param delFlag 数据状态
	 * @return 菜单列表
	 */
	////public List<SysMenu> findAllByDelFlag(Sort sort, Byte delFlag);

	/**
	 * 根据菜单名称查找菜单
	 * @param name 菜单名称
	 * @return 菜单信息列表
	 */
	List<SysMenu> findByName(String name);

	/**
	 * 根据菜单URL查询菜单
	 * @param url 菜单URL
	 * @return 菜单信息
	 */
	public SysMenu findByUrl(String url);

	/**
	 * 取消菜单与角色之间的关系
	 * @param id 菜单ID
	 * @return 影响行数
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM sys_role_menu WHERE menu_id = ?1", nativeQuery = true)
	public Integer cancelRoleJoin(Long id);

	/**
	 * 根据用户名查找菜单信息
	 * @param userName 用户名
	 * @return 菜单列表
	 */
	@Query(
		value = "select m.* from sys_menu m, sys_user u, sys_user_role ur, sys_role_menu rm " +
			"where u.username=:userName and u.id=ur.user_id and ur.role_id=rm.role_id and rm.menu_id=m.id",
		nativeQuery = true)
	List<SysMenu> findByUserName(String userName);

	/**
	 * 根据角色编号查找菜单信息
	 * @param roleId 角色ID
	 * @return 菜单列表
	 */
	@Query(value = "select m.* from sys_menu m, sys_role_menu rm where rm.role_id=:roleId and m.id=rm.menu_id",
		nativeQuery = true)
	List<SysMenu> findByRoleId(Long roleId);

}
