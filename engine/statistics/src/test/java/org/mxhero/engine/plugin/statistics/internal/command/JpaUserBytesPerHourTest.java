package org.mxhero.engine.plugin.statistics.internal.command;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.plugin.statistics.command.UserBytesPerHour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class JpaUserBytesPerHourTest {

	@Autowired
	private ApplicationContext ctx = null;
	
	@Test
	public void testWrongParameters(){
		UserBytesPerHour cmd = new JpaUserBytesPerHour();
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
		UserBytesPerHour cmd = (UserBytesPerHour)ctx.getBean("jpaUserBytesPerHour");
		Assert.assertTrue(cmd.exec(null, "someuserid","send","10").isTrue());
	}
}
