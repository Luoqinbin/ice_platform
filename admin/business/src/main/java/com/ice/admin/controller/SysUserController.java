package com.ice.admin.controller;

import com.ice.entity.system.SysUser;
import com.ice.interceptors.mySqlHelper.pagehelper.PageHelper;
import com.ice.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.ice.entity.BaseEntity;
import com.ice.entity.system.SysRole;
import com.ice.entity.system.SysUserRole;
import com.ice.result.BaseResult;
import com.ice.result.PageResult;
import com.ice.security.Constant;
import com.ice.security.service.SysResourcesService;
import com.ice.security.service.SysRoleService;
import com.ice.security.service.SysUserRoleService;
import com.ice.security.service.SysUserService;
import com.ice.utils.PasswordUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统用户
 */
@Controller
@RequestMapping("sysUser")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysResourcesService sysResourcesService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String listGroup(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId",menuId);
        return "system/sysUser/list";
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public Object listRoleByGroup(HttpServletRequest request, BaseEntity baseRequestParam, String username, String name, String menuId) {
        //定义列名
        String[] cols = {"username", "name", "last_login", "login_ip", "out_login_time", ""};
        //获取客户端需要那一列排序
        String orderColumn = request.getParameter("order[0][column]");
        orderColumn = cols[Integer.parseInt(orderColumn)];
        //获取排序方式 默认为asc
        String orderDir = request.getParameter("order[0][dir]");

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setName(name);

        if (!orderColumn.equals("")) {
            sysUser.setOrderColumn(orderColumn);
            sysUser.setOrderDir(orderDir);
        }else{
            sysUser.setOrderColumn("create_time");
            sysUser.setOrderDir("desc");
        }
        PageHelper.startPage(baseRequestParam.getStart(), baseRequestParam.getLength());
        List<SysUser> list = sysUserService.queryListByPage(sysUser);

        SysUser user = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
        String listBtn = sysResourcesService.queryBtnByRole(user.getRoleId(),menuId,2);
        for(SysUser s:list){
            String btn = listBtn.replaceAll("c.id", String.valueOf(s.getId()));
            s.setBttonOption(btn);
        }

        PageResult result = new PageResult<SysUser>(new PageInfo<SysUser>(list));


     /*   dataMap.put("data", list);
        dataMap.put("recordsTotal", count);
        dataMap.put("recordsFiltered", count);
        dataMap.put("draw", baseRequestParam.getDraw());*/

        return result;
    }

    @RequestMapping(value = "queryById")
    @ResponseBody
    public BaseResult queryById(String id) {
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isEmpty(id)) {
            baseResult.setMessage("用户ID 为空！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            return baseResult;
        } else {
            SysUser sysUser = sysUserService.queryById(id);
            SysUserRole s = new SysUserRole();
            s.setUser_id(id);
            List<SysUserRole> sysUserRole= sysUserRoleService.query(s);
            //查询角色
            Map<String, Object> map = new HashMap<>();
            if(sysUserRole!=null&&sysUserRole.size()>0) {
                SysRole role = sysRoleService.getById(sysUserRole.get(0).getRole_id());
                map.put("user", sysUser);
                map.put("role", role);
            }

            baseResult.setMessage("获取数据成功");
            baseResult.setCode(BaseResult.CODE_OK);
            baseResult.setData(map);
            return baseResult;
        }
    }
    @RequestMapping(value = "updateById")
    @ResponseBody
    public BaseResult updateById(String id,String name,String username,HttpServletRequest request){
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isEmpty(id)) {
            baseResult.setMessage("用户ID 为空！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            return baseResult;
        } else if(StringUtils.isEmpty(name)) {
            baseResult.setMessage("姓名为空！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            return baseResult;
        }else if(StringUtils.isEmpty(username)) {
            baseResult.setMessage("用户名为空！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            return baseResult;
        }
        SysUser sysUser = sysUserService.queryById(id);
        sysUser.setId(id);
        sysUser.setName(name);
        sysUser.setUsername(username);
        try {
            sysUserService.updateUser(sysUser);
            sysUserRoleService.delete(id);
            String roleId = request.getParameter("roleId");
            if(StringUtils.isNotEmpty(roleId)) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setId(UUID.randomUUID().toString().replace("-",""));
                sysUserRole.setUser_id(id);
                sysUserRole.setRole_id(roleId);
                sysUserRoleService.insert(sysUserRole);
            }
            baseResult.setMessage("成功！");
            baseResult.setCode(BaseResult.CODE_OK);
        } catch (Exception e) {
            e.printStackTrace();
            baseResult.setMessage("失败！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setData(e.getMessage());
        }
        return baseResult;
    }
    @RequestMapping(value = "deleteUser")
    @ResponseBody
    public BaseResult deleteUser(String id){
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isEmpty(id)) {
            baseResult.setMessage("用户ID 为空！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            return baseResult;
        }
        try {
            sysUserRoleService.delete(id);
            sysUserService.delete(id);
            baseResult.setMessage("成功！");
            baseResult.setCode(BaseResult.CODE_OK);
        } catch (Exception e) {
            e.printStackTrace();
            baseResult.setMessage("失败！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setData(e.getMessage());
        }
        return baseResult;
    }
    @RequestMapping(value = "restPwd")
    @ResponseBody
    public BaseResult restPwd(String id){
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isEmpty(id)) {
            baseResult.setMessage("用户ID 为空！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            return baseResult;
        }
        SysUser sysUser = sysUserService.queryById(id+"");
        sysUser.setId(id);
        PasswordUtil encoderMd5 = new PasswordUtil(sysUser.getUsername(), "MD5");
        String encode = encoderMd5.encode("666666");
        sysUser.setPassword(encode);
        try {
            sysUserService.updateUser(sysUser);
            baseResult.setMessage("成功！");
            baseResult.setCode(BaseResult.CODE_OK);
        } catch (Exception e) {
            e.printStackTrace();
            baseResult.setMessage("失败！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setData(e.getMessage());
        }
        return baseResult;
    }
    @RequestMapping(value = "add")
    @ResponseBody
    public BaseResult add(String username,String name,String password,String roleId){
        BaseResult baseResult = new BaseResult();
        SysUser s = sysUserService.loadUserName(username);
        if(s==null) {
            SysUser sysUser = new SysUser();
            sysUser.setName(name);
            sysUser.setUsername(username);
            PasswordUtil encoderMd5 = new PasswordUtil(username, "MD5");
            String encode = encoderMd5.encode(password);
            sysUser.setPassword(encode);
            sysUser.setAccountNonLocked(true);
            sysUser.setCredentialsNonExpired(true);
            sysUser.setAccountNonExpired(true);
            sysUser.setEnabled(true);
            sysUser.setCtDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            try {
                sysUser.setId(UUID.randomUUID().toString().replace("-", ""));
                sysUserService.insert(sysUser);
                String id = sysUser.getId();
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUser_id(id);
                sysUserRole.setRole_id(roleId);
                sysUserRole.setId(UUID.randomUUID().toString().replace("-", ""));
                sysUserRoleService.insert(sysUserRole);
                baseResult.setMessage("成功！");
                baseResult.setCode(BaseResult.CODE_OK);
            } catch (Exception e) {
                e.printStackTrace();
                baseResult.setMessage("失败！");
                baseResult.setCode(BaseResult.CODE_FAIL);
                baseResult.setData(e.getMessage());
            }
        }else{
            baseResult.setMessage("失败！");
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setData("添加失败！找到相同的用户名");
        }
        return baseResult;
    }
    @RequestMapping(value = "querySysRole")
    @ResponseBody
    public  List<Map<String,Object>> querySysRole(){
        List<Map<String,Object>> list = new ArrayList<>();
        for(SysRole sysRole:sysRoleService.queryAll()){
            Map<String,Object> map = new HashedMap();
            map.put("id",sysRole.getId());
            map.put("text",sysRole.getRole_name());
            list.add(map);
        }
        return list;
    }
    
    @RequestMapping(value = "queryAll")
    @ResponseBody
    public Object queryAll(){
    	return new BaseResult("success",sysUserService.queryAll());
    }
}
