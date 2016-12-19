package com.dreamer.view.pmall;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.ResponseBody;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.user.AgentDAO;

@Controller
@RequestMapping(value={"/pmall","/dmz/pmall"})
public class PmallIndexController {

	@RequestMapping(value = { "/index.html", "/", "/index" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<MallGoods> param,
			@RequestParam(value="ref",required=false) String ref,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			if(WebUtil.isLogin(request)){
				User user=(User)WebUtil.getCurrentUser(request);
				Agent agent=agentDAO.findById(user.getId());
				model.addAttribute("account_points", agent.getAccounts().getPointsBalance());
			}
			if(Objects.nonNull(ref)){
				model.addAttribute("agentCode", ref);
			}
		} catch (Exception exp) {
			LOG.error("进入积分商城首页异常", exp);
			exp.printStackTrace();
		}
		return "pmall/pmall_index";
	}
	
	@RequestMapping(value="/detail.html",method=RequestMethod.GET)
	public String detail(@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response,Model model){
		try{
			MallGoods g=mallGoodsDAO.findById(id);
			PointsGoodsDTO dto=new PointsGoodsDTO();
			dto.setId(g.getId());
			dto.setBenefitPoints(g.getBenefitPoints());
			dto.setMoneyPrice(g.getMoneyPrice());
			dto.setName(g.getName());
			dto.setPointPrice(g.getPointPrice());
			dto.setPrice(g.getPrice());
			dto.setSpec(g.getSpec());
			dto.setStockQuantity(g.getStockQuantity());
			dto.setVoucher(g.getVoucher());
			String imgUrl = WebUtil.getContextPath(request) + "/dmz/img/pmall/"
					+ g.getImgFile();
			dto.setImgUrl(imgUrl);
			model.addAttribute("goods", dto);
		}catch(Exception exp){
			LOG.error("进入积分商城商品详情页异常",exp);
			exp.printStackTrace();
		}
		return "pmall/goods_detail";
	}

	@RequestMapping(value = "/goods/query.json", method = RequestMethod.GET)
	@ResponseBody
	public List<PointsGoodsDTO> queryGoods(
			@ModelAttribute("parameter") SearchParameter<MallGoods> param,
			HttpServletRequest request) {
		param.getEntity().setShelf(true);
		List<MallGoods> goods = mallGoodsDAO.searchEntityByPage(param, null,
				(t) -> true);
		List<PointsGoodsDTO> dtos=new ArrayList<PointsGoodsDTO>();
		goods.forEach(g->{
			PointsGoodsDTO dto=new PointsGoodsDTO();
			dto.setId(g.getId());
			dto.setBenefitPoints(g.getBenefitPoints());
			dto.setMoneyPrice(g.getMoneyPrice());
			dto.setName(g.getName());
			dto.setPointPrice(g.getPointPrice());
			dto.setPrice(g.getPrice());
			dto.setSpec(g.getSpec());
			dto.setStockQuantity(g.getStockQuantity());
			dto.setVoucher(g.getVoucher());
			String imgUrl = WebUtil.getContextPath(request) + "/dmz/img/pmall/"
					+ g.getImgFile();
			dto.setImgUrl(imgUrl);
			dtos.add(dto);
		});
		return dtos;
	}
	
	@ModelAttribute("parameter")
	public SearchParameter<MallGoods> preprocessing() {
		SearchParameter<MallGoods> param = new SearchParameter<MallGoods>();
		MallGoods goods = new MallGoods();
		param.setEntity(goods);
		return param;
	}

	@Autowired
	private MallGoodsDAO mallGoodsDAO;
	@Autowired
	private AgentDAO agentDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
