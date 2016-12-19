package com.dreamer.view.mall;

import com.dreamer.domain.user.Agent;
import com.dreamer.repository.goods.DeliveryNoteDAO;
import com.dreamer.repository.user.AgentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequestMapping("/vmall/uc/")
@SessionAttributes({"agentCode","url","ref"})
public class UserCenterQueryController {

	@RequestMapping(value = "/dmz/register.html", method = RequestMethod.GET)
	public String register(
			Model model, HttpServletRequest request) {
		//model.addAttribute("agentCode", agentCode);
		return "mall/register";
	}

	@RequestMapping(value = {"/dmz/login.html","/dmz/slogin.html"}, method = RequestMethod.GET)
	public String login(
			@RequestParam(value = "url", required = false) String url,
			Model model, HttpServletRequest request) {
		if (Objects.nonNull(url)) {
			String reUrl=url;
			if(url.indexOf("/vmall/")>-1){
				UriComponents ucb = ServletUriComponentsBuilder
						.fromContextPath(request).path("/dmz/vmall/index.html").build();
				reUrl=ucb.toUriString();
			}else{
				UriComponents ucb = ServletUriComponentsBuilder
						.fromContextPath(request).path("/dmz/pmall/index.html").build();
				reUrl=ucb.toUriString();
			}
			model.addAttribute("url", reUrl);
			System.out.println(url);
			if(url.indexOf("ref=01")>=0||url.indexOf("ref=1")>=0){				
				return "mall/login_only";
			}
		}
		if(request.getRequestURI().indexOf("slogin.html")>-1){
			return "mall/login_only";
		}
		return "mall/login";
	}
	
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(@RequestParam(value="new",required=false) String newUser,
			Model model, HttpServletRequest request) {
		if(Objects.nonNull(newUser) && !newUser.isEmpty()){
			model.addAttribute("new",newUser);
		}
		Agent agent=(Agent)WebUtil.getCurrentUser(request);
		Agent my=agentDAO.findById(agent.getId());
	//	List<DeliveryNote> agents=devileryNoteDAO.findByAgent(agent);
	//	model.addAttribute("orders", agents);
		model.addAttribute("my",my);
		return "mall/uc/uc_index";
	}
	
	@RequestMapping(value = "/promoCode.html", method = RequestMethod.GET)
	public String promoCode(Model model, HttpServletRequest request) {
		return "mall/uc/uc_promoCode";
	}
	
	@Autowired
	private DeliveryNoteDAO devileryNoteDAO;
	@Autowired
	private AgentDAO agentDAO;
}
