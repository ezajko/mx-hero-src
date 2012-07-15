package org.mxhero.engine.plugin.disclaimercontract.contractserver.service;

import java.util.Calendar;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.RequestRepository;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.RequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcRequestService implements RequestService{

	private RequestRepository requestRepository;

	@Autowired(required=true)
	public JdbcRequestService(RequestRepository requestRepository) {
		this.requestRepository = requestRepository;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void approve(Long id, String type, String aditionalData) {
		if(!type.equalsIgnoreCase(RequestVO.CONTRACT_TYPE) && !type.equalsIgnoreCase(RequestVO.ONE_TYPE)){
			throw new IllegalArgumentException("wrong.type");
		}
		RequestVO request = requestRepository.readRequest(id);
		if(request==null){
			throw new RequestNotAvailableException();
		}
		if(request.getApprovedDate()!=null){
			throw new AlreadyApprovedException();
		}
		if(request.getVetoDate()!=null){
			throw new AlreadyRejectedException();
		}
		request.setApprovedDate(Calendar.getInstance());
		request.setType(type);
		request.setPending(true);
		request.setAdditionalData(aditionalData);
		requestRepository.updateRequest(request);
	}

	@Override
	public void veto(Long id, String aditionalData) {
		RequestVO request = requestRepository.readRequest(id);
		if(request==null){
			throw new RequestNotAvailableException();
		}
		if(request.getApprovedDate()!=null){
			throw new AlreadyApprovedException();
		}
		if(request.getVetoDate()!=null){
			throw new AlreadyRejectedException();
		}
		request.setVetoDate(Calendar.getInstance());
		request.setPending(true);
		request.setAdditionalData(aditionalData);
		requestRepository.updateRequest(request);
	}

}
