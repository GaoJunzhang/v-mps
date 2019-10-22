package com.zgj.mps.controller.vstu;

import com.zgj.mps.bean.ResourceNoUrlBean;
import com.zgj.mps.model.*;
import com.zgj.mps.service.*;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.tool.ShiroSecurityUtil;
import com.zgj.mps.vo.Result;
import com.zgj.mps.vo.router.VueRouter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(description = "外部接口接口")
@RequestMapping("/webapi")
public class ApiController {

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceTypeService iDeviceTypeService;

    @Autowired
    private IRtypeDtypeService iRtypeDtypeService;

    @Autowired
    private IResourceService iResourceService;

    @Autowired
    private RoleAuthService roleAuthService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/getUserRouters", method = RequestMethod.GET)
    @ApiOperation(value = "获取可分配权限")
    public ArrayList<VueRouter<Auth>> getUserRouters() {
//        User user = shiroSecurityUtil.getCurrentUser();
        return roleAuthService.getUserRouters((long)1);
    }

    @RequestMapping(value = "/deviceLoginCheck", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<List<Device>> deviceLoginCheck(@RequestParam(name = "deviceMac", required = true) String deviceMac) {
        List<Device> list = iDeviceService.deviceByDeviceMac(deviceMac);
        if (list.size() > 0) {
            return new ResultUtil<List<Device>>().setData(list);
        } else {
            list = iDeviceService.deviceByDeviceMac("");
            if (list.size() > 0) {
                Device device = list.get(0);
                device.setDeviceMac(deviceMac);
                iDeviceService.saveOrUpdate(device);
                list = new ArrayList<>(1);
                list.add(device);
                return new ResultUtil<List<Device>>().setData(list);
            }
            return new ResultUtil<List<Device>>().setErrorMsg("设备号无效，且无可用设备号");
        }

    }

    @RequestMapping(value = "/typeResourceByType", method = RequestMethod.GET)
    @ApiOperation(value = "/根据设备类型，获取资源列表")
    public Result<List<ResourceNoUrlBean>> typeResourceByType(@RequestParam(name = "deviceType", required = true) String deviceType) {
        DeviceType deviceType1 = iDeviceTypeService.findByName(deviceType);
        if (deviceType1 == null) {
            return new ResultUtil<List<ResourceNoUrlBean>>().setErrorMsg("无效设备类型");
        }
        List<RtypeDtype> rtypeDtypes = iRtypeDtypeService.findByDtypeName(deviceType1.getId());
        if (rtypeDtypes.size()<=0){
            return new ResultUtil<List<ResourceNoUrlBean>>().setData(null);
        }
        List<String> ids = new ArrayList<>(rtypeDtypes.size());
        for (RtypeDtype rtypeDtype:rtypeDtypes){
            ids.add(rtypeDtype.getRtId());
        }
        List<ResourceNoUrlBean> list = new ArrayList<>(rtypeDtypes.size());
        List<Resource> resources = iResourceService.findByTypeIds(ids);
        for (Resource resource :resources){
            ResourceNoUrlBean resourceNoUrlBean = new ResourceNoUrlBean();
            resourceNoUrlBean.setResourceId(resource.getId());
            resourceNoUrlBean.setContent(resource.getContent());
            resourceNoUrlBean.setLabId(resource.getLabId());
            resourceNoUrlBean.setProImg(resource.getProImg());
            resourceNoUrlBean.setProVideo(resource.getProVideo());
            resourceNoUrlBean.setTitle(resource.getTitle());
            resourceNoUrlBean.setResourceName(resource.getName());
            list.add(resourceNoUrlBean);
        }
/*        OrderResourceBean orderResourceBean = new OrderResourceBean();
        orderResourceBean.setTypeName(deviceType);
        List<OrderResourceBean> list = iResourceOrderService.orders(orderResourceBean);*/
        return new ResultUtil<List<ResourceNoUrlBean>>().setData(list);
    }

}
