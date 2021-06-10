package com.gis.omp.account.controller;

import com.alibaba.fastjson.JSON;
import com.gis.omp.account.dto.modelDto.SysMenuDto;
import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;
import com.gis.omp.account.service.iface.SysMenuService;
import com.gis.omp.common.api.CommonResult;
import com.gis.omp.common.api.CommonResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理Controller
 * @author Hongyu Jiang
 * @since  May. 13 2020
 */
@CrossOrigin
@Controller
@Api(tags = "菜单管理", value = "SysMenuController",
	description = "支持菜单信息的增加、查询、删除")
@RequestMapping("/menu")
@Validated
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;

	//@PreAuthorize("hasAuthority('sys:menu:add') AND hasAuthority('sys:menu:edit')")
	@ApiOperation(value = "添加菜单",
		notes = "添加菜单")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult save(@RequestBody SysMenuDto record) {
		int flag = sysMenuService.save(record);
		if (flag == 0) {
			return CommonResultUtil.success(flag);
		}
		return CommonResultUtil.failed();
	}


	//@PreAuthorize("hasAuthority('sys:menu:delete')")
	@ApiOperation(value = "删除菜单",
		notes = "删除菜单")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult delete(@RequestBody List<SysMenuDto> records) {
		List<SysMenuDto> toDeletes = new ArrayList<>();

		/*
		 * 1-- 过滤
		 * 		非超级管理员，禁止删除角色
		 * 		菜单被角色引用，不予删除
		 * 		含有被占用子菜单，不予删除
		 */
		// 非超级管理员，禁止删除角色
		// TODO.. 补充删除机制

//		for (SysMenuDto record : records) {
//			Long menuId = record.getId();
//			// 当前菜单被占用，不予删除
//			List<SysRoleMenuDO> curMenus = sysMenuService.findByMenuId(menuId);
//			if (curMenus != null && curMenus.size() != 0) {
//				return CommonResultUtil.failed(ResultCode.MENU_USED.getCode(),
//					ResultCode.MENU_USED.getMessage());
//			}
//			toDeletes.add(record);
//
//			// 获取对应所有子菜单
//			List<SysMenuDO> menus = new ArrayList<>();
//			getAllChildren(menus, record);
//			// 子菜单有被占用的，不予删除
//			if (menus != null && menus.size() != 0) {
//				for (SysMenuDO menu : menus) {
//					List<SysRoleMenuDO> curMenuses = sysMenuService.findByMenuId(menu.getId());
//					if (curMenuses !=null && curMenuses.size() != 0) {
//						return CommonResultUtil.failed(ResultCode.MENU_EXIST_USEDCHILD.getCode(),
//							ResultCode.MENU_EXIST_USEDCHILD.getMessage());
//					} else {
//						toDeletes.add(record);
//					}
//				}
//			}
//		}

		/*
		 * 2-- 执行删除
		 */
		int resultFlag = sysMenuService.delete(toDeletes);
		if (resultFlag == 0) {
			return CommonResultUtil.success(resultFlag);
		} else {
			return CommonResultUtil.failed();
		}
	}

	/**
	 * 获取指定菜单的所有子菜单
	 * @param menus 所有子菜单集合
	 * @param record 指定菜单对象
	 */
	private void getAllChildren(List<SysMenuDto> menus, SysMenuDto record) {
		List<SysMenuDto> childs = record.getChildren();
		if (childs != null) {
			for (int i=0, len=childs.size(); i<len; i++) {
				menus.add(childs.get(i));
				getAllChildren(menus, childs.get(i));
			}
		}
	}


	//@PreAuthorize("hasAuthority('sys:menu:view')")
	@ApiOperation(value = "获取菜单列表",
		notes = "分页获取菜单列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult<PageVO<SysMenuDto>> list(@RequestBody PageRequestVO pageRequest) {
		PageVO<SysMenuDto> curPage = sysMenuService.getPageList(pageRequest);
		System.out.println(JSON.toJSONString(curPage));

		return CommonResultUtil.success(curPage);
	}


	//@PreAuthorize("hasAuthority('sys:menu:view')")
	@ApiOperation(value = "获取单个菜单的详细信息",
		notes = "根据id获取菜单的详细信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult<SysMenuDto> getItem(@PathVariable Long id) {
		SysMenuDto menu = sysMenuService.getById(id);
		return CommonResultUtil.success(menu);
	}


	//@PreAuthorize("hasAuthority('sys:menu:view')")
	@ApiOperation(value = "获取菜单树",
		notes = "根据用户名获取菜单树")
	@RequestMapping(value = "/findNavTree", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult findNavTree(@RequestParam String userName) {
		// 1表示获取所有菜单，不包含按钮
		List<SysMenuDto> menus = sysMenuService.findTree(userName, 1);
		return CommonResultUtil.success(menus);
	}

	//@PreAuthorize("hasAuthority('sys:menu:view')")
	@ApiOperation(value = "获取菜单树",
		notes = "获取全部菜单树")
	@RequestMapping(value = "/findMenuTree", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult findMenuTree(HttpServletRequest request) {
		List<SysMenuDto> result = null;
		String name = request.getParameter("name");
		// 0表示获取所有菜单，包含按钮
		if (name == null || name.length() == 0) {
			result = sysMenuService.findTree(null, 0);
		} else {
			result = sysMenuService.findTreeByName(name, 0);
		}
		return CommonResultUtil.success(result);
	}


}
