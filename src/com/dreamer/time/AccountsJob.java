package com.dreamer.time;

import com.dreamer.repository.account.GoodsAccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by huangfei on 16/5/8.
 * 用于每个月  对所有用户的当月销量进行清零
 */
@Component("accountsJob")
public class AccountsJob {
    @Autowired
    private GoodsAccountDAO goodsAccountDAO;
    @Transactional
    public void  doIt(){
        //设置所有代理的本月累积量为0,增加记录
        goodsAccountDAO.retSetByMonth();
    }

    public void test(){
        System.out.println("----------");
    }

    public GoodsAccountDAO getGoodsAccountDAO() {
        return goodsAccountDAO;
    }

    public void setGoodsAccountDAO(GoodsAccountDAO goodsAccountDAO) {
        this.goodsAccountDAO = goodsAccountDAO;
    }
}