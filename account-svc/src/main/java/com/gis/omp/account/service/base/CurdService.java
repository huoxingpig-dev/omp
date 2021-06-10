package com.gis.omp.account.service.base;

import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;

import java.util.List;

/**
 * 通用CURD接口
 * 	对通用增删改查接口的封装，统一定义了包含保存、删除、批量删除、根据ID查询和分页查询方法。
 * 	一般的业务Service接口会继承此接口，提供基础增删改查服务。
 *
 * @author Hongyu Jiang
 * @since Apr 27, 2020
 */
public interface CurdService<T> {

    /**
     * 保存操作,返回0为正常，其他为异常
     * @param record 对象
     * @return 返回值
     */
    int save(T record);

    /**
     * 删除操作,返回0为正常，其他为异常
     * @param record 对象
     * @return 返回值
     */
    int delete(T record);

    /**
     * 批量删除操作,返回0为正常，其他为异常
     * @param records 对象列表
     */
    int delete(List<T> records);

    /**
     * 根据ID查询
     * @param id ID
     * @return 返回值
     */
    T getById(Long id);

    /**
     * 分页查询
     * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
     * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
     * 影响服务层以上的分页接口，起到了解耦的作用
     * @param pageRequestVO 自定义，统一分页查询请求
     * @return PageVO 自定义，统一分页查询结果
     */
    PageVO<T> getPageList(PageRequestVO pageRequestVO);

}
