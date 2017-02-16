package com.ice.admin.controller;

import com.ice.entity.system.SysResources;
import com.ice.entity.system.SysUser;
import com.ice.security.Constant;
import com.ice.security.service.SysResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 主页controller
 */
@Controller
@RequestMapping("index")
public class MainController {
    @Autowired
    private SysResourcesService sysResourcesService;

    @RequestMapping(value = "main" )
    public String gotoMain(HttpServletRequest request){
        SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
        List<SysResources> list = sysResourcesService.queryLeft(sysUser.getId());
        request.setAttribute("menuList",list);
        return "main/index";
    }

    @RequestMapping(value = "welcome" )
    public String welcome(){
        return "main/welcome";
    }
    
 /*   @RequestMapping("queryResource")
    @ResponseBody
    public List<SysResources> queryResource(HttpServletRequest request){
        SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
        List<SysResources> list = sysResourcesService.queryLeft(sysUser.getId());
        //List<SysResources> list = sysResourcesService.queryAllByUserId(sysUser.getId());
        return list;
    }*/
}
