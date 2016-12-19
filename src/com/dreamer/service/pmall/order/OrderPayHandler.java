package com.dreamer.service.pmall.order;

import java.math.BigDecimal;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ps.mx.otter.exception.ApplicationException;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.pmall.order.PaymentWay;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.repository.system.SystemParameterDAOInf;

@Service
public class OrderPayHandler {

	@Transactional
	public void pay(Order order, PaymentWay paymentWay, Double money) {
		if (!order.paymentRequired()) {
			throw new ApplicationException("此订单无需支付");
		}
		order.pay(paymentWay, money);
		fireEvent(order);
		orderDAO.merge(order);
	}

	private void fireEvent(Order order) {
		Agent temp = order.getUser().getParent();
		if(temp.isMutedUser()){
			LOG.error("订单代理为直属代理,不向上返回奖金和福利积分");
			return;
		}
		Integer voucherLevel = systemParameterDAO.getCouponsLevel();
		LOG.error("奖金返还层级:{}", voucherLevel);
		Integer benefitPointsLevel = systemParameterDAO.getBenefitPointsLevel();
		LOG.error("福利积分返还层级:{}", benefitPointsLevel);
		Double voucherPerLevel = (Objects.isNull(voucherLevel) || voucherLevel == 0) ? 0
				: new BigDecimal(order.getVoucher() * 0.3).setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
		LOG.error("每层级返奖金:{} 总返还:{}", voucherPerLevel, order.getVoucher());
		Double benefitPointsPerLevel = (Objects.isNull(benefitPointsLevel) || benefitPointsLevel == 0) ? 0
				: new BigDecimal(order.getBenefitPoints() * 0.3)
						.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		LOG.error("每层级返福利积分:{} 总返还:{}", benefitPointsPerLevel,
				order.getBenefitPoints());
		
		for (int level = 0; level < voucherLevel; level++) {
			String more=order.getUser().getRealName()+"在积分商城购物，订单编号"+order.getOrderNo(); 
			temp.getAccounts().increaseVoucher(new Double(voucherPerLevel),more);
			LOG.debug("准备进入返还第{}层代理{}奖金", level, voucherPerLevel);
			if (temp.isTopAgent() || Objects.isNull(temp.getParent())) {
				LOG.error("已经到了最上级代理或上级代理为空,不再上溯返还奖金");
				break;
			} else {
				temp = temp.getParent();
			}
		}
		temp = order.getUser().getParent();
		for (int level = 0; level < benefitPointsLevel; level++) {
			temp.getAccounts().increaseBenefitPoints(
					new Double(benefitPointsPerLevel));
			if (temp.isTopAgent() || Objects.isNull(temp.getParent())) {
				LOG.error("已经到了最上级代理或上级代理为空,不再上溯返还福利积分");
				break;
			} else {
				temp = temp.getParent();
			}
		}
	}

	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
