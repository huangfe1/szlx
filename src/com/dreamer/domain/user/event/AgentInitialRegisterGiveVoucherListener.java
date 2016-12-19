package com.dreamer.domain.user.event;

import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.user.GiftPointHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgentInitialRegisterGiveVoucherListener implements AgentListener {

	@Override
	public void actionPerformed(AgentEvent event) {
		// TODO Auto-generated method stub
//		Agent agent=event.getSource();
//		Double voucher=0.1;
//		Agent parent=agent.getParent();
//		if(!parent.isMutedUser()){
//			parent.getAccounts().increaseVoucher(voucher,"新增用户,赠送广告费");
//		}
//		agentDAO.merge(parent);//保存
	}
	
	@Autowired
	private GiftPointHandler giftPointHandler;

	@Autowired
	private AgentDAO agentDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
