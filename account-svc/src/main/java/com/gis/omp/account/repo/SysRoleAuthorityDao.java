package com.gis.omp.account.repo;

import com.gis.omp.account.model.SysRoleAuthorityRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysRoleAuthorityDao extends JpaRepository<SysRoleAuthorityRelation, Long> {
    /**
     * 通过角色编号查找角色权限集合(使用SQL形式)
     * @param roleId 角色ID
     * @return 角色权限信息列表
     */
    @Query("select rm from SysRoleAuthorityRelation rm where rm.roleId=:roleId")
    List<SysRoleAuthorityRelation> findRoleAuthoritie(Long roleId);

    /**
     * 根据角色编号查找角色权限信息(使用JPA DAO命名法)
     * @param roleId 角色ID
     * @return 角色权限信息列表
     */
    List<SysRoleAuthorityRelation> findByRoleId(Long roleId);

    /**
     * 根据角色编号删除角色权限
     * @param roleId 角色ID
     * @return 影响结果
     */
    @Transactional
    @Query(value = "DELETE FROM sys_role_authority rm WHERE rm.role_id=?1", nativeQuery = true)
    @Modifying
    int deleteByRoleId(Long roleId);


    /**
     * 根据菜单编号查找角色权限信息(使用JPA DAO命名法)
     * @param authorityId 权限ID
     * @return 角色权限信息列表
     */
    List<SysRoleAuthorityRelation> findByAuthorityId(Long authorityId);
}
