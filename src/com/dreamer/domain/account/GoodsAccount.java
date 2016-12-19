package com.dreamer.domain.account;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;


import ps.mx.otter.exception.ApplicationException;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.Price;
import com.dreamer.domain.goods.TransferApplyOrigin;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.domain.user.AgentLevelName;
import com.dreamer.domain.user.User;


public class GoodsAccount implements java.io.Serializable {

    private static final long serialVersionUID = -1216038804344510293L;
    private Integer id;
    private User user;
    private Goods goods;
    private Double currentPoint;
    private Integer currentBalance;//当前库存
    private Date updateTime;
    private Integer version;
    //累积购买量
    private Integer cumulative;
    //当月购买量
    private Integer currentCumulative;
    //代理针对货物所处的级别
    private AgentLevel agentLevel;
    //交易记录
    private String cumulativeRecord;

    public boolean isMainGoodsAccount() {

        return goods.isMainGoods();
    }

    public Price caculatePrice() {
        return goods.getPrice(agentLevel);
    }

    /**
     * 等级比我低
     *
     * @param GoodsAccount 参与比较的账户,传入的账户等级比我低时返回 true
     * @return
     */
    public boolean lowerThanMe(GoodsAccount gac) {
        return agentLevel.lowerThanMe(gac.getAgentLevel());
    }

    /**
     * 等级比我高
     *
     * @param GoodsAccount 参与比较的账户,传入的账户等级比高低时返回 true
     * @return
     */
    public boolean higherThanMe(GoodsAccount gac) {
        return agentLevel.higherThanMe(gac.getAgentLevel());
    }

    /**
     * 相同等级
     *
     * @param GoodsAccount
     * @return
     */
    public boolean myEqual(GoodsAccount gac) {
        return agentLevel.myEqual(gac.getAgentLevel());
    }

    public boolean currentBalanceNotEnough(Integer quantity) {
        return currentBalance < quantity;
    }

    public Integer getCurrentCumulative() {
        return currentCumulative;
    }

    public void setCurrentCumulative(Integer currentCumulative) {
        this.currentCumulative = currentCumulative;
    }

    public String getCumulativeRecord() {
        return cumulativeRecord;
    }

    public void setCumulativeRecord(String cumulativeRecord) {
        this.cumulativeRecord = cumulativeRecord;
    }


    /**
     * 增加
     *
     * @param another
     * @param amount
     */
    public void transferGoodsToAnother(GoodsAccount another, Integer amount, TransferApplyOrigin applyOrigin) {
        deductBalance(amount);
        if (applyOrigin == TransferApplyOrigin.OUT) {//主动转出
            if (another.isVip() && !user.isMutedUser()) {//不是公司转给vip
                another.nocumAccept(amount);//不增加当月进货量
            } else {
                another.accept(amount);//增加当月进货量
            }

        } else if (applyOrigin == TransferApplyOrigin.MALL) {//代理商城返利接受
            if (another.isVip() && !user.isMutedUser()) {//不是公司转给vip
                another.hf_nocumAccept(amount);//不增加当月进货量
            } else {
                another.hf_accept(amount);//增加当月进货量
            }
        }

        //another.accept(amount);
        if (user.isMutedUser()) {
            goods.deductCurrentBalance(amount);
        }

        if (another.getUser().isMutedUser()) {
            goods.increaseCurrentBalance(amount);
        }
    }


//    public void transferGoodsToAnother(GoodsAccount another, Integer amount) {
//        deductBalance(amount);
//		/*if(!user.isMutedUser()){
//			Double points=goods.caculatePoints(amount);
//			deductPoints(points);
//			user.deductPoints(points);
//		}*/
//
//        //官方返利接收
//        another.hf_accept(amount);
//        //another.accept(amount);
//        if (user.isMutedUser()) {
//            goods.deductCurrentBalance(amount);
//        }
//    }

    public void transferGoodsToBack(GoodsAccount me, GoodsAccount another, Integer amount) {
        deductBalance(amount);
        deductCumulative(amount);//减少累计
        if (!user.isMutedUser()) {
            Double points = goods.caculatePoints(amount);
            //deductPoints(points);
            //user.deductPoints(points);
        }
        another.acceptBack(amount, me);
        //然后这个人的的上级要扣减

    }


