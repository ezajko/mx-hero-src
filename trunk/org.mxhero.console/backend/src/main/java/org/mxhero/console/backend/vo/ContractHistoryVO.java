package org.mxhero.console.backend.vo;

import java.util.Calendar;

public class ContractHistoryVO {

	private String action;
	
	private Calendar actionDate;

	private ContractVO contract;
	
	public Calendar getActionDate() {
		return actionDate;
	}

	public void setActionDate(Calendar actionDate) {
		this.actionDate = actionDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public ContractVO getContract() {
		return contract;
	}

	public void setContract(ContractVO contract) {
		this.contract = contract;
	}
	
}
