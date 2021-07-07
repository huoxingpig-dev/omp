package com.gis.omp.account.repo;

import com.gis.omp.account.model.SysAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysAuthorityDAO extends JpaRepository<SysAuthority, Long>, JpaSpecificationExecutor<SysAuthority> {
    /**
     * 根据权限名查询权限数据
     * @param name 权限名
     * @return 权限数据
     */
    public SysAuthority findByName(String name);

    /**
     * 根据权限名查询用户数据,且排查指定ID的权限
     * @param name 权限名
     * @param id 排除的权限ID
     * @return 权限数据
     */
    public SysAuthority findByNameAndIdNot(String name, Long id);
}
