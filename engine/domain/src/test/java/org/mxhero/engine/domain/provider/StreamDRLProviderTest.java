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
		RuleDirection from = new RuleDirectionVO(DOMAIN,"gmail.com",1);
		RuleDirection to = new RuleDirectionVO(DOMAIN,"mxhero.com",0);
		
		this.getFromToConditions(from, to, true, sb);
		
		System.out.println(sb);
	}
	
	public class RuleDirectionVO implements RuleDirection{

		String directionType;
		String freeValue;
		Integer id;

		public RuleDirectionVO(String directionType, String freeValue,
				Integer id) {
			super();
			this.directionType = directionType;
			this.freeValue = freeValue;
			this.id = id;
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

	}
	
}
