package com.badminton.court.controller;

import com.badminton.court.service.FixedOrderService;
import com.badminton.entity.court.FixedOrder;
import com.badminton.entity.court.query.FixedOrderQuery;
import com.badminton.entity.system.SysUser;
import com.badminton.entity.test.TestCrud;
import com.badminton.entity.test.query.TestCrudQuery;
import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.badminton.result.BaseResult;
import com.badminton.result.PageResult;
import com.badminton.security.service.SysResourcesService;
import com.badminton.test.service.TestCrudService;
import com.badminton.utils.DateUtil;
import com.badminton.utils.PageUtils;
import com.badminton.utils.TimestampPkGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@Controller
@RequestMapping("fixedOrder")
public class FixedOrderController {
    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private FixedOrderService fixedOrderService;

    @RequestMapping(value = "init")
    public String initList(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId", menuId);
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String addBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
        request.setAttribute("addBtn", addBtn);

        String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 2);
        request.setAttribute("listBtn", listBtn);
        return "court/fixedOrderList";
    }


    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<FixedOrder> queryList(FixedOrderQuery query, HttpServletRequest request) {
        PageUtils<FixedOrderQuery> pageUtils = new PageUtils<FixedOrderQuery>();
        query = pageUtils.sort(query, request, "start_time", "desc", null);
        List<FixedOrder> list = fixedOrderService.query(query);
        for(FixedOrder fixedOrder:list){
            String[] sttr = fixedOrder.getStartTime().split(":");
            String[] ettr = fixedOrder.getEndTime().split(":");
            int si = Integer.parseInt(sttr[0]);
            int ei = Integer.parseInt(ettr[0]);
            fixedOrder.setConsume(((ei-si)*fixedOrder.getPrice())+"");
        }
        PageResult<FixedOrder> result = new PageResult<FixedOrder>(new PageInfo<FixedOrder>(list));
        return result;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult delete(Long id) {
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isNotEmpty(id+"")) {
            try {
                this.fixedOrderService.delete(id);
                baseResult.setCode(BaseResult.CODE_OK);
                baseResult.setMessage("删除数据成功");
            } catch (Exception e) {
                e.printStackTrace();
                baseResult.setCode(BaseResult.CODE_FAIL);
                baseResult.setMessage("删除数据失败");
            }
        } else {
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("删除数据失败");
        }

        return baseResult;
    }

    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult addOrUpdate(FixedOrderQuery query) {
        BaseResult baseResult = new BaseResult();
        //添加
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setId(new TimestampPkGenerator().next(getClass()));
        try {
            query.setStartDate(DateUtil.string2Date(query.getStartDateStr()));
            query.setEndDate(DateUtil.string2Date(query.getEndDateStr()));
            this.fixedOrderService.insert(query);
            baseResult.setCode(BaseResult.CODE_OK);
            baseResult.setMessage("添加数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("添加数据失败");
        }
        return baseResult;
    }
}
