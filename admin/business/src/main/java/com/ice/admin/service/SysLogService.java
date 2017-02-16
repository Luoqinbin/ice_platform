package com.ice.admin.service;


import com.ice.entity.system.SysLog;
import com.ice.entity.system.query.SysLogQuery;

import java.util.List;

/**
* @Author CodeGenerator
* @date 2017-02-14 12:51:22
**/

public interface SysLogService {

    public List<SysLog> query(SysLogQuery query);

    public void insert(SysLog testName);

    public void update(SysLog testName);

    public SysLog queryOne(String id);

    public void delete(String id);

    public SysLog queryById(String id);

}
