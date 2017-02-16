package com.ice.interceptors.mySqlHelper.pagehelper.dialect;

import com.ice.interceptors.mySqlHelper.pagehelper.Constant;
import com.ice.interceptors.mySqlHelper.pagehelper.Page;
import com.ice.interceptors.mySqlHelper.pagehelper.util.MetaObjectUtil;
import com.ice.interceptors.mySqlHelper.pagehelper.util.SqlUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuzh
 */
public class HsqldbDialect extends AbstractDialect {
    public HsqldbDialect(SqlUtil sqlUtil) {
        super(sqlUtil);
    }

    @Override
    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, Page page, BoundSql boundSql, CacheKey pageKey) {
        paramMap.put(Constant.PAGEPARAMETER_FIRST, page.getPageSize());
        paramMap.put(Constant.PAGEPARAMETER_SECOND, page.getStartRow());
        //处理pageKey
        pageKey.update(page.getPageSize());
        pageKey.update(page.getStartRow());
        //处理参数配置
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
            if (boundSql != null && boundSql.getParameterMappings() != null) {
                newParameterMappings.addAll(boundSql.getParameterMappings());
            }
            newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), Constant.PAGEPARAMETER_FIRST, Integer.class).build());
            newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), Constant.PAGEPARAMETER_SECOND, Integer.class).build());
            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }
        return paramMap;
    }

    @Override
    public String getPageSql(String sql, Page page, RowBounds rowBounds, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 20);
        sqlBuilder.append(sql);
        sqlBuilder.append(" limit ? offset ?");
        return sqlBuilder.toString();
    }
}
