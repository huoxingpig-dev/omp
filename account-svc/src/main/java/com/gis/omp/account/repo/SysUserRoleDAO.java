package com.gis.omp.account.repo;

import com.gis.omp.account.model.SysUserRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户和角色管理DAO接口及其实现
 * 	使用JPA的DAO层用命名法，免去SQL编写。
 *
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysUserRoleDAO extends JpaRepository<SysUserRoleRelation, Long> {

	/**
	 * 根据用户编号查找用户角色信息
	 * @param userId 用户ID
	 * @return 用户角色信息列表
	 */
	public List<SysUserRoleRelation> findByUserId(Long userId);

	/**
	 * 根据用户编号删除用户角色
	 * @param userId 用户ID
	 * @return 影响结果
	 */
	@Transactional
	@Query(value = "DELETE FROM sys_user_role ur WHERE ur.user_id=?1", nativeQuery = true)
	@Modifying
	int deleteByUserId(Long userId);


	@Query(value = "select ur.* from sys_user_role ur where ur.user_id=?1 and ur.role_id=?2", nativeQuery = true)
	SysUserRoleRelation findByUserIdAndRoleId(Long userId, Long roleId);

}
