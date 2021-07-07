package com.gis.omp.account.controller;

import com.gis.omp.account.dto.AccountDto;
import com.gis.omp.account.dto.CreateAccountRequest;
import com.gis.omp.account.dto.GenericAccountResponse;
import com.gis.omp.account.dto.modelDto.SysUserDto;
import com.gis.omp.account.service.iface.SysUserService;
import com.gis.omp.common.api.CommonResult;
import com.gis.omp.common.api.CommonResultUtil;
import com.gis.omp.common.auth.Sessions;
import com.gis.omp.common.auth.constant.MenuAuthConstant;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/v1/account")
@Validated
public class AccountController {
    static final ILogger logger = SLoggerFactory.getLogger(AccountController.class);

    @Autowired
    private SysUserService sysUserService;

    @PostMapping(path = "/create")
    public GenericAccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        GenericAccountResponse response = new GenericAccountResponse();
        AccountDto c = new AccountDto();
        c.setName(request.getName());
        response.setAccount(c);
        //response.setCode(ResultCode.SUCCESS);

        return response;
    }

    @PostMapping(path = "/test")
    public CommonResult test(@RequestParam(value="name") String name) {
        return CommonResultUtil.success(name);
    }

    @PostMapping(path = "/login")
    public CommonResult login( @RequestParam(value="name") String name,
                               @RequestParam(value="password", required = false) String password,
                               // rememberMe=True means that the session is set for a month instead of a day
                               @RequestParam(value="remember-me", required = false) String rememberMe,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        // yanzhengmima
        SysUserDto user = sysUserService.getByName(name);
        String[] permission = new String[1];
        permission[0] = MenuAuthConstant.AUTH_MENU_SYS_USER_DELETE;

        Sessions.loginUser(user.getId().toString(), true, true, "123", "", permission, response);

        return CommonResultUtil.success(0);
    }
}
