package com.dreamer.domain.goods;

import ps.mx.otter.exception.ApplicationException;

/**
 * Created by huangfei on 16/3/24.
 */
public class Logistics {

    private String name;

    private Integer orderIndex;//顺序

    private String provinces;//身份

    private String weights;//重量区间

    private String prices;//价额区间

    private Integer balance;//库存

    private Integer id;

    public Integer deductStock(Integer amount){
        if(balance>=amount){
            setBalance(balance-amount);
        }else {
            throw new ApplicationException(name+"货物总库存不足");
        }
        return getBalance();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getPrices() {
        return prices;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
