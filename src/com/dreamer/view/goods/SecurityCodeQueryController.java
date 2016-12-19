package com.dreamer.view.goods;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.goods.SecurityCode;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.SecurityCodeDAO;

@Controller
@RequestMapping("/securityCode")
public class SecurityCodeQueryController {

	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<SecurityCode> parameter,
			HttpServletRequest request, Model model) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			List<SecurityCode> codes = null;
			if (user.isAgent()) {
				Agent agent = (Agent) user;
				parameter.getEntity().setAgentCode(agent.getAgentCode());
			}
			codes = securityCodeDAO.searchEntityByPage(parameter, null,
					(t) -> true);

			WebUtil.turnPage(parameter, request);
			model.addAttribute("codes", codes);
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("防伪码查询失败", exp);
		}
		return "/goods/securityCode_index";
	}

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit_enter(
			@ModelAttribute("parameter") SearchParameter<SecurityCode> parameter,
			HttpServletRequest request, Model model) {

		return "/goods/securityCode_edit";
	}
	
	@RequestMapping(value = "/edit_num.html", method = RequestMethod.GET)
	public String edit_num_enter(
			@ModelAttribute("parameter") SearchParameter<SecurityCode> parameter,
			HttpServletRequest request, Model model) {

		return "/goods/securityCode_edit_num";
	}

	@ModelAttribute("parameter")
	public SearchParameter<SecurityCode> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<SecurityCode> parameter = new SearchParameter<SecurityCode>();
		SecurityCode code = null;
		if (id.isPresent()) {
			code = (SecurityCode) securityCodeDAO.findById(id.get());
		} else {
			code = new SecurityCode();
		}
		parameter.setEntity(code);
		return parameter;
	}

	@Autowired
	private SecurityCodeDAO securityCodeDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
