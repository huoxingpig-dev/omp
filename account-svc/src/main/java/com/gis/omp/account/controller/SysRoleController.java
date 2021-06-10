package com.gis.omp.account.controller;

import com.alibaba.fastjson.JSON;
import com.gis.omp.account.constants.SysConstants;
import com.gis.omp.account.dto.request.CreateRoleRequest;
import com.gis.omp.account.dto.modelDto.SysMenuDto;
import com.gis.omp.account.dto.modelDto.SysRoleDto;
import com.gis.omp.account.dto.modelDto.SysRoleMenuDto;
import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;
import com.gis.omp.account.service.iface.SysRoleService;
import com.gis.omp.common.api.CommonResult;
import com.gis.omp.common.api.CommonResultUtil;
import com.gis.omp.common.api.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理Controller
 * @author Hongyu Jiang
 * @since  May. 13 2020
 * @modify leexiao 2021.6.8
 */
@CrossOrigin
@Controller
@Api(tags = "角色管理", value = "SysRoleController",
	description = "支持角色信息的增加、查询、删除等管理")
@RequestMapping("/role")
@Validated
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;

	@ApiOperation(value = "添加角色", notes = "添加角色")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult create(@RequestBody CreateRoleRequest record) {
		sysRoleService.createRole(record);
		return CommonResultUtil.success(0);
	}

	@ApiOperation(value = "更新角色", notes = "更新角色")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult update(@RequestBody SysRoleDto record) {
		sysRoleService.updateRole(record);
		return CommonResultUtil.success(0);
	}

	//@PreAuthorize("hasAuthority('sys:role:add') AND hasAuthority('sys:role:edit')")
	@ApiOperation(value = "添加/更新角色", notes = "添加/更新角色")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult save(@RequestBody SysRoleDto record) {
		sysRoleService.save(record);
		return CommonResultUtil.success(0);
	}


	//@PreAuthorize("hasAuthority('sys:role:delete')")
	@ApiOperation(value = "删除角色", notes = "删除角色")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult delete(@RequestBody List<SysRoleDto> records) {
		sysRoleService.delete(records);
		return CommonResultUtil.success(0);
	}


	//@PreAuthorize("hasAuthority('sys:role:view')")
	@ApiOperation(value = "获取角色列表",
		notes = "分页获取角色列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult<PageVO<SysRoleDto>> list(@RequestBody PageRequestVO pageRequest) {
		PageVO<SysRoleDto> curPage = sysRoleService.getPageList(pageRequest);
		System.out.println(JSON.toJSONString(curPage));

		return CommonResultUtil.success(curPage);
	}

	//@PreAuthorize("hasAuthority('sys:role:view')")
	@ApiOperation(value = "获取单个角色的详细信息",
		notes = "根据id获取角色的详细信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult<SysRoleDto> getItem(@PathVariable Long id) {
		SysRoleDto role = sysRoleService.getById(id);
		return CommonResultUtil.success(role);
	}


	//@PreAuthorize("hasAuthority('sys:role:view')")
	@ApiOperation(value = "获取角色列表",
		notes = "获取全部角色列表")
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult<List<SysRoleDto>> getAll() {
		List<SysRoleDto> list = sysRoleService.listAll();
		return CommonResultUtil.success(list);
	}


	//@PreAuthorize("hasAuthority('sys:role:view')")
	@ApiOperation(value = "通过角色编号获取角色菜单列表",
		notes = "通过角色编号获取角色菜单列表")
	@RequestMapping(value = "/findRoleMenus", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult<List<SysMenuDto>> findRoleMenus(@RequestParam Long roleId) {
		return CommonResultUtil.success(sysRoleService.findRoleMenus(roleId));
	}


	//@PreAuthorize("hasAuthority('sys:role:view')")
	@ApiOperation(value = "保存角色菜单",
		notes = "保存角色菜单")
	@RequestMapping(value = "/saveRoleMenus", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult<Integer> saveRoleMenus(@RequestBody List<SysRoleMenuDto> records) {

		for (SysRoleMenuDto record : records) {
			SysRoleDto sysRole = sysRoleService.getById(record.getRoleId());
			if ( SysConstants.ADMIN.equalsIgnoreCase(sysRole.getName())) {
				// 如果是超级管理员，不允许修改
				return CommonResultUtil.failed(ResultCode.NO_ADMINROLEMENU_STATUS.getCode(),
					ResultCode.NO_ADMINROLEMENU_STATUS.getMessage());
			}
		}
		int flag = sysRoleService.saveRoleMenus(records);
		if (flag == 0) {
			return CommonResultUtil.success(flag);
		} else {
			return CommonResultUtil.failed();
		}
	}
}

