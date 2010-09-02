package org.mxhero.engine.plugin.statistics.internal.command;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.plugin.statistics.command.UserMailsPerHour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class JpaUserMailsPerHourTest {

	@Autowired
	private ApplicationContext ctx = null;
	
	@Test
	public void testWrongParameters(){
		UserMailsPerHour cmd = new JpaUserMailsPerHour();
		Assert.assertFalse(cmd.exec(null).isTrue());
		Assert.assertFalse(cmd.exec(null,(String[])null).isTrue());
		Assert.assertFalse(cmd.exec(null,null,null).isTrue());
		Assert.assertFalse(cmd.exec(null,null,null,null).isTrue());
		Assert.assertFalse(cmd.exec(null,"","","").isTrue());
		Assert.assertFalse(cmd.exec(null,"someuserid","wrongphase","10").isTrue());
		Assert.assertFalse(cmd.exec(null,"someuserid","send","-1").isTrue());
		Assert.assertFalse(cmd.exec(null,"someuserid","send","0").isTrue());
		Assert.assertFalse(cmd.exec(null,"someuserid","send","wornghours").isTrue());
	}
	
	@Test
	public void testCmd(){
		UserMailsPerHour cmd = (UserMailsPerHour)ctx.getBean("jpaUserMailsPerHour");
		Assert.assertTrue(cmd.exec(null, "someuserid","10").isTrue());
	}
}
