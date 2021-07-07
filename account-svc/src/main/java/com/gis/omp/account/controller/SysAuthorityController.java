package com.gis.omp.account.controller;

import com.gis.omp.account.dto.modelDto.SysAuthorityDto;
import com.gis.omp.account.dto.request.CreateAuthorityRequest;
import com.gis.omp.account.service.iface.SysAuthorityService;
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

/*
 *  权限管理controller
 */
@CrossOrigin
@Controller
@Api(tags = "权限管理", value = "SysUserController",
        description = "支持权限信息的增加、查询、删除")
@RequestMapping("/authority")
@Validated
public class SysAuthorityController {

    @Autowired
    private SysAuthorityService sysAuthorityService;

    @ApiOperation(value = "创建权限", notes = "创建权限")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse create(@RequestBody CreateAuthorityRequest record) {
        sysAuthorityService.createAuthority(record);
        return BaseResponseUtil.success();
    }

    @ApiOperation(value = "更新权限", notes = "更新权限")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse update(@RequestBody SysAuthorityDto record) {
        sysAuthorityService.updateAuthority(record);
        return BaseResponseUtil.success();
    }

    ////@PreAuthorize("hasAuthority('sys:user:add') AND hasAuthority('sys:user:edit')")
    @ApiOperation(value = "添加/更新权限", notes = "添加/更新权限")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse save(@RequestBody SysAuthorityDto record) {
        sysAuthorityService.save(record);
        return BaseResponseUtil.success();
    }

    ////@PreAuthorize("hasAuthority('sys:user:delete')")
    @ApiOperation(value = "删除权限", notes = "删除权限")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse delete(@RequestBody List<SysAuthorityDto> records) {
        sysAuthorityService.delete(records);
        return BaseResponseUtil.success();
    }

    @ApiOperation(value = "根据名称删除权限", notes = "根据名称删除权限")
    @RequestMapping(value = "/deleteByUserName", method = RequestMethod.DELETE)
    @ResponseBody
    public BaseResponse delete(@RequestParam String name) {
        sysAuthorityService.deleteAuthorityByName(name);
        return BaseResponseUtil.success();
    }

    //@PreAuthorize("hasAuthority('sys:user:view')")
    @ApiOperation(value = "通过名称查找权限", notes = "通过名称查找权限")
    @RequestMapping(value = "/findByUsername", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult findByUserName(@RequestParam String name) {
        SysAuthorityDto sysAuthorityDto = sysAuthorityService.getByName(name);

        return CommonResultUtil.success(sysAuthorityDto);
    }

    //@PreAuthorize("hasAuthority('sys:user:view')")
    @ApiOperation(value = "获取权限列表",
            notes = "获取全部权限列表")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SysAuthorityDto>> getAll() {
        List<SysAuthorityDto> list = sysAuthorityService.listAll();
        return CommonResultUtil.success(list);
    }
}
