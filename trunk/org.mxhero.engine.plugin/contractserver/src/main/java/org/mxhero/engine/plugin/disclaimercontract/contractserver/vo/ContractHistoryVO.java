package org.mxhero.engine.plugin.disclaimercontract.contractserver.vo;

public class ContractHistoryVO {

	private String action;
	
	private String actionDate;

	private ContractVO contract;
	
	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
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
