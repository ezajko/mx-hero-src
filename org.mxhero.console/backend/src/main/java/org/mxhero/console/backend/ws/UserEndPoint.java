package org.mxhero.console.backend.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.mxhero.console.backend.repository.UserRepository;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.springframework.beans.factory.annotation.Autowired;

@WebService
public class UserEndPoint {

	private UserRepository repository;
	
	@Autowired
	public UserEndPoint(UserRepository repository) {
		this.repository = repository;
	}

	@WebMethod(action="finbByLoginName")
	public ApplicationUserVO finbByLoginName(@WebParam(name="loginName")String loginName){
		return repository.finbByUserName(loginName);
	}	
	
	@WebMethod(action="changePassword")
	public void changePassword(
			@WebParam(name="newPassword")String newPassword, 
			@WebParam(name="userId")Integer userId){
		repository.setPassword(newPassword, userId);	
	}
	
}
