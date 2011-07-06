package org.mxhero.engine.domain.provider;

import java.io.IOException;

import org.junit.Test;
import org.mxhero.engine.domain.feature.Rule;
import org.mxhero.engine.domain.feature.RuleDirection;

public class StreamDRLProviderTest extends StreamDRLProvider {

	@Override
	protected StringBuilder processHeader(String domain) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StringBuilder processRule(String domain, Rule rule)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Test
	public void testFromToConditions(){
		StringBuilder sb = new StringBuilder(StreamDRLProvider.FROM_TO);
		RuleDirection from = new RuleDirectionVO(INDIVIDUAL,"gmail.com",1,"gmail.com","one","example");
		RuleDirection to = new RuleDirectionVO(INDIVIDUAL,"mxhero.com",0,"mxhero.com","two","other");
		
		this.getFromToConditions(from, to, true, sb);
		
		System.out.println(sb);
	}
	
	public class RuleDirectionVO implements RuleDirection{

		String directionType;
		String freeValue;
		Integer id;
		String group;
		String account;
		String domain;

		public RuleDirectionVO(String directionType, String freeValue,
				Integer id,String domain,String group,String account) {
			super();
			this.directionType = directionType;
			this.freeValue = freeValue;
			this.id = id;
			this.domain=domain;
			this.group=group;
			this.account=account;
		}
		public String getDirectionType() {
			return directionType;
		}
		public void setDirectionType(String directionType) {
			this.directionType = directionType;
		}
		public String getFreeValue() {
			return freeValue;
		}
		public void setFreeValue(String getFreeValue) {
			this.freeValue = getFreeValue;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getGroup() {
			return group;
		}
		public void setGroup(String group) {
			this.group = group;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public String getDomain() {
			return domain;
		}
		public void setDomain(String domain) {
			this.domain = domain;
		}

	}
	
}
