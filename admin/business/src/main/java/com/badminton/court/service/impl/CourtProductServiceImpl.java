package com.badminton.court.service.impl;

import com.badminton.court.mapper.CourtProductMapper;
import com.badminton.court.service.CourtProductService;
import com.badminton.entity.court.CourtProduct;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.ConditionConstant;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import com.badminton.interceptors.mySqlHelper.pagehelper.util.StringUtil;
import com.badminton.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Service("courtProductService")
public class CourtProductServiceImpl implements CourtProductService {
    @Autowired
    private CourtProductMapper courtProductMapper;
    @Override
    public List<CourtProduct> query(CourtProduct courtProduct) {
        if(courtProduct.getOrderDir().equals("asc")) {
            courtProduct.setCondition(Condition.build().addOrder(courtProduct.getOrderColumn(), OrderCondition.Direction.ASC));
        }else if(courtProduct.getOrderDir().equals("desc")) {
            courtProduct.setCondition(Condition.build().addOrder(courtProduct.getOrderColumn(), OrderCondition.Direction.DESC));
        }
        Condition query = Condition.build();
        if(StringUtil.isNotEmpty(courtProduct.getStartTimeQuery())){

            courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_GT + "start_time", courtProduct.getStartTimeQuery() ));
        }
        if(StringUtil.isNotEmpty(courtProduct.getEndTimeQuery())){
            courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_LT+"end_time",courtProduct.getEndTimeQuery()));
        }
        return courtProductMapper.select(courtProduct);
    }

    @Override
    public boolean checkOrder(CourtProduct courtProduct) {
        Condition query = Condition.build();
        if(StringUtil.isNotEmpty(courtProduct.getStartTimeQuery())){
            courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_GE + "start_time", DateUtil.getInitialTime( courtProduct.getStartTimeQuery()) ));
        }
        if(StringUtil.isNotEmpty(courtProduct.getEndTimeQuery())){
            courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_LE+"end_time",courtProduct.getEndTimeQuery()));
        }
        courtProduct.setState(1);
        List<CourtProduct> list = courtProductMapper.select(courtProduct);
        return list.size()>0;
    }

    @Override
    public List<CourtProduct> queryByDate(String date,String courtId) {
        CourtProduct courtProduct = new CourtProduct();
        Condition query = Condition.build();
        String startTime = date+" 00:00:00";
        String endTime = date+" 23:59:59";
        courtProduct.setCourtId(Long.parseLong(courtId));
        courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_GE + "start_time", startTime));
        courtProduct.setCondition(query.addWhere(ConditionConstant.PREFIX_LE+"end_time",endTime));
        return courtProductMapper.select(courtProduct);
    }

    @Override
    public CourtProduct queryById(Long id) {
        try{

            return courtProductMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            e.printStackTrace();;
        }
        return null;
    }

    @Override
    public void update(CourtProduct courtProduct) {
        this.courtProductMapper.updateByPrimaryKeySelective(courtProduct);
    }
}
