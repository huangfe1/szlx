package com.dreamer.view.pmall;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.pmall.order.OrderStatus;
import com.dreamer.domain.pmall.order.PaymentStatus;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.service.pay.GetOpenIdHandler;
import com.dreamer.service.pay.PayConfig;

@Controller
@RequestMapping("/pm/order")
public class OrderQueryController {

	@RequestMapping(value="/index.html",method=RequestMethod.GET)
	public String orderIndex(
			@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try{
			List<Order> orders=orderDAO.searchEntityByPage(param, null, (t)->true);
			WebUtil.turnPage(param, request);
			model.addAttribute("orders",orders);
			model.addAttribute("status",OrderStatus.values());
			model.addAttribute("paymentStatus",PaymentStatus.values());
		}catch(Exception exp){
			LOG.error("进入订单查询错误",exp);
			exp.printStackTrace();
		}
		return "pmall/order/order_index";
	}

	@RequestMapping(value="/pay.html",method=RequestMethod.GET)
	public String payIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			if(param.getEntity().isUnpaid()){
				model.addAttribute("paid", "1");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单付款确认失败");
		}
		return "pmall/order/order_pay";
	}
	
	@RequestMapping(value="/onlinepay.html",method=RequestMethod.GET)
	public String onlinePayIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			Order order=param.getEntity();
			Agent agent=order.getUser();
			UriComponents backUrl=null;
			if(Objects.nonNull(agent.getWxOpenid()) && !agent.getWxOpenid().isEmpty()){
				LOG.debug("weixin openid already get,direct ro pay.html");
				backUrl = ServletUriComponentsBuilder
						.fromContextPath(request).path("/pay/pay.html").queryParam("order", order.getId()).build();
			}else{
				backUrl = ServletUriComponentsBuilder
						.fromContextPath(request).path("/pay/callback.html").queryParam("order", order.getId()).build();
				LOG.debug("Get OpenId CallBack URL:{}",backUrl.toUriString());
			}
			String uri=GetOpenIdHandler.createGetBaseOpenIdCallbackUrl(payConfig, backUrl.toUriString(), order.getId());
			LOG.debug("To Pay URL:{}",uri);
			response.setHeader("Location", uri);
			return "redirect:"+uri;
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单在线付款失败");
		}
		return "pmall/index.html";
	}
	
	@RequestMapping(value="/revoke.html",method=RequestMethod.GET)
	public String revokeIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			if(param.getEntity().isUnpaid()){
				model.addAttribute("paid", "1");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单撤销操作失败");
		}
		return "pmall/order/order_revoke";
	}
	
	@RequestMapping(value="/refund.html",method=RequestMethod.GET)
	public String refundIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			if(param.getEntity().isUnpaid()){
				model.addAttribute("paid", "1");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单退款操作失败");
		}
		return "pmall/order/order_refund";
	}
	
	@RequestMapping(value="/return.html",method=RequestMethod.GET)
	public String returnIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			if(param.getEntity().isUnpaid()){
				model.addAttribute("paid", "1");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入订单退货操作失败");
		}
		return "pmall/order/order_return";
	}
	@RequestMapping(value="/detail.html",method=RequestMethod.GET)
	public String detail(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		return "pmall/order/order_detail";
	}
	
	@RequestMapping(value="/shipping.html",method=RequestMethod.GET)
	public String shippingIndex(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
			List<Order> orders=orderDAO.searchShippingEntityByPage(param, null, (t)->true);
			WebUtil.turnPage(param, request);
			model.addAttribute("orders",orders);
			model.addAttribute("status",OrderStatus.values());
			model.addAttribute("paymentStatus",PaymentStatus.values());
		}catch(Exception exp){
			LOG.error("进入订单发货管理错误",exp);
			exp.printStackTrace();
		}
		return "pmall/order/order_shipping";
	}
	
	@RequestMapping(value="/shipping/confirm.html",method=RequestMethod.GET)
	public String shippingConfirm(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		try{
		}catch(Exception exp){
			LOG.error("进入订单发货确认错误",exp);
			exp.printStackTrace();
		}
		return "pmall/order/shipping_confirm";
	}
	
	@RequestMapping(value="/shipping/print.html",method=RequestMethod.GET)
	public String printShipping(@ModelAttribute("parameter") SearchParameter<Order> param,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		return "pmall/order/shipping_print";
	}
	
	
	@ModelAttribute("parameter")
	public SearchParameter<Order> orderPreprocess(
			@RequestParam(required = false) Optional<Integer> id) {
		SearchParameter<Order> parameter = new SearchParameter<Order>();
		if (id.isPresent()) {
			parameter.setEntity(orderDAO.findById(id.get()));
		} else {
			parameter.setEntity(new Order());
		}
		return parameter;
	}

	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private PayConfig payConfig;
	@Autowired
	private GetOpenIdHandler getOpenIdHandler;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
