package com.ice.security.service.impl;

import com.ice.entity.system.Authority;
import com.ice.entity.system.SysResources;
import com.ice.security.mapper.SysResourcesDao;
import com.ice.security.service.SysResourcesService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service("sysResourcesService")
public class SysResourcesServiceImpl implements SysResourcesService {

    @Resource
    private SysResourcesDao dao;

    @Override
    public List<Authority> queryAllByType(String type) {
        return dao.queryAllByType(type);
    }

    @Override
    public List<SysResources> queryAllByUserId(String userId) {
        return dao.queryAllByUserId(userId);
    }

    @Override
    public List<SysResources> queryAllResource(SysResources sysResources)throws Exception {
        List<SysResources> sysResourcesList =  dao.queryAllResource(sysResources);
        return sysResourcesList;
    }

    @Override
    public List<SysResources> queryResourceForName(SysResources sysResources) throws Exception {
        List<SysResources> sysResourcesList =  dao.queryResourceForName(sysResources);
        return sysResourcesList;
    }

    @Override
    public int addResource(SysResources sysResources)throws Exception {
        return dao.addResource(sysResources);
    }

    @Override
    public int delResource(String id) throws Exception {
        dao.deleteRoleResource(id);
        return dao.delResource(id);
    }

    @Override
    public int updateResource(SysResources sysResources)throws Exception {
        return dao.updateResource(sysResources);
    }

    @Override
    public String queryBtnByRole(String roleId, String menuId,int index) {
        List<SysResources> list = dao.queryBtnByRole(roleId,menuId,index);
        StringBuffer sb = new StringBuffer();
        for(SysResources sysResources:list){
            sb.append(sysResources.getBtn_style());
        }
        return sb.toString();
    }

    @Override
    public List<SysResources> queryLeft(String userId) {
        List<SysResources> listMenu = new ArrayList<>();
        List<SysResources> list = queryAllByUserId(userId);
        //先找所以的一级菜单
        for(SysResources sysResources:list){
            if(sysResources.getResource_parent().equals("0")){
                listMenu.add(sysResources);
            }
        }

        // 为一级菜单设置子菜单，getChild是递归调用的
        for (SysResources menu : listMenu) {
            menu.setChilds(getChild(menu.getId(), list));
        }
        return listMenu;
    }

    /**
     * 递归查找子菜单
     *
     * @param id
     *            当前菜单id
     * @param rootMenu
     *            要查找的列表
     * @return
     */
    private List<SysResources> getChild(String id, List<SysResources> rootMenu) {
        // 子菜单
        List<SysResources> childList = new ArrayList<>();
        for (SysResources menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (StringUtils.isNotBlank(menu.getResource_parent())) {
                if (menu.getResource_parent().equals(id)) {
                    childList.add(menu);
                    menu.setChilds(getChild(menu.getId(), rootMenu));
                }
            }
        }
         // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

}
