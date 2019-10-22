package com.zgj.mps.controller;

import com.zgj.mps.generator.base.BaseController;
import com.zgj.mps.model.User;
import com.zgj.mps.model.UserEnum;
import com.zgj.mps.model.UserStatusEnum;
import com.zgj.mps.service.UserRoleService;
import com.zgj.mps.service.UserService;
import com.zgj.mps.tool.CommonUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.tool.ShiroSecurityUtil;
import com.zgj.mps.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user/*")
@Transactional
public class UserController extends BaseController<User, Long> {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserService getService() {
        return userService;
    }

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequiresPermissions("userList")
    @GetMapping(path = "/userListData")
    public Result<Map<String, Object>> userList(String name, String account, Short status,
                                                @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "descend") String sortOrder, @RequestParam(defaultValue = "createTime") String sortField) {
        User user = shiroSecurityUtil.getCurrentUser();
        return new ResultUtil().setData(userService.userList(user.getId(), user.getCode(), name, account, status, pageNo, pageSize, sortOrder, sortField));
    }

    @RequiresPermissions("user:add")
    @GetMapping(path = "/user")
    public Map<String, Object> user() {
        log.info("user");

        return CommonUtil.defaultResponse(1, "user");
    }

    @PostMapping(path = "/saveUser")
    public Object saveUser(HttpServletRequest request,
                           Long id, String avatar,
                           @RequestParam(name = "name", required = true) String name,
                           @RequestParam(name = "account", required = true)String account,
                           @RequestParam(name = "password", required = true)String password,
                           @RequestParam(name = "status", required = true)String status,
                           @RequestParam(name = "mobile", required = true)String mobile,
                           @RequestParam(name = "roleId", required = true)Long roleId) {
        User user = shiroSecurityUtil.getCurrentUser();
        log.info("user");
        try {
            User u = userService.saveUser(id,name,account,password,UserEnum.DEFAULT,status=="1"?UserStatusEnum.ENABLE:UserStatusEnum.DISABLE,mobile,avatar,user.getId(),CommonUtil.getClientIP(request));
            userRoleService.deleteByUserId(u.getId());
            userRoleService.savaUserRole(u.getId(),roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultUtil<>().setErrorMsg("保存失败");
        }
        return new ResultUtil<>().setSuccessMsg("保存成功");
    }

    @RequiresPermissions("user:del")
    @GetMapping(path = "/delUser")
    public Map<String, Object> delUser() {
        log.info("delUser");
        return CommonUtil.defaultResponse(1, "delUser");
    }


}
