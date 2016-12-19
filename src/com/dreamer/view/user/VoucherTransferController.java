package com.dreamer.view.user;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.domain.user.VoucherTransfer;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.repository.user.UserDAO;
import com.dreamer.repository.user.VoucherTransferDAO;
import com.dreamer.service.user.AgentHandler;

@RestController
@RequestMapping("/voucher")
public class VoucherTransferController {

	@RequestMapping(value = "/transfer.json", method = RequestMethod.POST)
	public Message transfer(
			@ModelAttribute("parameter") VoucherTransfer transfer,
			@RequestParam("realName") String realName,
			@RequestParam("agentCode") String agentCode, Model model,
			HttpServletRequest request) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAgent()) {
				Agent agent = agentDAO.findById(user.getId());
				transfer.setUserByFromAgent(agent);
				agentHandler.transferVoucher(user, transfer, agentCode,
						realName, transfer.getVoucher());
			}
			if (user.isAdmin()) {
				Agent agent = mutedUserDAO.loadFirstOne();
				transfer.setUserByFromAgent(agent);
				agentHandler.transferVoucher(user, transfer, agentCode,
						realName, transfer.getVoucher());
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@ModelAttribute("parameter")
	public VoucherTransfer preprocess(@RequestParam("id") Optional<Integer> id) {
		VoucherTransfer parameter = new VoucherTransfer();
		if (id.isPresent()) {
			parameter = voucherTransferDAO.findById(id.get());
		} else {
			parameter = new VoucherTransfer();
		}
		return parameter;
	}

	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private VoucherTransferDAO voucherTransferDAO;
	@Autowired
	private AgentHandler agentHandler;
	@Autowired
	private MutedUserDAO mutedUserDAO;
}
