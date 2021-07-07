package com.gis.omp.account.controller;

import com.alibaba.fastjson.JSON;
import com.gis.omp.account.constants.StatusEnum;
import com.gis.omp.account.dto.request.CreateUserRequest;
import com.gis.omp.account.dto.modelDto.SysUserDto;
import com.gis.omp.account.dto.modelDto.SysUserRoleDto;
import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;
import com.gis.omp.account.dto.response.GenericUserResponse;
import com.gis.omp.account.service.iface.SysUserService;
import com.gis.omp.account.utils.StatusUtil;
import com.gis.omp.common.api.BaseResponse;
import com.gis.omp.common.api.BaseResponseUtil;
import com.gis.omp.common.api.CommonResult;
import com.gis.omp.common.api.CommonResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

//import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 用户管理Controller控制器
 * @author Hongyu Jiang
 * @since  May. 13 2020
 */
@CrossOrigin
@Controller
@Api(tags = "用户管理", value = "SysUserController",
	description = "支持用户信息的增加、查询、删除")
@RequestMapping("/user")
@Validated
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@ApiOperation(value = "创建用户", notes = "创建用户")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse create(@RequestBody CreateUserRequest record) {
		sysUserService.createUser(record);
		return BaseResponseUtil.success();
	}

	@ApiOperation(value = "更新用户", notes = "更新用户")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse update(@RequestBody SysUserDto record) {
		sysUserService.updateUser(record);
		return BaseResponseUtil.success();
	}


	////@PreAuthorize("hasAuthority('sys:user:add') AND hasAuthority('sys:user:edit')")
	@ApiOperation(value = "添加/更新用户", notes = "添加/更新用户")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse save(@RequestBody SysUserDto record) {
		sysUserService.save(record);
		return BaseResponseUtil.success();
	}

	////@PreAuthorize("hasAuthority('sys:user:delete')")
	@ApiOperation(value = "删除用户", notes = "删除用户")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse delete(@RequestBody List<SysUserDto> records) {
		sysUserService.delete(records);
		return BaseResponseUtil.success();
	}

	@ApiOperation(value = "根据姓名删除用户", notes = "根据姓名删除用户")
	@RequestMapping(value = "/deleteByUserName", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResponse delete(@RequestParam String userName) {
		sysUserService.deleteUserByName(userName);
		return BaseResponseUtil.success();
	}

	//@PreAuthorize("hasAuthority('sys:user:view')")
	@ApiOperation(value = "通过用户名查找用户", notes = "通过用户名查找用户")
	@RequestMapping(value = "/findByUsername", method = RequestMethod.GET)
	@ResponseBody
	public GenericUserResponse findByUserName(@RequestParam String userName) {
		SysUserDto user = sysUserService.getByName(userName);

		GenericUserResponse genericUserResponse = new GenericUserResponse(user);
		return genericUserResponse;
	}

	//@PreAuthorize("hasAuthority('sys:user:view')")
	@ApiOperation(value = "查找用户的菜单权限标识集合",
		notes = "通过用户名查找用户的菜单权限标识集合")
	@RequestMapping(value = "/findPermissions", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult<Set<String>> findPermissions(@RequestParam String userName) {
		return CommonResultUtil.success(sysUserService.getPermissions(userName));
	}

	//@PreAuthorize("hasAuthority('sys:user:view')")
	@ApiOperation(value = "查找用户的用户角色集合",
		notes = "通过用户编号查找用户的用户角色集合")
	@RequestMapping(value = "/findUserRoles", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult<List<SysUserRoleDto>> findUserRoles(@RequestParam Long userId) {
		return CommonResultUtil.success(sysUserService.findUserRoles(userId));
	}

	//@PreAuthorize("hasAuthority('sys:user:view')")
	@ApiOperation(value = "获取用户列表",
		notes = "分页获取用户列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult<PageVO<SysUserDto>> list(@RequestBody PageRequestVO pageRequest) {
		PageVO<SysUserDto> curPage = sysUserService.getPageList(pageRequest);
		System.out.println(JSON.toJSONString(curPage));

		return CommonResultUtil.success(curPage);
	}

	//@PreAuthorize("hasAuthority('sys:user:view')")
	@ApiOperation(value = "获取用户列表",
		notes = "获取全部用户列表")
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult<List<SysUserDto>> getAll() {
		List<SysUserDto> list = sysUserService.listAll();
		return CommonResultUtil.success(list);
	}

	//@PreAuthorize("hasAuthority('sys:user:view')")
	@ApiOperation(value = "获取单个用户的详细信息",
		notes = "根据id获取用户的详细信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public GenericUserResponse getItem(@PathVariable Long id) {
		SysUserDto user = sysUserService.getById(id);

		GenericUserResponse genericUserResponse = new GenericUserResponse(user);
		return genericUserResponse;
	}

	@ApiOperation(value = "设置用户状态",
		notes = "设置一条或者多条数据的状态")
	@RequestMapping(value = "/status/{param}", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult<String> updateStatus(@PathVariable String param,
									   @RequestParam(value="ids", required = false) List<Long> ids) {
		// 不能修改超级管理员状态
		/*if (ids.contains(SysConstants.ADMIN_ID)) {
			return CommonResultUtil.failed(ResultCode.NO_ADMIN_STATUS.getCode(),
				ResultCode.NO_ADMIN_STATUS.getMessage());
		}*/

		// 更新状态
		StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
		if (sysUserService.updateStatus(statusEnum, ids)) {
			return CommonResultUtil.success(statusEnum.getMessage() + "成功");
		} else {
			return CommonResultUtil.failed(statusEnum.getMessage() + "失败，请重新操作");
		}
	}


	public BaseResponse updatePassword(@RequestParam String password,
									   @RequestParam String newPassword) {
		// TODO.. 跟权限控制相关，参mango-demo user controller
		return null;
	}


	@ApiOperation(value = "统计用户总数量",
		notes = "统计用户总数量")
	@RequestMapping(value = "/countUserGross", method = RequestMethod.GET)
	@ResponseBody
	public CommonResult countSvcByState() {
		Long userGross = sysUserService.countUserGross();
		return CommonResultUtil.success(userGross);
	}

}
