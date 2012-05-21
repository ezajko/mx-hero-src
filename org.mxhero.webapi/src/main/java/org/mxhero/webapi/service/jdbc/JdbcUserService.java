package org.mxhero.webapi.service.jdbc;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.repository.UserRepository;
import org.mxhero.webapi.service.UserService;
import org.mxhero.webapi.vo.PageVO;
import org.mxhero.webapi.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jdbcUserService")
public class JdbcUserService implements UserService{

	private UserRepository userRepository;
	
	@Autowired(required=true)
	public JdbcUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(readOnly=true)
	@Override
	public PageVO<UserVO> readAll(String domain, Integer limit, Integer offset) {
		
		PageResult<UserVO> result = userRepository.findall(domain, offset, limit);
		PageVO<UserVO> userVOPage = new PageVO<UserVO>();
		userVOPage.setActualPage(result.getPageNumber());
		userVOPage.setTotalElements(result.getTotalRecordsNumber());
		userVOPage.setTotalPages(result.getPageAmount());
		userVOPage.setElements(result.getPageData());
		
		return userVOPage;
	}

	@Override
	public UserVO create(UserVO user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetPassword(String email) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserVO read(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(String username, UserVO user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(String username, String oldPassword,
			String newPassword, String domain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String username, String domain) {
		// TODO Auto-generated method stub
		
	}

}
