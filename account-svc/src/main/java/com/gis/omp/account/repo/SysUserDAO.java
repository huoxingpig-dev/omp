package com.gis.omp.account.repo;

import com.gis.omp.account.model.SysUser;
import com.gis.omp.account.repo.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 用户管理DAO接口及其实现
 * 	使用JPA的DAO层用命名法，免去SQL编写。
 *
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysUserDAO extends BaseRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

	/**
	 * 根据用户名查询用户数据
	 * @param username 用户名
	 * @return 用户数据
	 */
	public SysUser findByUsername(String username);


	/**
	 * 根据用户名查询用户数据,且排查指定ID的用户
	 * @param username 用户名
	 * @param id 排除的用户ID
	 * @return 用户数据
	 */
	public SysUser findByUsernameAndIdNot(String username, Long id);


	/**
	 * 查找多个相应部门的用户列表
	 * @param deptId 部门ID
	 * @return 用户列表
	 */
	public List<SysUser> findByDeptId(Long deptId);


	/**
	 * 删除多条数据
	 * @param ids ID列表
	 * @return 影响行数
	 */
	@Transactional
	public Integer deleteByIdIn(List<Long> ids);


	/**
	 * 批量更新数据状态
	 * @param status 状态
	 * @param id ID列表
	 * @return 更新数量
	 */
	@Modifying
	@Transactional
	@Query(
		value = "update sys_user set status = ?1  where id in ?2 ",
		nativeQuery = true)
	public Integer updateStatus(Byte status, List<Long> id);


	/**
	 * 统计所有用户数量
	 * @return 统计结果
	 */
	@Query("select count(user.id) from SysUser user")
	Long countUserGross();

}
