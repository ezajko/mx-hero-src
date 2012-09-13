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

package org.mxhero.engine.plugin.emailquarantine.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class CachedQuarantineRepository implements Runnable, QuarantineRepository{

	private static Logger log = LoggerFactory.getLogger(CachedQuarantineRepository.class);
	private Map<String,String> quarantineEmails = new HashMap<String,String>();
	private static final long CHECK_TIME = 1000;
	private Thread thread;
	private boolean keepWorking = false;
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public CachedQuarantineRepository(DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		//First time so it starts with real data.
		findAll();
		thread.start();
	}

	public void stop(){
		keepWorking=false;	
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public void run() {
		long lastReload = System.currentTimeMillis();
		while(keepWorking){
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {}
			if(lastReload+(30*1000)-System.currentTimeMillis()<0){
				findAll();
				lastReload=System.currentTimeMillis();
			}

		}
	}
	
	private void findAll(){
		try{
		List<QuarantineVO> result = template.getJdbcOperations().query("SELECT `"+QuarantineMapper.DOMAIN+"`,`"+QuarantineMapper.EMAIL+"` FROM `"+QuarantineMapper.DATABASE+"`.`"+QuarantineMapper.TABLE_NAME+"`",new QuarantineMapper());
		Map<String,String> newQuarantineEmails = new HashMap<String,String>();
		if(result!=null){
			log.debug("found: "+result.size());
			for(QuarantineVO quarantine : result){
				newQuarantineEmails.put(quarantine.getDomain(), quarantine.getEmail());
			}
		}
		Map<String,String> oldQuarantineEmails = quarantineEmails;
		quarantineEmails = newQuarantineEmails;
		oldQuarantineEmails.clear();
		}catch (Exception e) {
			log.warn("error while reading: "+e.getMessage());
		}
	}

	@Override
	public String findEmail(String domain) {
		return quarantineEmails.get(domain);
	}
}
