package com.dreamer.view.user;

import com.dreamer.domain.user.AdvanceTransfer;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.user.AdvanceTransferDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.repository.user.UserDAO;
import com.dreamer.service.user.AgentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/advance")
public class AdvanceTransferController {

	@RequestMapping(value = "/transfer.json", method = RequestMethod.POST)
	public Message transfer(
			@ModelAttribute("parameter") AdvanceTransfer transfer,
			@RequestParam("realName") String realName,
			@RequestParam("agentCode") String agentCode, Model model,
			HttpServletRequest request) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAgent()) {
				Agent agent = agentDAO.findById(user.getId());
				transfer.setUserByFromAgent(agent);
				agentHandler.transferAdvance(user, transfer, agentCode,
						realName, transfer.getAdvance());
			}
			if (user.isAdmin()) {
				Agent agent = mutedUserDAO.loadFirstOne();
				transfer.setUserByFromAgent(agent);
				agentHandler.transferAdvance(user, transfer, agentCode,
						realName, transfer.getAdvance());
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@ModelAttribute("parameter")
	public AdvanceTransfer preprocess(@RequestParam("id") Optional<Integer> id) {
		AdvanceTransfer parameter;
		if (id.isPresent()) {
			parameter = advanceTransferDAO.findById(id.get());
		} else {
			parameter = new AdvanceTransfer();
		}
		return parameter;
	}

	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AdvanceTransferDAO advanceTransferDAO;
	@Autowired
	private AgentHandler agentHandler;
	@Autowired
	private MutedUserDAO mutedUserDAO;
}