    /**
     * 主打产品账户价格等级比本产品账户价格等级高
     *
     * @param mainGoodsAgentLevel
     * @return
     */
    public boolean mainGoodsAgentLevelHigherThanMe(AgentLevel mainGoodsAgentLevel) {
        return agentLevel.higherThanMe(mainGoodsAgentLevel);
    }

    /**
     * 普通接受
     *
     * @param amount
     */
    public void hf_accept(Integer amount) {
        accept(amount);//不返利
        FanLi(amount);//返利
    }


    /**
     * vip接受,不增加销量
     *
     * @param amount
     */
    public void hf_nocumAccept(Integer amount) {
        nocumAccept(amount);//不增加当月销量
        FanLi(amount);//返利
    }

    /**
     * 只有总代进货返利 并且上级不是公司
     *
     * @return
     */
    public boolean isVip() {
        if (getAgentLevel().getName().equals(AgentLevelName.全国总代.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 是股东
     *
     * @return
     */
    public boolean isGd() {
        if (getAgentLevel().getName().equals(AgentLevelName.股东.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 上级不是公司的vip才能给上级返利
     *
     * @return
     */
    public boolean isFanLiVip() {
        if (isVip() && !user.getParent().isMutedUser()) {
            return true;
        }
        return false;
    }
//
//    /**
//     * 返利vip  总代、累计转货量达到200
//     * @return
//     */
//    public boolean isFlVip(){
//        int cum=getCurrentCumulative();//本月转货量
//        if(cum>=200&&getAgentLevel().getName().equals(AgentLevelName.全国总代.toString())){
//            return true;
//        }
//        return false;
//    }

    /**
     * 返利条件,累计转货物量达到200  是官方
     *
     * @param amount
     */
    public void FanLi(Integer amount) {
        if (isFanLiVip()) {//如果可以返利
            String more = user.getRealName() + "购买" + goods.getName() + "货物" + amount + "" + goods.getSpec();//转账备注
            //返利模式
            Double[] backVouchers = getbackVouchers();
            Agent parent = user.getParent();
            int i = 0;
            Double voucher_temp;
            while (!parent.isMutedUser()) {
                //如果是总代
                if (parent.isFlVip() && i < backVouchers.length-1) {//可以返利
                    voucher_temp = getbackVoucher(i, amount);
                    parent.getAccounts().increaseVoucher(voucher_temp, more);//返利
                    i++;
                }
                //如果是股东
                if (parent.isGd()) {
                    voucher_temp = getbackVoucher(backVouchers.length-1, amount);
                    parent.getAccounts().increaseVoucher(voucher_temp, more);//返利
                    return;
                }
                parent = parent.getParent();

            }
        }
    }

    /**
     * 获取返利模式
     *
     * @return
     */
    public Double[] getbackVouchers() {
        String[] strs = getGoods().getVoucher().trim().split("_");
        Double[] temps = new Double[strs.length];
        for (int i = 0; i < temps.length; i++) {
            temps[i] = Double.parseDouble(strs[i]);
        }
        return temps;
    }


    /**
     * 获取返利
     *
     * @param i
     * @return 没有的为0
     */
    public Double getbackVoucher(int i, int amount) {
        Double[] vous = getbackVouchers();
        if (i < vous.length) {
            return vous[i] * amount;
        }
        return 0.0;
    }


    /**
     * 不增加当月销量,用于非公司转给vip
     *
     * @param amount
     */
    public void nocumAccept(Integer amount) {
        increaseBalance(amount);//增加库存
        increaseCumulative(amount);//增加累积量
//        increaseCurrentCumulative(amount);//增加当月销量
    }


    public void accept(Integer amount) {
        increaseBalance(amount);//增加库存
        increaseCumulative(amount);//增加累积量
        increaseCurrentCumulative(amount);//增加当月销量
    }

    /**
     * 账户等级自动晋升
     */
    public void autoPromotion() {
        AgentLevel parentLevel = this.agentLevel.getParent();
        while (Objects.nonNull(parentLevel)) {
            Price priceLevel = goods.getPrice(parentLevel);
            if (Objects.isNull(priceLevel)) {
                parentLevel = parentLevel.getParent();
                continue;
            }
            if (cumulativeExceedsThreshold(priceLevel.getThreshold()) && parentLevel.canAutoPromotion()) {
                this.agentLevel = parentLevel;
            }
            parentLevel = parentLevel.getParent();
        }
    }

    public boolean cumulativeExceedsThreshold(Integer threshold) {
        return threshold <= this.getCumulative();
    }

    public void acceptBack(Integer amount, GoodsAccount from) {
        increaseBalance(amount);
        if (user.isMutedUser()) {
            goods.increaseCurrentBalance(amount);
        }
        //扣除from的上级的利润
        from.teqFanLi(amount, 1);
    }

    public void teqFanLi(Integer amount, int toback) {

    }

    /**
     * 增加积分
     *
     * @param points
     * @return
     */
    public Double increasePoints(Double points) {
        if (getCurrentPoint() + points < 0) {
            throw new ApplicationException("积分增加值非法");
        }
        setCurrentPoint(getCurrentPoint() + points);
        return getCurrentPoint();
    }

    /**
     * 积分扣减
     *
     * @param points
     * @return
     */
    public Double deductPoints(Double points) {
        if (points < 0) {
            throw new ApplicationException("积分扣减值不能为负数");
        }
        setCurrentPoint(getCurrentPoint() - points);
        return getCurrentPoint();
    }

    /**
     * 累积购买量增加
     *
     * @param cumulative
     * @return
     */
    public Integer increaseCumulative(Integer cumulative) {
        setCumulative(getCumulative() + cumulative);
        return getCumulative();
    }

    /**
     * 累积购买量减少
     *
     * @param cumulative
     * @return
     */
    public Integer deductCumulative(Integer cumulative) {
        setCumulative(getCumulative() - cumulative);
        if (this.getCumulative() < 0) {
            this.setCumulative(0);
        }
        return getCumulative();
    }

    /**
     * 增加账户余额
     *
     * @param balance
     * @return
     */
    public Integer increaseBalance(Integer balance) {
        if (getCurrentBalance() + balance < 0) {
            throw new ApplicationException("货物账户余额增加值非法");
        }
        setCurrentBalance(getCurrentBalance() + balance);
        return getCurrentBalance();
    }

    /**
     * 增加当月进货量
     *
     * @param balance
     * @return
     */
    public Integer increaseCurrentCumulative(Integer balance) {
        setCurrentCumulative(getCurrentCumulative() + balance);
        return getCurrentCumulative();
    }

    /**
     * 扣减账户余额
     *
     * @param balance
     * @return
     */
    public Integer deductBalance(Integer balance) {
        if (balance < 0) {
            throw new ApplicationException("账户余额扣减值不能为负数");
        }
        if (balance > getCurrentBalance()) {
            throw new ApplicationException("需扣减的[" + goods.getName() + "]账户余额不足");
        }
        setCurrentBalance(getCurrentBalance() - balance);
        return getCurrentBalance();
    }

    public Double getPrice() {
        Price price = goods.getPrice(agentLevel);
        return price == null ? 0.0D : price.getPrice();

    }

    // Constructors

    public Integer getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     * default constructor
     */
    public GoodsAccount() {
    }

    /**
     * minimal constructor
     */
    public GoodsAccount(Agent user, Goods goods, Double currentPoint) {
        this.user = user;
        this.goods = goods;
        this.currentPoint = currentPoint;
    }

    /**
     * full constructor
     */
    public GoodsAccount(Agent user, Goods goods, Double currentPoint,
                        Timestamp updateTime, Integer version) {
        this.user = user;
        this.goods = goods;
        this.currentPoint = currentPoint;
        this.updateTime = updateTime;
        this.version = version;
    }

    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Goods getGoods() {
        return this.goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;

    }

    public Double getCurrentPoint() {
        return this.currentPoint;
    }

    public void setCurrentPoint(Double currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCumulative() {
        return cumulative;
    }

    public void setCumulative(Integer cumulative) {
        this.cumulative = cumulative;
    }

    public AgentLevel getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(AgentLevel agentLevel) {
        this.agentLevel = agentLevel;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return Objects.hash(goods);
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GoodsAccount)) {
            return false;
        }
        GoodsAccount other = (GoodsAccount) obj;
        return Objects.equals(goods, other.getGoods());
    }


}