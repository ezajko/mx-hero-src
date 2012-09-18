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

package org.mxhero.engine.plugin.attachmentlink.alcommand.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.cleaner.DaysPeriodCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:cleaner-bundle-context.xml")
public class CleanerTest {

	@Autowired
	private DaysPeriodCleaner cleaner;

	public DaysPeriodCleaner getCleaner() {
		return cleaner;
	}

	public void setCleaner(DaysPeriodCleaner cleaner) {
		this.cleaner = cleaner;
	}
	
	@Test
	public void test() throws InterruptedException{
		Thread.sleep(1000);
		cleaner.stop();
		Thread.sleep(1000);
	}
}
