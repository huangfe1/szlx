package com.dreamer.service.authorization;

import com.dreamer.domain.authorization.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamer.repository.authorization.AuthorizationDAO;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizationHandler {

	
	@Autowired
	private AuthorizationDAO authorizationDAO;

    @Transactional
	public void  activeAuth(Authorization authorization){
		authorization.active();
        authorizationDAO.save(authorization);
	}
}
