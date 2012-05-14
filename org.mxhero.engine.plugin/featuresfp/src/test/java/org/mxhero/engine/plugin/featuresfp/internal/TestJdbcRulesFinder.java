package org.mxhero.engine.plugin.featuresfp.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.plugin.featuresfp.internal.JdbcRulesFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class TestJdbcRulesFinder {

	@Autowired(required=true)
	private JdbcRulesFinder finder;

	@Test
	public void test(){
		finder.find("org.mxhero.feature.signature", 1);
	}
	
	public JdbcRulesFinder getFinder() {
		return finder;
	}

	public void setFinder(JdbcRulesFinder finder) {
		this.finder = finder;
	}
	
}
