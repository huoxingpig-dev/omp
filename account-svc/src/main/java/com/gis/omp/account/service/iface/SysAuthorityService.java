package com.gis.omp.account.service.iface;

import com.gis.omp.account.dto.modelDto.SysAuthorityDto;
import com.gis.omp.account.dto.request.CreateAuthorityRequest;
import com.gis.omp.account.service.base.CurdService;

import java.util.List;

public interface SysAuthorityService extends CurdService<SysAuthorityDto> {
    /**
     *  创建权限
     * @param record 创建权限信息
     * @return 0:suc, -1:failed
     */
    int createAuthority(CreateAuthorityRequest record);

    /**
     *  更新权限信息
     * @param record 更新权限信息
     * @return 0:suc, -1:failed
     */
    int updateAuthority(SysAuthorityDto record);

    /**
     * 保存权限列表, 创建/更新
     * @param userList 权限实体列表
     * @return 权限数据列表
     */
    List<SysAuthorityDto> save(List<SysAuthorityDto> userList);

    /**
     * 通过权限名查找权限
     * @param name 权限名
     * @return 权限数据
     */
    SysAuthorityDto getByName(String name);

    /**
     * 通过权限删除权限
     * @param name 权限名
     */
    int deleteAuthorityByName(String name);

    /**
     * 权限名是否重复
     * @param authorityDto 权限对象
     * @return 是否重复
     */
    Boolean duplicateByUserName(SysAuthorityDto authorityDto);


    /**
     * 查询所有权限信息
     * @return 权限列表
     */
    List<SysAuthorityDto> listAll();
}
