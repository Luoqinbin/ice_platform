package com.ice.admin.controller;

import com.ice.entity.system.SysResources;
import com.ice.entity.system.SysUser;
import com.ice.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.ice.interceptors.mySqlHelper.conditionHelper.query.ConditionConstant;
import com.ice.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import com.ice.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.ice.entity.system.vo.SysResourceVo;

import com.ice.entity.system.vo.SysResourceUpdateVo;
import com.ice.result.BaseResult;
import com.ice.result.PageResult;
import com.ice.security.Constant;


import com.ice.security.SecurityMetadataSourceService;
import com.ice.security.service.SysResourcesService;
import com.ice.utils.PageUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 资源
 */
@Controller
@RequestMapping("resource")
public class ResourceController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @Autowired
    private SysResourcesService sysResourcesService;

    /**
     * Resource string.
     *
     * @param request the request
     * @return the string
     */
    @RequestMapping(value = "resource", method = RequestMethod.GET)
    public String resource(HttpServletRequest request) {
        return "system/sysResource/list";
    }

    /**
     * 菜单用户所拥有的资源
     *
     * @param sysResourceVo the sys resource vo
     * @param request       the request
     * @return object
     */
    @RequestMapping(value = "queryResources")
    @ResponseBody
    public Object queryResources(SysResourceVo sysResourceVo, HttpServletRequest request) {
        PageResult<SysResources> result = new PageResult<SysResources>(new PageInfo<SysResources>(null));
        logger.info("菜单查询业务开始");
        try {
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }
            //菜询菜单资源
            SysResources sysResource = new SysResources();
            BeanUtils.copyProperties(sysResource, sysResourceVo);
            PageUtils<SysResources> pageUtils = new PageUtils<>();
            //解析排序条件植入分页
            SysResources sysResources = pageUtils.sort(sysResource, request, "create_time", "desc", null);
            sysResources.setCondition(Condition.build()
                                               .addWhere(ConditionConstant.PREFIX_NOT_EQ + "resourceParent", -1)
                                               .addOrder("createTime", OrderCondition.Direction.DESC)
                                     );
            List<SysResources> sysResourcesList = sysResourcesService.queryAllResource(sysResources);
            if (sysResourcesList == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("查询菜单失败!");
                return result;
            }
            result = new PageResult<SysResources>(new PageInfo<SysResources>(sysResourcesList));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        logger.info("菜单查询业务结束{}", result.toString());
        return result;
    }


    /**
     * 不分页查菜单名称和ID和菜单级别
     *
     * @param request the request
     * @return object
     */
    @RequestMapping(value = "queryResourceParent", method = RequestMethod.POST)
    @ResponseBody
    public Object queryResourceParent(HttpServletRequest request) {
        BaseResult result = new BaseResult();
        logger.info("不分页查菜单名称和ID和菜单级别");
        try {
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }
            //空的查所有
            SysResources sysResource = new SysResources();
            sysResource.setOrderColumn("create_time");
            sysResource.setOrderDir("desc");
            List<SysResources> sysResourcesList = sysResourcesService.queryAllResource(sysResource);
            if (sysResourcesList == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("查询菜单失败!");
                return result;
            }
            result.setData(sysResourcesList);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        logger.info("不分页查菜单名称和ID和菜单级别{}", result.toString());
        return result;
    }

    /**
     * 通过ID不分页查资源
     *
     * @param id      the id
     * @param request the request
     * @return object
     */
    @RequestMapping(value = "queryResourceForId", method = RequestMethod.POST)
    @ResponseBody
    public Object queryResourceForId(String id, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        logger.info("不分页查菜单名称和ID和菜单级别");
        try {
            if (id == null || id.isEmpty() || id.equals("")) {
                throw new IllegalArgumentException();
            }
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }
            //空的查所有
            SysResources sysResource = new SysResources();
            sysResource.setId(id);
            sysResource.setOrderColumn("create_time");
            sysResource.setOrderDir("desc");
            List<SysResources> sysResourcesList = sysResourcesService.queryAllResource(sysResource);

            if (sysResourcesList == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("查询菜单失败!");
                return result;
            }
            SysResources resources = new SysResources();
            if (sysResourcesList.size() == 1) {
                resources = sysResourcesList.get(0);
            }
            result.setData(resources);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        logger.info("不分页查菜单名称和ID和菜单级别{}", result.toString());
        return result;
    }

    /**
     * 增加资源
     *
     * @param sysResourceVo the sys resource vo
     * @param bindingResult the binding result
     * @param request       the request
     * @return object
     */
    @RequestMapping(value = "addResource", method = RequestMethod.POST)
    @ResponseBody
    public Object addResource(SysResourceVo sysResourceVo, BindingResult bindingResult, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        try {
            //1、入参校验
            orderCheck(sysResourceVo, bindingResult);
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }
            //2、表单转换
            SysResources sysResource = new SysResources();
            BeanUtils.copyProperties(sysResource, sysResourceVo);
            sysResource.setId(UUID.randomUUID().toString().replace("-",""));
            sysResource.setEnable(1);
            sysResource.setCreate_id(sysUser.getId());
            sysResource.setCreate_name(sysUser.getUsername());
            sysResource.setCreate_time(new Date());
            //3、服务调用
            sysResourcesService.addResource(sysResource);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        referCache(request);
        return result;
    }

    /**
     * 删除资源
     *
     * @param id      the id
     * @param request the request
     * @return object
     */
    @RequestMapping(value = "delResource", method = RequestMethod.POST)
    @ResponseBody
    public Object delResource(String id, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        try {
            if (id == null || id.equals("")) {
                throw new IllegalArgumentException();
            }
            sysResourcesService.delResource(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        referCache(request);
        return result;
}

    /**
     * 修改资源
     *
     * @param sysResourceUpdateVo the sys resource update vo
     * @param bindingResult       the binding result
     * @param request             the request
     * @return object
     */
    @RequestMapping(value = "updateResource", method = RequestMethod.POST)
    @ResponseBody
    public Object updateResource(SysResourceUpdateVo sysResourceUpdateVo, BindingResult bindingResult, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        try {
            //1、入参校验
            orderCheck(sysResourceUpdateVo, bindingResult);
            if (sysResourceUpdateVo.getIdUpdate() == null) {
                throw new IllegalArgumentException();
            }
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }
            //2、表单转换
            SysResources sysResource = new SysResources();
            sysResource.setId(sysResourceUpdateVo.getIdUpdate());
            sysResource.setUpdate_time(new Date());
            sysResource.setBtn_index(sysResourceUpdateVo.getBtn_index_update());
            sysResource.setEnable(1);
            sysResource.setResource_desc(sysResourceUpdateVo.getResource_desc_update());
            sysResource.setOrder_no(sysResourceUpdateVo.getOrder_no_update());
            sysResource.setResource_level(sysResourceUpdateVo.getResource_level_update());
            sysResource.setBtn_style(sysResourceUpdateVo.getBtn_style_update());
            sysResource.setResource_icon(sysResourceUpdateVo.getResource_icon_update());
            sysResource.setResource_type(sysResourceUpdateVo.getResource_type_update());
            sysResource.setResource_name(sysResourceUpdateVo.getResource_name_update());
            sysResource.setResource_path(sysResourceUpdateVo.getResource_path_update());
            sysResource.setUpdate_id(sysUser.getId());
            sysResource.setUpdate_name(sysUser.getName());
            sysResource.setResource_parent(sysResourceUpdateVo.getResource_parent_update());

            //3、服务调用
            sysResourcesService.updateResource(sysResource);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        referCache(request);
        return result;
    }

    /**
     * Refer cache.
     *
     * @param request the request
     */
    public static void referCache(HttpServletRequest request) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        SecurityMetadataSourceService cs = ctx.getBean("securityMetadataSource", SecurityMetadataSourceService.class);
        cs.loadResourceDefine();
    }
}

