package com.dreamer.view.pmall;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.user.User;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.service.pmall.order.OrderPayHandler;
import com.dreamer.service.pmall.order.OrderReceiveHandler;
import com.dreamer.service.pmall.order.OrderRefundHandler;
import com.dreamer.service.pmall.order.OrderReturnHandler;
import com.dreamer.service.pmall.order.OrderRevokeHandler;
import com.dreamer.service.pmall.order.OrderShippingHandler;

@RestController
@RequestMapping("/pm/order")
public class OrderController {


	@RequestMapping(value="/pay.json",method=RequestMethod.POST)
	public Message pay(@ModelAttribute("parameter") Order param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			orderPayHandler.pay(param, param.getPaymentWay(), param.getActualPayment());
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单付款确认失败");
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value="/revoke.json",method=RequestMethod.POST)
	public Message revoke(@ModelAttribute("parameter") Order param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			param.setRevokeOperator(user.getRealName());
			orderRevokeHandler.revoke(param);
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("订单撤销操作失败");
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value="/receive.json",method=RequestMethod.POST)
	public Message receive(@ModelAttribute("parameter") Order param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			orderReceiveHandler.receive(param, user);
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("订单收货确认操作失败");
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value="/shipping/confirm.json",method=RequestMethod.POST)
	public Message confirmShipping(@ModelAttribute("parameter") Order param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			param.setShippingOperator(user.getRealName());
			orderShippingHandler.shipping(param);
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("订单发货操作失败");
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value="/refund.json",method=RequestMethod.POST)
	public Message refund(@ModelAttribute("parameter") Order param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			orderRefundHandler.refund(param, user.getRealName());
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("订单发货操作失败");
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value="/return.json",method=RequestMethod.POST)
	public Message returns(@ModelAttribute("parameter") Order param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			orderReturnHandler.returns(param, user.getRealName());
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("订单退货操作失败");
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@ModelAttribute("parameter")
	public Order orderPreprocess(
			@RequestParam(required = false) Optional<Integer> id) {
		if (id.isPresent()) {
			return (orderDAO.findById(id.get()));
		} else {
			return new Order();
		}
	}

	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private OrderPayHandler orderPayHandler;
	
	@Autowired
	private OrderShippingHandler orderShippingHandler;
	
	@Autowired
	private OrderRevokeHandler orderRevokeHandler;
	
	@Autowired
	private OrderReceiveHandler orderReceiveHandler;
	
	@Autowired
	private OrderRefundHandler orderRefundHandler;
	
	@Autowired
	private OrderReturnHandler orderReturnHandler;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
