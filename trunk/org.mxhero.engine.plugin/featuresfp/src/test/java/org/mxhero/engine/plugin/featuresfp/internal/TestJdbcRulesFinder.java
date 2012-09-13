/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
