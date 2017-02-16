package com.ice.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.ice.entity.system.SysUser;
import com.ice.security.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ice.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.ice.entity.system.SysRole;
import com.ice.entity.system.query.SysRoleQuery;
import com.ice.entity.system.vo.ResourceTreeVo;
import com.ice.result.BaseResult;
import com.ice.result.PageResult;
import com.ice.security.Constant;
import com.ice.utils.PageUtils;
/**
 * @desc 系统角色
 * @author zhousg
 * @date 2016年8月18日上午11:48:40
 */
@Controller
@RequestMapping("sysRole")
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String listGroup(HttpServletRequest request) {

        return "system/sysRole/list";
    }
    
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public Object listRoleByGroup(HttpServletRequest request,SysRoleQuery sysRole) {
        String[] cols = new String[]{"role_name","role_desc","role_auth"};//数据库的列名 ,前提是对象和数据库字段名称一致
        PageUtils<SysRole> pageUtils = new PageUtils<>();
        SysRole role = pageUtils.sort(sysRole,request,"role_name","desc",new String[]{});
        List<SysRole> list = null;
        try{

           list = sysRoleService.queryListByPage(role);
        }catch (Exception e){
            e.printStackTrace();
        }
        PageResult result = new PageResult<>(new PageInfo<>(list));

        return result;
    }
    
    /**
     * @desc 新增角色
     * @author zhousg
     * @date 2016年8月19日上午10:24:39
     * @param request
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public Object add(HttpServletRequest request,@ModelAttribute SysRole sysRole) throws Exception{
    	SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);

    	sysRole.setRole_auth("ROLE_"+sysRole.getRole_auth());
    	sysRole.setCreate_id(sysUser.getId());
    	sysRole.setCreate_name(sysUser.getUsername());
        sysRole.setCreate_time(new Date());
    	sysRole.setEnable(Constant.TRUE);
        sysRole.setId(UUID.randomUUID().toString().replace("-",""));
    	return sysRoleService.add(sysRole);
    }
    
    /**
     * @desc 根据id获取详情
     * @author zhousg
     * @date 2016年8月19日下午2:41:08
     * @param id
     * @return
     */
    @RequestMapping(value="getById")
    @ResponseBody
    public Object getById(String id){
    	SysRole sysRole = new SysRole();
    	sysRole = sysRoleService.getById(id);
    	sysRole.setRole_auth(sysRole.getRole_auth().replace("ROLE_", ""));
    	BaseResult result = new BaseResult("success", sysRole);
    	return result;
    }
    /**
     * @desc 修改角色
     * @author zhousg
     * @date 2016年8月19日上午11:36:21
     * @param request
     * @param sysRole
     * @return
     * @throws Exception
     */
    @RequestMapping(value="update")
    @ResponseBody
    public Object update(HttpServletRequest request,@ModelAttribute SysRole sysRole) throws Exception{
    	SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
    	sysRole.setRole_auth("ROLE_"+sysRole.getRole_auth());
    	sysRole.setUpdate_id(sysUser.getId());
    	sysRole.setUpdate_name(sysUser.getUsername());
        sysRole.setUpdate_time(new Date());
    	return sysRoleService.update(sysRole);
    }
    /**
     * @desc 逻辑删除
     * @author zhousg
     * @date 2016年8月19日下午3:23:13
     * @param id
     * @return
     */
    @RequestMapping(value="delete")
    @ResponseBody
    public Object delete(String id){
    	return sysRoleService.delete(id);
    }
    /**
     * @desc 根据角色Id获取资源树
     * @author zhousg
     * @date 2016年8月22日下午4:37:09
     * @param roleId
     * @return
     */
    @RequestMapping(value="getResource")
    @ResponseBody
    public Object getResource(String roleId){
    	List<ResourceTreeVo> list= new ArrayList<>();
    	list = sysRoleService.getResource(roleId);
    	return new BaseResult("success",list);
    }
    
    
    /**
     * @desc 根据角色Id获取资源树
     * @author zhousg
     * @date 2016年8月22日下午4:37:09
     * @param roleId
     * @return
     */
    @RequestMapping(value="getResourceBtn")
    @ResponseBody
    public Object getResourceBtn(String partentId,String roleId){
    	return sysRoleService.getResourceByPartentId(partentId,roleId);
    }
    /**
     * @desc 保存权限
     * @author zhousg
     * @date 2016年8月23日下午3:58:03
     * @param roleId
     * @param resourceValue
     * @param resourceId
     * @param btnValue
     * @return
     */
    @RequestMapping(value="saveResource")
    @ResponseBody
    public Object saveResource(HttpServletRequest request,String roleId,String[] resourceValue,String resourceId,String[] btnValue) throws Exception{
    	return sysRoleService.saveResource(request,roleId, resourceValue, resourceId, btnValue);
    }
}
