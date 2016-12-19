package com.dreamer.view.authorization;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.authorization.Authorization;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.domain.user.AgentStatus;
import com.dreamer.domain.user.User;
import com.dreamer.repository.authorization.AuthorizationDAO;
import com.dreamer.repository.user.AgentLevelDAO;
import com.dreamer.service.user.AuthorizationLetterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthorizationQueryController {

	@RequestMapping(value = "/letter.html", method = RequestMethod.GET)
	public String myLetter(@ModelAttribute("parameter")  SearchParameter<Authorization> auths, Model model) {
        Authorization auth=auths.getEntity();
		try {
			/*Agent agent=auth.getAgent();
			Goods goods=auth.getAuthorizationType().getGoods();
			model.addAttribute("name", agent.getRealName());
			String wx= agent.getWeixin();
			model.addAttribute("wx",replacePrivacy(wx));
			String mobile=agent.getMobile();
			model.addAttribute("mobile",replacePrivacy(mobile));
			String idcard=agent.getIdCard();
			model.addAttribute("idcard",replacePrivacy(idcard));
			model.addAttribute("agentCode",agent.getAgentCode());
			model.addAttribute("goods",goods.getName());
			GoodsAccount gac=agent.loadAccountForGoodsNotNull(goods);
			model.addAttribute("level", gac.getAgentLevel().getName());
			model.addAttribute("date", DateUtil.date2string(gac.getUpdateTime(),DateUtil.DATE_FORMAT));
*/
			model.addAttribute("code", auth.getAgent().getAgentCode());
			model.addAttribute("id", auth.getId());
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("进入授权证书失败", exp);
		}
		return "authorization/letter";
	}
	
	@RequestMapping(value = "/dmz/letterimg/{agentCode}.html", method = RequestMethod.GET)
	public void letterJpg(@ModelAttribute("parameter") SearchParameter<Authorization> auths,
			@PathVariable("agentCode") String agentCode,
			Model model,HttpServletRequest request,HttpServletResponse response) {
        Authorization  auth=auths.getEntity();
		try {
			response.setContentType("image/jpeg");
			Agent agent=auth.getAgent();
			String dir=request.getServletContext().getRealPath("/WEB-INF/view/letter");
			byte[] letterImg=authorizationLetterHandler.generateLetter(agent, auth, Paths.get(dir, "letter.jpg"));
			OutputStream out=response.getOutputStream();
			out.write(letterImg);
			out.flush();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("授权证书生成失败", exp);
		}
	}

    /**
     * 注册页面
     * @param lid
     * @param aid
     * @param request
     * @param model
     * @return
     */
	@RequestMapping(value = "/dmz/letterForm.html")
	public  String letterForm(Integer lid,Integer aid ,HttpServletRequest request,Model model){
        Authorization authorization= authorizationDAO.findById(aid);//获取授权
        if(!WebUtil.isLogin(request)){//游客访问

            Agent agent =authorization.getAgent();//获取授权人
            AgentLevel agentLevel= agentLevelDAO.findById(lid);
            model.addAttribute("agent",agent);
            model.addAttribute("agentLevel",agentLevel);
            model.addAttribute("aid",authorization.getAuthorizationType().getId());//授权类型的id
            return "/mall/letter_register";
        }else{//自己访问
            //后期在这里添加 游客不能分享链接
            model.addAttribute("code",authorization.getAgent().getAgentCode());
            model.addAttribute("id",aid);
            return "authorization/letter";
        }

    }

    /**
     * 授权审核
     * @param parameter
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
    public String audthIndex(
            @ModelAttribute("parameter") SearchParameter<Authorization> parameter,
            Model model, HttpServletRequest request) {
        try {
            parameter.getEntity().setStatus(AgentStatus.NO_ACTIVE);
            List<Authorization> authorizations = authorizationDAO.searchEntityByPage(parameter, null, (
                    t) -> true);
            WebUtil.turnPage(parameter, request);
            model.addAttribute("authorizations", authorizations);
        } catch (Exception exp) {
            exp.printStackTrace();
            LOG.error("待审核授权查询失败", exp);
        }
        return "authorization/authAudit_index";
    }

    /**
     * 修改授权界面
     * @param parameter
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit_enter(
            @ModelAttribute("parameter") SearchParameter<Authorization> parameter, HttpServletRequest request,
            Model model) {
        User user=(User)WebUtil.getCurrentUser(request);
        if(user.isAdmin()){
            HashMap<Integer,GoodsAccount> levels=new HashMap<Integer,GoodsAccount>();
            parameter.getEntity().getAgent().getGoodsAccounts().forEach(acs->{
                levels.put(acs.getGoods().getId(), acs);
            });
            model.addAttribute("levels",levels);

            return "/authorization/auth_edit";
        }else{
            LOG.error("非管理员身份,无授权类型编辑权限");
            return "/common/403";
        }

    }
	

	@ModelAttribute("parameter")
	public SearchParameter<Authorization> preprocess(@RequestParam("id") Optional<Integer> id) {
		Authorization auth = new Authorization();
        SearchParameter<Authorization> parameter = new SearchParameter<>();
		if (id.isPresent()) {
            System.out.println("=============");
            auth = authorizationDAO.findById(id.get());
		}
        parameter.setEntity(auth);

		return parameter;
	}

	@Autowired
	private AuthorizationDAO authorizationDAO;
	
	@Autowired
	private AuthorizationLetterHandler authorizationLetterHandler;
	@Autowired
    private AgentLevelDAO agentLevelDAO;
	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
