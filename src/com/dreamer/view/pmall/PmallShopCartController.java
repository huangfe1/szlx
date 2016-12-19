package com.dreamer.view.pmall;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.pmall.shopcart.ShopCart;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.pay.GetOpenIdHandler;
import com.dreamer.service.pay.PayConfig;
import com.dreamer.service.pmall.order.OrderCommitHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.exception.DataNotFoundException;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/pmall/shopcart")
@SessionAttributes({ "ref" })
public class PmallShopCartController {

	private static final String PMCART = "pmshopcart";

	@RequestMapping(value = "/add.json", method = RequestMethod.POST)
	public Message addGoodsToShopcart(
			@RequestParam("goodsId") Integer goodsId,
			@RequestParam(value = "quantity", required = false) Integer quantity,
			HttpServletRequest request) {
		try {
			MallGoods goods = mallGoodsDAO.findById(goodsId);
			if (Objects.isNull(goods)) {
				throw new DataNotFoundException("商品不存在");
			}
			Object ob = WebUtil.getSessionAttribute(request, PMCART);
			Integer addQuantity = Objects.nonNull(quantity) ? quantity : 1, result = 0;
			if (Objects.nonNull(ob)) {
				ShopCart cart = (ShopCart) ob;
				cart.addGoods(goods, addQuantity, goods.getPrice(),
						goods.getMoneyPrice(), goods.getPointPrice());
				result = cart.getQuantity();
			} else {
				ShopCart cart = new ShopCart();
				cart.addGoods(goods, addQuantity, goods.getPrice(),
						goods.getMoneyPrice(), goods.getPointPrice());
				WebUtil.addSessionAttribute(request, PMCART, cart);
				result = cart.getQuantity();
			}
			return Message.createSuccessMessage("OK", String.valueOf(result));
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/put.json", method = { RequestMethod.POST,
			RequestMethod.PUT })
	public Message putGoodsToShopcart(
			@RequestParam("goodsId") Integer goodsId,
			@RequestParam(value = "quantity", required = false) Integer quantity,
			HttpServletRequest request) {
		try {
			MallGoods goods = mallGoodsDAO.findById(goodsId);
			if (Objects.isNull(goods)) {
				throw new DataNotFoundException("商品不存在");
			}
			Object ob = WebUtil.getSessionAttribute(request, PMCART);
			Integer addQuantity = Objects.nonNull(quantity) ? quantity : 1, result = 0;
			if (Objects.nonNull(ob)) {
				ShopCart cart = (ShopCart) ob;
				cart.removeItems(goods);
				cart.addGoods(goods, addQuantity, goods.getPrice(),
						goods.getMoneyPrice(), goods.getPointPrice());
				result = cart.getQuantity();
			} else {
				ShopCart cart = new ShopCart();
				cart.addGoods(goods, addQuantity, goods.getPrice(),
						goods.getMoneyPrice(), goods.getPointPrice());
				WebUtil.addSessionAttribute(request, PMCART, cart);
				result = cart.getQuantity();
			}
			return Message.createSuccessMessage("OK", String.valueOf(result));
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/mins.json", method = RequestMethod.POST)
	public Message minsGoodsToShopcart(
			@RequestParam("goodsId") Integer goodsId, HttpServletRequest request) {
		try {
			MallGoods goods = mallGoodsDAO.findById(goodsId);
			if (Objects.isNull(goods)) {
				throw new DataNotFoundException("商品不存在");
			}
			Object ob = WebUtil.getSessionAttribute(request, PMCART);
			if (Objects.nonNull(ob)) {
				ShopCart cart = (ShopCart) ob;
				cart.removeGoods(goods, 1);
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/remove.json", method = RequestMethod.POST)
	public Message removeGoodsToShopcart(
			@RequestParam("goodsId") Integer goodsId, HttpServletRequest request) {
		try {
			MallGoods goods = mallGoodsDAO.findById(goodsId);
			if (Objects.isNull(goods)) {
				throw new DataNotFoundException("商品不存在");
			}
			Object ob = WebUtil.getSessionAttribute(request, PMCART);
			if (Objects.nonNull(ob)) {
				ShopCart cart = (ShopCart) ob;
				cart.removeItems(goods);
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/commit.json", method = RequestMethod.POST)
	public Message pay(@ModelAttribute("parameter") Order order,
			@ModelAttribute("indexUrl") String indexUrl,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Agent agentUser = (Agent) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(agentUser.getId());
			ShopCart cart = (ShopCart) WebUtil.getSessionAttribute(request,
					PMCART);
			orderCommitHandler
					.commitOrder(order, agent, cart.buildOrderItems());
			UriComponents backUrl=null;
			if(Objects.nonNull(agent.getWxOpenid()) && !agent.getWxOpenid().isEmpty()){
				LOG.debug("weixin openid already get,direct ro pay.html");
				/*backUrl = ServletUriComponentsBuilder
						.fromContextPath(request).path("/pay/pay.html").queryParam("order", order.getId()).build();*/
				backUrl = ServletUriComponentsBuilder
						.fromContextPath(request).path("/pay/callback.html").queryParam("order", order.getId()).build();
			}else{
				backUrl = ServletUriComponentsBuilder
						.fromContextPath(request).path("/pay/callback.html").queryParam("order", order.getId()).build();
				LOG.debug("Get OpenId CallBack URL:{}",backUrl.toUriString());
			}
			String uri=GetOpenIdHandler.createGetBaseOpenIdCallbackUrl(payConfig, backUrl.toUriString(), order.getId());
			LOG.debug("To Pay URL:{}",uri);
			response.setHeader("Location", uri);
			WebUtil.removeSessionAttribute(request, PMCART);
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("积分商城订单提交失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	/*private final static String GET_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize"
			+ "?appid={APPID}&redirect_uri={REDIRECT_URI}&response_type=code&scope=snsapi_base&state={STATE}#wechat_redirect";*/

	@Autowired
	private MallGoodsDAO mallGoodsDAO;
	@Autowired
	private OrderCommitHandler orderCommitHandler;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private PayConfig payConfig;
	@Autowired
	private GetOpenIdHandler getOpenIdHandler;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
