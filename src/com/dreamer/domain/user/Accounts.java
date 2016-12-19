package com.dreamer.domain.user;

import ps.mx.otter.exception.ApplicationException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class Accounts implements java.io.Serializable {

	private static final long serialVersionUID = 956581918691993666L;
	// Fields

	private Integer id;
	private Integer version;
	private User user;
	private Date updateTime;
	private Double pointsBalance;
	private Double voucherBalance;
	private Double advanceBalance;//物流费
	private Double benefitPointsBalance;
	/**
	 * 奖金转移
	 * @param acs
	 * @param voucher
	 */
	public void payVoucherTo(Accounts acs,Double voucher){
		String more=user.getRealName()+"转货支付给"+acs.getUser().getRealName();
//		deductVoucher(vouch       er,more);
//		acs.increaseVoucher(voucher,more);
		payVoucherTo(acs,voucher,more);
	}

	/**
	 * 奖金转移
	 * @param acs
	 * @param voucher
	 */
	public void payVoucherTo(Accounts acs,Double voucher,String more){
		deductVoucher(voucher,more);
		acs.increaseVoucher(voucher,more);
	}

    /**
     * 物流费支付
     * @param acs
     * @param advance
     */
    public void payAdvanceTo(Accounts acs,Double advance){
        String more=user.getRealName()+"转货支付给"+acs.getUser().getRealName();
        deductAdvance(advance,more);//减少自己的
        acs.increaseAdvance(advance,more);//增加收款人的
    }

    /**
     * 物流费转移
     * @param acs
     * @param more
     */
    public void transferAdvanceToAnother(Accounts acs,Double advance){
        String more=user.getRealName()+"主动转移物流费到"+acs.getUser().getRealName();
		if(!this.getUser().isMutedUser()) {
			this.deductAdvance(advance, more);//本人减少
		}
        acs.increaseAdvance(advance,more);//接收方增加
    }
	
	/**
	 * 积分转移
	 * @param acs
	 * @param points
	 */
	public void transferPointsToAnoher(Accounts acs,Double points){
		deductPoints(points);
		acs.increasePoints(points);
	}
	
	/**
	 * 奖金转移
	 * @param acs
	 * @param points
	 */
	public void transferVoucherToAnoher(Accounts acs,Double voucher){
		String more=user.getRealName()+"主动转移奖金到"+acs.getUser().getRealName();
		if(!this.getUser().isMutedUser()){
			this.deductVoucher(voucher,more);
		}
		acs.increaseVoucher(voucher,more);
	}
	
	/**
	 * 增加积分
	 * @param points
	 * @return
	 */
	public Double increasePoints(Double points){
		if(getPointsBalance()+points<0){
			throw new ApplicationException("积分增加值非法");
		}
		setPointsBalance(getPointsBalance()+points);
		return getPointsBalance();
	}
	/**
	 * 积分扣减
	 * @param points
	 * @return
	 */
	public Double deductPoints(Double points){
		if(points<0){
			throw new ApplicationException("积分扣减值不能为负数");
		}
		if(points>getPointsBalance()){
			throw new ApplicationException("执行积分扣减时,积分余额不足");
		}
		setPointsBalance(getPointsBalance()-points);
		return getPointsBalance();
	}
	/**
	 * 增加奖金
	 * @param voucher
	 * @return
	 */
	public Double increaseVoucher(Double voucher,String more){
		if(voucher==0){//为0不处理
			return getVoucherBalance();
		}
		if(getVoucherBalance()+voucher<0){
			throw new ApplicationException("奖金增加值非法");
		}
        // 进行加法运算
        BigDecimal b1 = new BigDecimal(getVoucherBalance());
        BigDecimal b2 = new BigDecimal(voucher);
        setVoucherBalance(b1.add(b2).doubleValue());
		user.addVoucherRecord(1, more, b2.doubleValue());//增加记录
		return getVoucherBalance();
	}
	/**
     * 扣减奖金
     * @param voucher
     * @return
     */
    public Double deductVoucher(Double voucher,String more){
		if(voucher==0){//为0不处理
			return getVoucherBalance();
		}
        if(voucher<0){
            throw new ApplicationException("奖金扣减值不能为负数");
        }
        if(voucher>getVoucherBalance()){
            throw new ApplicationException("奖金/物流费余额不足,请及时充值");
        }
        // 进行减法运算
        BigDecimal b1 = new BigDecimal(getVoucherBalance());
        BigDecimal b2 = new BigDecimal(voucher);
        setVoucherBalance(b1.subtract(b2).doubleValue());
        user.addVoucherRecord(0, more, b2.doubleValue());//减少记录
        return getVoucherBalance();
    }

    /**
     * 增加物流费
     * @param advance
     * @param more
     * @return
     */
    public Double increaseAdvance(Double advance,String more){
		if(advance==0)return  getAdvanceBalance();//不处理
        if(advance<0){
            throw new ApplicationException("物流费增加值非法");
        }
        // 进行加法运算
        BigDecimal b1 = new BigDecimal(getAdvanceBalance());
        BigDecimal b2 = new BigDecimal(advance);
        setAdvanceBalance(b1.add(b2).doubleValue());
        user.addAdvanceRecord(1,more,b2.doubleValue());
        return getAdvanceBalance();
    }

    /**
     * 扣减物流费
     * @param advance 数量
     * @param more 原因
     * @return
     */
    public Double deductAdvance(Double advance,String more){
		if(advance==0)return  getAdvanceBalance();//不处理

       if(advance<0){
            throw new ApplicationException("物流费扣减值不能为负数");
       }
        if(advance>getAdvanceBalance()){
            throw new ApplicationException("物流费不足,需要"+advance);
        }
        // 进行减法运算
        BigDecimal b1 = new BigDecimal(getAdvanceBalance());
        BigDecimal b2 = new BigDecimal(advance);
        setAdvanceBalance(b1.subtract(b2).doubleValue());
        user.addAdvanceRecord(0,more,b2.doubleValue());
        return  getAdvanceBalance();
    }
	
	
	public boolean pointsBalanceEnough(Double points){
		return Double.compare(this.pointsBalance, points)>=0;
	}
	/**
	 * 增加福利积分
	 * @param benefitPoints
	 * @return
	 */
	public Double increaseBenefitPoints(Double benefitPoints){
		if(getBenefitPointsBalance()+benefitPoints<0){
			throw new ApplicationException("福利积分增加值非法");
		}
		benefitPointsBalance=(getBenefitPointsBalance()+benefitPoints);
		return getBenefitPointsBalance();
	}
	
	/**
	 * 扣减福利积分
	 * @param benefitPoints
	 * @return
	 */
	public Double deductBenefitPoints(Double benefitPoints){
		if(benefitPoints<0){
			throw new ApplicationException("福利积分扣减值不能为负数");
		}
		if(benefitPoints>this.getBenefitPointsBalance()){
			throw new ApplicationException("福利积分账户余额不足");
		}
		benefitPointsBalance=this.getBenefitPointsBalance()-benefitPoints;
		return this.getBenefitPointsBalance();
	}

	// Constructors

	/** default constructor */
	public Accounts() {
	}

	/** minimal constructor */
	public Accounts(User user) {
		this.user = user;
	}

	/** full constructor */
	public Accounts(User user, Timestamp updateTime, Double pointsBalance,
			Double voucherBalance) {
		this.user = user;
		this.updateTime = updateTime;
		this.pointsBalance = pointsBalance;
		this.voucherBalance = voucherBalance;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Double getPointsBalance() {
		return this.pointsBalance;
	}

	public void setPointsBalance(Double pointsBalance) {
		this.pointsBalance = pointsBalance;
	}

	public Double getVoucherBalance() {
		return this.voucherBalance;
	}

	public void setVoucherBalance(Double voucherBalance) {
		this.voucherBalance = voucherBalance;
	}

	
	public Double getBenefitPointsBalance() {
		return benefitPointsBalance;
	}

	public void setBenefitPointsBalance(Double benefitPointsBalance) {
		this.benefitPointsBalance = benefitPointsBalance;
	}

    public Double getAdvanceBalance() {
        return advanceBalance;
    }

    public void setAdvanceBalance(Double advanceBalance) {
        this.advanceBalance = advanceBalance;
    }

    @Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(user);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Accounts)) {
			return false;
		}
		Accounts other = (Accounts) obj;
		return Objects.equals(user, other.getUser());
	}
	
	

}