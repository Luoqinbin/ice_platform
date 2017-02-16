package com.ice.security.service;

import com.ice.entity.system.Authority;
import com.ice.entity.system.SysResources;

import java.util.List;

/**
 *
 */
public interface SysResourcesService {

    public List<Authority> queryAllByType(String type);

    public List<SysResources> queryAllByUserId(String userId);

    public List<SysResources> queryAllResource(SysResources sysResources)throws Exception;

    public List<SysResources> queryResourceForName(SysResources sysResources)throws Exception;

    int addResource(SysResources sysResources)throws Exception;

    int updateResource(SysResources sysResources)throws Exception;

    int delResource(String id)throws Exception;

    public String queryBtnByRole(String roleId, String menuId, int index);

    public List<SysResources> queryLeft(String userId);

}
