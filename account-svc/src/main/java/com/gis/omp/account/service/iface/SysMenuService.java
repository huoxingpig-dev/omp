package com.gis.omp.account.service.iface;

import com.gis.omp.account.dto.modelDto.SysMenuDto;
import com.gis.omp.account.model.SysMenu;
import com.gis.omp.account.service.base.CurdService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 菜单管理Service
 * @author Hongyu Jiang
 * @since Apr. 27 2020
 */
public interface SysMenuService extends CurdService<SysMenuDto> {

	/**
	 * 查询菜单树，用户名为空则查询全部
	 * @param userName 用户名称
	 * @param menuType 获取菜单类型。0：获取所有菜单，包含按钮，1：获取所有菜单，不包含按钮
	 * @return 菜单列表
	 */
	List<SysMenuDto> findTree(String userName, int menuType);

	/**
	 * 根据菜单名称查询菜单树,不能获取到对应的子菜单
	 * @param name 菜单名称
	 * @param menuType 获取菜单类型。0：获取所有菜单，包含按钮，1：获取所有菜单，不包含按钮
	 * @return 菜单列表
	 */
	List<SysMenuDto> findTreeByName(String name, int menuType);

	/**
	 * 根据菜单名称查找菜单列表
	 * @param name 菜单名称
	 * @return 菜单列表
	 */
	List<SysMenuDto> findByName(String name);

	/**
	 * 根据用户名查找菜单列表
	 * @param userName 用户名
	 * @return 菜单列表
	 */
	List<SysMenuDto> findByUserName(String userName);

	/**
	 * 通过角色编号查找菜单列表
	 * @param roleId 角色编号
	 * @return 菜单列表
	 */
	List<SysMenuDto> findByRoleId(Long roleId);

	/**
	 * 通过菜单编号查找角色菜单列表
	 * @param menuId 菜单编号
	 * @return 角色菜单列表
	 */
	////List<SysRoleMenuDO> findByMenuId(Long menuId);

	/**
	 * 获取菜单列表数据
	 * @param example 查询实例
	 * @param sort 排序对象
	 * @return 菜单列表
	 */
	List<SysMenuDto> getListByExample(Example<SysMenu> example, Sort sort);

	/**
	 * 根据菜单对象的Example判断是否存在
	 * @param menu 菜单对象
	 * @return 菜单信息
	 */
	SysMenuDto getByMenuToExample(SysMenu menu);

	/**
	 * 根据菜单url查询菜单数据
	 * @param url 菜单url
	 * @return 菜单信息
	 */
	SysMenuDto getByUrl(String url);






}
