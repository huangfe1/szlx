package com.dreamer.service.pmall.order;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ps.mx.otter.exception.ApplicationException;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.pmall.order.OrderItem;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.pmall.order.OrderDAO;

@Service
public class OrderCommitHandler {

	@Transactional
	public Order commitOrder(Order order,Agent agent,List<OrderItem> items){
		LOG.debug("进入积分商城订单提交");
		try{
			items.stream().forEach(item->{
				order.addItem(item);
			});
			order.commit(agent);
			orderDAO.save(order);
		}catch(ApplicationException aep){
			LOG.error("积分商城订单提交失败",aep);
			aep.printStackTrace();
			throw aep;
		}catch(Exception exp){
			LOG.error("积分商城订单提交异常",exp);
			exp.printStackTrace();
			throw new ApplicationException(exp);
		}
		
		return order;
	}
	
	
	@Autowired
	private OrderDAO orderDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
